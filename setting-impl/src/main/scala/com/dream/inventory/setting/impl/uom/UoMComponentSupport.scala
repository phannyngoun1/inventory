package com.dream.inventory.setting.impl.uom

import akka.Done
import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

trait UoMComponentSupport {
  this: UoMComponent =>


  trait UoMDaoSupport {

    this: DaoSupport[String, UoMRecord] =>

  }

}
