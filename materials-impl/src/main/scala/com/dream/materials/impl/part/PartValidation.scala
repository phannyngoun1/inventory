package com.dream.materials.impl.part


import cats.implicits._
import com.dream.inventory.common.PartType
import com.dream.inventory.validate.Validation._
import com.dream.inventory.validate.TypeValidator._
import com.dream.materials.api.part.CreatePartRequest

trait PartValidation {

  implicit val check: Validator[CreatePartRequest] = Validator.doValidate { value =>

    (validateNotEmptyString("Part Number", value.partBasicInfo.partNr), validateNotEmptyString("Part Type", "doe")).mapN((_, _) => value)

  }
}



