package com.dream.inventory.setting.impl

import com.dream.inventory.api.SettingService
import com.dream.inventory.setting.impl.uom.UoMServiceImpl
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

class SettingServiceImpl (val registry: PersistentEntityRegistry)(implicit val ec: ExecutionContext)
  extends SettingService with UoMServiceImpl