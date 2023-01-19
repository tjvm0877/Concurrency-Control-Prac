package hyun.concurrencycontrolprac.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyun.concurrencycontrolprac.entity.Stock;
import hyun.concurrencycontrolprac.repository.StockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PessimisticStockService {

	private final StockRepository stockRepository;

	@Transactional
	public Long decrease(Long id, Long quantity) {
		// 비관적 잠금(Pessimistic Lock)을 걸고 데이터 조회 후, 데이터를 저장
		Stock stock = stockRepository.findByIdWithPessimisticLock(id);
		stock.decrease(quantity);

		return stock.getQuantity();
	}
}
