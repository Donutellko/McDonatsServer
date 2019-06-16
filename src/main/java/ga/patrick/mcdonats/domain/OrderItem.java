package ga.patrick.mcdonats.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Class used to store information about counts of food in order.
 */
@Data
@Entity
@NoArgsConstructor
@IdClass(OrderItem.OrderItemId.class)
public class OrderItem {

@Id
private int orderId;

@Id
private int foodId;

@EqualsAndHashCode.Exclude
@ManyToOne
@JoinColumn(name = "orderId", insertable = false, updatable = false)
Order order;

@ManyToOne
@JoinColumn(name = "foodId", insertable = false, updatable = false)
Food food;

int count;

public OrderItem(Order order, Food food, int count) {
    setOrder(order);
    setFood(food);
    setCount(count);
}

void setOrder(Order order) {
    this.order = order;
    this.orderId = order.orderId;
}

void setFood(Food food) {
    this.food = food;
    this.foodId = food.foodId;
}

/**
 * Class that is used to link {@link OrderItem OrderItems) to {@link Order Orders}.
 * Each order must contain not more than one occurence of each food,
 * so {@link Order#orderId order id} and {@link Food#foodId food id}} are used
 * as an identifier.
 */
@Data
public static class OrderItemId implements Serializable {
    int orderId;
    int foodId;
}

}
