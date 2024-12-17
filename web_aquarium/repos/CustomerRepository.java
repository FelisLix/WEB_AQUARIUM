package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.Customer;
import web_aquarium.data.Order;

import java.util.Set;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerByOrdersOrderById(Set<Order> orders);

}
