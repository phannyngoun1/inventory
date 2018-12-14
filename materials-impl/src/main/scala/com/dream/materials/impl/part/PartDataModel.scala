package com.dream.materials.impl.part

import java.time.Instant
import java.util.UUID

import akka.Done
import com.dream.inventory.common._
import com.dream.inventory.common.dao.{AuditData, BaseModel, RecordStatus}
import com.dream.inventory.datamodel.DataModelMngt.DataError
import com.dream.inventory.utils.JsonFormats.singletonFormat
import com.dream.materials.api.part.{PartTrackingMethod, _}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import play.api.libs.json.{Format, Json}


/** *************************Data *****************************/

case class PartDefaultLocationDataModel(
  groupLocationId: UUID,
  locationId: UUID
)

object PartDefaultLocationDataModel {
  implicit val format: Format[PartDefaultLocationDataModel] = Json.format
}

case class PartAcctDataModel(
  assetAccountId: Option[UUID],
  cosgsAccountId: Option[UUID],
  adjustmentAccountId: Option[UUID],
  scrapAccountId: Option[UUID]
)

object PartAcctDataModel {
  implicit val format: Format[PartAcctDataModel] = Json.format
}


case class PartSizeWeightDataModel(
  length: Option[Float],
  width: Option[Float],
  height: Option[Float],
  sizeUoM: Option[UUID],
  weight: Option[Float],
  weightUoM: Option[UUID]
)

object PartSizeWeightDataModel {
  implicit val format: Format[PartSizeWeightDataModel] = Json.format
}

case class PartTrackingMethodDataModel(
  partTrackingType: PartTrackingType,
  nextValue: Option[String],
  isPrimary: Boolean
)


object PartTrackingMethodDataModel {
  implicit val format: Format[PartTrackingMethodDataModel] = Json.format
}

case class PartReorderLevelDataModel(
  groupLocationId: UUID,
  rop: Float, // Reorder Point
  oul: Option[Float] // Order Up to Level
)

object PartReorderLevelDataModel {
  implicit val format: Format[PartReorderLevelDataModel] = Json.format
}

case class InventoryDataModel(

  onHand: Float = 0,
  allocated: Float = 0,
  notAvailable: Float = 0,
  dropShip: Float = 0,
  committed: Float = 0,
  shot: Float = 0,
  onOrder: Float = 0,
  availableToPick: Float = 0,
  availableForSale: Float = 0,

  avgCost: Double = 0,
  totalAvgCost: Double = 0,

  stdCost: Double = 0,
  totalStdCost: Double = 0,

  costingLayers: List[CostLayerDataModel] = List.empty,
  partTracking: List[PartTrackingDataModel] = List.empty
) {
  def receiving(qty: Float, unitCost: Double, date: Instant, partTracking: List[PartTrackingDataModel], validate: InventoryDataModel => Boolean): InventoryDataModel = {

    copy(
      onHand = onHand + qty,
      availableForSale = availableForSale + qty,
      availableToPick = availableToPick + qty,
      totalAvgCost = totalAvgCost + (qty * unitCost),
      avgCost = (totalAvgCost + (qty * unitCost)) / (onHand + qty),
      costingLayers = CostLayerDataModel(date, qty, unitCost, qty * unitCost) :: costingLayers
      //partTracking = addToTracking(partTracking)
    )
  }

//  private def addToTracking(newPartTracking: List[PartTrackingDataModel]): List[PartTrackingDataModel] = {
//
//    val all = List.concat(partTracking, newPartTracking)
//
//    List.concat(
//      all.filter(_.trackingTrackingValues.exists(_.trackingType == PartTrackingType.SerialNumber))
//        .groupBy(f => (f.locationId, f.trackingTrackingValues.filter(_.trackingType != PartTrackingType.SerialNumber).sorted))
//        .map(m => PartTrackingDataModel(
//          locationId = m._1._1,
//          qty = m._2.reduce(_.qty + _.qty),
//          trackingTrackingValues = mergeSerialTrackingValues(m._2.map(_.trackingTrackingValues.filter(_.trackingType != PartTrackingType.SerialNumber)).reduce(List.concat(_, _))) :: m._1._2
//        )).toList,
//      all.filter(_.trackingTrackingValues.exists(_.trackingType != PartTrackingType.SerialNumber))
//        .groupBy(f => (f.locationId, f.trackingTrackingValues.sorted))
//        .map(m => PartTrackingDataModel(
//          locationId = m._1._1,
//          qty = m._2.reduce(_.qty + _.qty),
//          trackingTrackingValues = m._1._2
//        )).toList
//    )
//  }

//  private def mergeSerialTrackingValues(values: List[PartTrackingValue]): PartTrackingValue = {
//
//    TrackingBySerials(
//      values.map {
//        _ match {
//          case v: TrackingBySerials => v.value
//        }
//      }.reduce(List.concat(_, _))
//    )
//  }
}

object InventoryDataModel {
  implicit val format: Format[InventoryDataModel] = Json.format
}

case class PartTrackingDataModel(
  locationId: UUID,
  qty: Float,
  trackingTrackingValues: List[PartTrackingValue]
)

object PartTrackingDataModel {
  implicit val format: Format[PartTrackingDataModel] = Json.format
}

case class CostLayerDataModel(
  date: Instant,
  qty: Float,
  unitCost: Double,
  totalCost: Double,
  committed: Boolean = false
)

