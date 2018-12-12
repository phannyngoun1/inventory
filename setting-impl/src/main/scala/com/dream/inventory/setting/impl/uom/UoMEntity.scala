package com.dream.inventory.setting.impl.uom

import java.time.Instant
import java.util.UUID

import akka.Done
import com.dream.inventory.common.MeasurementType
import com.dream.inventory.common.dao.{AuditData, BaseModel, RecordStatus}
import com.dream.inventory.utils.JsonFormats.singletonFormat
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger, PersistentEntity}
import play.api.libs.json.{Format, Json}

object UoMEntity {

  implicit class EitherOps(val self: Either[UoMError, UoMDataModel]) {
    def toSomeOrThrow: Option[UoMDataModel] = self.fold(error => throw new IllegalStateException(error.message), Some(_))
  }

}

class UoMEntity extends PersistentEntity {

  import UoMEntity._

  override type Command = UoMCommand
  override type Event = UoMEvent
  override type State = Option[UoMDataModel]
  private val getUoMCommand = Actions().onReadOnlyCommand[GetUoM.type, Option[UoMDataModel]] {
    case (GetUoM, ctx, state) => ctx.reply(state)
  }
  private val notCreated = {
    Actions().onCommand[CreateUoM, Done] {
      case (CreateUoM(uoM), ctx, state) =>
        ctx.thenPersist(UoMCreated(UoMDataModel(
          id = uoM.id,
          code = uoM.code,
          abbr = uoM.abbr,
          measurementType = uoM.measurementType,
          description = uoM.description,
          auditData = uoM.auditData,
          recordStatus = uoM.recordStatus
        )))(_ => ctx.reply(Done))
    }.onEvent {
      case (UoMCreated(uoM), _) => Some(uoM)
    }.orElse(getUoMCommand)
  }

  override def initialState: Option[UoMDataModel] = None

  override def behavior: Behavior = {
    case None => notCreated
    case Some(uoM) => created(uoM)
  }

  private def created(uoM: UoMDataModel) = {
    Actions().onCommand[DeleteUoM, Done] {
      case (uom: UpdateUoM, ctx, _) =>
        ctx.thenPersist(UoMUpdated(
          code = uom.code,
          abbr = uom.abbr,
          measurementType = uom.measurementType,
          description = uom.description,
          modifiedBy = uom.modifiedBy
        ))(_ => ctx.reply(Done))
      case (data: DeleteUoM, ctx, _) =>
        ctx.thenPersist(UoMDeleted(data.modifiedBy))(_ => ctx.reply(Done))
      case (data: DisableUoM, ctx, _) =>
        ctx.thenPersist(UoMDisabled(data.modifiedBy))(_ => ctx.reply(Done))
      case (data: ActivateUoM, ctx, _) =>
        ctx.thenPersist(UoMActive(data.modifiedBy))(_ => ctx.reply(Done))
    }.onEvent {
      case (data: UoMUpdated, _) => uoM.update(data).toSomeOrThrow
      case (data: UoMDeleted, _) => uoM.delete(data).toSomeOrThrow
    }
  }
}

trait UoMDataModel extends BaseModel {

  def code: String

  def abbr: String

  def measurementType: MeasurementType

  def description: String

  def update(uoMUpdated: UoMUpdated): Either[UoMError, UoMDataModel]

  def delete(uoMDeleted: UoMDeleted): Either[UoMError, UoMDataModel]

  def disable(uoMDisabled: UoMDisabled): Either[UoMError, UoMDataModel]

  def enable(uoMActive: UoMActive): Either[UoMError, UoMDataModel]

}

object UoMDataModel {

  implicit val format: Format[UoMDataModel] = Json.format

  def apply(
    id: UUID,
    code: String,
    abbr: String,
    measurementType: MeasurementType,
    description: String,
    auditData: AuditData,
    recordStatus: RecordStatus
  ): UoMDataModel = new UoMImpl(id, code, abbr, measurementType, description, auditData, recordStatus)

  def unapply(self: UoMDataModel): Option[(UUID, String, String, MeasurementType, String, AuditData, RecordStatus)] =
    Some((self.id, self.code, self.abbr, self.measurementType, self.description, self.auditData, self.recordStatus))

  case class UoMImpl(
    override val id: UUID,
    override val code: String,
    override val abbr: String,
    override val measurementType: MeasurementType,
    override val description: String,
    override val auditData: AuditData,
    override val recordStatus: RecordStatus
  ) extends UoMDataModel {

    override def delete(uoMDeleted: UoMDeleted): Either[UoMError, UoMDataModel] = Right(copy())

    override def disable(uoMDisabled: UoMDisabled): Either[UoMError, UoMDataModel] = Right(copy())

    override def enable(uoMActive: UoMActive): Either[UoMError, UoMDataModel] = Right(copy())

    override def update(uoMUpdated: UoMUpdated): Either[UoMError, UoMDataModel] = Right(copy())
  }

}

/** *************************Command *****************************/
sealed trait UoMCommand

case object GetUoM extends UoMCommand with ReplyType[Option[UoMDataModel]] {
  implicit val format: Format[GetUoM.type] = singletonFormat(GetUoM)
}

case class CreateUoM(uoM: UoMDataModel) extends UoMCommand with ReplyType[Done]

object CreateUoM {
  implicit val format: Format[CreateUoM] = Json.format
}

case class UpdateUoM(
  code: String,
  abbr: String,
  measurementType: MeasurementType,
  description: String,
  modifiedBy: UUID
) extends UoMCommand with ReplyType[Done]

object UpdateUoM {
  implicit val format: Format[UpdateUoM] = Json.format
}

case class DeleteUoM(modifiedBy: UUID) extends UoMCommand with ReplyType[Done]

object DeleteUoM {
  implicit val format: Format[DeleteUoM] = Json.format
}

case class DisableUoM(modifiedBy: UUID) extends UoMCommand with ReplyType[Done]

object DisableUoM {
  implicit val format: Format[DisableUoM] = Json.format
}

case class ActivateUoM(modifiedBy: UUID) extends UoMCommand with ReplyType[Done]

object ActivateUoM {
  implicit val format: Format[ActivateUoM] = Json.format
}

/** *************************Event *****************************/

sealed trait UoMEvent extends AggregateEvent[UoMEvent] {
  override def aggregateTag: AggregateEventTagger[UoMEvent] = UoMEvent.Tag
}

object UoMEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[UoMEvent](NumShards)
}

case class UoMCreated(uom: UoMDataModel) extends UoMEvent

object UoMCreated {
  implicit val format: Format[UoMCreated] = Json.format
}

case class UoMUpdated(
  code: String,
  abbr: String,
  measurementType: MeasurementType,
  description: String,
  modifiedBy: UUID
) extends UoMEvent

object UoMUpdated {
  implicit val format: Format[UoMUpdated] = Json.format
}

case class UoMDeleted(
  modifiedBy: UUID
) extends UoMEvent

object UoMDeleted {
  implicit val format: Format[UoMUpdated] = Json.format
}

case class UoMDisabled(
  modifiedBy: UUID
) extends UoMEvent

object UoMDisabled {
  implicit val format: Format[UoMDisabled] = Json.format
}

case class UoMActive(
  modifiedBy: UUID
) extends UoMEvent

object UoMActive {
  implicit val format: Format[UoMActive] = Json.format
}

/** *************************Error *****************************/

sealed abstract class UoMError(val message: String)

case class DefaultUoMError(override val message: String) extends UoMError(message)