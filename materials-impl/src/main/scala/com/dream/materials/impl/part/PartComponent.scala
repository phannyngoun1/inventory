package com.dream.materials.impl.part

import java.time.Instant

import akka.Done
import com.dream.inventory.common.dao.ComponentSupport

import scala.concurrent.ExecutionContext

trait PartComponent extends ComponentSupport with PartComponentSupport {

  import profile.api._

  case class PartDefaultLocationRecord(
    id: String,
    partId: String,
    locationId: String,
    val deleted: Boolean = false
  ) extends Record with PartIdRecord with SoftDeletableRecord

  case class PartRecord(
    id: String,
    partNr: String,
    description: String,
    partType: String,
    abcCode: Option[String] = None,
    stCost: Option[Float] = None,

    defaultBoM: Option[String] = None,
    defaultProduct: Option[String] = None,

    details: Option[String] = None,

    pickInUoMPartOnly: Boolean = false,

    creator: String,
    modifiedBy: String,
    createdAt: Instant = Instant.now(),
    modifiedAt: Option[Instant] = None,
    isActive: Boolean = true,
    deleted: Boolean = false
  ) extends Record with SoftDeletableRecord with AuditRecord


  case class PartAcctRecord(
    id: String,
    partId: String,
    assetAccount: Option[String],
    cosgsAccount: Option[String],
    adjustmentAccount: Option[String],
    scrapAccount: Option[String]

  ) extends Record with PartIdRecord

  case class PartSizeWeightRecord(
    id: String,
    partId: String,
    length: Option[Float],
    width: Option[Float],
    height: Option[Float],
    sizeUoM: Option[String],
    weight: Option[Float],
    weightUoM: Option[String]
  ) extends Record with PartIdRecord


  case class Parts(tag: Tag) extends TableBase[PartRecord](tag, "part")
    with AuditTableSupport[PartRecord]
    with StatusTableSupport[PartRecord]
    with SoftDeletableTableSupport[PartRecord] {

    def measurement = column[String]("measurement")

    override def * =
      (id, partNr, description, partType, abcCode, stCost, defaultBoM, defaultProduct, details, pickInUoMPartOnly, creator, modifiedBy, createdAt, modifiedAt, isActive, deleted) <> (PartRecord.tupled, PartRecord.unapply)

    def partNr = column[String]("partNr")

    def description = column[String]("description")

    def partType = column[String]("partType")

    def abcCode = column[Option[String]]("abcCode")

    def stCost = column[Option[Float]]("stCost")

    def defaultBoM = column[Option[String]]("defaultBoM")

    def defaultProduct = column[Option[String]]("defaultProductId")

    def details = column[Option[String]]("details")

    def pickInUoMPartOnly = column[Boolean]("pickInUoMPartOnly")

  }

  case class PartTrackingRecord(
    id: String,
    partId: String,
    partTrackingType: String,
    nextValue: String,
    isPrimary: Boolean
  ) extends Record with PartIdRecord

  case class PartReorderLevelRecord(
    id: String,
    partId: String,
    groupLocationId: String,
    rop: Float, // Reorder Point
    oul: Option[Float] // Order Up to Level
  ) extends Record with PartIdRecord


  case class PartDefaultLocations(tag: Tag) extends TableBase[PartDefaultLocationRecord](tag, "partDetaultLocation")
    with PartIdTableSupport[PartDefaultLocationRecord]
    with SoftDeletableTableSupport[PartDefaultLocationRecord] {

    override def * = (id, partId, locationId, deleted) <> (PartDefaultLocationRecord.tupled, PartDefaultLocationRecord.unapply)

    def locationId = column[String]("locationId")
  }

  case class PartAccounts(tag: Tag)
    extends TableBase[PartAcctRecord](tag, "partAcct")

      with PartIdTableSupport[PartAcctRecord] {

    override def * = (id, partId, assetAccount, cosgsAccount, adjustmentAccount, scrapAccount) <> (PartAcctRecord.tupled, PartAcctRecord.unapply)

    def assetAccount = column[Option[String]]("stCost")

    def cosgsAccount = column[Option[String]]("stCost")

    def adjustmentAccount = column[Option[String]]("adjustmentAccountId")

    def scrapAccount = column[Option[String]]("scrapAccountId")
  }


  case class PartSizeWeights(tag: Tag)
    extends TableBase[PartSizeWeightRecord](tag, "partSizeWeight")
      with PartIdTableSupport[PartSizeWeightRecord] {


    override def * = (id, partId, length, width, height, sizeUoM, weight, weightUoM) <> (PartSizeWeightRecord.tupled, PartSizeWeightRecord.unapply)

    def length = column[Option[Float]]("length")

    def width = column[Option[Float]]("width")

    def height = column[Option[Float]]("height")

    def sizeUoM = column[Option[String]]("sizeUomId")

    def weight = column[Option[Float]]("weight")

    def weightUoM = column[Option[String]]("weightUomId")
  }

  case class PartTrackings(tag: Tag)
    extends TableBase[PartTrackingRecord](tag, "partTracking")
      with PartIdTableSupport[PartTrackingRecord] {

    override def * = (id, partId, partTrackingType, nextValue, isPrimary) <> (PartTrackingRecord.tupled, PartTrackingRecord.unapply)

    def partTrackingType = column[String]("partTrackingType")

    def nextValue = column[String]("nextValue")

    def isPrimary = column[Boolean]("nextValue")
  }

  object PartDefaultLocationDao extends TableQuery(PartDefaultLocations)
    with DaoSupport[String, PartDefaultLocationRecord]

  object PartAccountDao extends TableQuery(PartAccounts)
    with DaoSupport[String, PartAcctRecord]

  object PartSizeWeightDao extends TableQuery(PartSizeWeights)
    with DaoSupport[String, PartSizeWeightRecord]

  object PartTrackingDao extends TableQuery(PartTrackings)
    with DaoSupport[String, PartTrackingRecord]

  object PartDao
    extends TableQuery(Parts)
      with DaoSupport[String, PartRecord]
      with PartDaoSupport {

    override def added(partRecord: PartRecord)(implicit ec: ExecutionContext) = this.forceInsert(partRecord).map(_ => Done)
  }

}
