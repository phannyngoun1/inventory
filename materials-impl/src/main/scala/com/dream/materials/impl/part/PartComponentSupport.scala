package com.dream.materials.impl.part

import scala.concurrent.ExecutionContext

trait PartComponentSupport {
  this: PartComponent =>

  import profile.api._
  import slick.dbio.DBIO
  import akka.Done

  trait PartIdRecord {
    val partId: String
  }

  trait PartIdTableSupport[T] {
    this: TableBase[T] =>
    def partId = column[String]("partId")
  }

  trait PartDaoSupport {
    this: DaoSupport[String, PartRecord] =>

    def added(partRecord: PartRecord)(implicit ec: ExecutionContext): DBIO[Done]
  }

}
