package com.dream.inventory.common

import java.time.Instant
import java.util.UUID

trait BaseObject extends ObjectIdentity {
  def creator: UUID

  def modifiedBy: Option[UUID]

  def createdAt: Option[Instant]

  def modifiedAt: Option[Instant]
}

trait ObjectType {

  def value: String

  def name: String

  def description: Option[String]

}
