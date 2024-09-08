package org.jyj.order.entity;

import lombok.*;
import org.jyj.common.utils.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_OUT_ORD_ITEM")
public class OutOrderItem extends BaseEntity {
    @Id
    @Column(name = "item_cd")
    private String itemCd;

    @Column(name = "item_nm")
    private int itemNm;

    @Column(name = "item_cnt")
    private int itemCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no")
    private OutOrder outOrder;
}
