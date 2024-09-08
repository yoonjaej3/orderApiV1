package org.jyj.order.service.interf;


import org.jyj.order.request.OrderExcelInfo;
import org.jyj.order.response.OrderSaveExcelParseResponseDto;
import org.jyj.order.response.OrderSaveExcelResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrderServiceExcel {
    OrderSaveExcelParseResponseDto parseExcelFile(MultipartFile file);

    OrderSaveExcelResponseDto saveOrders(List<OrderExcelInfo> orderExcelInfo);

}
