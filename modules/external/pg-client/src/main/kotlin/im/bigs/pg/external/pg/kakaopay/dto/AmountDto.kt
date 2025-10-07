package im.bigs.pg.external.pg.kakaopay.dto

data class AmountDto(
    val total: Int,          // 총 결제 금액
    val tax_free: Int,       // 비과세 금액
    val tax: Int,            // 부가세 금액
    val point: Int,          // 사용한 포인트
    val discount: Int,       // 할인 금액
    val green_deposit: Int   // 컵 보증금
)