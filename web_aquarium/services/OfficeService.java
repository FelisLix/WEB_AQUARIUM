package web_aquarium.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_aquarium.data.Employee;
import web_aquarium.data.Order;
import web_aquarium.data.OrderLine;
import web_aquarium.data.Payment;
import web_aquarium.repos.EmployeeRepository;
import web_aquarium.repos.OrderLineRepository;
import web_aquarium.repos.OrderRepository;
import web_aquarium.repos.PaymentRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class OfficeService {

    @Autowired
    OrderLineRepository orderLineRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(int id) {
        return orderRepository.findOrderById(id);
    }

    public List<Employee> getSalesRepresentative() {
        return employeeRepository.findEmployeesByTitleIsContaining("Sales Representative");
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public List<OrderLine> getOrderLines() {
        return orderLineRepository.findAll();
    }

    public List<OrderLine> getOrderLinesById(int id) {
        return orderLineRepository.findOrderLinesByOrder_Id(id);
    }

    public Payment getPaymentByOrderId(int id) {
        return paymentRepository.findPaymentByOrder_Id(id);
    }

    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    public void updatePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Transactional
    public void deleteOrder(int id) {
        orderRepository.deleteOrderById(id);
    }
}
