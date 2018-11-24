package com.dream.materials.impl.part

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.dream.inventory.utils.JsonFormats._

object PartEntity {

  implicit class EitherOps(val self: Either[PartError, PartDataModel]) {
    def toSomeOrThrow: Option[PartDataModel] = self.fold(error => throw new IllegalStateException(error.message), Some(_))
  }

}

class PartEntity extends PersistentEntity {

  import PartEntity._

  override type Command = PartCommand
  override type Event = PartEvent
  override type State = Option[PartDataModel]

  override def initialState: Option[PartDataModel] = None

  override def behavior: Behavior = {

    case None => notCreated
    case Some(part) if (part.isActive) => created(part)
  }

  private val getPartCommand = Actions().onReadOnlyCommand[GetPart.type, Option[PartDataModel]] {
    case (GetPart, ctx, state) => ctx.reply(state)
  }

  private def notCreated = {
    Actions().onCommand[CreatePart, Done] {
      case (CreatePart(part), ctx, _) =>
        ctx.thenPersist(PartCreated(part))(_ => ctx.reply(Done))
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