package ga.patrick.mcdonats.repository;

import ga.patrick.mcdonats.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItem.OrderItemId> {
}
