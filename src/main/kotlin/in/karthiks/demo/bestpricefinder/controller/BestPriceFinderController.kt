package `in`.karthiks.demo.bestpricefinder.controller

import `in`.karthiks.demo.bestpricefinder.model.Product
import `in`.karthiks.demo.bestpricefinder.service.BestPriceService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping()
class BestPriceFinderController(private val bestPriceService: BestPriceService) {

    @GetMapping("find-best-price/{upc}")
    fun findBestPrice(@PathVariable upc: String) : Product {
        return bestPriceService.findFor(upc)
    }

    @ExceptionHandler(value = [BestPriceService.ProductNotFoundException::class])
    fun handleException(response: HttpServletResponse) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UPC!")
    }

}
