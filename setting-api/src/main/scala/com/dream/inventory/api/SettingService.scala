package com.dream.inventory.api

import com.dream.inventory.api.uom.UoMService
import com.dream.inventory.security.SecurityHeaderFilter
import com.lightbend.lagom.scaladsl.api.Service

trait SettingService extends Service with UoMService{

  final override def descriptor = {
    import Service._

    named("setting").withCalls(
      pathCall("/api/uom/:id", getUoM _)
    ).withHeaderFilter(SecurityHeaderFilter.Composed)
  }

}
