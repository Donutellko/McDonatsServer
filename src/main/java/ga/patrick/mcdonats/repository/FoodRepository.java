package ga.patrick.mcdonats.repository;

import ga.patrick.mcdonats.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

@Query("select f from Food f where f.count > 0")
List<Food> findPresentFood();

}
