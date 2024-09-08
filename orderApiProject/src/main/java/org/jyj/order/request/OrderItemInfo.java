package org.jyj.order.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jyj.common.validation.group.NotEmptyGroup;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemInfo {
    @NotEmpty(groups = {NotEmptyGroup.class})
    private String itemCd;

    @NotEmpty(groups = {NotEmptyGroup.class})
    private String itemNm;

    @NotNull(groups = {NotEmptyGroup.class})
    private int itemCnt;

}
