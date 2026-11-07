# 주문 할인 시스템 리팩터링 과제

여러 상품의 주문 금액을 계산하고 회원 등급에 맞는 할인을 적용하는 Java 과제입니다. `LegacyOrderService`를 참고해, 주문 계산·정책 선택·할인 계산·출력의 책임을 분리하세요.

## 현재 코드 상태

이 저장소는 **TODO가 있는 메서드 본문만 완성하는 시작 코드**입니다. 필드, import, 생성자, 예외 클래스, 실행 시작점은 제공됩니다. 아래 5개 메서드에서 TODO를 구현하고 임시 `UnsupportedOperationException`을 삭제하세요. 그 밖의 코드를 수정할 필요는 없습니다. `OrderApplication`도 구현 완료 후 정상 실행할 수 있습니다.

- `VipDiscountPolicy.discount()`
- `GoldDiscountPolicy.discount()`
- `FixedDiscountPolicy.discount()`
- `MapDiscountPolicyResolver.resolve()`
- `OrderService.order()`

| 파일 | 현재 역할과 해야 할 일 |
| --- | --- |
| `LegacyOrderService` | 합계 계산, 등급 분기, 출력을 한 메서드에서 수행하는 비교용 코드입니다. VIP/GOLD만 처리합니다. |
| `DiscountPolicy` | `discount(long orderAmount)`로 **할인 금액**을 반환하는 공통 인터페이스입니다. |
| `VipDiscountPolicy`, `GoldDiscountPolicy`, `FixedDiscountPolicy` | 각 할인 규칙을 구현해야 합니다. |
| `DiscountPolicyResolver` | `resolve(String grade)`로 등급에 맞는 정책을 찾는 인터페이스입니다. |
| `MapDiscountPolicyResolver` | 정책 Map 필드와 방어적 복사를 하는 생성자는 제공됩니다. `resolve()`의 정책 조회와 예외 처리만 구현하세요. |
| `OrderService` | 생성자 주입은 제공되어 있습니다. `order()`의 입력 검증, 합계, 정책 호출, 결과 반환을 구현해야 합니다. |
| `OrderItem` | 상품명, `int` 단가, `int` 수량을 담는 record입니다. 자체 검증 로직은 없습니다. |
| `OrderResult` | 주문 금액, 할인 금액, 최종 결제 금액을 `long`으로 담는 record입니다. |
| 예외 클래스 4개 | 예외 메시지를 포함한 생성자가 제공됩니다. `UnknownGradeException`에는 등급을 전달하고, 나머지 입력 예외는 인자 없이 생성합니다. |
| `OrderApplication` | 실제 정책 등록, 생성자 주입, 실행과 콘솔 출력이 준비된 시작점입니다. |

위 파일은 `src/main/java/order`에 있습니다. 공개 테스트는 `src/test/java/order`에 정책 테스트 3개, 서비스 테스트 8개로 총 11개가 있습니다.

## 동작 규칙

### 주문과 할인 금액

- 주문에는 하나 이상의 상품이 있어야 합니다.
- 상품 금액은 `unitPrice × quantity`, 주문 금액은 모든 상품 금액의 합입니다.
- 단가는 0원 이상, 수량은 1개 이상이어야 합니다.
- VIP는 주문 금액의 20%, GOLD는 10%를 할인하며 원 미만 소수 부분은 버립니다.
- FIXED는 1,000원을 할인하되 주문 금액을 초과할 수 없습니다. 700원 주문은 700원 할인, 0원 주문은 0원 할인입니다.
- `DiscountPolicy.discount()`의 반환값은 최종 결제 금액이 아닌 **할인 금액**입니다.
- 최종 결제 금액은 `주문 금액 - 할인 금액`입니다.
- 합계와 결과는 `long`으로 계산하세요. 오버플로 감지나 관련 예외 처리는 이번 과제의 요구사항과 테스트 범위에 포함하지 않습니다.

### 검증과 예외

| 입력 또는 상황 | 처리 |
| --- | --- |
| 상품 목록이 `null`이거나 비어 있음 | `InvalidOrderException` |
| 목록 안에 `null` 상품이 있음 | `InvalidOrderException` |
| 단가가 음수 | `InvalidPriceException` |
| 수량이 0 이하 | `InvalidQuantityException` |
| 등급이 미등록 값이거나 `null` | `UnknownGradeException` |
| 정책이 주문 금액보다 큰 할인액을 반환 | `IllegalStateException` — 공개 테스트로 검증 |
| 정책이 음수 할인액을 반환 | `IllegalStateException` — 공개 테스트로 검증 |

상품 목록과 **모든 상품의 유효성을 확인한 다음** 정책을 선택하고 할인 계산을 호출하세요. 입력 오류가 있으면 `resolve()`도 호출하면 안 됩니다. 알 수 없는 등급은 resolver에서 예외를 발생시키며, 할인 계산으로 넘어가지 않습니다.

입력 목록이나 상품을 수정하지 말고, 검증이나 계산 중 예외가 발생하면 부분 결과를 반환하지 마세요. 제공된 타입에는 상품명 검증 규칙이 별도로 정의되어 있지 않습니다.

## 구현할 일과 권장 순서

