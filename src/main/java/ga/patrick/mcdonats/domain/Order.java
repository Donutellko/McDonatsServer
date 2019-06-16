package ga.patrick.mcdonats.domain;

import ga.patrick.mcdonats.service.OrderService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class represents an order made by a customer in the system.
 * It contains positions that were ordered, and codes that are used to {@link #orderId identify} order and
 * to {@link #code authorise} a customer that has made the order, info about order's current {@link #status status},
 * its price at the moment, time, when order was created and closed, a staff member that has processed or cancelled it.
 */
@Data
@Entity(name = "orders")
public class Order {

@Id
@GeneratedValue
int orderId;

/**
 * A code that will be used by customer to prove rights on his food.
 * Might consist of several letters, printed on his cheque.
 *
 * @see OrderService#generateCode()
 */
String code;

/**
 * Summary price of whole order at the moment, when it was made.
 * Prices of each individual item in the order at the moment is considered not important.
 * This value is updated in
 */
@Setter(AccessLevel.PRIVATE)
private double price;


/**
 * Is order done or cancelled.
 * The value is contained in database as string.
 * Default value is {@link OrderStatus#OPEN}.
 *
 * @see OrderStatus
 */
@Enumerated(EnumType.STRING)
OrderStatus status = OrderStatus.OPEN;

/**
 * A time, when the order was added to the database.
 * This field should not be null.
 */
LocalDateTime start = LocalDateTime.now();

/**
 * A time, when the order was ended: done or cancelled.
 * This field should not be null, if {@link #status order status} is
 * * {@link OrderStatus#DONE}, {@link OrderStatus#CANCELLED_BY_STAFF CANCELLED_BY_STAFF}
 * or {@link OrderStatus#CANCELLED_BY_CLIENT CANCELLED_BY_CLIENT}, and vice versa.
 */
LocalDateTime end;

/**
 * A staff member that was assigned for processing this order.
 * The field should not be null if {@link #status order status} is
 * <b>not</b> {@link OrderStatus#OPEN OPEN} or {@link OrderStatus#CANCELLED_BY_STAFF CANCELLED_BY_STAFF}
 */
@ManyToOne
@JoinColumn(name = "username")
Staff staff;

/** Ordered items. */
@OneToMany(mappedBy = "order")
private Set<OrderItem> items = new HashSet<>();

public void add(OrderItem item) {
    items.add(item);
    price += item.count * item.food.price;
}

/**
 * Setter for order items. Creates a new set from array.
 * This is done so items cannot be changed from outside, invalidating the price.
 *
 * @param items Iterable collection of OrderItem.
 */
public void setItems(Iterable<OrderItem> items) {
    this.items = new HashSet<>();
    for (OrderItem i: items) add(i);
}

/**
 * Get list of orders as an array.
 * so the list won't be changed from outside the class.
 *
 * @return array of order items.
 */
public OrderItem[] getItems() {
    return items.toArray(new OrderItem[0]);
}

/**
 * Set an end date and time of an order to current time.
 */
public void end() {
    setEnd(LocalDateTime.now());
}

/**
 * Used to print cheques.
 * Cheque contains order id and code, all items, price sum and date of order.
 *
 * @return
 */
public String getCheque() {
    StringBuilder sb = new StringBuilder();
    sb.append("Заказ №").append(orderId)
            .append("\nВаш код для получения заказа: ").append(code);
    for (OrderItem i : items) {
        sb.append("\n\n").append(i.food.title).append(":\n")
                .append("    ").append(i.count).append(" * ").append(i.food.price)
                .append(" = ").append(i.count * i.food.price);
    }

    sb.append("\n\nИТОГО: ").append(price)
            .append("\nДата: ").append(start.format(DateTimeFormatter.ofPattern("uuuu.MM.dd kk:mm")));
    return sb.toString();
}


/** Enum used to determine state of an order. */
public enum OrderStatus {
    /** Order has been created and no staff member is currently working on it. */
    OPEN,

    /**
     * Order is being processed by staff member.
     * {@link Order#staff} must not be null.
     */
    PROCESSING,

    /**
     * Order was processed.
     * {@link Order#staff} must not be null.
     */
    DONE,

    /** Order was cancelled by client. */
    CANCELLED_BY_CLIENT,

    /**
     * Order was cancelled by staff member.
     * {@link Order#staff} must not be null.
     */
    CANCELLED_BY_STAFF
}

}
