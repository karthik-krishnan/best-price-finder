package `in`.karthiks.demo.bestpricefinder.model

import java.math.BigDecimal

data class Product(
  val upc: String,
  val name: String?,
  val vendor: VendorPrice?
)

class VendorPrice(val vendorName: String, val price: BigDecimal)
