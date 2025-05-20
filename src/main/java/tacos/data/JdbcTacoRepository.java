package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.domain.Ingredient;
import tacos.domain.Taco;

@Repository
public class JdbcTacoRepository implements TacoRepository {
	private JdbcTemplate jdbc;

	public JdbcTacoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Taco save(Taco taco) {
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);
		for (Ingredient ingredient : taco.getIngredients()) {
			saveIngredientToTaco(ingredient, tacoId);
		}
		return taco;
	}

	// 타코 식자재 저장
	private long saveTacoInfo(Taco taco) {
		taco.setDate(new Date());
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
			"insert into Taco (name, date) values (?, ?)",
			Types.VARCHAR, Types.TIMESTAMP).newPreparedStatementCreator(
			Arrays.asList(
				taco.getName(),
				new Timestamp(taco.getDate().getTime())));

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc, keyHolder);

		return keyHolder.getKey().longValue();
	}

	// 타코, 식자재 연관 정보 저장
	private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
		jdbc.update("insert into Taco_Ingredients (taco, ingredient) values (?, ?)",
			tacoId, ingredient.getId());
	}
}
