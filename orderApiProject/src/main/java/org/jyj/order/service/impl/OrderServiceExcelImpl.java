package org.jyj.order.service.impl;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.jyj.order.entity.enums.OrderStatusType;
import org.jyj.order.entity.enums.OrderType;
import org.jyj.order.request.OrderBasicInfo;
import org.jyj.order.request.OrderExcelInfo;
import org.jyj.order.request.OrderItemInfo;
import org.jyj.order.response.OrderSaveExcelParseResponseDto;
import org.jyj.order.response.OrderSaveExcelResponseDto;
import org.jyj.order.service.interf.OrderService;
import org.jyj.order.service.interf.OrderServiceExcel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jyj.order.message.ExcelMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceExcelImpl implements OrderServiceExcel {

    private final OrderService orderService;


    public OrderSaveExcelParseResponseDto parseExcelFile(MultipartFile file) {
        List<OrderExcelInfo> orderExcelInfoList = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                //첫번째 행 건너뛰기
                if (row.getRowNum() == 0) {
                    continue;
                }

                try {
                    //엑셀 파일 파싱
                    OrderExcelInfo orderExcelInfo = excelParse(row);

                    //필수값 겁증
                    if (nullValidation(errorMessages, row, orderExcelInfo)) continue;

                    orderExcelInfoList.add(orderExcelInfo);

                } catch (NullPointerException | IllegalStateException e) {
                    String errorMessage = String.format(ROW_DATA_ERROR, row.getRowNum(), e.getMessage());
                    errorMessages.add(errorMessage);
                }
            }
        } catch (IOException e) {
            errorMessages.add(FILE_PROCESSING_ERROR + e.getMessage());
        }

        if (!errorMessages.isEmpty()) {
            return OrderSaveExcelParseResponseDto.builder()
                    .errorMessages(errorMessages)
                    .build();
        }

        return OrderSaveExcelParseResponseDto.builder()
                .orderExcelInfoList(orderExcelInfoList)
                .build();
    }

    private boolean nullValidation(List<String> errorMessages, Row row, OrderExcelInfo orderExcelInfo) {
        List<String> missingFields = isAnyFieldEmptyWithColumnInfo(orderExcelInfo);
        if (!missingFields.isEmpty()) {
            String errorMessage = String.format(ROW_FORMAT_ERROR,
                    row.getRowNum(), String.join(", ", missingFields));
            errorMessages.add(errorMessage);
            return true;
        }
        return false;
    }

    private OrderExcelInfo excelParse(Row row) {
        OrderExcelInfo orderExcelInfo = OrderExcelInfo.builder()
                .orderGroup(getCellValueAsString(row.getCell(0)))
                .whCd(getCellValueAsString(row.getCell(1)))
                .orderDt(getCellValueAsString(row.getCell(2)))
                .itemCd(getCellValueAsString(row.getCell(3)))
                .itemNm(getCellValueAsString(row.getCell(4)))
                .itemCnt(getCellValueAsString(row.getCell(5)))
                .ordererNm(getCellValueAsString(row.getCell(6)))
                .ordererAdress(getCellValueAsString(row.getCell(7)))
                .build();
        return orderExcelInfo;
    }

    private List<String> isAnyFieldEmptyWithColumnInfo(OrderExcelInfo info) {
        List<String> missingFields = new ArrayList<>();

        if (StringUtils.isBlank(info.getOrderGroup())) {
            missingFields.add("주문그룹");
        }
        if (StringUtils.isBlank(info.getWhCd())) {
            missingFields.add("센터");
        }
        if (StringUtils.isBlank(info.getOrderDt())) {
            missingFields.add("주문일자");
        }
        if (StringUtils.isBlank(info.getItemCd())) {
            missingFields.add("상품PK");
        }
        if (StringUtils.isBlank(info.getItemNm())) {
            missingFields.add("상품명");
        }
        if (StringUtils.isBlank(info.getItemCnt())) {
            missingFields.add("상품수량");
        }
        if (StringUtils.isBlank(info.getOrdererNm())) {
            missingFields.add("주문자명");
        }
        if (StringUtils.isBlank(info.getOrdererAdress())) {
            missingFields.add("주문자 주소");
        }

        return missingFields;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim(); // 문자열의 공백 제거
            case NUMERIC:
                // 숫자를 문자열로 변환
                if (Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue()) {
                    // 정수형으로 변환
                    return String.valueOf((long) cell.getNumericCellValue());
                } else {
                    // 실수형으로 변환
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }

    public OrderSaveExcelResponseDto saveOrders(List<OrderExcelInfo> orderExcelInfoList) {
        List<String> successfulGroups = new ArrayList<>();
        List<String> failedGroups = new ArrayList<>();

        // 주문 그룹별로 그룹화
        Map<String, List<OrderExcelInfo>> groupedOrders = orderExcelInfoList.stream()
                .collect(Collectors.groupingBy(OrderExcelInfo::getOrderGroup));

        for (String orderGroup : groupedOrders.keySet()) {

            List<OrderExcelInfo> group = groupedOrders.get(orderGroup);
            OrderBasicInfo orderBasicInfo = getOrderBasicInfo(group.get(0));
            List<OrderItemInfo> orderItemInfoList = getOrderItemInfoList(group);

            try {
                orderService.saveOrders(orderBasicInfo, orderItemInfoList);
                successfulGroups.add(orderGroup);
            } catch (Exception e) {
                String failureMessage = String.format(ORDER_GROUP_SAVE_FAILURE, orderGroup, e.getMessage());
                failedGroups.add(failureMessage);
            }
        }

        return OrderSaveExcelResponseDto.builder()
                .seccuessfulCount(successfulGroups.size())
                .failedCount(failedGroups.size())
                .successfulGroups(successfulGroups)
                .failedGroups(failedGroups)
                .build();
    }

    private static List<OrderItemInfo> getOrderItemInfoList(List<OrderExcelInfo> group) {
        List<OrderItemInfo> orderItemInfoList = group.stream()
                .map(info -> OrderItemInfo.builder()
                        .itemCd(info.getItemCd())
                        .itemNm(info.getItemNm())
                        .itemCnt(Integer.parseInt(info.getItemCnt()))
                        .build())
                .collect(Collectors.toList());
        return orderItemInfoList;
    }

    private static OrderBasicInfo getOrderBasicInfo(OrderExcelInfo firstEntry) {
        OrderBasicInfo orderBasicInfo = OrderBasicInfo.builder()
                .orderDt(firstEntry.getOrderDt())
                .whCd(firstEntry.getWhCd())
                .ordererNm(firstEntry.getOrdererNm())
                .ordererAdress(firstEntry.getOrdererAdress())
                .orderType(OrderType.NORMAL_ORDER)
                .status(OrderStatusType.REQUEST)
                .build();
        return orderBasicInfo;
    }
}

