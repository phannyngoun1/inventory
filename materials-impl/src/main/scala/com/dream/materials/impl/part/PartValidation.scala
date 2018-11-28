package com.dream.materials.impl.part

import cats.implicits._
import com.dream.inventory.validate.Validation._
import com.dream.materials.api.part.CreatePartRequest

object PartValidation {

  implicit object NewPartRequestValidator extends Validator[CreatePartRequest] {

    override def validate(value: CreatePartRequest): ValidationResult[CreatePartRequest] = {
      (
        validateRequired("Part Number", value.partBasicInfo.partNr),
        validateRequired("Part Description", value.partBasicInfo.description)
      ).mapN {
        case (_: String, _: String) =>
          value
      }
    }
  }

}

