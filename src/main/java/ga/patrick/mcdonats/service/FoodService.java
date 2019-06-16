package ga.patrick.mcdonats.service;

import ga.patrick.mcdonats.domain.Food;
import ga.patrick.mcdonats.repository.FoodRepository;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

private FoodRepository foodRepository;

public FoodService(FoodRepository foodRepository) {
    this.foodRepository = foodRepository;
}

/**
 * Find food with provided id.
 *
 * @throws UnknownFoodException when food with provided id does not exist.
 */
Food findById(int id) throws UnknownFoodException {
    return foodRepository.findById(id).orElseThrow(() -> new UnknownFoodException(String.valueOf(id)));
}

public class UnknownFoodException extends Throwable {
    UnknownFoodException(String message) { super(message); }
}
}
