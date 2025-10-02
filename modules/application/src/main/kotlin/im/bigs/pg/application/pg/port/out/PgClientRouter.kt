package im.bigs.pg.application.pg.port.out

import org.springframework.stereotype.Component

@Component
class PgClientRouter(private val adapters: List<PgClientOutPort>) {
    fun getAdapter(partnerCode: String): PgClientOutPort =
        adapters.find { it.supports(partnerCode) }
            ?: throw IllegalArgumentException("Not supported partner's code: $partnerCode")
}