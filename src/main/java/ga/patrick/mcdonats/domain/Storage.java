package ga.patrick.mcdonats.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * Class is used to provide information on which foods are available,
 * and limits for orders. Class also contains redundant information on number
 * items do still exist, but are {@link #reserve reserved} for orders that are
 * open or being processed.
 */
@Data
@NoArgsConstructor
@Entity
public class Storage {

public Storage(Food food, int count) {
    this.food = food;
    this.count = count;
}

@Id
//@GeneratedValue
int foodId;

@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
@JoinColumn(name = "food_id")
Food food;

/**
 * A number of food items that can be ordered and are not reserved for orders
 * that are open or being processed.
 */
int count;

/**
 * Number of food items that still exist, but is reserved in one of open or processing orders.
 */
int reserve = 0;

/**
 * Setter for count of items in storage.
 * Throws RuntimeException, if trying to set value that is
 * less than zero.
 */
public void setCount(int n) {
    if (n < 0) throw new RuntimeException("Count cannot be negative.");
    count = n;
}

/**
 * Setter for count of items in storage.
 * Throws RuntimeException, if trying to set value that is
 * less than zero.
 */
public void setReserve(int n) {
    if (n < 0) throw new RuntimeException("Reserve cannot be negative.");
    reserve = n;
}

}
