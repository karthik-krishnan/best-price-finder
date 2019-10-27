package `in`.karthiks.demo.bestpricefinder.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "priceservice")
class AppConfig {
    var baseurl: String = ""
}
