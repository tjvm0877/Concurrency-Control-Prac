package hyun.concurrencycontrolprac.facade;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import hyun.concurrencycontrolprac.service.StockService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

	private RedissonClient redissonClient;
	private StockService stockService;

	public void decrease(Long key, Long quantity) {
		RLock lock = redissonClient.getLock(key.toString());

		try {
			boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

			if (!available) {
				System.out.println("락 획득 실패");
			}

			stockService.decrease(key, quantity);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}
