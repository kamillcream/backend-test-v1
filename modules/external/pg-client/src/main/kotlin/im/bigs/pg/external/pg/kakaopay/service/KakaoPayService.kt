package im.bigs.pg.external.pg.kakaopay.service

import im.bigs.pg.external.pg.kakaopay.config.KakaoPayProperties
import im.bigs.pg.external.pg.kakaopay.dto.KakaoPayReadyResponseDto
import im.bigs.pg.external.pg.kakaopay.dto.KakaoPayRequestDto
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class KakaoPayService(
    private val webClientBuilder: WebClient.Builder,
    private val payProperties: KakaoPayProperties
) {
    private val kakaoBaseUrl = "https://open-api.kakaopay.com"

    /**
     * 결제 준비 요청
     * https://developers.kakaopay.com/docs/payment/online/single-payment#payment-ready-sample-request
     */
    fun kakaoPayReady(requestDto: KakaoPayRequestDto): KakaoPayReadyResponseDto {
        val webClient = webClientBuilder
            .baseUrl(kakaoBaseUrl)
            .build()

        val response = webClient.post()
            .uri("/online/v1/payment/ready")
            .header("Authorization", "SECRET_KEY ${payProperties.secretKey}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestDto) // JSON 문자열로 직접 전달
            .retrieve()
            .bodyToMono(KakaoPayReadyResponseDto::class.java)
            .block() ?: throw IllegalStateException("KakaoPay server response is not expected.")

        return response
    }
}