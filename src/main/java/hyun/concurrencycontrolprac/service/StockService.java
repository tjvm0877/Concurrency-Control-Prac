package hyun.concurrencycontrolprac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyun.concurrencycontrolprac.entity.Stock;
import hyun.concurrencycontrolprac.repository.StockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;

	@Transactional
	public void decrease(Long id, Long quantity) {
		Stock selectedStock = stockRepository.findById(id).orElseThrow();

		selectedStock.decrease(quantity);
	}

	@Transactional
	public void pessimisticDecrease(Long id, Long quantity) {
		// 비관적 잠금(Pessimistic Lock)을 걸고 데이터 조회 후
		Stock stock = stockRepository.findByIdWithPessimisticLock(id);
		stock.decrease(quantity);
	}

	@Transactional
	public void optimisticDecrease(Long id, Long quantity) {
		// 낙관적 잠금(Optimistic Lock)을 걸고 데이터 조회
		Stock stock = stockRepository.findByIdWithOptimisticLock(id);
		stock.decrease(quantity);
	}
}
