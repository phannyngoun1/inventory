package com.dream.inventory.common.dao

import java.util.UUID

import akka.Done
import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

trait WithStatus[ID] {

  def disable(id: ID, userId: UUID)(implicit ec: ExecutionContext): DBIO[Done]

  def activate(id: ID, userId: UUID)(implicit ec: ExecutionContext): DBIO[Done]
}
