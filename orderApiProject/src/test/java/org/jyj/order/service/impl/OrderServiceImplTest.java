package org.jyj.order.service.impl;

import org.junit.jupiter.api.*;
import org.jyj.order.entity.Item;
import org.jyj.order.entity.OutOrder;
import org.jyj.order.entity.enums.OrderStatusType;
import org.jyj.order.entity.enums.OrderType;
import org.jyj.order.exception.Item.NotEnoughStockException;
import org.jyj.order.request.OrderBasicInfo;
import org.jyj.order.request.OrderItemInfo;
import org.jyj.order.response.OrderSaveResponseDto;
import org.jyj.order.respository.interf.ItemRepository;
import org.jyj.order.respository.interf.OutOrderRepository;
import org.jyj.order.service.interf.OrderService;
import org.jyj.order.service.interf.OrderServiceLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceImplTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OutOrderRepository outOrderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderServiceLock orderServiceLock;


    @BeforeEach
    public void setup() {
        outOrderRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("주문_생성_성공")
    void 주문_생성() {
        //given
        OrderBasicInfo orderBasicInfo = orderBasicInfoDummy();
        List<OrderItemInfo> orderItemInfoList = orderItemInfoListDummy();

        //when
        OrderSaveResponseDto responseDto = orderService.saveOrders(orderBasicInfo, orderItemInfoList);

        //then
        assertEquals("YI012024090100001", responseDto.getOrderNo());
    }

    @Test
    @Order(2)
    @DisplayName("동시에_100개_요청_순차적_확인")
    public void 동시에_100개_요청_순차적_확인() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<OutOrder> orderList = new ArrayList<>();

        //when
        for (int i = 0; i < threadCount; i++) {
            int seq = i + 1;  // 각 스레드에 고유한 seq 값을 할당
            executorService.submit(() -> {
                try {
                    OutOrder outOrder = OutOrder.builder()
                            .seq((long) seq)
                            .whCd("WH01")
                            .orderDt("20240904")
                            .ordererNm("TestUser")
                            .ordererAddress("TestAddress")
                            .orderType("NORMAL")
                            .status("PENDING")
                            .build();

                    OutOrder order = orderServiceLock.makeOrdNo(outOrder);
                    synchronized (orderList) {
                        orderList.add(order);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        //then
        outOrderRepository.saveAll(orderList);
        assertEquals(threadCount, orderList.size(), "저장된 주문 개수가 예상과 다릅니다.");
    }

    @Test
    @Order(3)
    @DisplayName("동시에_100개_요청_락_실패_예외_확인")
    public void 동시에_100개_요청_락_실패_예외_확인() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<OutOrder> orderList = new ArrayList<>();
        List<Exception> exceptionList = Collections.synchronizedList(new ArrayList<>());

        //when
        for (int i = 0; i < threadCount; i++) {
            int seq = i + 1;  // 각 스레드에 고유한 seq 값을 할당
            executorService.submit(() -> {
                try {
                    OutOrder outOrder = OutOrder.builder()
                            .seq((long) seq)
                            .whCd("WH01")
                            .orderDt("20240904")
                            .ordererNm("TestUser")
                            .ordererAddress("TestAddress")
                            .orderType("NORMAL")
                            .status("PENDING")
                            .build();

                    OutOrder order = orderServiceLock.makeOrdNo(outOrder);
                    synchronized (orderList) {
                        orderList.add(order);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        //then
        exceptionList.forEach(e -> assertTrue(e.getMessage().contains("주문 번호 생성 작업이 현재 다른 작업에 의해 사용 중입니다. 잠시 후 다시 시도해 주세요."),
                "예외 메시지가 예상과 일치하지 않습니다."));
    }

    @Test
    @Order(4)
    @DisplayName("주문생성_재고감소_성공")
    void 주문생성_재고감소_확인() {
        //given
        getTotalCntWhenSaveItem("M00001", 10);
        int orderCnt = 2;

        //when
        orderService.processItem("M00001", orderCnt);

        //then
        Item updatedItem = itemRepository.findCntByItemCd("M00001");
        assertEquals(8, updatedItem.getItemCnt()); // itemCnt(2) 만큼 감소했는지 확인
    }

    @Test
    @Order(5)
    @DisplayName("주문생성_재고감소_실패")
    void 주문생성_재고감소_실패() {
        //given
        getTotalCntWhenSaveItem("M00003", 10);
        int orderCnt = 11;

        //when & then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.processItem("M00003", orderCnt);
        });

        Item updatedItem = itemRepository.findCntByItemCd("M00003");
        assertEquals(10, updatedItem.getItemCnt()); // 재고가 감소하지 않았음을 확인
    }

    private void getTotalCntWhenSaveItem(String itemCd, int totalCnt) {
        Item item = Item.builder()
                .itemCd(itemCd)
                .itemCnt(totalCnt)
                .build();

        itemRepository.save(item);
    }


    private List<OrderItemInfo> orderItemInfoListDummy() {
        List<OrderItemInfo> orderItemInfoList = new ArrayList<>();

        OrderItemInfo orderItemInfo01 = OrderItemInfo.builder()
                .itemCd("M00001")
                .itemNm("아이템명01")
                .itemCnt(2)
                .build();

        OrderItemInfo orderItemInfo02 = OrderItemInfo.builder()
                .itemCd("M00002")
                .itemNm("아이템명02")
                .itemCnt(1)
                .build();

        orderItemInfoList.add(orderItemInfo01);
        orderItemInfoList.add(orderItemInfo02);
        return orderItemInfoList;
    }

    private OrderBasicInfo orderBasicInfoDummy() {
        OrderBasicInfo orderBasicInfo = OrderBasicInfo.builder()
                .orderDt("20240901")
                .whCd("YI01")
                .ordererNm("주문자명01")
                .ordererAdress("주문자명01주소")
                .orderType(OrderType.NORMAL_ORDER)
                .status(OrderStatusType.REQUEST)
                .build();
        return orderBasicInfo;
    }
}