package ga.patrick.mcdonats.domain;

import ga.patrick.mcdonats.service.OrderService;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
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
 */
double price;


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
LocalDateTime end = LocalDateTime.now();

/**
 * A staff member that was assigned for processing this order.
 * The field should not be null if {@link #status order status} is
 * <b>not</b> {@link OrderStatus#OPEN OPEN} or {@link OrderStatus#CANCELLED_BY_STAFF CANCELLED_BY_STAFF}
 */
@ManyToOne
        @JoinColumn(name="username")
Staff staff;

/** Ordered items. */
@OneToMany(mappedBy = "order")
Set<OrderItem> items = new HashSet<>();

/**
 * Set an end date and time of an order to current time.
 */
public void end() {
    setEnd(LocalDateTime.now());
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
