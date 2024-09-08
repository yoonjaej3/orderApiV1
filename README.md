# OrderApiV1

## 사용된 외부 라이브러리 및 오픈소스

### 1. Spring Boot
- **버전:** 2.6.2
- **목적:** Spring Boot는 Spring 기반의 어플리케이션을 쉽게 설정하고 시작할 수 있도록 지원하는 프레임워크

### 2. Spring Data JPA
- **목적:** Spring Data JPA는 데이터 액세스 계층을 개발하기 위한 ORM

### 3. Redis 
- **버전:** 3.0.504
- **목적:** Redis는 인메모리 데이터 구조 저장소로, 분산락 사용을 위해 사용

### 4. QueryDSL
- **버전:** 5.0.0
- **목적:** QueryDSL은 타입 안전한 쿼리 생성을 위한 프레임워크

### 5. JUnit 5
- **버전:** 5.8.2
- **목적:** JUnit 5는 Java에서 단위 테스트를 작성하고 실행하기 위한 프레임워크

### 6. Apache POI
- **버전:** 5.2.3
- **목적:** Apache POI는 Microsoft Office 형식의 파일을 읽고 쓰기 위한 Java API

### 7. Lombok
- **목적:** Lombok은 Java에서 boilerplate 코드를 줄여주는 라이브러리

### 8. Logback
- **버전:** 1.2.6
- **목적:** Logback은 Java의 로깅 프레임워크로, SLF4J와 함께 사용

## ERD

![ERD](https://github.com/user-attachments/assets/e40b44c9-b34c-4528-be44-0d76fec5d955)

## 로컬환경 포트번호 6379 Redis 설치 후 테스트 진행

### 테스트 결과

![TEST 코드](https://github.com/user-attachments/assets/87d8bb84-aa76-4ea0-bc44-c23c3a6e46c1)

## Postman

- **POSTMAN COLLECTION**
  
    [orderApiV1.postman_collection.zip](https://github.com/user-attachments/files/16920328/orderApiV1.postman_collection.zip)

- **API 엔드포인트**

    - **Health API**: 
      ```plaintext
      http://localhost:8080/api/status/health
      ```
    - **단건등록 API**: 
      ```plaintext
      http://localhost:8080/api/v1/orders
      ```
    - **엑셀 다건 등록 API**: 
      ```plaintext
      http://localhost:8080/api/v1/orders/excel
      ```
        - **기본 엑셀 파일**: [excel_order.xlsx](https://github.com/user-attachments/files/16920290/excel_order.xlsx)
        - **빈값 존재 엑셀파일**: [excel_order_fail_empty.xlsx](https://github.com/user-attachments/files/16920291/excel_order_fail_empty.xlsx)
        - **천건 등록 엑셀파일**: [excel_order_1000.xlsx](https://github.com/user-attachments/files/16920292/excel_order_1000.xlsx)
        - **천건 등록 재고부족 엑셀파일**: [excel_order_1000_fail_stock.xlsx](https://github.com/user-attachments/files/16920293/excel_order_1000_fail_stock.xlsx)
     
     
## 필터로그확인
```
2024-09-08 10:12:21.084  INFO 32252 --- [nio-8080-exec-3] org.jyj.common.filter.CustomFilter       : {"threadId":"http-nio-8080-exec-3","method":"POST","url":"/api/v1/orders","userAgent":"PostmanRuntime/7.41.2","host":"localhost:8080","clientIp":"0:0:0:0:0:0:0:1","requestParams":"{\r\n    \"orderBasicInfo\": {\r\n        \"orderDt\": \"2024-09-01\",\r\n        \"whCd\": \"YI01\",\r\n        \"ordererNm\": \"주문자명01\",\r\n        \"ordererAdress\": \"주문자명01주소\",\r\n        \"orderType\": \"NORMAL_ORDER\",\r\n        \"status\": \"REQUEST\"\r\n    },\r\n    \"orderItemInfoList\": [\r\n        {\r\n            \"itemCd\": \"M00001\",\r\n            \"itemNm\": \"상품명01\",\r\n            \"itemCnt\": 2\r\n        },\r\n        {\r\n            \"itemCd\": \"M00002\",\r\n            \"itemNm\": \"상품명02\",\r\n            \"itemCnt\": 1\r\n        }\r\n    ]\r\n}\r\n","responseParams":"{\"message\":\"주문 단건 등록\",\"data\":{\"orderNo\":\"YI012024-09-0100001\"}}","requestAt":"2024-09-08 10:12:20.669","responseAt":"2024-09-08 10:12:21.084","elapsedTimeInMS":415}
```
