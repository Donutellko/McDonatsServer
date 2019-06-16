package ga.patrick.mcdonats.domain;

import javax.persistence.*;

import lombok.*;


/**
 * Class represents items in the menu.
 * <p>
 * Records are not to be deleted from system, and a list of foods that are
 * currently available for order is controlled by {@link Storage}.
 *
 * @see Storage
 * @see ga.patrick.mcdonats.repository.FoodRepository
 * @see ga.patrick.mcdonats.service.FoodService
 */

@Data
@NoArgsConstructor
@Entity
public class Food {

public Food(String title, double price, String desc) {
    this.title = title;
    this.price = price;
    this.desc = desc;
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
}
