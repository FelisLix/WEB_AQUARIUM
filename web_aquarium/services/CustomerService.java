package web_aquarium.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_aquarium.data.*;
import web_aquarium.repos.*;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void createAddress(Address address) {
        addressRepository.save(address);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public void createPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public void createOrderLine(OrderLine orderLine) {
        orderLineRepository.save(orderLine);
    }


    public void saveAllLines(Order order, Cart cart){
        for (int i = 0; i < cart.getListOfItems().size(); i++) {
            OrderLine orderLine = new OrderLine();
            orderLine.setOrder(order);
            orderLine.setProduct(cart.getListOfItems().get(i).getProduct());
            orderLine.setQuantity(cart.getListOfItems().get(i).getQuantity());
            orderLineRepository.save(orderLine);
        }
    }

    public Customer getAllCustomersByOrderId(Order order) {
        return customerRepository.findCustomerByOrdersOrderById(Set.of(order));
    }

    public Address getAddressByOrderId(Order order) {
        return addressRepository.findAddressByCustomer_Id(order.getCustomer().getId());
    }

}
