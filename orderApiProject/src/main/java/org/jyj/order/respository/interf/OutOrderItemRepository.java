package org.jyj.order.respository.interf;

import org.jyj.order.entity.OutOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutOrderItemRepository extends JpaRepository<OutOrderItem, Long> {

}
