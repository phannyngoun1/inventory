package com.dream.inventory.setting.impl


import com.dream.inventory.setting.impl.uom._
import com.lightbend.lagom.scaladsl.playjson._

import scala.collection.immutable

object SettingSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: immutable.Seq[JsonSerializer[_]] = List(
    JsonSerializer[UoMDataModel],

    JsonSerializer[CreateUoM],
    JsonSerializer[GetUoM.type],
    JsonSerializer[UpdateUoM],
    JsonSerializer[DeleteUoM],
    JsonSerializer[DisableUoM],
    JsonSerializer[ActivateUoM],

    JsonSerializer[UoMCreated],
    JsonSerializer[UoMUpdated],


  )
}
