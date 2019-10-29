package com.kroger.dc.savings.pact

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.core.model.annotations.PactFolder
import org.apache.http.client.fluent.Request
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(PactConsumerTestExt::class)
@PactFolder("target/pacts")
class ProductPriceContractTest {
    @Pact(provider = "ProductPriceService", consumer = "BestPriceFinder")
    fun getPrices(builder: PactDslWithProvider): RequestResponsePact {
        val responseBody = PactDslJsonBody()
                .stringMatcher("upc", "\\d*", "12341234")
                .stringType("name")
                .eachLike("catalog")
                .stringType("vendorName")
                .numberType("price")
                .closeArray()
                .asBody()
        return builder
                .given("Valid UPC")
                .uponReceiving("Request for Best Price")
                .matchPath("/products/(\\d+)/prices")
                .method("GET")
                .willRespondWith()
                .headers(mapOf("Content-Type" to "application/json"))
                .body(responseBody)
                .status(200)
                .toPact()
    }

    @PactTestFor(pactMethod = "getPrices")
    @Test
    @Throws(IOException::class)
    fun testForResponseWithValidUPC(mockServer: MockServer) {
        val httpResponse = Request.Get(mockServer.getUrl() + "/products/12345/prices")
                .execute().returnResponse()
        assertEquals(200, httpResponse.getStatusLine().getStatusCode())
    }
}