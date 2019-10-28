package `in`.karthiks.demo.bestpricefinder.service

import `in`.karthiks.demo.bestpricefinder.config.AppConfig
import `in`.karthiks.demo.bestpricefinder.model.Product
import `in`.karthiks.demo.bestpricefinder.model.VendorPrice
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class BestPriceService() {

    private val restTemplate = RestTemplate()

    @Autowired
    private lateinit var appConfig: AppConfig


    fun findFor(upc: String): Product {
        try {
            var jsonString = restTemplate.getForObject(appConfig.baseurl + "/products/{upc}/prices", String::class.java, upc)
            var json = Gson().fromJson(jsonString, JsonObject::class.java)
            var name = json?.asJsonObject?.get("name")?.asString
            var catalog = json?.getAsJsonArray("catalog")
            var bestPriceVendor : VendorPrice? = null

            catalog?.forEach {
                var newVendor = VendorPrice(it.asJsonObject.get("vendorName").asString, it.asJsonObject.get("price").asBigDecimal.setScale(2))
                if(bestPriceVendor == null || (newVendor.price < bestPriceVendor!!.price))
                    bestPriceVendor = newVendor
            }
            return Product(upc, name, bestPriceVendor)
        } catch (ex: HttpClientErrorException) {
            if (ex.statusCode == HttpStatus.BAD_REQUEST )
                throw ProductNotFoundException()
            else
                throw ex
        }
    }

    class ProductNotFoundException : Throwable("Product Not Found!")
}
