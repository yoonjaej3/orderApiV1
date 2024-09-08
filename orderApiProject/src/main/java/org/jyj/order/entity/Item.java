package org.jyj.order.entity;

import lombok.*;
import org.jyj.common.utils.BaseEntity;
import org.jyj.order.exception.Item.NotEnoughStockException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_ITEM")
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_cd")
    private String itemCd;

    @Column(name = "item_cnt")
    private int itemCnt;

    public void decrease(int cnt) {
        if (this.itemCnt - cnt < 0) {
            throw new NotEnoughStockException();
        }
        this.itemCnt -= cnt;
    }

}
