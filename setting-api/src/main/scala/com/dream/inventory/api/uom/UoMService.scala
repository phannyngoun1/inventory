package com.dream.inventory.api.uom

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.dream.inventory.utils

trait UoMService {

  def createUoM: ServiceCall[UoM, UoM]

  def getUoM(id: UUID) : ServiceCall[NotUsed, UoM]

  def getUoMs(page: Option[String]): ServiceCall[NotUsed, utils.PagingState[UoM]]

}