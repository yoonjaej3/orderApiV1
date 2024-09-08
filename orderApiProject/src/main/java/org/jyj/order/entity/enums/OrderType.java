package org.jyj.order.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    NORMAL_ORDER("NORMAL", "정상 주문"),
    ONEDAY_ORDER("ONEDAY", "하루 배송");

    private final String key;
    private final String description;
    private static final Map<String, OrderType> keyToOrderTypeMap = new HashMap<>();

    static {
        for (OrderType type : OrderType.values()) {
            keyToOrderTypeMap.put(type.getKey(), type);
        }
    }

    public static String getKey(OrderType orderType) {
        if (orderType == null) {
            throw new IllegalArgumentException("OrderType이 존재하지 않습니다.");
        }
        return orderType.getKey();
    }
}
