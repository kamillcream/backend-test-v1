package im.bigs.pg.application.payment.port.out

import java.time.Instant

data class PaymentCursorWrapper(
    val cursorCreatedAt: Instant?,
    val cursorId: Long?
)