package org.jyj.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jyj.order.entity.Item;
import org.jyj.order.entity.OutOrder;
import org.jyj.order.entity.OutOrderItem;
import org.jyj.order.entity.enums.OrderStatusType;
import org.jyj.order.entity.enums.OrderType;
import org.jyj.order.request.OrderBasicInfo;
import org.jyj.order.request.OrderItemInfo;
import org.jyj.order.response.OrderSaveResponseDto;
import org.jyj.order.respository.interf.ItemRepository;
import org.jyj.order.respository.interf.OutOrderItemRepository;
import org.jyj.order.respository.interf.OutOrderRepository;
import org.jyj.order.service.interf.OrderService;
import org.jyj.order.service.interf.OrderServiceLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OutOrderRepository outOrderRepository;
    private final OutOrderItemRepository outOrderItemRepository;
    private final ItemRepository itemRepository;
    private final OrderServiceLock orderServiceLock;

    @Override
    @Transactional
    public OrderSaveResponseDto saveOrders(OrderBasicInfo orderBasicInfo, List<OrderItemInfo> orderItemInfoList) {
        OutOrder savedOrder = processOutOrder(orderBasicInfo);
        processOutOrderItem(orderItemInfoList, savedOrder);

        return getOrderSaveResponseDto(savedOrder);

    }

    private OrderSaveResponseDto getOrderSaveResponseDto(OutOrder savedOrder) {
        OrderSaveResponseDto orderSaveResponseDto = OrderSaveResponseDto.builder()
                .orderNo(savedOrder.getOrderNo())
                .build();
        return orderSaveResponseDto;
    }

    private void processOutOrderItem(List<OrderItemInfo> orderItemInfoList, OutOrder savedOrder) {
        for (OrderItemInfo orderItemInfo : orderItemInfoList) {
            OutOrderItem outOrderItem = OutOrderItem.builder()
                    .itemCd(orderItemInfo.getItemCd())
                    .itemCnt(orderItemInfo.getItemCnt())
                    .outOrder(savedOrder)
                    .build();

            outOrderItemRepository.save(outOrderItem);

            processItem(orderItemInfo.getItemCd(), orderItemInfo.getItemCnt());
        }
    }

    public void processItem(String itemCd, int itemCnt) {
        Item item = itemRepository.findCntByItemCd(itemCd);

        Item resultItem = orderServiceLock.decreaseItem(item, itemCnt);

        itemRepository.save(resultItem);
    }

    private OutOrder processOutOrder(OrderBasicInfo orderBasicInfo) {
        OutOrder outOrder = OutOrder.builder()
                .orderDt(orderBasicInfo.getOrderDt())
                .whCd(orderBasicInfo.getWhCd())
                .ordererNm(orderBasicInfo.getOrdererNm())
                .ordererAddress(orderBasicInfo.getOrdererAdress())
                .orderType(OrderType.getKey(orderBasicInfo.getOrderType()))
                .status(OrderStatusType.getKey(orderBasicInfo.getStatus()))
                .build();

        OutOrder savedOrder = outOrderRepository.save(outOrder);

        OutOrder resultOrder = orderServiceLock.makeOrdNo(savedOrder);

        outOrderRepository.save(resultOrder);

        return resultOrder;
    }
}
