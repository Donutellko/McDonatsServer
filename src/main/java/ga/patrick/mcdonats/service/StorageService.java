package ga.patrick.mcdonats.service;

import ga.patrick.mcdonats.domain.Food;
import ga.patrick.mcdonats.domain.Storage;
import ga.patrick.mcdonats.repository.FoodRepository;
import ga.patrick.mcdonats.repository.StorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StorageService {

private final StorageRepository storageRepository;
private final FoodRepository foodRepository;

public StorageService(StorageRepository storageRepository, FoodRepository foodRepository) {
    this.storageRepository = storageRepository;
    this.foodRepository = foodRepository;

    Storage[] initial = new Storage[] {
            new Storage(new Food("Ьургер", 119.90, "Ьургер из мяса молодых ьычков."), (int) (Math.random() * 100)),
            new Storage(new Food("Ўартошка Ўри", 49.90, "Ўартошка из солнечной РµРєС‚СЂС„РёРєР°"), (int) (Math.random() * 100)),
            new Storage(new Food("Ъоъа-ъола", 69.90, ""), (int) (Math.random() * 100)),
            new Storage(new Food("Їоїиїка в теїте", 79.90, "Качеїтвенный продукт прямо ї наших грядок."), (int) (Math.random() * 100)),
            new Storage(new Food("Wороженое с wалиной", 24.90, "Охладит ваш пыл жаркиw летоw."), (int) (Math.random() * 100))
    };

    for (Storage s: initial) {
//        s.setFood(foodRepository.save(s.getFood()));
        storageRepository.save(s);
    }
}

/**
 * Get list of all food that can be ordered at the moment: where count in storage is greater than zero.
 */
public List<Food> getAllPresentFood() {
    return storageRepository.findPresentFood();
}

/**
 * Remove given food from storage and add it to reserved so it won't be ordered twice.
 *
 * @param food  Food to reserve.
 * @param count Count to reserve.
 */
void reserve(Food food, int count) throws NotEnoughFoodException {
    Storage storage = storageRepository.findById(food).orElse(null);

    if (storage == null || storage.getCount() < count)
        throw new NotEnoughFoodException(storage);

    storage.setCount(storage.getCount() - count);
    storage.setReserve(storage.getReserve() + count);

    storageRepository.save(storage);
}

/**
 * Remove food from reserve to complete the order.
 *
 * @param food  Food that was reserved for order.
 * @param count Count of food.
 */
void complete(Food food, int count) {
    Storage storage = storageRepository.findById(food).orElseThrow(RuntimeException::new);
    storage.setReserve(storage.getReserve() - count);
}

/**
 * The exception means that number of food in storage is less than number of food in order.
 * {@link NotEnoughFoodException#storage storage} field can be used to determine, which food
 * lead to this exception, and what is its current limits.
 */
public static class NotEnoughFoodException extends Throwable {
    private Storage storage;

    NotEnoughFoodException(Storage storage) { this.storage = storage; }

    public Storage getStorage() { return storage; }
}

}
