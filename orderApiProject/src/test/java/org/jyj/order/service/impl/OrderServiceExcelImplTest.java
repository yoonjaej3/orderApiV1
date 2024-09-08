package org.jyj.order.service.impl;

import org.junit.jupiter.api.*;
import org.jyj.order.request.OrderExcelInfo;
import org.jyj.order.response.OrderSaveExcelParseResponseDto;
import org.jyj.order.response.OrderSaveExcelResponseDto;
import org.jyj.order.respository.interf.OutOrderRepository;
import org.jyj.order.service.interf.OrderServiceExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceExcelImplTest {
    @Autowired
    private OrderServiceExcel orderServiceExcel;

    @Autowired
    private OutOrderRepository outOrderRepository;


    @BeforeEach
    public void setup() {
        outOrderRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("엑셀파일 파싱성공")
    void 엑셀파일_파싱성공() throws IOException {
        //given
        ClassPathResource resource = new ClassPathResource("excel_order.xlsx");
        try (InputStream inputStream = resource.getInputStream()) {
            MultipartFile file = new MockMultipartFile("file.xlsx", "excel_order.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

            //when
            OrderSaveExcelParseResponseDto result = orderServiceExcel.parseExcelFile(file);

            //then
            // 첫 번째 행 데이터 검증
            OrderExcelInfo firstOrder = result.getOrderExcelInfoList().get(0);
            assertEquals("1", firstOrder.getOrderGroup());
            assertEquals("YI01", firstOrder.getWhCd());
            assertEquals("20240901", firstOrder.getOrderDt());
            assertEquals("M00001", firstOrder.getItemCd());
            assertEquals("상품1", firstOrder.getItemNm());
            assertEquals("2", firstOrder.getItemCnt());
            assertEquals("주문자명01", firstOrder.getOrdererNm());
            assertEquals("주문자명01주소", firstOrder.getOrdererAdress());
        }
    }

    @Test
    @Order(2)
    @DisplayName("엑셀파일_파싱실패")
    void 엑셀파일_파싱실패_빈값존재() throws IOException {
        //given
        ClassPathResource resource = new ClassPathResource("excel_order_fail_empty.xlsx");
        try (InputStream inputStream = resource.getInputStream()) {
            MultipartFile file = new MockMultipartFile("file.xlsx", "excel_order_fail_empty.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

            //when
            OrderSaveExcelParseResponseDto result = orderServiceExcel.parseExcelFile(file);

            //then
            // 첫 번째 행 데이터 검증
            List<String> errorMessages = result.getErrorMessages();
            assertEquals("Row 1: 양식 오류 - 필수 값이 비어 있습니다. (비어 있는 칼럼: 주문일자, 상품수량)", errorMessages.get(0));
        }
    }

    @Test
    @Order(3)
    @DisplayName("엑셀파일_1000건등록_정상")
    void 엑셀파일_1000건등록_정상() throws IOException {
        //given
        ClassPathResource resource = new ClassPathResource("excel_order_1000.xlsx");
        try (InputStream inputStream = resource.getInputStream()) {
            MultipartFile file = new MockMultipartFile("file.xlsx", "excel_order_1000", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

            //when
            OrderSaveExcelParseResponseDto orderSaveExcelParseResponseDto = orderServiceExcel.parseExcelFile(file);
            OrderSaveExcelResponseDto orderSaveExcelResponseDto = orderServiceExcel.saveOrders(orderSaveExcelParseResponseDto.getOrderExcelInfoList());

            //then
            assertEquals(1000, orderSaveExcelResponseDto.getSeccuessfulCount());
            assertEquals(0, orderSaveExcelResponseDto.getFailedCount());
        }
    }

    @Test
    @Order(4)
    @DisplayName("엑셀파일_1000건등록_재고부족")
    void 엑셀파일_1000건등록_재고부족() throws IOException {
        //given
        ClassPathResource resource = new ClassPathResource("excel_order_1000_fail_stock.xlsx");
        try (InputStream inputStream = resource.getInputStream()) {
            MultipartFile file = new MockMultipartFile("file.xlsx", "excel_order_1000_fail_stock.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);

            //when
            OrderSaveExcelParseResponseDto orderSaveExcelParseResponseDto = orderServiceExcel.parseExcelFile(file);
            OrderSaveExcelResponseDto orderSaveExcelResponseDto = orderServiceExcel.saveOrders(orderSaveExcelParseResponseDto.getOrderExcelInfoList());

            //then
            assertNotEquals(1000, orderSaveExcelResponseDto.getSeccuessfulCount());

        }
    }
}