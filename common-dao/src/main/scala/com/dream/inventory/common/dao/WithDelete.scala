package com.dream.inventory.common.dao

import java.util.UUID

import akka.Done
import slick.dbio.DBIO
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext

trait WithDelete[ID] {
  def delete(id: ID, user: UUID)(implicit ec: ExecutionContext): DBIO[Done]
}
