package org.jyj.order.service.interf;


import org.jyj.order.request.OrderBasicInfo;
import org.jyj.order.request.OrderItemInfo;
import org.jyj.order.response.OrderSaveResponseDto;

import java.util.List;

public interface OrderService {

    OrderSaveResponseDto saveOrders(OrderBasicInfo orderBasicInfo, List<OrderItemInfo> orderItemInfoList);

    void processItem(String itemCd, int orderCnt);
}
