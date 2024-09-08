package org.jyj.order.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderSaveResponseDto {
    private final String orderNo;

}
