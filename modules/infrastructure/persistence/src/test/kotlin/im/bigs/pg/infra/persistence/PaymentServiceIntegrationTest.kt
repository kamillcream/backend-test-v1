package im.bigs.pg.infra.persistence

import im.bigs.pg.infra.persistence.config.JpaConfig
import im.bigs.pg.infra.persistence.payment.entity.PaymentEntity
import im.bigs.pg.infra.persistence.payment.repository.PaymentJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import java.math.BigDecimal
import java.time.Instant

import kotlin.test.Test

@DataJpaTest
@ContextConfiguration(classes = [JpaConfig::class])
class 결제저장소Test @Autowired constructor(
    private val repo: PaymentJpaRepository
) {

    @Test
    fun `결제 엔티티를 저장하고 조회할 수 있다`() {
        // given
        val payment = PaymentEntity(
            partnerId = 1L,
            amount = BigDecimal("10000"),
            appliedFeeRate = BigDecimal("0.02"),
            feeAmount = BigDecimal("200"),
            netAmount = BigDecimal("9800"),
            cardBin = "123456",
            cardLast4 = "1111",
            approvalCode = "APPRV001",
            approvedAt = Instant.now(),
            status = "APPROVED",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )

        // when
        val saved = repo.save(payment)

        // then
        val found = repo.findById(saved.id!!)
            .orElseThrow { IllegalStateException("Payment not found") }

        assertEquals(saved.id, found.id)
        assertEquals(saved.amount, found.amount)
    }
}