package ga.patrick.mcdonats.repository;

import ga.patrick.mcdonats.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

/**
 * Find all orders by their status.
 *
 * @param status {@link Order#status}
 *
 * @return List of Order objects that have specified status.
 */
List<Order> findAllByStatus(Order.OrderStatus status);

}
