package ga.patrick.mcdonats.service;

import ga.patrick.mcdonats.domain.Food;
import ga.patrick.mcdonats.domain.Order;
import ga.patrick.mcdonats.domain.Order.OrderStatus;
import ga.patrick.mcdonats.domain.OrderItem;
import ga.patrick.mcdonats.repository.OrderItemRepository;
import ga.patrick.mcdonats.repository.OrderRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

private final OrderRepository orderRepository;
private final FoodService foodService;
private final OrderItemRepository orderItemRepository;

public OrderService(OrderRepository orderRepository, FoodService foodService, OrderItemRepository orderItemRepository) {
    this.orderRepository = orderRepository;
    this.foodService = foodService;
    this.orderItemRepository = orderItemRepository;
}

/**
 * Place new order. Add to the database new {@link Order} object with provided {@link FoodCount}'s as positions.
 *
 * @param foodCounts List of {@link FoodCount} objects, containing food id and count for each position in order.
 *
 * @return Order object to be used as cheque, if order was added successfully.
 *
 * @throws EmptyOrderException                   when provided list is empty.
 * @throws FoodService.UnknownFoodException      when in provided list there is unknown food id.
 * @throws FoodService.NotEnoughFoodException when there is not enough food in Food to complete an order.
 *                                               Exception message contains title and id of first such food found.
 */
public Order placeOrder(List<FoodCount> foodCounts)
        throws EmptyOrderException, FoodService.UnknownFoodException, FoodService.NotEnoughFoodException {

    Order order = new Order();
    order.setCode(generateCode());

    if (foodCounts.size() == 0) {
        throw new EmptyOrderException();
    }

    order = orderRepository.save(order);

    for (FoodCount fc : foodCounts) {
        Food food = foodService.findById(fc.id);
        foodService.reserve(food, fc.count);
        OrderItem item = new OrderItem(order, food, fc.count);
        orderItemRepository.save(item);
        order.add(item);
    }

    return orderRepository.save(order);
}

/**
 * An overload for {@link #completeOrder(Order)} that is helpful,
 * when {@link Order} object was not fetched.
 *
 * @param id id of an order to get marked as completed.
 *
 * @throws OrderNotFoundException when there is no order with provided id.
 * @see #completeOrder(Order)
 */
public void completeOrder(int id) throws OrderNotFoundException {
    Order order = findOrder(id);
    completeOrder(order);
}

private Order findOrder(int id) throws OrderNotFoundException {
    return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
}

/**
 *
 * @param order
 */
private void completeOrder(Order order) {
    for (OrderItem item : order.getItems()) {
        foodService.complete(item.getFood(), item.getCount());
    }
    order.setEnd(LocalDateTime.now());
    order.setStatus(Order.OrderStatus.DONE);
    orderRepository.save(order);
}


private String generateCode() {
    return String.format("%04d", (int) (10000 * Math.random() % 1000));
}

/**
 * Cancel order by id, if provided code is correct and order is not closed or being currently processed.
 * Set {@link OrderStatus#CANCELLED_BY_CLIENT CANCELLED_BY_CLIENT }
 * as {@link Order#status} and current time as {@link Order#end}.
 *
 * @param id   Order id.
 * @param code Order security code.
 *
 * @return true, if cancelled. false, if order is already closed or is being processed.
 *
 * @throws OrderNotFoundException  when there is no order with provided id.
 * @throws WrongOrderCodeException when provided code does not match the security code of order.
 */
public boolean customerCancelOrder(int id, String code) throws OrderNotFoundException, WrongOrderCodeException {
    Order order = findOrder(id);

    if (!code.equals(order.getCode())) {
        throw new WrongOrderCodeException();
    } else if (order.getStatus() != Order.OrderStatus.OPEN) {
        return false;
    } else {
        order.setStatus(Order.OrderStatus.CANCELLED_BY_CLIENT);
        order.end();
        orderRepository.save(order);
        return true;
    }
}

/**
 * Get info on order by id, if provided code is correct.
 *
 * @param id   Order id.
 * @param code Order security code.
 *
 * @return Order with provided id.
 *
 * @throws OrderNotFoundException  when there is no order with provided id.
 * @throws WrongOrderCodeException when provided code does not match the security code of order.
 */
public Order getOrder(int id, String code) throws OrderNotFoundException, WrongOrderCodeException {
    Order order = findOrder(id);
    if (code.equals(order.getCode())) {
        return order;
    } else {
        throw new WrongOrderCodeException();
    }
}


/**
 * A class used to get info about orders from clients.
 */
@Data
public static class FoodCount {
    int id;
    int count;
}

public class EmptyOrderException extends Throwable {}

public class OrderNotFoundException extends Throwable {}

public class WrongOrderCodeException extends Throwable {}

}
