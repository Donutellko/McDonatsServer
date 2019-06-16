package ga.patrick.mcdonats.repository;

import ga.patrick.mcdonats.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Food> {

@Query("select f from Storage s join s.food f where s.count > 0")
List<Food> findPresentFood();

}
