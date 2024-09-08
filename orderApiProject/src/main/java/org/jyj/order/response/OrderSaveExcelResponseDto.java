package org.jyj.order.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderSaveExcelResponseDto {
    private final int seccuessfulCount;
    private final int failedCount;
    private final List<String> successfulGroups;
    private final List<String> failedGroups;
    private final List<String> errorMessages;
}
