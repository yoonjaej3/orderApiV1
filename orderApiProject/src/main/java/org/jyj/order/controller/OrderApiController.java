package org.jyj.order.controller;

import lombok.RequiredArgsConstructor;
import org.jyj.common.response.GenericResponse;
import org.jyj.common.validation.ValidationSequence;
import org.jyj.order.request.AddOrderRequestDto;
import org.jyj.order.response.OrderSaveExcelParseResponseDto;
import org.jyj.order.response.OrderSaveExcelResponseDto;
import org.jyj.order.response.OrderSaveResponseDto;
import org.jyj.order.service.interf.OrderService;
import org.jyj.order.service.interf.OrderServiceExcel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderApiController {
    private static final String SAVE_ONE_ORDER_MESSAGE = "주문 단건 등록";
    private static final String SAVE_EXCEL_ORDER_MESSAGE = "엑셀 주문 등록";

    private final OrderService orderService;
    private final OrderServiceExcel orderServiceExcel;

    @PostMapping("/orders")
    public GenericResponse<OrderSaveResponseDto> saveOder(@Validated(ValidationSequence.class) @RequestBody AddOrderRequestDto addOrderRequestDto) {

        return new GenericResponse<>(
                SAVE_ONE_ORDER_MESSAGE, orderService.saveOrders(addOrderRequestDto.getOrderBasicInfo(), addOrderRequestDto.getOrderItemInfoList())
        );
    }

    @PostMapping("/orders/excel")
    public GenericResponse<OrderSaveExcelResponseDto> saveExcel(@RequestParam("file") MultipartFile file) {
        OrderSaveExcelParseResponseDto orderSaveExcelParseResponseDto = orderServiceExcel.parseExcelFile(file);

        if (orderSaveExcelParseResponseDto.getErrorMessages() != null) {
            return new GenericResponse<>(
                    SAVE_EXCEL_ORDER_MESSAGE, OrderSaveExcelResponseDto.builder()
                    .errorMessages(orderSaveExcelParseResponseDto.getErrorMessages())
                    .build()
            );
        }

        return new GenericResponse<>(
                SAVE_EXCEL_ORDER_MESSAGE, orderServiceExcel.saveOrders(orderSaveExcelParseResponseDto.getOrderExcelInfoList())
        );
    }

}
