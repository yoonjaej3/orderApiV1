package org.jyj.order.respository;

import org.jyj.order.respository.interf.RedisLockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisLockRepositoryImplTest {
    @Autowired
    private RedisLockRepository redisLockRepository;

    @Test
    @DisplayName("Redis_실행_확인")
    public void Redis_실행_확인() {
        Boolean lockResult = redisLockRepository.lock(1L);
        assertTrue(lockResult);

        Boolean unlockResult = redisLockRepository.unlock(1L);
        assertTrue(unlockResult);
    }

}