package im.bigs.pg.api.config

import im.bigs.pg.infra.persistence.partner.entity.FeePolicyEntity
import im.bigs.pg.infra.persistence.partner.entity.PartnerEntity
import im.bigs.pg.infra.persistence.partner.repository.FeePolicyJpaRepository
import im.bigs.pg.infra.persistence.partner.repository.PartnerJpaRepository
import im.bigs.pg.infra.persistence.payment.entity.PaymentEntity
import im.bigs.pg.infra.persistence.payment.repository.PaymentJpaRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.time.Instant

/**
 * 로컬/데모 환경에서 빠른 실행을 위한 간단한 시드 데이터.
 * - 운영 환경에서는 제거하거나 마이그레이션 도구로 대체합니다.
 */
@Configuration
class DataInitializer {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun seed(
        partnerRepo: PartnerJpaRepository,
        feeRepo: FeePolicyJpaRepository,
        paymentRepo: PaymentJpaRepository,
    ) = CommandLineRunner {
        if (partnerRepo.count() == 0L) {
            val p1 = partnerRepo.save(PartnerEntity(code = "MOCK1", name = "Mock Partner 1", active = true))
            val p2 = partnerRepo.save(PartnerEntity(code = "TESTPAY1", name = "TestPay Partner 1", active = true))

            feeRepo.save(
                FeePolicyEntity(
                    partnerId = p1.id!!,
                    effectiveFrom = Instant.parse("2020-01-01T00:00:00Z"),
                    percentage = BigDecimal("0.0235"),
                    fixedFee = BigDecimal.ZERO,
                ),
            )
            feeRepo.save(
                FeePolicyEntity(
                    partnerId = p2.id!!,
                    effectiveFrom = Instant.parse("2020-01-01T00:00:00Z"),
                    percentage = BigDecimal("0.0300"),
                    fixedFee = BigDecimal("100"),
                ),
            )

            val now = Instant.now()

            val payments = listOf(
                PaymentEntity(
                    partnerId = p1.id!!,
                    amount = BigDecimal("10000"),
                    appliedFeeRate = BigDecimal("0.0235"),
                    feeAmount = BigDecimal("235"),
                    netAmount = BigDecimal("9765"),
                    cardBin = "123456",
                    cardLast4 = "1111",
                    approvalCode = "APPRV001",
                    approvedAt = now,
                    status = "APPROVED",
                    createdAt = now,
                    updatedAt = now
                ),
                PaymentEntity(
                    partnerId = p1.id!!,
                    amount = BigDecimal("15000"),
                    appliedFeeRate = BigDecimal("0.0235"),
                    feeAmount = BigDecimal("353"),
                    netAmount = BigDecimal("14647"),
                    cardBin = "123456",
                    cardLast4 = "2222",
                    approvalCode = "APPRV002",
                    approvedAt = now,
                    status = "APPROVED",
                    createdAt = now.plusSeconds(10),
                    updatedAt = now
                ),
                PaymentEntity(
                    partnerId = p2.id!!,
                    amount = BigDecimal("20000"),
                    appliedFeeRate = BigDecimal("0.0300"),
                    feeAmount = BigDecimal("700"), // 20000 * 0.03 + 100
                    netAmount = BigDecimal("19300"),
                    cardBin = "654321",
                    cardLast4 = "3333",
                    approvalCode = "APPRV003",
                    approvedAt = now,
                    status = "APPROVED",
                    createdAt = now.plusSeconds(20),
                    updatedAt = now
                ),
                PaymentEntity(
                    partnerId = p2.id!!,
                    amount = BigDecimal("5000"),
                    appliedFeeRate = BigDecimal("0.0300"),
                    feeAmount = BigDecimal("250"),
                    netAmount = BigDecimal("4750"),
                    cardBin = "654321",
                    cardLast4 = "4444",
                    approvalCode = "APPRV004",
                    approvedAt = now,
                    status = "APPROVED",
                    createdAt = now.plusSeconds(30),
                    updatedAt = now
                ),
                PaymentEntity(
                    partnerId = p2.id!!,
                    amount = BigDecimal("30000"),
                    appliedFeeRate = BigDecimal("0.0300"),
                    feeAmount = BigDecimal("1000"), // 30000 * 0.03 + 100
                    netAmount = BigDecimal("29000"),
                    cardBin = "987654",
                    cardLast4 = "5555",
                    approvalCode = "APPRV005",
                    approvedAt = now,
                    status = "APPROVED",
                    createdAt = now.plusSeconds(40),
                    updatedAt = now
                ),
            )

            paymentRepo.saveAll(payments)

            log.info("Seeded partners with {} dummy payments", payments.size)
        }
    }
}
