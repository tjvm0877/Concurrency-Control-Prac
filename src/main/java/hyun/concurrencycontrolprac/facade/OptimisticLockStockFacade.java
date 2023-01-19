package hyun.concurrencycontrolprac.facade;

import org.springframework.stereotype.Service;

import hyun.concurrencycontrolprac.service.StockService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

	private final StockService stockService;

	public void decrease(Long id, Long quantity) throws InterruptedException {
		boolean success = false;

		// Lock으로 인해 저장 실패 시 50ms이후 다시 시도
		while (!success) {
			try {
				stockService.optimisticDecrease(id, quantity);
				success = true;
			} catch (Exception e) {
				Thread.sleep(50);
			}
		}
	}
}
