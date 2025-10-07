package im.bigs.pg.api.im.bigs.pg.api.payment.service

import im.bigs.pg.api.payment.dto.CreatePaymentRequest
import im.bigs.pg.api.payment.dto.PaymentResponse
import im.bigs.pg.api.payment.dto.QueryResponse
import im.bigs.pg.infra.persistence.payment.repository.PaymentJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest @Autowired constructor(
    private val restTemplate: TestRestTemplate,
    private val paymentRepository: PaymentJpaRepository,
) {

    @LocalServerPort
    private var port: Int = 8080

    @Test
    fun `결제 생성 API - 정상 요청 시 200 OK와 저장된 결제 반환`() {
        // given
        val request = CreatePaymentRequest(
            partnerId = 1L,
            amount = BigDecimal("10000"),
            cardBin = "123456",
            cardLast4 = "1111",
            productName = "테스트 상품"
        )

        val url = "http://localhost:$port/api/v1/payments"

        // when
        val response: ResponseEntity<PaymentResponse> = restTemplate.exchange(
            url,
            HttpMethod.POST,
            HttpEntity(request),
            PaymentResponse::class.java
        )

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        requireNotNull(body)

        assertThat(body.partnerId).isEqualTo(1L)
        assertThat(body.amount).isEqualByComparingTo(BigDecimal("10000"))

        val saved = paymentRepository.findById(body.id!!)
            .orElseThrow { IllegalStateException("Payment not found") }
        assertThat(saved.amount).isEqualByComparingTo(BigDecimal("10000"))
        assertThat(saved.status).isEqualTo("APPROVED")
    }

    @Test
    fun `결제 목록 조회 API - 정상 요청 시 200 OK와 결제 목록 반환`() {
        // given
        val partnerId = 1L
        val status = "APPROVED"
        val from = "2025-10-01 00:00:00"
        val to = "2025-10-07 23:59:59"
        val limit = 10

        val url = "http://localhost:$port/api/v1/payments?" +
                "partnerId=$partnerId&status=$status&from=$from&to=$to&limit=$limit"

        // when
        val response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            QueryResponse::class.java
        )

        // then
        println("응답 상태: ${response.statusCode}")
        println("응답 본문: ${response.body}")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.items).isNotNull
    }
}