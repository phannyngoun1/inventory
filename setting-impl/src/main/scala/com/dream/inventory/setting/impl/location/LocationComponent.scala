package com.dream.inventory.setting.impl.location

import java.time.Instant

import com.dream.inventory.common.dao.ComponentSupport
import slick.collection.heterogeneous.HNil

trait LocationComponent extends ComponentSupport with LocationComponentSupport {

  import profile.api._

  case class LocationGroupRecord(
    id: String,
    name: String,
    qbClassId: String,
    creator: String,
    modifiedBy: String,
    createdAt: Instant = Instant.now(),
    modifiedAt: Option[Instant] = None,
    isActive: Boolean = true
  ) extends Record with AuditRecord with StatusRecord

  case class LocationGroups(tag: Tag)
    extends TableBase[LocationGroupRecord](tag, "location")
      with AuditTableSupport[LocationGroupRecord]
      with StatusTableSupport[LocationGroupRecord] {

    def name = column[String]("name")

    def qbClassId = column[String]("qbClassId")

    override def * = (id :: name :: qbClassId :: creator :: modifiedBy :: createdAt :: modifiedAt :: isActive :: HNil).mapTo[LocationGroupRecord]
  }

  case class LocationRecord(
    id: String,
    name: String,
    description: String,
    locationGroupId: String,
    defaultCustomer: String,
    locationType: String,
    //Default location for this location type and group
    isDefaultLocation: Boolean,
    isAvailableForSale: Boolean,
    isPickable: Boolean,
    isReceivable: Boolean,
    creator: String,
    modifiedBy: String,
    createdAt: Instant = Instant.now(),
    modifiedAt: Option[Instant] = None,
    isActive: Boolean = true
  ) extends Record with AuditRecord with StatusRecord

  case class Locations(tag: Tag)
    extends TableBase[LocationRecord](tag, "location")
      with AuditTableSupport[LocationRecord]
      with StatusTableSupport[LocationRecord] {

    def name = column[String]("name")

    def description = column[String]("description")

    def locationGroupId = column[String]("locationGroupId")

    def defaultCustomerId = column[String]("defaultCustomerId")

    def locationType = column[String]("locationType")

    def isDefaultLocation = column[Boolean]("isDefaultLocation")

    def isAvailableForSale = column[Boolean]("isAvailableForSale")

    def isPickable = column[Boolean]("isPickable")

    def isReceivable = column[Boolean]("isReceivable")

//    override def * = (id, name, description, locationGroupId, defaultCustomerId, locationType, isDefaultLocation, isAvailableForSale, isPickable, isReceivable, creator, modifiedBy, createdAt, modifiedAt, isActive) <> (LocationRecord.tupled, LocationRecord.unapply)

    override def * = (
      id :: name :: description :: locationGroupId :: defaultCustomerId
        :: locationType :: isDefaultLocation :: isAvailableForSale
        :: isPickable :: isReceivable :: creator :: modifiedBy
        :: createdAt :: modifiedAt :: isActive :: HNil).mapTo[LocationRecord]
  }

  object locationGroupDao extends TableQuery(LocationGroups) with DaoSupport[String, LocationGroupRecord]
    with LocationGroupDaoSupport

  object locationDao extends TableQuery(Locations) with DaoSupport[String, LocationRecord]
    with LocationDaoSupport

}
