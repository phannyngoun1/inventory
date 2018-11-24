package com.dream.inventory.setting.impl.uom

import java.time.Instant
import java.util.UUID
import slick.collection.heterogeneous.HNil

import akka.Done
import com.dream.inventory.common.dao._

import scala.concurrent.ExecutionContext

trait UoMComponent extends ComponentSupport with UoMComponentSupport {

  import profile.api._

  final case class UoMRecord(
    id: String,
    code: String,
    abbr: String,
    measurementType: String,
    description: String,
    creator: String,
    modifiedBy: String,
    createdAt: Instant = Instant.now(),
    modifiedAt: Option[Instant] = None,
    isActive: Boolean = true,
    deleted: Boolean = false
  ) extends Record with AuditRecord with StatusRecord with SoftDeletableRecord

  final case class UoMs(tag: Tag) extends TableBase[UoMRecord](tag, "uom")
    with AuditTableSupport[UoMRecord]
    with StatusTableSupport[UoMRecord]
    with SoftDeletableTableSupport[UoMRecord]{

    override def * = (
      id :: code :: abbr :: measurementType :: description
        :: creator :: modifiedBy :: createdAt
        :: modifiedAt :: isActive :: deleted :: HNil
      ).mapTo[UoMRecord]

    def code = column[String]("code")

    def abbr = column[String]("abbr")

    def measurementType = column[String]("measurementType")

    def description = column[String]("description")
  }

  object UoMDao extends TableQuery(UoMs) with DaoSupport[String, UoMRecord]
    with UoMDaoSupport
    with WithAdd[UoMRecord]
    with WithUpdate[String, UoMUpdated]
    with WithDelete[String]
    with WithStatus[String] {

    override def add(uoMRecord: UoMRecord)(implicit ec: ExecutionContext): DBIO[Done] =
      this.forceInsert(uoMRecord).map(_ => Done)

    override def update(id: String)(uoMUpdated: UoMUpdated)(implicit ec: ExecutionContext): DBIO[Done] =
      this.filter(_.id === id).map(e => (e.code)).update((uoMUpdated.code)).map(_ => Done)

    override def delete(id: String, userId: UUID)(implicit ec: ExecutionContext): DBIO[Done] =
      this.filter(_.id === id).map(e => (e.deleted, e.modifiedBy)).update((true, userId.toString)).map(_ => Done)

    override def disable(id: String, userId: UUID)(implicit ec: ExecutionContext): DBIO[Done] =
      this.filter(_.id === id).map(e => (e.isActive, e.modifiedBy)).update((false, userId.toString)).map(_ => Done)

    override def activate(id: String, userId: UUID)(implicit ec: ExecutionContext): DBIO[Done] =
      this.filter(_.id === id).map(e => (e.isActive, e.modifiedBy)).update((true, userId.toString)).map(_ => Done)
  }

}
