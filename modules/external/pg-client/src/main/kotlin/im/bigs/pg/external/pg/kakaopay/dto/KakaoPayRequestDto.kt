package im.bigs.pg.external.pg.kakaopay.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoPayRequestDto(
    @JsonProperty("cid")
    val cid: String,

    @JsonProperty("partner_order_id")
    val partnerOrderId: String,

    @JsonProperty("partner_user_id")
    val partnerUserId: String,

    @JsonProperty("item_name")
    val itemName: String,

    @JsonProperty("quantity")
    val quantity: Int,

    @JsonProperty("total_amount")
    val totalAmount: Int,

    @JsonProperty("tax_free_amount")
    val taxFreeAmount: Int,

    @JsonProperty("approval_url")
    val approvalUrl: String,

    @JsonProperty("fail_url")
    val failUrl: String,

    @JsonProperty("cancel_url")
    val cancelUrl: String
)