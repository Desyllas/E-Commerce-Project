package dev.desyllas.E_CommerceProject.repository;

import dev.desyllas.E_CommerceProject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {


    List<Order> findByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Integer id, Long userId);
}
