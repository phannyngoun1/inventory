package com.dream.inventory.common.dao

import akka.Done
import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

trait WithUpdate[ID, T] {
  def update(id: ID)(data: T)(implicit ec: ExecutionContext): DBIO[Done]
}
