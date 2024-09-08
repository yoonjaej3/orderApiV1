# 개발 스펙

- **Gradle:** 7.4.1
- **Spring Boot:** 2.6.2
- **Spring Data JPA**
- **JAVA:** 11
- **H2**
- **RESTful API**
- **QueryDSL**
- **Junit5**
- **Redis:** 3.0.504

## ERD

![ERD](https://github.com/user-attachments/assets/e40b44c9-b34c-4528-be44-0d76fec5d955)

## 로컬환경 포트번호 6379 Redis 설치 후 테스트 진행

### 테스트 결과

![TEST 코드](https://github.com/user-attachments/assets/87d8bb84-aa76-4ea0-bc44-c23c3a6e46c1)

## Postman

- **POSTMAN COLLECTION**

    [orderApiV1.postman_collection.zip](https://github.com/user-attachments/files/16920299/orderApiV1.postman_collection.zip)

- **API 엔드포인트**

    - **Health API**: [localhost:8080/api/status/health](http://localhost:8080/api/status/health)
    - **단건등록 API**: [localhost:8080/api/v1/orders](http://localhost:8080/api/v1/orders)
    - **엑셀 다건 등록 API**: [localhost:8080/api/v1/orders/excel](http://localhost:8080/api/v1/orders/excel)
        - **기본 엑셀 파일**: [excel_order.xlsx](https://github.com/user-attachments/files/16920290/excel_order.xlsx)
        - **빈값 존재 엑셀파일**: [excel_order_fail_empty.xlsx](https://github.com/user-attachments/files/16920291/excel_order_fail_empty.xlsx)
        - **천건 등록 엑셀파일**: [excel_order_1000.xlsx](https://github.com/user-attachments/files/16920292/excel_order_1000.xlsx)
        - **천건 등록 재고부족 엑셀파일**: [excel_order_1000_fail_stock.xlsx](https://github.com/user-attachments/files/16920293/excel_order_1000_fail_stock.xlsx)
