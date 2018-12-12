package com.dream.inventory.common

object Decorator {

  trait DataError {
    def message: String
  }

  implicit class EitherOps[E <: DataError, D ](val self: Either[E, D]) {
    def toSomeOrThrow: Option[D] = self.fold(error => throw new IllegalStateException(error.message), Some(_))
    def toObjOrThrow: D = self.fold(error => throw new IllegalStateException(error.message), f => f)
  }
}
