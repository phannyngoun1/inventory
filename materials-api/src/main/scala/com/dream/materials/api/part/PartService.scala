package com.dream.materials.api.part

import java.util.UUID

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait PartService {
  this: Service =>

  def createPart: ServiceCall[Part, Part]

  def getPart(id: UUID): ServiceCall[NotUsed, Part]

}
