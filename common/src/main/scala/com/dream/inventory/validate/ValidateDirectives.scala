package com.dream.inventory.validate

import cats.data.ValidatedNel
import cats.implicits._


//object Validation {
//
//  sealed trait Error {
//    val message: String
//    val cause: Option[Throwable]
//  }
//
//  case class RequiredError(message: String, cause: Option[Throwable] = None) extends Error
//
//  def validateRequired[A](fieldName: String, value: A): ValidationResult[A] = {
//    value match {
//      case v: String =>
//        if (v.isEmpty) RequiredError(s"${fieldName} is required").invalidNel
//        else value.validNel
//    }
//  }
//
//  type ValidationResult[A] = ValidatedNel[Error, A]
//
//  trait Validator[A] {
//    def validate(value: A): ValidationResult[A]
//  }
//
//}
//
//trait Validation {
//  protected def validate[A: Validator](value: A): ValidationResult[A] = implicitly[Validator[A]].validate(value)
//
//}



