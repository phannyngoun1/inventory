package com.dream.inventory.common

import cats.Functor

object Factor {

  /**
    * An implicit class which just allows us to call .nestedMap on any nested functor e.g. a List[Option[LineItem]
    *
    * @param underlying "something" that consists of two nested "wrapper types" e.g. List[Option]
    * @tparam A The type of the item itself
    * @tparam F Outer wrapper
    * @tparam G Inner wrapper
    */
  implicit class RichFunctor[A, F[_]: Functor, G[_]: Functor](underlying: F[G[A]]) {
    def nestedMap[B](op: A => B): F[G[B]] = Functor[F].compose[G].map(underlying)(op)
  }

}
