package com.dream.inventory.setting.impl

import akka.stream.Materializer
import com.dream.inventory.api.SettingService
import com.dream.inventory.setting.impl.uom.{PartEventProcessor, UoMEntity, UoMRepository}
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

trait SettingComponents extends LagomServerComponents
  with ReadSideSlickPersistenceComponents
  with WriteSideCassandraPersistenceComponents
  with HikariCPComponents {

  implicit def executionContext: ExecutionContext

  override lazy val lagomServer = serverFor[SettingService](wire[SettingServiceImpl])

  implicit def materializer: Materializer

  lazy val partRepository = wire[UoMRepository]
  lazy val jsonSerializerRegistry = SettingSerializerRegistry

  def environment: Environment

  persistentEntityRegistry.register(wire[UoMEntity])
  readSide.register(wire[PartEventProcessor])

  }


abstract class SettingApplication (context: LagomApplicationContext) extends LagomApplication(context)
  with AhcWSComponents
  with SettingComponents{


}

class SettingApplicationLoader  extends LagomApplicationLoader {
  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new SettingApplication(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext): LagomApplication =
    new SettingApplication(context) with LagomServiceLocatorComponents


  override def describeService = Some(readDescriptor[SettingService])
}


