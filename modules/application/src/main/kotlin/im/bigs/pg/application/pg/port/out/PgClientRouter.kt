package im.bigs.pg.application.pg.port.out

import org.springframework.stereotype.Component


/**
 * 파트너 코드(unique)를 기준으로
 * 해당 PG사에 맞는 클라이언트를 찾아주는 라우터 클래스입니다.
 *
 * 여러 PG 클라이언트(PgClientOutPort 구현체)가 존재할 경우,
 * 주어진 파트너 코드에 대해 `supports()` 메서드가 true를 반환하는
 * 어댑터를 찾아 반환합니다.

 * 새로운 제휴사가 추가되더라도 어댑터만 구현하면
 * 라우팅 로직을 수정하지 않고 확장할 수 있도록 설계했습니다..
 */

@Component
class PgClientRouter(private val adapters: List<PgClientOutPort>) {
    fun getAdapter(partnerCode: String): PgClientOutPort =
        adapters.find { it.supports(partnerCode) }
            ?: throw IllegalArgumentException("Not supported partner's code: $partnerCode")
}