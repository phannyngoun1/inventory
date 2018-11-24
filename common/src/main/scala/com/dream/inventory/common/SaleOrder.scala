package com.dream.inventory.common

import enumeratum.values._

sealed abstract class SoType(val value: String, val name: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

object SaleOrder extends StringEnum[SoType] with StringPlayJsonValueEnum[SoType] {

  override def values = findValues

  case object Standard extends SoType("Standard", "Standard")

  case object Pos extends SoType("POS", "Point of Sale")

}

sealed abstract class SoStatusType(val value: String, val name: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

object SoStatusType extends StringEnum[SoStatusType] with StringPlayJsonValueEnum[SoStatusType] {

  override def values = findValues

  case object Issued extends SoStatusType("Issued", "Issued")

  case object InProgress extends SoStatusType("InProgress", "In Progress")

  case object Estimate extends SoStatusType("Estimate", "Estimate")

  case object Fulfilled extends SoStatusType("Fulfilled", "Fulfilled")

  case object Expired extends SoStatusType("Expired", "Expired")

  case object ClosedShort extends SoStatusType("ClosedShort", "Closed Short")

  case object Void extends SoStatusType("Void", "Void")


}

sealed abstract class SoItemStatusType(val value: String, val name: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

object SoItemStatusType extends StringEnum[SoItemStatusType] with StringPlayJsonValueEnum[SoItemStatusType] {

  override def values = findValues

  case object Entered extends SoStatusType("Entered", "Entered")

  case object AwaitingBuild extends SoStatusType("AwaitingBuild", "Awaiting Build")

  case object Building extends SoStatusType("Building", "Building")

  case object Built extends SoStatusType("Built", "Built")

  case object Picking extends SoStatusType("Picking", "Picking")

  case object Partial extends SoStatusType("Partial", "Partial")

  case object Picked extends SoStatusType("Picked", "Picked")

  case object Fulfilled extends SoStatusType("Fulfilled", "Fulfilled")

  case object ClosedShort extends SoStatusType("ClosedShort", "Closed Short")

  case object Voided extends SoStatusType("Voided", "Voided")

}


sealed abstract class SoItemType(val value: String, val name: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

object SoItemType extends StringEnum[SoItemType] with StringPlayJsonValueEnum[SoItemType] {

  override def values = findValues

  case object Sale extends SoStatusType("Sale", "Sale ")

  case object MiscSale extends SoStatusType("Misc.Sale", "Misc. Sale")

  case object DropShip extends SoStatusType("DropShip", "Drop Ship")

  case object CreditReturn extends SoStatusType("CreditReturn", "Credit Return")

  case object MiscCredit extends SoStatusType("Misc.Credit", "Misc. Credit")

  case object DiscountPercentage extends SoStatusType("DiscountPercentage", "Discount Percentage")

  case object DiscountAmount extends SoStatusType("DiscountAmount", "Discount Amount")

  case object Subtotal extends SoStatusType("Subtotal", "Subtotal")

  case object AssocPrice extends SoStatusType("Assoc.Price", "Assoc. Price")

  case object Shipping extends SoStatusType("Shipping", "Shipping")

  case object Tax extends SoStatusType("Tax", "Tax")

  case object Kit extends SoStatusType("Kit", "Kit")

  case object Note extends SoStatusType("Note", "Note")

}
