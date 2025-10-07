package im.bigs.pg.external.pg.testpg.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import im.bigs.pg.application.pg.port.out.PgApproveRequest
import im.bigs.pg.application.pg.port.out.PgApproveResult
import im.bigs.pg.application.pg.port.out.PgClientOutPort
import im.bigs.pg.application.pg.port.out.TestPgApproveResult
import im.bigs.pg.external.pg.testpg.config.TestPgProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class TestPgClient(
    private val webClientBuilder: WebClient.Builder,
    private val props: TestPgProperties
): PgClientOutPort {
    private val baseUrl = "https://api-test-pg.bigs.im"
    private val objectMapper = jacksonObjectMapper()
    override fun supports(partnerCode: String): Boolean = partnerCode.contains("TESTPAY")

    override fun approve(request: PgApproveRequest): PgApproveResult {
        // 예: 암호화 호출
        val apiKey = props.apiKey
        val enc = props.enc

        val webClient = webClientBuilder.baseUrl(baseUrl).build()
        val response = webClient.post()
            .uri("/api/v1/pay/credit-card")
            .header("API-KEY", apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(mapOf("enc" to enc))
            .retrieve()
            .bodyToMono(TestPgApproveResult::class.java)
            .block() ?: throw IllegalStateException("PG Server Response is not expected.")

        return PgApproveResult.Companion.from(response)

    }
}