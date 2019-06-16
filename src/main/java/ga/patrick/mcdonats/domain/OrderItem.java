package ga.patrick.mcdonats.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Class used to store information about counts of food in order.
 */
@Data
@Entity
@IdClass(OrderItem.OrderItemId.class)
public class OrderItem {

@Id
@ManyToOne
@JoinColumn(name = "order_id")
Order order;

@Id
@ManyToOne
@JoinColumn(name = "food_id")
Food food;

int count;

/**
 * Class that is used to link {@link OrderItem OrderItems) to {@link Order Orders}.
 * Each order must contain not more than one occurence of each food,
 * so {@link Order#orderId order id} and {@link Food#foodId food id}} are used
 * as an identifier.
 */
@Data
static class OrderItemId implements Serializable {
    Order order;
    Food food;
}

}
