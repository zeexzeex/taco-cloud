package tacos.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import tacos.domain.Order;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

	// 타코 주문 폼
	@GetMapping("/current")
	public String orderForm(Model model) {
		model.addAttribute("order", new Order());
		return "orderForm";
	}

	// 주문 유효성 검사
	@PostMapping
	public String processOrder(@Valid Order order, Errors errors) {
		if(errors.hasErrors()) {
			return "orderForm";
		}

		// log.info("Order submitted: " + order);
		return "redirect:/";
	}
}
