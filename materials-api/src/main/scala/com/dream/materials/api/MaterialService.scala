package com.dream.materials.api

import com.dream.inventory.security.SecurityHeaderFilter
import com.dream.materials.api.part.PartService
import com.lightbend.lagom.scaladsl.api.Service

trait MaterialService extends Service with PartService {
  final override def descriptor = {
    import Service._

    named("material").withCalls(
      pathCall("/api/part/:id", getPart _),
      pathCall("/api/part", createPart)
    ).withHeaderFilter(SecurityHeaderFilter.Composed)
  }
}
