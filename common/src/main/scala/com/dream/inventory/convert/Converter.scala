package com.dream.inventory.convert

import cats.Functor

object Converter {

  implicit class ConverterFunctor[A, F[_]: Functor](underlying: F[A]) {
    def convert[B](implicit op: A => B): F[B] = Functor[F].map(underlying)(op)
  }

  implicit class TypeConverter[A](underlying: A) {
    def convert[B](implicit op: A => B) : B = op(underlying)
  }

}
