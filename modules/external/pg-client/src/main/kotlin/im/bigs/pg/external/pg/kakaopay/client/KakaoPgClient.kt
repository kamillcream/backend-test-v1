package im.bigs.pg.external.pg.kakaopay.client

import im.bigs.pg.application.pg.port.out.PgApproveRequest
import im.bigs.pg.application.pg.port.out.PgApproveResult
import im.bigs.pg.application.pg.port.out.PgClientOutPort
import im.bigs.pg.domain.payment.PaymentStatus
import im.bigs.pg.external.pg.kakaopay.dto.KakaoPayRequestDto
import im.bigs.pg.external.pg.kakaopay.service.KakaoPayService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class KakaoPgClient(
    private val kakaoPayService: KakaoPayService
) : PgClientOutPort {

    override fun supports(partnerCode: String): Boolean = partnerCode.contains("KAKAOPAY", ignoreCase = true)

    override fun approve(request: PgApproveRequest): PgApproveResult {
        val kakaoRequest = KakaoPayRequestDto(
            cid = "TC0ONETIME",
            partnerOrderId = request.partnerId.toString(),
            partnerUserId = "TEST_USER1",
            itemName = "초코파이",
            quantity = 3,
            totalAmount = request.amount.toInt(),
            taxFreeAmount = 0,
            approvalUrl = "http://localhost:8080" + "/success",
            failUrl = "http://localhost:8080" + "/fail",
            cancelUrl = "http://localhost:8080" + "/cancel"
        )

        val kakaoResponse = kakaoPayService.kakaoPayReady(kakaoRequest)

        return PgApproveResult(
            approvalCode = kakaoResponse.tid,
            approvedAt = LocalDateTime.now(),
            status = PaymentStatus.APPROVED
        )
    }
}