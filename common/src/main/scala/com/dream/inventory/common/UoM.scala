package com.dream.inventory.common

import enumeratum.values._


sealed abstract class MeasurementType(val value: String, val name: String, val description: Option[String] = None) extends StringEnumEntry with ObjectType

case object MeasurementType extends StringEnum[MeasurementType] with StringPlayJsonValueEnum[MeasurementType] {
  override def values = findValues

  case object Count extends MeasurementType("Count", "Count")

  case object Weight extends MeasurementType("Weight", "Weight")

  case object Size extends MeasurementType("Size", "Size")

  case object Area extends MeasurementType("Area", "Area")

  case object Volume extends MeasurementType("Volume", "Volume")

  case object Time extends MeasurementType("Time", "Time")

}

/*
object MeasurementType extends Enumeration {
  val Count, Weight, Size, Area, Volume, Time = Value

  type Type = Value
  implicit val format: Format[Type] = enumFormat(MeasurementType)
}
*/
sealed abstract class Measurement {

  def code: String

  def abbr: String

  def measurementType: MeasurementType

  def description: String

}


case object Inch extends Measurement {

  override val code: String = "Inch"

  override val abbr: String = "in"

  override val measurementType = MeasurementType.Size

  override val description: String = "inch"

  //  implicit val format: Format[Inch.type] = Json.format

}

case object foot extends Measurement {

  override val code: String = "Foot"

  override val abbr: String = "ft"

  override val measurementType = MeasurementType.Size

  override val description: String = "foot"

  //  implicit val format: Format[foot.type] = Json.format
}

case object yard extends Measurement {

  override val code: String = "Yard"

  override val abbr: String = "ft"

  override val measurementType = MeasurementType.Size

  override val description: String = "yard"

  //  implicit val format: Format[yard.type] = Json.format
}