1. **할인 정책 3개 구현**: VIP 20%, GOLD 10%, FIXED 최대 1,000원 규칙을 구현합니다.
2. **정책 선택 구현**: `MapDiscountPolicyResolver.resolve()`에서 제공된 `policies` 필드를 등급으로 조회합니다. `null` 등급과 미등록 등급은 `new UnknownGradeException(grade)`로 처리하세요. Map 보관과 방어적 복사는 생성자에 이미 구현되어 있습니다.
3. **주문 입력 검증**: `OrderService.order()`에서 목록, 각 상품, 단가, 수량을 검증합니다. 모든 상품을 확인하기 전에는 resolver나 할인 정책을 호출하지 않습니다.
4. **합계와 결과 계산**: 주문 금액을 계산하고 주입받은 resolver로 정책을 선택합니다. 할인액의 유효 범위를 확인한 뒤 `OrderResult`를 반환합니다.
5. **책임 분리 유지**: 서비스에서 `new VipDiscountPolicy()` 같은 구체 정책 생성이나 등급별 `if`/`switch`를 사용하지 않습니다. 정책 등록은 `OrderApplication`, 등급 조회는 resolver, 할인 계산은 정책, 출력은 시작점에서 담당합니다.
6. **테스트와 실행 확인**: 공개 테스트를 통과시키고, 실제 객체를 연결한 `OrderApplication`도 실행합니다. PR에 설계 이유를 설명합니다.

새 등급을 추가할 때는 새 정책 구현과 등록 정보를 추가하고, 기존 `OrderService`는 수정하지 않는 구조를 목표로 합니다. 이번 필수 구현은 위의 TODO 메서드 5개 안에서 완료할 수 있습니다. 제공된 필드, 생성자, 메서드 시그니처는 그대로 사용하세요.

## 실제 객체와 Mockito mock의 차이

`MapDiscountPolicyResolver`는 실제 서비스에서 사용하는 구현체입니다. `OrderApplication.main()`은 VIP/GOLD/FIXED 정책을 Map에 등록하고 resolver를 생성한 뒤 `OrderService`에 주입합니다.

`OrderServiceTest`의 단위 테스트는 Mockito mock을 주입해 서비스의 계산과 협력 객체 호출을 검증합니다. `integratesAllPoliciesAndUnknownGrade()`는 실제 resolver와 정책을 연결해 통합 동작을 확인합니다. `verifyNoInteractions(resolver)`는 잘못된 주문이 정책 선택 단계에 도달하지 않았는지 검사합니다.

## 공개 테스트로 확인하는 내용

| 사례 | 기대 결과 |
| --- | --- |
| VIP, 10,000원 × 2개 + 5,000원 × 1개 | 주문 25,000원 / 할인 5,000원 / 결제 20,000원 |
| GOLD, 9,999원 × 1개 | 주문 9,999원 / 할인 999원 / 결제 9,000원 |
| FIXED 정책에 25,000원 전달 | 할인 1,000원 |
| FIXED, 700원 × 1개 | 할인 700원 / 결제 0원 |
| FIXED, 0원 × 1개 | 할인 0원 / 결제 0원 |
| 음수 단가, 수량 0 또는 음수, 빈 목록, null 목록, null 상품 | 해당 예외 / resolver 호출 없음 |
| 뒤쪽 상품의 수량이 잘못된 주문 | `InvalidQuantityException` / resolver 호출 없음 |
| 정책이 100원 주문에 101원 또는 음수 할인 반환 | `IllegalStateException` |
| 정상 처리와 입력 오류 처리 전후의 상품 목록 | 상품 내용과 순서 유지 |
| BASIC 또는 null 등급 | `UnknownGradeException` |

테스트는 위에 명시한 요구사항을 확인합니다. 오버플로, 상품명 형식, 예외 메시지 형식 등 별도로 요구하지 않은 조건은 검사하지 않습니다. OCP·DIP와 출력 책임 분리 등 설계 항목은 코드 리뷰로 확인합니다.

## 실행 환경과 명령

Java 17 이상과 Maven 3.9 이상을 권장합니다. Spring은 사용하지 않습니다. `pom.xml`은 Java 17, JUnit 5, AssertJ, Mockito를 설정하고 있습니다. 프로젝트 루트에서 실행하세요.

```shell
mvn test
```

시작 코드에서는 미구현 메서드 때문에 테스트가 실패하는 것이 예상됩니다. 구현 후에는 공개 테스트 11개가 모두 통과해야 합니다.

```shell
mvn compile
java -cp target/classes order.OrderApplication
```

구현 완료 후 예제 실행의 기대 출력입니다.

```text
주문 완료
주문 금액: 25000원
할인 금액: 5000원
최종 결제 금액: 20000원
```

## PR 제출

1. 과제 저장소를 자신의 GitHub 계정으로 Fork합니다.
2. Fork한 저장소에서 본인의 닉네임이나 이름으로 중복되지 않는 브랜치를 만듭니다.
3. 별도 이름 폴더를 만들지 않고 제공된 Java 프로젝트를 수정합니다.
4. 작업 전 `mvn test`의 실패를 확인하고, 구현 후 테스트 통과를 확인합니다.
5. 원본 과제 저장소의 `main`을 대상으로 PR을 올립니다.
6. 제공된 PR 템플릿에 변경 내용, 적용한 객체지향 원칙, 실제 테스트 결과, 고민한 점을 작성합니다.

## 검토 기준

- Collection을 이용한 여러 상품 처리와 경계 조건
- 주문 계산, 정책 선택, 할인 계산, 출력의 책임 분리
- 새 정책 추가 시 기존 주문 서비스를 수정하지 않는 OCP 적용
- 서비스가 구체 정책 대신 인터페이스에 의존하는 DIP 적용과 생성자 주입
- 비즈니스 규칙과 협력 객체 호출에 대한 테스트 통과
- 변경 이유와 설계 선택을 설명하는 PR
