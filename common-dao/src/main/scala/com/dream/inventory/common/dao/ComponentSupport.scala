package com.dream.inventory.common.dao

import java.time.Instant
import java.util.UUID

trait ComponentSupport {

  val profile: slick.jdbc.JdbcProfile

  import profile.api._

  implicit val dateTimeColumnType = MappedColumnType.base[Instant, java.sql.Timestamp](
    { zdt =>
      new java.sql.Timestamp(zdt.toEpochMilli)
    }, { ts =>
      Instant.ofEpochMilli(ts.getTime)
    }
  )


  implicit val uuIdColumnType =  MappedColumnType.base[UUID, String](
    uuid => uuid.toString,
    st => UUID.fromString(st)
  )

  trait DaoSupport[Id, Entity]

  trait Record {
    val id: String
  }

  trait SoftDeletableRecord {
    val deleted: Boolean
  }

  trait AuditRecord {
    val creator: String
    val modifiedBy: String
    val createdAt: Instant
    val modifiedAt: Option[Instant]
  }

  trait StatusRecord {
    val isActive: Boolean
  }

  trait StatusTableSupport[T] {
    this: TableBase[T] =>
    def isActive = column[Boolean]("isActive")
  }

  trait AuditTableSupport[T] {
    this: TableBase[T] =>

    def creator = column[String]("creator")

    def modifiedBy = column[String]("modifiedBy")

    def createdAt = column[Instant]("createdAt")

    def modifiedAt = column[Option[Instant]]("modifiedAt")

  }

  trait SoftDeletableTableSupport[T] {
    this: TableBase[T] =>

    def deleted = column[Boolean]("deleted")

  }

  abstract class TableBase[T](_tableTag: Tag, _tableName: String, _schemaName: Option[String] = None)
    extends Table[T](_tableTag, _schemaName, _tableName) {
    def id: Rep[String] = column[String]("id", O.PrimaryKey)
  }

}
