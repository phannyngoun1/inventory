package com.dream.inventory.common.dao

import akka.Done
import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

trait WithAdd[T] {
  def add(data: T) (implicit ec: ExecutionContext): DBIO[Done]
}
