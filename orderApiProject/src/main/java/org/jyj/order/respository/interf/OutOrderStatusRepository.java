package org.jyj.order.respository.interf;

import org.jyj.order.entity.OutOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutOrderStatusRepository extends JpaRepository<OutOrderStatus, Long> {

}
