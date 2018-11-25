package com.dream.inventory.common

import java.time.Instant

import enumeratum.values._
import julienrf.json.derived
import play.api.libs.json._


sealed abstract class PartType(val value: String, val name: String, val description: Option[String]) extends StringEnumEntry with ObjectType

object PartType extends StringEnum[PartType] with StringPlayJsonValueEnum[PartType] {

  override def values = findValues

  case object Inventory extends PartType("Inventory", "Inventory", Some("Can be purchased and sold and stocked in inventory"))

  case object Service extends PartType("Service", "Service", Some("Can be purchased and sold but is not stocked in inventory"))

  case object Labor extends PartType("Labor", "Labor", Some("Cannot be purchased or sold, but can be added during manufacturing or reconciling"))

  case object Overhead extends PartType("Overhead", "Overhead", Some("Can be included as a component of a part's cost during manufacturing"))

  case object NonInventory extends PartType("NonInventory", "Non-Inventory", Some("Can be purchased and sold but is not tracked in inventory"))

  case object InternalUsed extends PartType("InternalUsed", "Internal Use", Some("Can be purchased but is not sold or stocked in inventory"))

  case object CapitalEquipment extends PartType("CapitalEquipment", "Capital Equipment", Some("Equipment that depreciates in value and can be purchased but is not sold or stocked in inventory"))

  case object Shipping extends PartType("Shipping", "Shipping", Some("Can be purchased or sold, but is not stocked in inventory"))

}


sealed abstract class PartTrackingType(val value: String, val name: String, abbr: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

case object PartTrackingType extends StringEnum[PartTrackingType] with StringPlayJsonValueEnum[PartTrackingType] {
  override def values = findValues

  case object Text extends PartTrackingType("Text", "Text", "Txt")

  case object Date extends PartTrackingType("Date", "Date", "Date")

  case object Number extends PartTrackingType("Number", "Number", "Nr")

  case object SerialNumber extends PartTrackingType("SerialNumber", "Serial Number", "SN(s)")

  case object ExpirationDate extends PartTrackingType("ExpirationDate", "Expiration Date", "ExpDate")

  case object RevisionLevel extends PartTrackingType("RevisionLevel", "Revision Level", "Rev#")

  case object LotNumber extends PartTrackingType("LotNumber", "Lot Number", "Lot#")


}

/*
object PartTrackingType extends Enumeration {
  type Type = Value
  val TextType, DateType, NumberType = Value

  implicit val format: Format[Type] = enumFormat(PartTrackingType)
}
*/



sealed trait PartTracking {
  def toString: String
}

object PartTracking {
  implicit val formats: OFormat[PartTracking] = derived.flat.oformat((__ \ "type").format[String])
}

sealed abstract class AbstractPartTracking[T] extends PartTracking{
  def value: T
  def trackingType: PartTrackingType

  override def toString: String = value match {
    case v: String => v
    case _ => "To be implemented"
  }

}


case class TrackingBySerial(override val value: String) extends AbstractPartTracking[String] {
  override val trackingType = PartTrackingType.Text
}

object TrackingBySerial {
  implicit val format: Format[TrackingBySerial] = Json.format
}


case class TrackingByExpiryDate(override val value: Instant) extends AbstractPartTracking[Instant] {
  override val trackingType = PartTrackingType.Date
}

object TrackingByExpiryDate {
  implicit val format: Format[TrackingByExpiryDate] = Json.format
}


case class TrackingByLot(override val value: String) extends AbstractPartTracking[String] {
  override val trackingType = PartTrackingType.Text
}

object TrackingByLot {
  implicit val format: Format[TrackingByLot] = Json.format
}


case class TrackingBySerials(override val value: List[Map[String, Boolean]]) extends AbstractPartTracking[List[Map[String, Boolean]]] {
  override val trackingType = PartTrackingType.SerialNumber
}

object PartTrackingDataModel {
  implicit val format: Format[TrackingBySerials] = Json.format
}


