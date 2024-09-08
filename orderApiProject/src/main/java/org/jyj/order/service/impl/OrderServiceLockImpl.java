package org.jyj.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jyj.order.entity.Item;
import org.jyj.order.entity.OutOrder;
import org.jyj.order.exception.Item.NotAllocateStockException;
import org.jyj.order.exception.order.NotMakeOrderNoException;
import org.jyj.order.respository.interf.RedisLockRepository;
import org.jyj.order.service.interf.OrderServiceLock;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceLockImpl implements OrderServiceLock {
    private final RedisLockRepository redisLockRepository;

    public OutOrder makeOrdNo(OutOrder outOrder) {

        // seq 값을 이용해 락 키 생성
        Long lockKey = generateLockKey(Long.toString(outOrder.getSeq()));

        // 락 획득
        boolean isLocked = redisLockRepository.lock(lockKey);

        if (!isLocked) {
            throw new NotMakeOrderNoException();
        }

        try {
            // 주문 번호 생성
            outOrder.makeOrdNo(outOrder.getSeq());

            return outOrder;
        } finally {
            // 락 해제
            redisLockRepository.unlock(lockKey);
        }
    }

    public Item decreaseItem(Item item, int itemCnt) {
        Long lockKey = generateLockKey(item.getItemCd());

        // 락 획득
        boolean isLocked = redisLockRepository.lock(lockKey);

        if (!isLocked) {
            throw new NotAllocateStockException();
        }

        try {
            item.decrease(itemCnt);

            return item;
        } finally {
            //락 해제
            redisLockRepository.unlock(lockKey);
        }
    }

    private Long generateLockKey(String locKey) {
        // 아이템 코드를 조합하여 고유한 락 키 생성
        return (locKey).hashCode() & 0xffffffffL;
    }
}
