package dev.desyllas.E_CommerceProject.repository;

import dev.desyllas.E_CommerceProject.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
