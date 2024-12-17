package web_aquarium.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web_aquarium.data.*;
import web_aquarium.services.CustomerService;
import web_aquarium.services.ProductService;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@AllArgsConstructor
@Controller
public class CartController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    private final Cart cart = new Cart();

    // Відображення вмісту кошика
    @GetMapping("/cart")
    public String showProducts(Model model) {
        List<Cart.CartItem> itemsInCart = cart.showCart();
        double totalPrice = itemsInCart.stream().mapToDouble(item -> cart.calculateTotal().doubleValue()).sum();
        String formattedTotalPrice = String.format("%.2f", totalPrice);
        model.addAttribute("cartItems", itemsInCart);
        model.addAttribute("totalPrice", formattedTotalPrice);
        return "cart";
    }

    // Обробка запиту для додавання товару у кошик
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        cart.addToCart( new Cart.CartItem(productService.showProductById(productId), quantity));
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    // Обробка запиту для видалення продукту з кошика
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("productId") int productId, HttpServletRequest request) {
        cart.removeFromCart(productId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    // Відображення сторінки для оформлення замовлення
    @GetMapping("/order")
    public String orderPage(Model model) {
        List<Cart.CartItem> itemsInCart = cart.showCart();
        double totalPrice = itemsInCart.stream().mapToDouble(item -> cart.calculateTotal().doubleValue()).sum();
        String formattedTotalPrice = String.format("%.2f", totalPrice);
        model.addAttribute("cartItems", itemsInCart);
        model.addAttribute("totalPrice", formattedTotalPrice);
        return "order";
    }

    // Оформлення замовлення
    @PostMapping("/order/new_order")
    public String makeAnOrder(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                              @RequestParam("phone") String phone, @RequestParam("email") String email,
                              @RequestParam("country") String country, @RequestParam("city") String city,
                              @RequestParam("street") String street, @RequestParam("building") String building,
                              @RequestParam("details") String details, @RequestParam("postalCode") String postalCode,
                              @RequestParam("payment") String payment, Model model) {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);
        newCustomer.setPhone(phone);
        newCustomer.setEmail(email);

        Customer createdCustomer = customerService.createCustomer(newCustomer);

        Address newAddress = new Address();
        newAddress.setCountry(country);
        newAddress.setCity(city);
        newAddress.setStreet(street);
        newAddress.setBuilding(building);
        newAddress.setDetails(details);
        newAddress.setPostalCode(postalCode);
        newAddress.setCustomer(createdCustomer);

        customerService.createAddress(newAddress);

        BigDecimal total = cart.calculateTotal();
        LocalDate orderDate = LocalDate.now();

        Order order = new Order();
        order.setStatus("В обробці");
        order.setCustomer(newCustomer);
        order.setTotal(total);
        order.setOrderDate(orderDate);
        Order createdOrder = customerService.createOrder(order);

        Payment newPayment = new Payment();
        newPayment.setMethod(payment);
        newPayment.setOrder(createdOrder);
        newPayment.setStatus("Очікує оплати");
        customerService.createPayment(newPayment);

        customerService.saveAllLines(createdOrder, cart);

        cart.emptyCart();
        model.addAttribute("orderId", createdOrder.getId());
        return "congrats";
    }
}
