package tacos.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import tacos.domain.Order;
import tacos.domain.Taco;

@Repository
public class JdbcOrderRepository implements OrderRepository {
	private SimpleJdbcInsert orderInserter;		// Taco_order에 주문 데이터 추가
	private SimpleJdbcInsert orderTacoInserter;	// Taco_order_Tacos에 주문 id, 타코 id 추가
	private ObjectMapper objectMapper;

	public JdbcOrderRepository(JdbcTemplate jdbc) {
		this.orderInserter = new SimpleJdbcInsert(jdbc)
			.withTableName("Taco_order")
			.usingGeneratedKeyColumns("id");
		this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
			.withTableName("Taco_order_Tacos");
		this.objectMapper = new ObjectMapper();
	}

	// 주문, 타코 객체 저장 처리 총괄
	@Override
	public Order save(Order order) {
		order.setPlacedAt(new Date());
		long orderId = saveOrderDetail(order);
		order.setId(orderId);
		List<Taco> tacos = order.getTacos();

		for (Taco taco : tacos) {
			saveTacoToOrder(taco, orderId);
		}

		return order;
	}

	// 타코, 주문 객체 저장
	private void saveTacoToOrder(Taco taco, long orderId) {
		Map<String, Object> values = new HashMap<>();
		values.put("tacoOrder", orderId);
		values.put("taco", taco.getId());
		orderTacoInserter.execute(values);
	}

	// 주문 데이터 저장, 자동 생성된 기본키(ID) 반환
	private long saveOrderDetail(Order order) {
		@SuppressWarnings("unchecked") Map<String, Object> values =
			objectMapper.convertValue(order, Map.class); 	// Taco_order 테이블 placedAt 속성 타입으로 변환
		values.put("placedAt", order.getPlacedAt());

		long orderId = orderInserter
			.executeAndReturnKey(values)
			.longValue();

		return orderId;
	}
}
