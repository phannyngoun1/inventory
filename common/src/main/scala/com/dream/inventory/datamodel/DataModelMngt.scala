package com.dream.inventory.datamodel

object DataModelMngt {

  trait DataError {
    def message: String
  }

  implicit class EitherOps[E <: DataError, D](val self: Either[E, D]) {
    def toSomeOrThrow: Option[D] = self.fold(error => throw new IllegalStateException(error.message), Some(_))

    def toObjOrThrow: D = self.fold(error => throw new IllegalStateException(error.message), f => f)
  }

  trait CreateDataModelProcessor[A, B] {
    def process(data: A): Either[DataError, B]
  }

  trait DeleteDataModelProcessor[A, B] {
    def process(obj: A): Either[DataError, B]
  }

  case class PartData(
    id: Int
  )

  implicit val createPartDataProcessing = new CreateDataModelProcessor[PartData, String] {
    override def process(data: PartData): Either[DataError, String] = Right(s"hello ${data.id.toString}")
  }

  implicit val deletePartDataProcessing = new DeleteDataModelProcessor[PartData, Unit] {
    override def process(obj: PartData): Either[DataError, Unit] = Right(())
  }

  implicit class Operations[A](underlying: A) {
    def create[B](implicit evidence: CreateDataModelProcessor[A, B]): Either[DataError, B] = {
      evidence.process(underlying)
    }

    def delete[B](implicit evidence: DeleteDataModelProcessor[A, B]): Either[DataError, B] = {
      evidence.process(underlying)
    }
  }

}