object CostLayerDataModel {
  implicit val format: Format[CostLayerDataModel] = Json.format
}

case class PartDataModel(

  id: UUID,

  partNr: String,

  description: String,

  partType: PartType,

  upc: Option[String] = None,

  uomId: Option[String] = None,

  abcCode: Option[String] = None,

  stCost: Option[Float] = None,

  defaultBoMId: Option[UUID] = None,

  defaultProductId: Option[UUID] = None,

  details: Option[String] = None,

  pickInUoMPartOnly: Boolean = false,

  defaultLocations: List[PartDefaultLocationDataModel] = List.empty,

  accounts: Option[PartAcctDataModel] = None,

  sizeWeight: Option[PartSizeWeightDataModel] = None,

  trackingMethod: List[PartTrackingMethodDataModel] = List.empty,

  reorderLevels: List[PartReorderLevelDataModel] = List.empty,

  inventory: Option[InventoryDataModel] = None,

  auditData: AuditData,

  recordStatus: RecordStatus = RecordStatus()

) extends BaseModel {

  def withTrackingMethod(partTrackingMethods: List[PartTrackingMethod]): Either[PartError, PartDataModel] = {
    Right(copy(trackingMethod = partTrackingMethods.map(t => PartTrackingMethodDataModel(t.partTrackingType, t.nextValue, t.isPrimary))))
  }

  def withInitialInventory(pInventory: InitialInventory): Either[PartError, PartDataModel] = {

      Right(copy(inventory = inventory.map(_.receiving(
      pInventory.qty,
      unitCost = pInventory.unitCost,
      date = pInventory.date,
      partTracking = List(PartTrackingDataModel(pInventory.locationId, pInventory.qty, pInventory.partTrackingValue)),
      { _ => true }
      ))))
  }

  def disable: Either[PartError, PartDataModel] = Right(copy(
    recordStatus = recordStatus.copy(isActive = false)
  ))

  def enable: Either[PartError, PartDataModel] = Right(copy(
    recordStatus = recordStatus.copy(isActive = true)
  ))
}

object PartDataModel {

  implicit val format: Format[PartDataModel] = Json.format

  def create(
    id: UUID, partBasicInfo: PartBasicInfo,
    partTrackingMethods: List[PartTrackingMethod] = List.empty,
    initialInventory: Option[InitialInventory] = None,
    defaultLocation: List[UUID] = List.empty,
    defaultVendor: Option[DefaultVendor] = None,
    defaultAccount: Option[DefaultAccount] = None
  ): Either[PartError, PartDataModel]  = {

    import cats.Monad
    import cats.instances.either._


    val part = PartDataModel(
      id = id,
      partNr = partBasicInfo.partNr,
      description = partBasicInfo.description,
      partType = partBasicInfo.partType,
      upc = partBasicInfo.upc,
      uomId = partBasicInfo.uomId,
      auditData = AuditData(
        creator = partBasicInfo.creator,
        modifiedBy = None
      )
    )


    Right(part)
      .map(_.withTrackingMethod(partTrackingMethods).toObjOrThrow)
      .map(_.withInitialInventory(initialInventory.get).toObjOrThrow)


//    part.withTrackingMethod(partTrackingMethods).toObjOrThrow
//      .whenDefined(initialInventory)((init, part) =>  part.withInitialInventory(init).toObjOrThrow)
//      .whenDefined(defaultAccount)((_, part) => part.copy(accounts = defaultAccount.map(f =>
//        PartAcctDataModel(
//          assetAccountId = f.adjustmentAcctId,
//          cosgsAccountId = f.cogsAcctId,
//          adjustmentAccountId =  f.adjustmentAcctId,
//          scrapAccountId = f.scrapAcctId)))
//      )
  }
}

/** *************************Command *****************************/
sealed trait PartCommand

case object GetPart extends PartCommand with ReplyType[Option[PartDataModel]] {
  implicit val format: Format[GetPart.type] = singletonFormat(GetPart)
}

case class CreatePart(dataModel: PartDataModel) extends PartCommand with ReplyType[Done]

case class ReceivePart()

object CreatePart {
  implicit val format: Format[CreatePart] = Json.format
}

object DisablePart extends PartCommand with ReplyType[Done] {
  implicit val format: Format[DisablePart.type] = singletonFormat(DisablePart)
}

case object EnablePart extends PartCommand with ReplyType[Done] {
  implicit val format: Format[EnablePart.type] = singletonFormat(EnablePart)
}


/** *************************Event *****************************/

sealed trait PartEvent extends AggregateEvent[PartEvent] {
  override def aggregateTag: AggregateEventTagger[PartEvent] = PartEvent.Tag
}

object PartEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[PartEvent](NumShards)
}

case class PartCreated(part: PartDataModel) extends PartEvent

object PartCreated {
  implicit val format: Format[PartCreated] = Json.format
}

case object PartDisabled extends PartEvent {
  implicit val format: Format[PartDisabled.type] = singletonFormat(PartDisabled)
}

case object PartEnabled extends PartEvent {
  implicit val format: Format[PartEnabled.type] = singletonFormat(PartEnabled)
}


/** *************************Error *****************************/

sealed abstract class PartError(val message: String) extends DataError

case class DefaultPartError(override val message: String) extends PartError(message)
case class CreatePartError(override val message: String) extends PartError(message)
case class ReceivingError(override val message: String) extends PartError(message)
