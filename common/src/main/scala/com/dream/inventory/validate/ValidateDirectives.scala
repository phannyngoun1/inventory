package com.dream.inventory.validate

import cats.data.ValidatedNel
import com.dream.inventory.validate.Validation._


object Validation {

  sealed trait Error {
    val message: String
    val cause: Option[Throwable]
  }

  type ValidationResult[A] = ValidatedNel[Error, A]

  trait Validator[A] {
    def validate(value: A): ValidationResult[A]
  }
}

trait Validation{
  protected def validateRequest[A: Validator](value: A): ValidationResult[A] = implicitly[Validator[A]].validate(value)
}



