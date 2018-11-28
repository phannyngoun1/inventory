package com.dream.materials.impl

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.dream.inventory.common.PartType
import com.dream.materials.api.part.PartBasicInfo
import com.dream.materials.impl.part._
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalatest.{BeforeAndAfterAll, Matchers, OptionValues, WordSpec}

class PartEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll with OptionValues {

  private val system = ActorSystem("test", JsonSerializerRegistry.actorSystemSetupFor(MaterialSerializerRegistry))
  private val partId = UUID.randomUUID
  private val creator = UUID.fromString("b0a6a6a0-ecbb-43fa-b4e0-6e7272202402")


  private val partBasicInfo = PartBasicInfo(
    partNr = "Part 001",
    description = "Part 001",
    partType = PartType.Inventory,
    creator = UUID.randomUUID()
  )

  override def afterAll = {
    TestKit.shutdownActorSystem(system)
  }

  private def withDriver[T](block: PersistentEntityTestDriver[PartCommand, PartEvent, Option[PartDataModel]] => T): T = {
    val driver = new PersistentEntityTestDriver(system, new PartEntity, partId.toString)
    try {
      block(driver)
    } finally {
      driver.getAllIssues shouldBe empty
    }
  }

  "The part entity" should {
    "allow creating an part" in withDriver { driver =>

      val partData = PartDataModel.create(partId, partBasicInfo = partBasicInfo)

      val outcome = driver.run(CreatePart(partData))
      outcome.events should contain only PartCreated(partData)
      outcome.state should ===(Some(partData))

    }
  }
}
