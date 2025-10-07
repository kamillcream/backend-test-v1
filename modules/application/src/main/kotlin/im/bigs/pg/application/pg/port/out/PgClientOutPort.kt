package im.bigs.pg.application.pg.port.out

/** 외부 결제사(PG) 승인 연동 포트. */

/**
 *
 * 원래대로라면 파트너 ID로 제휴사를 조회한 뒤 파트너 코드 값을 한 번 더 조회해야
 * 제휴사 구분이 가능했습니다.
 * 파트너 코드는 unique 이기 때문에 파트너 ID를 대체하면서
 * 조회 단계를 생략하면서 로직을 수행할 수 있게 되었습니다.
 *
 * 각 PG사별로 이 인터페이스를 구현하면,
 * `PgClientRouter`에서 적절한 어댑터를 찾아 호출하게 됩니다.
 */
interface PgClientOutPort {
    fun supports(partnerCode: String): Boolean
    fun approve(request: PgApproveRequest): PgApproveResult
}
