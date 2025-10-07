package im.bigs.pg.external.pg.kakaopay.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "kakaopay")
class KakaoPayProperties {
    var secretKey: String? = null
}