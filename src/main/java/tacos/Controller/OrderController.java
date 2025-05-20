package tacos.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import tacos.data.OrderRepository;
import tacos.domain.Order;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {
	private OrderRepository orderRepository;

	public OrderController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	// 타코 주문 폼
	@GetMapping("/current")
	public String orderForm() {
		return "orderForm";
	}

	// 주문 유효성 검사, 주문 처리
	@PostMapping
	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
		if(errors.hasErrors()) {
			return "orderForm";
		}

		orderRepository.save(order);
		sessionStatus.setComplete();	// 세션 재설정

		return "redirect:/";
	}
}
