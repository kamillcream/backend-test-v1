package im.bigs.pg.application.payment.port.out

import com.fasterxml.jackson.annotation.JsonFormat
import im.bigs.pg.application.payment.port.`in`.QueryFilter
import im.bigs.pg.domain.payment.PaymentStatus
import java.time.LocalDateTime

/** 통계용 필터 – 페이지와 동일 조건 사용 권장. */
data class PaymentSummaryFilter(
    val partnerId: Long? = null,
    val status: PaymentStatus? = null,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val from: LocalDateTime? = null,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val to: LocalDateTime? = null,
) {
    companion object {
        fun from (q: QueryFilter) = PaymentSummaryFilter (
            partnerId = q.partnerId,
            status = q.status?.let { PaymentStatus.valueOf(it) },
            from = q.from,
            to = q.to
        )
    }
}

