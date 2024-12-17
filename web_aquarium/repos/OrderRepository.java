package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.Order;

import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findOrderById(Integer id);
    void deleteOrderById(Integer id);



}