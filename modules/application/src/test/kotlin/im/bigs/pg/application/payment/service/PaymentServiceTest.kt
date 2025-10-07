package im.bigs.pg.application.payment.service

import im.bigs.pg.application.partner.port.out.FeePolicyOutPort
import im.bigs.pg.application.partner.port.out.PartnerOutPort
import im.bigs.pg.application.payment.port.`in`.PaymentCommand
import im.bigs.pg.application.payment.port.out.PaymentOutPort
import im.bigs.pg.application.pg.port.out.PgApproveResult
import im.bigs.pg.application.pg.port.out.PgClientOutPort
import im.bigs.pg.application.pg.port.out.PgClientRouter
import im.bigs.pg.domain.partner.FeePolicy
import im.bigs.pg.domain.partner.Partner
import im.bigs.pg.domain.payment.Payment
import im.bigs.pg.domain.payment.PaymentStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.DisplayName
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class 결제서비스Test {
    private val partnerRepo = mockk<PartnerOutPort>()
    private val feeRepo = mockk<FeePolicyOutPort>()
    private val paymentRepo = mockk<PaymentOutPort>()
    val pgClient = mockk<PgClientOutPort>()
    val pgClientRouter = mockk<PgClientRouter>()

    @Test
    @DisplayName("MOCK 결제 시 수수료 정책을 적용하고 저장해야 한다")
    fun `MOCK 결제 시 수수료 정책을 적용하고 저장해야 한다`() {
        val service = PaymentService(partnerRepo, feeRepo, paymentRepo, pgClientRouter)

        every { pgClientRouter.getAdapter("MOCK") } returns pgClient
        every { pgClient.approve(any()) } returns PgApproveResult(
            approvalCode = "APPRV001",
            approvedAt = LocalDateTime.now(),
            status = PaymentStatus.APPROVED
        )
        every { partnerRepo.findById(1L) } returns Partner(1L, "MOCK", "Test", true)
        every { feeRepo.findEffectivePolicy(1L, any()) } returns FeePolicy(
            id = 10L, partnerId = 1L, effectiveFrom = LocalDateTime.ofInstant(Instant.parse("2020-01-01T00:00:00Z"), ZoneOffset.UTC),
            percentage = BigDecimal("0.0300"), fixedFee = BigDecimal("100")
        )
        val savedSlot = slot<Payment>()
        every { paymentRepo.save(capture(savedSlot)) } answers { savedSlot.captured.copy(id = 99L) }

        val cmd = PaymentCommand(partnerId = 1L, amount = BigDecimal("10000"), cardLast4 = "4242")
        val res = service.pay(cmd)

        assertEquals(99L, res.id)
        assertEquals(BigDecimal("400"), res.feeAmount)
        assertEquals(BigDecimal("9600"), res.netAmount)
        assertEquals(PaymentStatus.APPROVED, res.status)
    }

    @Test
    @DisplayName("TESTPAY 결제 시 수수료 정책을 적용하고 저장해야 한다")
    fun `TESTPAY 결제 시 수수료 정책을 적용하고 저장해야 한다`() {
        val service = PaymentService(partnerRepo, feeRepo, paymentRepo, pgClientRouter)

        every { pgClientRouter.getAdapter("TESTPAY") } returns pgClient
        every { pgClient.approve(any()) } returns PgApproveResult(
            approvalCode = "APPRV001",
            approvedAt = LocalDateTime.now(),
            status = PaymentStatus.APPROVED
        )
        every { partnerRepo.findById(2L) } returns Partner(2L, "TESTPAY", "Test", true)
        every { feeRepo.findEffectivePolicy(2L, any()) } returns FeePolicy(
            id = 10L, partnerId = 2L, effectiveFrom = LocalDateTime.ofInstant(Instant.parse("2020-01-01T00:00:00Z"), ZoneOffset.UTC),
            percentage = BigDecimal("0.0300"), fixedFee = BigDecimal("100")
        )
        val savedSlot = slot<Payment>()
        every { paymentRepo.save(capture(savedSlot)) } answers { savedSlot.captured.copy(id = 99L) }

        val cmd = PaymentCommand(partnerId = 2L, amount = BigDecimal("10000"), cardLast4 = "4242")
        val res = service.pay(cmd)

        assertEquals(99L, res.id)
        assertEquals(BigDecimal("400"), res.feeAmount)
        assertEquals(BigDecimal("9600"), res.netAmount)
        assertEquals(PaymentStatus.APPROVED, res.status)
    }

    @Test
    @DisplayName("카카오페이 결제 시 수수료 정책을 적용하고 저장해야 한다")
    fun `카카오페이 결제 시 수수료 정책을 적용하고 저장해야 한다`() {
        val service = PaymentService(partnerRepo, feeRepo, paymentRepo, pgClientRouter)

        every { pgClientRouter.getAdapter("KAKAOPAY") } returns pgClient
        every { pgClient.approve(any()) } returns PgApproveResult(
            approvalCode = "APPRV001",
            approvedAt = LocalDateTime.now(),
            status = PaymentStatus.APPROVED
        )
        every { partnerRepo.findById(3L) } returns Partner(3L, "KAKAOPAY", "Test", true)
        every { feeRepo.findEffectivePolicy(3L, any()) } returns FeePolicy(
            id = 10L, partnerId = 3L, effectiveFrom = LocalDateTime.ofInstant(Instant.parse("2020-01-01T00:00:00Z"), ZoneOffset.UTC),
            percentage = BigDecimal("0.0300"), fixedFee = BigDecimal("100")
        )
        val savedSlot = slot<Payment>()
        every { paymentRepo.save(capture(savedSlot)) } answers { savedSlot.captured.copy(id = 99L) }

        val cmd = PaymentCommand(partnerId = 3L, amount = BigDecimal("10000"), cardLast4 = "4242")
        val res = service.pay(cmd)

        assertEquals(99L, res.id)
        assertEquals(BigDecimal("400"), res.feeAmount)
        assertEquals(BigDecimal("9600"), res.netAmount)
        assertEquals(PaymentStatus.APPROVED, res.status)
    }

    @Test
    @DisplayName("비활성화된 제휴사의 결제 요청은 실패한다")
    fun `비활성화된 제휴사의 결제 요청은 실패한다`() {
        // given
        val service = PaymentService(partnerRepo, feeRepo, paymentRepo, pgClientRouter)
        val partner = Partner(
            id = 3L,
            code = "KAKAOPAY",
            name = "카카오페이",
            active = false
        )

        every { partnerRepo.findById(3L) } returns partner

        val request = PaymentCommand(partnerId = partner.id, amount = BigDecimal("10000"))

        val exception = assertFailsWith<IllegalArgumentException> {
            service.pay(request)
        }

        assertEquals("Partner is inactive: ${partner.id}", exception.message)
    }

    @Test
    @DisplayName("DB에 존재하지 않는 제휴사의 결제 요청은 실패한다.")
    fun `DB에 존재하지 않는 제휴사의 결제 요청은 실패한다`() {
        // given
        val service = PaymentService(partnerRepo, feeRepo, paymentRepo, pgClientRouter)

        every { partnerRepo.findById(999L) } returns null

        val request = PaymentCommand(
            partnerId = 999L,
            amount = BigDecimal("50000"),
            cardBin = "123456",
            cardLast4 = "9999",
            productName = "네이버페이 테스트 결제"
        )

        // when & then
        val exception = assertFailsWith<IllegalArgumentException> {
            service.pay(request)
        }

        assertEquals("Partner not found: 999", exception.message)
    }
}
