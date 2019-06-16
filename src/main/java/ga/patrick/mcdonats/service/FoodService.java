package ga.patrick.mcdonats.service;

import ga.patrick.mcdonats.domain.Food;
import ga.patrick.mcdonats.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

private FoodRepository foodRepository;


public FoodService(FoodRepository foodRepository) {
    this.foodRepository = foodRepository;



    if (foodRepository.count() == 0) {
        Food[] initial = new Food[]{
                new Food("Ьургер", 119.90, "Ьургер из мяса молодых ьычков.", (int) (Math.random() * 100)),
                new Food("Ўартошка Ўри", 49.90, "Ўартошка из солнечной РµРєС‚СЂС„РёРєР°", (int) (Math.random() * 100)),
                new Food("Ъоъа-ъола", 69.90, "", (int) (Math.random() * 100)),
                new Food("Їоїиїка в теїте", 79.90, "Качеїтвенный продукт прямо ї наших грядок.", (int) (Math.random() * 100)),
                new Food("Wороженое с wалиной", 24.90, "Охладит ваш пыл жаркиw летоw.", (int) (Math.random() * 100))
        };

        for (Food s : initial) {
//        s.setFood(foodRepository.save(s.getFood()));
            foodRepository.save(s);
        }
    }
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




/**
 * Get list of all food that can be ordered at the moment: where count in Food is greater than zero.
 */
public List<Food> getAllPresentFood() {
    return foodRepository.findPresentFood();
}

/**
 * Remove given food from Food and add it to reserved so it won't be ordered twice.
 *
 * @param food  Food to reserve.
 * @param count Count to reserve.
 */
void reserve(Food food, int count) throws NotEnoughFoodException {
    if (food == null || food.getCount() < count)
        throw new NotEnoughFoodException(food);

    food.setCount(food.getCount() - count);
    food.setReserve(food.getReserve() + count);

    foodRepository.save(food);
}

/**
 * Remove food from reserve to complete the order.
 *
 * @param food  Food that was reserved for order.
 * @param count Count of food.
 */
void complete(Food food, int count) {
    food.setReserve(food.getReserve() - count);
    foodRepository.save(food);
}


/**
 * The exception means that number of food in Food is less than number of food in order.
 * {@link NotEnoughFoodException#Food Food} field can be used to determine, which food
 * lead to this exception, and what is its current limits.
 */
public static class NotEnoughFoodException extends Throwable {
    private Food Food;

    NotEnoughFoodException(Food Food) { this.Food = Food; }

    public Food getFood() { return Food; }
}

}
