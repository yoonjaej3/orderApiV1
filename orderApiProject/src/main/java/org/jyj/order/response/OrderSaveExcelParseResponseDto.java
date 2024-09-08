package org.jyj.order.response;

import lombok.Builder;
import lombok.Getter;
import org.jyj.order.request.OrderExcelInfo;

import java.util.List;

@Getter
@Builder
public class OrderSaveExcelParseResponseDto {
    private final List<OrderExcelInfo> orderExcelInfoList;
    List<String> errorMessages;
}
