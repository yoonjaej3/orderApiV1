package org.jyj.order.respository.interf;

import org.jyj.order.entity.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom{
    Item findCntByItemCd(String itemCd);
}
