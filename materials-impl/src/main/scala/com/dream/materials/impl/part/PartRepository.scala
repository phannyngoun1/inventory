package com.dream.materials.impl.part

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.slick.SlickReadSide
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext

private[impl] class PartRepository(val profile: JdbcProfile, val db: JdbcProfile#Backend#Database)(implicit ec: ExecutionContext) extends PartComponent {

  import profile.api._

  def createTable = {
    MTable.getTables.flatMap { tables =>
      if (!tables.exists(_.name.name == PartDao.baseTableRow.tableName)) {
        PartDao.schema.create
      } else {
        DBIO.successful(())
      }
    }.transactionally
  }


  def processPartCreated(eventElement: EventStreamElement[PartCreated]): DBIO[Done] = PartDao.added(PartRecord(
    id = eventElement.event.part.id.toString,
    partNr = eventElement.event.part.partNr,
    description = eventElement.event.part.description,
    partType = eventElement.event.part.partType.toString,
    creator = eventElement.event.part.auditData.creator.toString,
    modifiedBy = eventElement.event.part.auditData.creator.toString
  ))
}

private[impl] class PartEventProcessor(readSide: SlickReadSide, partRepository: PartRepository)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[PartEvent] {

  override def buildHandler(): ReadSideProcessor.ReadSideHandler[PartEvent] = readSide.builder[PartEvent]("partEventOffset")
    .setGlobalPrepare(partRepository.createTable)
    .setEventHandler[PartCreated](partRepository.processPartCreated)
    .build()

  override def aggregateTags: Set[AggregateEventTag[PartEvent]] = PartEvent.Tag.allTags

}