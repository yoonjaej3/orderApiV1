package org.jyj.order.service.interf;


import org.jyj.order.entity.Item;
import org.jyj.order.entity.OutOrder;

public interface OrderServiceLock {

    OutOrder makeOrdNo(OutOrder outOrder);

    Item decreaseItem(Item item, int itemCnt);
}
