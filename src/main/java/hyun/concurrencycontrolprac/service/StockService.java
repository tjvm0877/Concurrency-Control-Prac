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
}
