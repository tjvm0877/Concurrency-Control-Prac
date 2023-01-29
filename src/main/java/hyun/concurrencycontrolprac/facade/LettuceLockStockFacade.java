package hyun.concurrencycontrolprac.facade;

import org.springframework.stereotype.Component;

import hyun.concurrencycontrolprac.repository.RedisLockRepository;
import hyun.concurrencycontrolprac.service.StockService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

	private RedisLockRepository redisLockRepository;
	private StockService stockService;

	public void decrease(Long key, Long quantity) throws InterruptedException {
		while (!redisLockRepository.lock(key)) {
			Thread.sleep(100);
		}

		try {
			stockService.decrease(key, quantity);
		} finally {
			redisLockRepository.unlock(key);
		}
	}
}
