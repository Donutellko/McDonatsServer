package ga.patrick.mcdonats.domain;

import javax.persistence.*;

import lombok.*;


/**
 * Class represents items in the menu.
 * <p>
 * Records are not to be deleted from system, and a list of foods that are
 * currently available for order is controlled by {@link Food}.
 *
 * @see Food
 * @see ga.patrick.mcdonats.repository.FoodRepository
 * @see ga.patrick.mcdonats.service.FoodService
 */

@Data
@NoArgsConstructor
@Entity
public class Food {

public Food(String title, double price, String desc, int count) {
    this.title = title;
    this.price = price;
    this.desc = desc;
    this.count = count;
}

@Id
@GeneratedValue
int foodId;

/**
 * A short title of a menu item.
 */
String title;

/**
 * Description of a menu item.
 * May also contain info about item's composition.
 */
String desc;

/**
 * Current price of a menu item with precision of two digits after decimal point.
 */
@Column(nullable = false)
double price;


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
 * Setter for count of items in Food.
 * Throws RuntimeException, if trying to set value that is
 * less than zero.
 */
public void setCount(int n) {
    if (n < 0) throw new RuntimeException("Count cannot be negative.");
    count = n;
}

/**
 * Setter for count of items in Food.
 * Throws RuntimeException, if trying to set value that is
 * less than zero.
 */
public void setReserve(int n) {
    if (n < 0) throw new RuntimeException("Reserve cannot be negative.");
    reserve = n;
}
}
