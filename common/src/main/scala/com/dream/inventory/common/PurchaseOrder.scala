package com.dream.inventory.common

import enumeratum.values._

sealed abstract class PoStatusType(val value: String, val name: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

object PoStatusType extends StringEnum[PoStatusType] with StringPlayJsonValueEnum[PoStatusType] {

  override def values = findValues

  case object BidRequest extends PoStatusType("BidRequest", "Bid Request")

  case object Issued extends PoStatusType("Issued", "Issued")

  case object Picking extends PoStatusType("Picking", "Picking")

  case object Partial extends PoStatusType("Partial", "Partial")

  case object Picked extends PoStatusType("Picked", "Picked")

  case object Shipped extends PoStatusType("Shipped", "Shipped")

  case object Fulfilled extends PoStatusType("Fulfilled", "Fulfilled")

  case object ClosedShort extends PoStatusType("ClosedShort", "Closed Short")

  case object Void extends PoStatusType("Void", "Void")

}

sealed abstract class PoItemType(val value: String, val name: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

object PoItemType extends StringEnum[PoItemType] with StringPlayJsonValueEnum[PoItemType] {
  override def values = findValues

  case object Purchase extends PoStatusType("Purchase", "Purchase")

  case object MiscPurchase extends PoStatusType("Misc.Purchase", "Misc. Purchase")

  case object CreditReturn extends PoStatusType("CreditReturn", "Credit Return")

  case object MiscCredit extends PoStatusType("Misc.Credit", "Misc. Credit")

  case object OutSourced extends PoStatusType("OutSourced", "Out Sourced")

  case object Shipping extends PoStatusType("Shipping", "Shipping")

}


sealed abstract class PoItemStatusType(val value: String, val name: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

object PoItemStatusType extends StringEnum[PoItemStatusType] with StringPlayJsonValueEnum[PoItemStatusType] {

  override def values = findValues

  case object Entered extends PoStatusType("Entered", "Entered")

  case object Picking extends PoStatusType("Picking", "Picking")

  case object Partial extends PoStatusType("Partial", "Partial")

  case object Picked extends PoStatusType("Picked", "Picked")

  case object Shipped extends PoStatusType("Shipped", "Shipped")

  case object Fulfilled extends PoStatusType("Fulfilled", "Fulfilled")

  case object ClosedShort extends PoStatusType("ClosedShort", "Closed Short")

  case object Void extends PoStatusType("Void", "Void")

}