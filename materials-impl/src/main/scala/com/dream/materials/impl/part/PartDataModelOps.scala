package com.dream.materials.impl.part

import java.time.Instant
import java.util.UUID

import com.dream.inventory.common.dao.AuditData
import com.dream.materials.api.part._

trait PartDataModelOps {

  type Result[A <: PartError] = Either[A, PartDataModel]

  implicit class PartOperations(part: PartDataModel) {
    def create(
      id: UUID, partBasicInfo: PartBasicInfo,
      partTrackingMethods: List[PartTrackingMethod] = List.empty,
      initialInventory: Option[InitialInventory] = None,
      defaultLocation: List[UUID] = List.empty,
      defaultVendor: Option[DefaultVendor] = None,
      defaultAccount: Option[DefaultAccount] = None
    ): Result[CreatePartError] = {

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
    }

    def receiving(qty: Float, unitCost: Double, date: Instant, partTracking: List[PartTrackingDataModel]): Result[PartError] = {

      Right(part.copy(

      ))
    }
  }



}
