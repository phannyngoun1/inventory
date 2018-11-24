package com.dream.inventory.common

import java.util.UUID

trait ObjectIdentity {
  def id: Option[UUID]
}
