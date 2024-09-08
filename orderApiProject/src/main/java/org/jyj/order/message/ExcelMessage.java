package org.jyj.order.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExcelMessage {

    public static final String FILE_PROCESSING_ERROR = "엑셀 파일을 처리하는 중 오류 발생: ";
    public static final String ROW_DATA_ERROR = "Row %d에서 데이터 오류: %s";
    public static final String ROW_FORMAT_ERROR = "Row %d: 양식 오류 - 필수 값이 비어 있습니다. (비어 있는 칼럼: %s)";
    public static final String ORDER_GROUP_SAVE_FAILURE = "주문 그룹 %s 등록 실패: %s";
}
