package org.jyj.order.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderExcelInfo {
    private String orderGroup;

    private String whCd;

    private String orderDt;

    private String itemCd;

    private String itemNm;

    private String itemCnt;

    private String ordererNm;

    private String ordererAdress;

}