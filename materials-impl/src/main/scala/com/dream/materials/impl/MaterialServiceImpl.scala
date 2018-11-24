package com.dream.materials.impl

import com.dream.materials.api.MaterialService
import com.dream.materials.impl.part.PartServiceImpl
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

class MaterialServiceImpl(val registry: PersistentEntityRegistry)(implicit val ec: ExecutionContext)
  extends MaterialService with PartServiceImpl


