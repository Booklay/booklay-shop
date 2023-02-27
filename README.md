# 북레이 서점 프로젝트

https://www.booklay.shop/

## 서비스 소개

스프링 클라우드를 이용한 온라인 서점 어플리케이션

### 쇼핑몰
<img width="3660" alt="Frame 1@2x" src="https://user-images.githubusercontent.com/96007926/221072111-58cc4238-99fc-4a42-9340-920e95b05271.png">

### CI/CD
<img width="3660" alt="Frame 1@2x" src="https://user-images.githubusercontent.com/38172794/221101311-76d888ea-b502-42e6-a63c-8af75ab7b9b4.png">

### WBS

![image](https://user-images.githubusercontent.com/96007926/221077197-8ce10da9-6136-4773-8692-2b4d7390638a.png)

### ER Diagram
<img width="3660" alt="Frame 1@2x" src="https://user-images.githubusercontent.com/38172794/221082399-ebb131d3-4f83-4336-88d9-9fd2941cbc16.png">

### 두레이 칸반 보드
<img width="3660" alt="Frame 1@2x" src="https://user-images.githubusercontent.com/38172794/221079684-bd162567-c1b3-43cb-a49c-291a888e7373.png">


### 테스트 커버리지
- 목표 80%, 달성률 65.9% (2월 26일) 
![image](https://user-images.githubusercontent.com/96007926/221397915-931a4ac7-d696-441d-8526-e368ca75899f.png)

## 기능

### 상품

- 담당자 : 최규태, 이성준
- 사용 기술 : NHN cloud 오브젝트 스토리지, tui-editor
- 주요 기능
  - 상품 관리
  - 태그
  - 연관상품
  - 위시리스트
  - 작가
    - 작가는 본인 서적 문의 답변 가능
  - 리뷰

### 게시판

- 담당자 : 최규태
- 주요기능
  - 종류
    - 상품문의
      - 관리자, 사용자, 작가 사용 가능
      - 문의 답변 처리 가능
      - 댓글 작성 가능
    - 공지사항
      - 관리자만 작성 가능
      - 댓글 작성 불가
  - 계층형 게시판

### Dev Ops
- 담당자 : 이성준
- 사용기술 : Eureka, Jenkins, Github Action, NHN Cloud
- 주요 기능
  - 유래카 health check
  - Jenkins, Github Action
  - NHN Cloud의 Log & Crash
  - 무중단 배포
  
### 검색

- 담당자 : 이성준
- 사용 기술 : 엘라스틱 서치
- 주요기능
    - 검색
      - 키워드, 제목, 작가명, 카테고리 단어 검색
    - 카테고리
      - 생성, 수정, 조회
      - 2단계 depth 구조
      - 상품당 최대 3개 등록 가능


### Dev Ops
- 담당자 : 이성준
- 사용기술 : Eureka, Jenkins, Github Action, NHN Cloud
- 주요 기능
  - 유래카 health check
  - Jenkins, Github Action
  - NHN Cloud의 Log & Crash
  - 무중단 배포


### 인증

- 담당자 : 조현진, 이성준
- 사용 기술 : JWT
- 주요기능
    - 로그인
    - github OAuth
- 링크
    - https://github.com/Booklay/booklay-auth

### 회원

- 담당자 : 양승아
- 사용 기술 : 배치
- 주요기능
    - 배송지 
      - 10개까지 저장
    - 포인트
      - 선물 가능
      - 포인트 쿠폰 등록 가능
      - 내역 조회 가능
    - 회원등급
      - 매월 등급 자동 산정
      - 매월 등급별 포인트 자동 지급
    - 회원권한) 사용자, 관리자 등
    - 회원관리
      - 차단
      - 탈퇴
      - 회원 통계
    - 링크
        - https://github.com/Booklay/booklay-batch

### 쿠폰

- 담당자 : 김승혜
- 사용 기술 : 카프카
- 주요기능
    - 쿠폰 관리
        - 정액, 정률, 포인트 충전 쿠폰
        - 상품/카테고리/장바구니 쿠폰
        - 등급별 쿠폰, 수량 제한 있는 쿠폰
        - 쿠폰존에 쿠폰 등록
        - 회원 지정 쿠폰 발급
    - 쿠폰존
        - 사용자가 쿠폰을 발급 받을 수 있음.
        - 이달의 쿠폰, 등급별 쿠폰/무제한 쿠폰 등록/
        - 트래픽이 몰릴 수 있는 수량 제한 쿠폰은 카프카를 사용하여 처리.
- 링크
    - https://github.com/Booklay/booklay-coupon

### 결제

- 담당자 : 오준후
- 사용 기술 : toss API, Redis
- 주요기능
    - 카트
      - 비회원 주문 가능하도록 redis 사용
      - 상품 수량 조절
      - 결제 대상 상품 선택
    - 결제
      - 쿠폰 / 포인트 적용 가능
      - Toss API 사용
      - 기본 배송지 적용
      
### 캐시

- 담당자 : 오준후
- 사용기술 : Redis
- 주요기능
  - 메인페이지 캐시
  - 각 페이지네이션 첫 페이지
 

## 기술

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
![ElasticSearch](https://img.shields.io/badge/-ElasticSearch-005571?style=for-the-badge&logo=elasticsearch)

![](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)

![](https://img.shields.io/badge/redis-CC0000.svg?&style=for-the-badge&logo=redis&logoColor=white)
![](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)

![](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
![](https://img.shields.io/badge/SonarLint-CB2029?style=for-the-badge&logo=sonarlint&logoColor=white)
![](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&amp;logo=SonarQube&amp;logoColor=white)
![](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white)
<a href="https://github.com/nhn"><img src="https://camo.githubusercontent.com/1fb5eae4a4360c5f08dd061260fc68839f26aecc2f3de6859bff0166548b4268/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f2533432532462533452532307769746825323025453225393925413525323062792d4e484e5f436c6f75642d6666313431342e737667" alt="code with hearth by NHN Cloud" data-canonical-src="https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-NHN_Cloud-ff1414.svg" style="max-width: 100%;"></a>

![](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

### workspace

![](https://img.shields.io/badge/apple%20silicon-333333?style=for-the-badge&logo=apple&logoColor=white)
![](https://img.shields.io/badge/mac%20os-000000?style=for-the-badge&logo=apple&logoColor=white)

## 팀원

<a href="https://github.com/Booklay/booklay-shop/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Booklay/booklay-shop" />
</a>
