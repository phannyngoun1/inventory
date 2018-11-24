package com.dream.inventory.setting.impl.uom

import java.util.UUID

import akka.NotUsed
import com.dream.inventory.api
import com.dream.inventory.api.uom.UoMService
import com.dream.inventory.security.ServerSecurity.authenticated
import com.dream.inventory.setting.impl.SettingServiceImpl
import com.dream.inventory.utils.PagingState
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.server.ServerServiceCall


trait UoMServiceImpl extends UoMService { this: SettingServiceImpl =>


  override def createUoM: ServiceCall[api.uom.UoM, api.uom.UoM] = authenticated(userId => ServerServiceCall { uom => {
    val uomP = UoMDataModel(
      id = uom.safeId,
      code = uom.code,
      abbr = uom.abbr,
      measurementType = uom.measurementType,
      description = uom.description,
      creator = uom.creator,
      modifiedBy = uom.modifiedBy
    )

    entityRef(uomP.id).ask(CreateUoM(uomP)).map { _ =>
      uom
    }
  }
  })

  override def getUoM(id: UUID): ServiceCall[NotUsed, api.uom.UoM] = authenticated(_ => ServerServiceCall { _ => {
    entityRef(id).ask(GetUoM).map {
      case Some(part) => convertUoM(part)
      case None => throw NotFound(s"Uom Id ${id.toString} not found");
    }
  }
  })

  private def entityRef(uomId: UUID) = entityRefString(uomId.toString)

  private def entityRefString(uomId: String) = registry.refFor[UoMEntity](uomId)

  private def convertUoM(uoM: UoMDataModel): api.uom.UoM = api.uom.UoM(
    id = Some(uoM.id),
    code = uoM.code,
    abbr = uoM.abbr,
    measurementType = uoM.measurementType,
    description = uoM.description,
    creator = uoM.creator,
    modifiedBy = uoM.modifiedBy

  )

  override def getUoMs(page: Option[String]): ServiceCall[NotUsed, PagingState[api.uom.UoM]] = ???
}
