package org.jyj.order.respository.interf;

import org.springframework.stereotype.Repository;

@Repository
public interface RedisLockRepository {

    Boolean lock(Long key);

    Boolean unlock(Long key);
}