package com.dream.inventory.setting.impl.uom

import com.lightbend.lagom.scaladsl.persistence.slick.SlickReadSide
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext

private[impl] class UoMRepository(val profile: JdbcProfile, val db: JdbcProfile#Backend#Database)(implicit ec: ExecutionContext) extends UoMComponent {

  import profile.api._

  def createTable = {
    MTable.getTables.flatMap { tables =>
      if (!tables.exists(_.name.name == UoMDao.baseTableRow.tableName)) {
        UoMDao.schema.create
      } else {
        DBIO.successful(())
      }
    }.transactionally
  }

  def processUoMCreated(eventElement: EventStreamElement[UoMCreated]) = UoMDao.add(UoMRecord(
    id = eventElement.event.uom.id.toString,
    code = eventElement.event.uom.code,
    abbr = eventElement.event.uom.abbr,
    measurementType = eventElement.event.uom.measurementType.toString,
    description = eventElement.event.uom.description,
    creator = eventElement.event.uom.creator.toString,
    modifiedBy = eventElement.event.uom.modifiedBy.toString
  ))

  def processUoMUpdated(eventElement: EventStreamElement[UoMUpdated]) = UoMDao.update(eventElement.entityId)(eventElement.event)

  def processUoMDeleted(eventElement: EventStreamElement[UoMDeleted]) = UoMDao.delete(eventElement.entityId, eventElement.event.modifiedBy)

  def processUoMDisable(eventElement: EventStreamElement[UoMDisabled]) = UoMDao.disable(eventElement.entityId, eventElement.event.modifiedBy)

  def processUoMActive(eventElement: EventStreamElement[UoMActive]) = UoMDao.activate(eventElement.entityId, eventElement.event.modifiedBy)

}

private[impl] class PartEventProcessor(readSide: SlickReadSide, uomRepository: UoMRepository)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[UoMEvent] {

  override def buildHandler(): ReadSideProcessor.ReadSideHandler[UoMEvent] = readSide.builder[UoMEvent]("uomEventOffset")
    .setGlobalPrepare(uomRepository.createTable)
    .setEventHandler[UoMCreated](uomRepository.processUoMCreated)
    .setEventHandler[UoMDeleted](uomRepository.processUoMDeleted)
    .setEventHandler[UoMDisabled](uomRepository.processUoMDisable)
    .setEventHandler[UoMActive](uomRepository.processUoMActive)
    .build()

  override def aggregateTags: Set[AggregateEventTag[UoMEvent]] = UoMEvent.Tag.allTags
}
