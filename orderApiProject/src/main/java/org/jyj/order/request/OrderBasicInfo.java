package org.jyj.order.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jyj.common.validation.group.NotEmptyGroup;
import org.jyj.order.entity.enums.OrderStatusType;
import org.jyj.order.entity.enums.OrderType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBasicInfo {
    @NotEmpty(groups = {NotEmptyGroup.class})
    private String orderDt;

    @NotEmpty(groups = {NotEmptyGroup.class})
    private String whCd;

    @NotEmpty(groups = {NotEmptyGroup.class})
    private String ordererNm;

    @NotEmpty(groups = {NotEmptyGroup.class})
    private String ordererAdress;

    @NotNull(groups = {NotEmptyGroup.class})
    private OrderType orderType;

    @NotNull(groups = {NotEmptyGroup.class})
    private OrderStatusType status;
}