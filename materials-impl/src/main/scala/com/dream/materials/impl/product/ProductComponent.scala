package com.dream.materials.impl.product

import java.time.Instant

import com.dream.inventory.common.SoItemType
import com.dream.inventory.common.dao.ComponentSupport

trait ProductComponent extends ComponentSupport with ProductComponentSupport {

  case class PartSizeWeightRecord(
    id: String,
    productId: String,
    length: Option[Float],
    width: Option[Float],
    height: Option[Float],
    sizeUoM: Option[String],
    weight: Option[Float],
    weightUoM: Option[String]
  ) extends Record

  case class SubstituteProduct(
    id: String,
    productId: String,
    substituteProductId: String
  ) extends Record

  case class ProductRecord(

    id: String,
    partId: String,
    productNr: String,
    upc: Option[String],
    suk: Option[String],
    defaultSoItemType: SoItemType,
    description: String,
    price: Float,
    qbClassId: Option[String],
    canSellInOtherUoMs: Boolean = true,
    isShowInSoSelection: Boolean =true,
    isTaxable: Boolean = true,
    taxId: String,
    creator: String,
    modifiedBy: String,
    createdAt: Instant = Instant.now(),
    modifiedAt: Option[Instant] = None,
    isActive: Boolean = true,
    deleted: Boolean = false
  ) extends Record with AuditRecord with StatusRecord with SoftDeletableRecord

}
