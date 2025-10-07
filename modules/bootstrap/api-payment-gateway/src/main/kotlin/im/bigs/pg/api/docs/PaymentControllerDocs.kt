package im.bigs.pg.api.docs

import im.bigs.pg.api.payment.dto.CreatePaymentRequest
import im.bigs.pg.api.payment.dto.PaymentResponse
import im.bigs.pg.api.payment.dto.QueryResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

interface PaymentControllerDocs {
    @Operation(
        summary = "결제 생성",
        description = "결제를 요청하고 그에 대한 결과 요약을 응답으로 받습니다"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = [
            Content(
                mediaType = "application/json",
                examples = [
                    ExampleObject(
                        name = "기본 요청 예시",
                        value = """
                        {
                          "partnerId": 1,
                          "amount": 10000,
                          "cardBin": "123456",
                          "cardLast4": "4242",
                          "productName": "테스트 결제"
                        }
                        """
                    )
                ]
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "결제 생성 성공",
                                summary = "생성된 결제의 요약본",
                                value = """
                                {
                                    "id": 1,
                                    "partnerId": 1,
                                    "amount": 10000,
                                    "appliedFeeRate": 0.030000,
                                    "feeAmount": 400,
                                    "netAmount": 9600,
                                    "cardLast4": "4242",
                                    "approvalCode": "10055619",
                                    "approvedAt": "2025-10-05 03:52:47",
                                    "status": "APPROVED",
                                    "createdAt": "2025-10-05 12:52:44"
                                }
                            """
                            )
                        ]
                    )
                ]

            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "요청 양식 미준수",
                                summary = "RequestBody에 필요한 값이 누락되어 있습니다.",
                                value = """
                                {
                                    "timestamp": "2025-10-05T03:55:39.639+00:00",
                                    "status": 400,
                                    "error": "Bad Request",
                                    "path": "/api/v1/payments"
                                }
                            """
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "서버 에러 응답",
                                summary = "예상치 못한 서버 에러",
                                value = """
                                {
                                    "timestamp": "2025-10-05T03:58:56.445+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "path": "/api/v1/payments"
                                }
                            """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun create(
        @RequestBody req: CreatePaymentRequest
    ): ResponseEntity<PaymentResponse>

    @Operation(
        summary = "결제 조회",
        description = "검색 조건을 만족하는 결제 내역이 페이지네이션으로 제공됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "결제 조회 성공",
                                summary = "검색 조건을 만족하는 결제 내역, 통계, 커서 정보를 나타냅니다.",
                                value = """
                                {
                                    "items": [
                                        {
                                            "id": 5,
                                            "partnerId": 2,
                                            "amount": 30000,
                                            "appliedFeeRate": 0.030000,
                                            "feeAmount": 1000,
                                            "netAmount": 29000,
                                            "cardLast4": "5555",
                                            "approvalCode": "APPRV005",
                                            "approvedAt": "2025-10-05 09:53:30",
                                            "status": "APPROVED",
                                            "createdAt": "2025-10-05 09:54:10"
                                        }
                                    ],
                                    "summary": {
                                        "count": 5,
                                        "totalAmount": 80000,
                                        "totalNetAmount": 77462
                                    },
                                    "nextCursor": "MTc1OTY1ODA1MDcwMzo1",
                                    "hasNext": true
                                }
                            """
                            )
                        ]
                    )
                ]

            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "서버 에러 응답",
                                summary = "예상치 못한 서버 에러",
                                value = """
                                {
                                    "timestamp": "2025-10-05T03:58:56.445+00:00",
                                    "status": 500,
                                    "error": "Internal Server Error",
                                    "path": "/api/v1/payments"
                                }
                            """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun query(
        @Parameter(example = "1", description = "파트너 ID")
        @RequestParam(required = false) partnerId: Long?,

        @Parameter(example = "APPROVED", description = "결제 상태 (APPROVED, CANCELED 등)")
        @RequestParam(required = false) status: String?,

        @Parameter(example = "2025-10-05 00:00:00", description = "조회 시작일 (yyyy-MM-dd HH:mm:ss)")
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") from: LocalDateTime?,

        @Parameter(example = "2025-10-07 23:59:59", description = "조회 종료일")
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") to: LocalDateTime?,

        @RequestParam(required = false) cursor: String?,

        @RequestParam(defaultValue = "20") limit: Int,
    ): ResponseEntity<QueryResponse>
}