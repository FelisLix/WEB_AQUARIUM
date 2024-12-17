package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.OrderLine;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findOrderLinesByOrder_Id(Integer orderId);

}