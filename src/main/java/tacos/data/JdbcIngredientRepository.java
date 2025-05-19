package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tacos.domain.Ingredient;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {
	private JdbcTemplate jdbc;

	@Autowired
	public JdbcIngredientRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	// 객체 반환
	@Override
	public Iterable<Ingredient> findAll() {
		return jdbc.query("select id, name, type from Ingredient",
			this::mapRowatoIngredient);
	}

	@Override
	public Ingredient findById(String id) {
		return jdbc.queryForObject("select id, name, type from Ingredient where id=?",
			this::mapRowatoIngredient);
	}

	// 데이터 저장
	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbc.update(
			"insert into Ingredient (id, name, type) values (?, ?, ?)",
			ingredient.getId(),
			ingredient.getName(),
			ingredient.getType().toString());
		return ingredient;
	}

	// 쿼리 결과 세트의 행만큼 호출하고 객체로 생성, List에 저장해 반환
	private Ingredient mapRowatoIngredient(ResultSet rs, int rowNum) throws SQLException {
			return new Ingredient(
				rs.getString("id"),
				rs.getString("name"),
				Ingredient.Type.valueOf(rs.getString("type"))
			);
	}
}

