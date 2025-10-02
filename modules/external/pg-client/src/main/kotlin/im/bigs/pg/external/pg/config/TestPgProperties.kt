package im.bigs.pg.external.pg.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
class TestPgProperties(
    @Value("\${test.pg.api-key}") val apiKey: String,
    @Value("\${test.pg.enc}") val enc: String
)