package com.dream.materials.impl

import akka.stream.Materializer
import com.dream.materials.api.MaterialService
import com.dream.materials.impl.part.{PartEntity, PartEventProcessor, PartRepository}
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.WriteSideCassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.persistence.slick.ReadSideSlickPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServerComponents}
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire.wire
import play.api.Environment
import play.api.db.HikariCPComponents
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.ExecutionContext

trait MaterialComponents extends LagomServerComponents
  with ReadSideSlickPersistenceComponents
  with WriteSideCassandraPersistenceComponents
  with HikariCPComponents {

  implicit def executionContext: ExecutionContext

  override lazy val lagomServer = serverFor[MaterialService](wire[MaterialServiceImpl])

  implicit def materializer: Materializer

  lazy val partRepository = wire[PartRepository]
  lazy val jsonSerializerRegistry = MaterialSerializerRegistry

  def environment: Environment

  persistentEntityRegistry.register(wire[PartEntity])
  readSide.register(wire[PartEventProcessor])


}

abstract class MaterialApplication(context: LagomApplicationContext) extends LagomApplication(context)
  with AhcWSComponents
  with MaterialComponents {

}

class MaterialApplicationLoader extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new MaterialApplication(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext): LagomApplication =
    new MaterialApplication(context) with LagomServiceLocatorComponents


  override def describeService = Some(readDescriptor[MaterialService])
}