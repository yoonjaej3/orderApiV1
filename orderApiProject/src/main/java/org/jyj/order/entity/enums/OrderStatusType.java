package org.jyj.order.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum OrderStatusType {
    REQUEST("REQUEST", "출고 요청"),
    COMPLETED("COMPLETED", "출고 완료"),
    CANCELED("CANCELED", "출고 취소");

    private final String key;
    private final String description;
    private static final Map<String, OrderStatusType> keyToOrderStatusMap = new HashMap<>();

    static {
        for (OrderStatusType status : OrderStatusType.values()) {
            keyToOrderStatusMap.put(status.getKey(), status);
        }
    }

    public static String getKey(OrderStatusType orderStatus) {
        if (orderStatus == null) {
            throw new IllegalArgumentException("OrderStatus가 존재하지 않습니다.");
        }
        return orderStatus.getKey();
    }

}
