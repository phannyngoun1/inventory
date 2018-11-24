package com.dream.inventory.common.dao

import java.time.Instant
import java.util.UUID

trait BaseModel {
  def id: UUID
  def creator: UUID
  def modifiedBy: UUID
  def createdAt: Instant
  def modifiedAt: Option[Instant]
  def isActive: Boolean
  def isDeleted: Boolean
}