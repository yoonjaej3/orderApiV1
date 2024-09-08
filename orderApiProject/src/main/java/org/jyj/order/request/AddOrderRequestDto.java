package org.jyj.order.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jyj.common.validation.ValidationSequence;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated(ValidationSequence.class)
public class AddOrderRequestDto {
    @Valid
    private OrderBasicInfo orderBasicInfo;
    @Valid
    private List<OrderItemInfo> orderItemInfoList;
}
