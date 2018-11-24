package com.dream.materials.impl

import com.dream.materials.impl.part._
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

object MaterialSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[PartDataModel],

    JsonSerializer[CreatePart],
    JsonSerializer[EnablePart.type],
    JsonSerializer[DisablePart.type],

    JsonSerializer[PartCreated],
    JsonSerializer[PartEnabled.type],
    JsonSerializer[PartDisabled.type]

  )
}
