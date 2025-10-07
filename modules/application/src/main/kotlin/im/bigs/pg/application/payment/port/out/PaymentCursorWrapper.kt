package im.bigs.pg.application.payment.port.out

import java.time.Instant

/**
 * 결제 내역 조회 시 사용되는 커서 정보를 담는 객체.
 * 기존에는 Pair<Instant, Long> 형태로 관리하던 커서 데이터를 보다 명시적으로 관리.
 */
data class PaymentCursorWrapper(
    val cursorCreatedAt: Instant?,
    val cursorId: Long?
)