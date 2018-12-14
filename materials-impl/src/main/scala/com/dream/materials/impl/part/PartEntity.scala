package com.dream.materials.impl.part

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

object PartEntity {

  //  implicit class EitherOps[E <: DataError, D](val self: Either[E, D]) {
  //    def toSomeOrThrow: Option[D] = self.fold(error => throw new IllegalStateException(error.message), Some(_))
  //  }
}

class PartEntity extends PersistentEntity {

  override type Command = PartCommand
  override type Event = PartEvent
  override type State = Option[PartDataModel]

  override def initialState: Option[PartDataModel] = None

  override def behavior: Behavior = {

    case None => notCreated
    case Some(part) if (part.recordStatus.isActive) => created(part)
  }

  private val getPartCommand = Actions().onReadOnlyCommand[GetPart.type, Option[PartDataModel]] {
    case (GetPart, ctx, state) => ctx.reply(state)
  }

  private def notCreated = {
    Actions().onCommand[CreatePart, Done] {
      case (CreatePart(dataModel), ctx, _) =>
        ctx.thenPersist(PartCreated(dataModel))(_ => ctx.reply(Done))
    }.onEvent {
      case (PartCreated(part), _) => Some(part)
    }.orElse(getPartCommand)
  }

  private def created(part: PartDataModel) = {
    Actions().onCommand[DisablePart.type, Done] {
      case (DisablePart, ctx, _) =>
        ctx.thenPersist(PartDisabled)(_ => ctx.reply(Done))
    }.onCommand[EnablePart.type, Done] {
      case (EnablePart, ctx, _) =>
        ctx.thenPersist(PartEnabled)(_ => ctx.reply(Done))
    }.onEvent {
      case (PartDisabled, _) => part.disable.toSomeOrThrow
      case (PartEnabled, _) => part.enable.toSomeOrThrow
    }.orElse(getPartCommand)
  }

}
