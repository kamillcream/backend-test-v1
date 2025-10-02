package im.bigs.pg.application.pg.port.out

import im.bigs.pg.domain.payment.PaymentStatus
import java.time.LocalDateTime

data class TestPgApproveResult(
    val approvalCode: String,
    val approvedAt: LocalDateTime,
    val maskedCardLast4: String,
    val amount: Long,
    val status: PaymentStatus
)