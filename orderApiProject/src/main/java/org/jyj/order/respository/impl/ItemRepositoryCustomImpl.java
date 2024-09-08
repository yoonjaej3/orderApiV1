package org.jyj.order.respository.impl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.jyj.order.entity.Item;
import org.jyj.order.entity.QItem;
import org.jyj.order.respository.interf.ItemRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Item findCntByItemCd(String itemCd) {
        QItem item = QItem.item;

        Item foundItem = jpaQueryFactory
                .selectFrom(item)
                .where(item.itemCd.eq(itemCd))
                .fetchOne();

        return foundItem;
    }
}
