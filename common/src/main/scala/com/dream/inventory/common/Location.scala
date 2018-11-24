package com.dream.inventory.common

import enumeratum.values._


sealed abstract class LocationType(val value: String, val name: String, val description: Option[String]) extends StringEnumEntry with ObjectType

case object LocationType extends StringEnum[LocationType] with StringPlayJsonValueEnum[LocationType] {
  override def values = findValues

  case object Consignment extends LocationType("Consignment", "Consignment", Some("Used to store inventory that is on consignment"))

  case object InTransit extends LocationType("InTransit", "In Transit", Some("Used to store inventory that is being moved by a transfer order"))

  case object Inspection extends LocationType("Inspection", "Inspection", Some("Could be used to store inventory that is being inspected"))

  case object Locked extends LocationType("Locked", "Locked", Some("Could be used to designate a secure warehouse area"))

  case object Manufacturing extends LocationType("Manufacturing ", "Manufacturing", Some("Used to store inventory that is being used on a manufacture order. By default, inventory picked for a work order will be moved to the default manufacturing location for the specified location group"))

  case object Picking extends LocationType("Picking ", "Picking", Some("Could be used to store inventory that is being picked"))

  case object Receiving extends LocationType("Receiving", "Receiving", Some("Used to store inventory that is received in the Small.Receiving.png Receiving module. By default, inventory purchased from a vendor will be received into the default receiving location for the specified location group, unless the part has a default location"))

  case object Shipping extends LocationType("Shipping ", "Shipping ", Some("Used to store inventory that is ready to be shipped. By default, inventory that has been picked will be stored in the default shipping location for the specified location group."))

  case object Stock extends LocationType("Stock ", "Stock ", Some("Used to store inventory in the warehouse. Typically, inventory will be picked from a stock location, unless the part has a default location"))

  case object StoreFront extends LocationType("Store Front", "Store Front", Some("Could be used to store inventory in a location that is accessible to customers"))

  case object Vendor extends LocationType("Vendor", "Vendor", Some("Could be used to store inventory that is associated with a vendor"))

}

sealed abstract class ReorderLevelType(val value: String, val name: String, val description: Option[String]) extends StringEnumEntry with ObjectType

case object ReorderLevelType extends StringEnum[ReorderLevelType] with StringPlayJsonValueEnum[ReorderLevelType] {

  override def values = findValues

  case object CompanyWide extends ReorderLevelType("CompanyWide", "Company Wide", Some("Reorder level is for entire company's inventory"))

  case object ByLocationGroup extends ReorderLevelType("ByLocationGroup", "By Location Group", Some("Reorder level is for each of location group"))

}



