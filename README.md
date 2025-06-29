# 🍿 MovieTalk : 영화 리뷰 커뮤니티 API

> Spring Boot를 공부하며 진행한 개인 프로젝트입니다. 사용자는 영화와 관련된 정보를 검색하고, 영화에 평점을 포함하여 리뷰를 남길 수 있으며, 리뷰에 좋아요를 표시할 수 있습니다. Spring
> Security와 JWT를 이용하여 인증 및 인가 프로세스를 구현하였습니다. 또, SpringDoc을 사용하여 Swagger 페이지를 통해 API 명세를 확인할 수 있도록 하였습니다.

## 🔍 About MovieTalk

👤 개인 프로젝트

📅 2025.05.20 - 2025.05.28

🔍 [상세 노션 페이지](https://www.notion.so/chloeeekim/MovieTalk-API-2016e927bee08014bd32e1b2c61008a2)

## 🛠️ 사용 기술 및 라이브러리

- JAVA `17`
- Spring Boot `3.4.5`, Spring Data JPA `3.4.5`, Spring Security `6.4.5`, JWT (JJWT `0.11.5`)
- H2 `2.3.232`, Redis `3.4.5`
- JUnit5 `5.11.4`, Mockito `5.14.2`
- SpringDoc `2.8.8`

## 👩‍💻 구현 내용

1. **사용자 인증 및 권한 관리 (JWT + Spring Security)**
    - 회원가입, 로그인, 로그아웃 기능 구현
    - Access Token / Refresh Token을 통한 인증 시스템 구축
    - Spring Security 필터 커스터마이징으로 요청별 인증 처리
    - `BCryptPasswordEncoder` 를 사용하여 비밀번호를 암호화하여 DB에 저장


2. **영화/배우/감독 정보 CRUD + 검색 + 페이징 기능**
    - 영화/배우/감독 등 주요 항목에 대해 등록, 수정, 삭제, 검색 기능 구현
    - 영화:배우(N:N), 영화:감독(N:1) 데이터 매핑
    - Spring Data JPA를 통해 CRUD 처리 및 유효성 검증 적용
    - `Pageable`을 사용하여 리스트 형태로 반환되는 요청들에 페이지네이션 적용


3. **영화 리뷰 및 평점 시스템**
    - 사용자는 영화에 대해 평점을 포함한 리뷰 작성 가능
    - 영화 조회 시 평균 평점 확인 가능
    - 사용자는 리뷰에 대해 좋아요 표시/취소 가능
    - 영화 상세 조회 시 평균 평점 및 좋아요 순 상위 3개 리뷰 확인 가능
    - 평점은 0.5점~5점으로, 0.5점 단위로 입력 가능하도록 Validation 처리 추가
    - `Pageable`을 사용하여 리스트 형태로 반환되는 요청들에 페이지네이션 적용


4. **Redis 기반 Refresh Token 저장 및 재발급**
    - Refresh Token은 Redis에 저장하고 만료 시간 설정
    - Access Token 만료 시, 특정 에러를 반환하여 refresh를 요청하도록 구현
    - Refresh Token은 Redis TTL 기반으로 자동 만료
    - Token Blacklist 방식으로 로그아웃 기능 구현


5. **응답 공통 포맷 적용**
    - `ResponseBodyAdvice` 인터페이스를 구현하여 모든 성공 응답을 `{ success, status, data, timestamp }` 형태로 래핑
    - 에러 발생 시 응답을 `{ success, status, code, reason, timestamp, path }` 형태로 래핑
    - `@ExceptionHandler` 등을 사용하여 전역 예외 처리
    - 에러 발생 원인(e.g., `USER_NOT_FOUND`)을 특정하여 커스텀 에러 코드 및 메시지로 응답


6. **SpringDoc 기반 Swagger 문서 자동화**
    - `springdoc-openapi` 를 사용하여 API 명세 자동 생성
    - `OperationCustomizer` 를 활용하여 실제 응답 포맷 구조로 Swagger 응답 스키마 재정의

## 💾 ERD

![MovieTalk ERD.png](readme_assets/MovieTalk%20ERD.png)

## 🗒️ API 명세 (Swagger)

- API 명세서는 다음과 같이 자동 생성됩니다.
  ![swagger example.png](readme_assets/swagger%20example.png)
- 응답 예시는 다음과 같이 실제 response의 형태와 동일하도록 구현하였습니다.
  ![swagger response example.png](readme_assets/swagger%20response%20example.png)
