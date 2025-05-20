package tacos.Controller;

import static tacos.domain.Ingredient.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.domain.Ingredient;
import tacos.domain.Taco;

@Controller
@RequestMapping("/design")
public class DesignTacoController {
	private final IngredientRepository ingredientRepository;
	private TacoRepository tacoRepository;

	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository tacoRepository) {
		this.ingredientRepository = ingredientRepository;
		this.tacoRepository = tacoRepository;
	}

	// DB에서 조회해서 가져오기
	@GetMapping
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepository.findAll().forEach(ingredients::add);

		Type[] types = Ingredient.Type.values();	// 재료 배열
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(),
				filterByType(ingredients, type));
		}

		model.addAttribute("taco", new Taco());
		return "design";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
			.stream()
			.filter(x -> x.getType().equals(type)) 	// 특정 종류에 해당하는 것만 반환
			.collect(Collectors.toList());
	}

	// 타코 유효성 검사
	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors) {
		if(errors.hasErrors()) {
			return "design";
		}

		// log.info("Processing Design: " + design);
		return "redirect:/orders/current";
	}
}
