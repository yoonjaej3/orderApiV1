package org.jyj.order.respository.interf;

import org.jyj.order.entity.OutOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutOrderRepository extends JpaRepository<OutOrder, Long> {

}
