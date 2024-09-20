# E-Commerce Platform

이 프로젝트는 모노리스에서 마이크로서비스 아키텍처(MSA)로 전환된 전자상거래(E-Commerce) 플랫폼입니다. 사용자 관리, 주문, 상품 관리 등 각기 다른 책임을 가진 마이크로서비스로 구성되어 있으며, 독립적으로 배포, 확장이 가능합니다.

## 주요 기술 스택
- Java Spring Boot: 각 마이크로서비스의 애플리케이션 레이어
- Spring Cloud Netflix Eureka: 서비스 레지스트리와 디스커버리
- Spring Cloud Gateway: API Gateway 기능
- MySQL: 데이터베이스 시스템
- Docker: 컨테이너 관리 및 배포

## 서버 구성

### 1.  Eureka 서버
- 서비스 레지스트리 역할을 담당하는 Eureka 서버는 각 마이크로서비스가 자신을 등록하고, 서로의 위치를 동적으로 찾을 수 있도록 도와줌.
- 주요 기능:
    - 각 마이크로서비스의 등록 및 디스커버리
    - 마이크로서비스 간 통신 시 로드 밸런싱 제공

### 2. Gateway 서버
- API Gateway 역할을 담당하여, 클라이언트의 요청을 각 마이크로서비스로 라우팅합니다.
- 주요 기능:
    - API 요청의 라우팅 및 필터링
    - 인증, 권한 관리
    - 로드 밸런싱

### 3. User 서버
- 사용자 관리를 담당하며, 회원가입, 로그인, 사용자 정보 수정 등의 기능을 제공합니다.
- 주요 기능:
    - 이메일 인증을 통한 회원가입
    - 로그인
    - Database: 사용자 정보를 저장하는 데이터베이스 (예: PostgreSQL)

### 4. Product 서버
- 상품 정보 및 재고 관리를 담당하는 서비스로, 상품의 등록, 수정, 삭제 및 조회 기능을 제공합니다.
- 주요 기능:
    - 상품 목록 조회
    - 상품 상세 정보 조회
    - 상품 추가, 수정, 삭제 (관리자 기능)
    - Database: 상품 정보 및 재고를 관리하는 데이터베이스

### 5. UserProduct 서버
- 주문 및 위시리스트 관리를 담당하는 서비스로, 사용자가 상품을 주문하거나 위시리스트에 등록하는 기능을 제공합니다.
- 주요 기능:
    - 상품 주문
    - 주문 상태 조회 및 관리 (주문 취소, 반품)
    - 위시리스트 등록 및 관리
    - Database: 주문 및 위시리스트 데이터를 관리하는 데이터베이스
