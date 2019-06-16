package ga.patrick.mcdonats.api;

import ga.patrick.mcdonats.domain.*;
import ga.patrick.mcdonats.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

private OrderService orderService;
private FoodService FoodService;

public CustomerController(OrderService orderService, FoodService FoodService) {
    this.orderService = orderService;
    this.FoodService = FoodService;
}

@GetMapping("/food")
public List<Food> getFoods() {
    return FoodService.getAllPresentFood();
}

/**
 * Add new order to queue.
 *
 * @param foodCounts (order in query) - list of objects containing id of food and count to order.
 *
 * @return Order object, if order is added in queue
 * or 421: UNPROCESSABLE_ENTITY otherwise with possible response bodies:
 * - "В заказе нет блюд!" if list size is 0.
 * - "Неизвестный id блюда: [id]" if there is no food with such id.
 * - "Заказанного блюда ([title], id: [id]) осталось несдостаточно: [count]." if there is no such
 * food in Food. Client needs to retrieve the list of food again to see the limits.
 */
@PostMapping("/order")
public ResponseEntity<String> placeOrder(@RequestBody List<OrderService.FoodCount> foodCounts) {
    String response;
    HttpStatus status = HttpStatus.OK;
    try {
        Order order = orderService.placeOrder(foodCounts);
        response = order.getCheque();
    } catch (OrderService.EmptyOrderException e) {
        status = HttpStatus.UNPROCESSABLE_ENTITY;
        response = "В заказе нет блюд!";
    } catch (FoodService.UnknownFoodException e) {
        status = HttpStatus.UNPROCESSABLE_ENTITY;
        response = "Неизвестный id блюда: " + e.getMessage();
    } catch (FoodService.NotEnoughFoodException e) {
        status = HttpStatus.UNPROCESSABLE_ENTITY;
        Food food = e.getFood();
        response = String.format("Заказанного блюда (%s, id: %s) осталось недостаточно: %s.",
                food.getTitle(), food.getFoodId(), food.getCount());
    }
    return new ResponseEntity<>(response, status);
}

/**
 * Get info about order by id, if provided code matches the code in database.
 *
 * @param id   Order id
 * @param code Order code
 *
 * @return Order object, if id and code are correct.
 * <p>
 * 403: FORBIDDEN, if unknown id.
 * 421: UNPROCESSABLE_ENTITY, if wrong code.
 */
@GetMapping("/order")
public ResponseEntity<Order> getOrder(@RequestParam("id") int id, @RequestParam("code") String code) {
    Order order = null;
    HttpStatus status = HttpStatus.OK;
    try {
        order = orderService.getOrder(id, code);
    } catch (OrderService.WrongOrderCodeException e) {
        status = HttpStatus.FORBIDDEN;
    } catch (OrderService.OrderNotFoundException e) {
        status = HttpStatus.UNPROCESSABLE_ENTITY;
    }
    return new ResponseEntity<>(order, status);
}

/**
 * Cancel order by id, if provided code matches the code in database.
 *
 * @param id   Order id
 * @param code Order code
 *
 * @return true, if operation was successful
 * <p>
 * 403: FORBIDDEN, if unknown id.
 * 421: UNPROCESSABLE_ENTITY, if wrong code.
 */
@DeleteMapping("/order")
public ResponseEntity<Boolean> cancelOrder(@RequestParam("id") int id, @RequestParam("code") String code) {
    Boolean result = null;
    HttpStatus status = HttpStatus.OK;
    try {
        result = orderService.customerCancelOrder(id, code);
    } catch (OrderService.WrongOrderCodeException e) {
        status = HttpStatus.FORBIDDEN;
    } catch (OrderService.OrderNotFoundException e) {
        status = HttpStatus.UNPROCESSABLE_ENTITY;
    }
    return new ResponseEntity<>(result, status);
}

}
