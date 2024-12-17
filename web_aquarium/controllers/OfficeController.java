package web_aquarium.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web_aquarium.data.*;
import web_aquarium.repos.StatisticRepo;
import web_aquarium.services.CustomerService;
import web_aquarium.services.EmployeeService;
import web_aquarium.services.OfficeService;
import web_aquarium.services.ProductService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
public class OfficeController {

    @Autowired
    OfficeService officeService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @Autowired
    StatisticRepo statisticRepo;

    @Autowired
    EmployeeService employeeService;

    // Відображення головної сторінки "офісу"
    @GetMapping("/office")
    public String adminOffice() {
        return "office";
    }

    // Відображення сторінки зі списком замовлень
    @GetMapping("/order_list")
    public String showOrders(Model model) {

        List<Order> orders = officeService.getOrders();
        model.addAttribute("orders", orders);

        List<Employee> employees = officeService.getSalesRepresentative();
        model.addAttribute("employees", employees);

        Map<Integer, Payment> payments = new HashMap<>();
        Map<Integer, List<OrderLine>> orderLinesMap = new HashMap<>();
        Map<Integer, Customer> customers = new HashMap<>();
        Map<Integer, Address> addresses = new HashMap<>();

        for (Order aboutOrder : orders) {
            List<OrderLine> orderLines = officeService.getOrderLinesById(aboutOrder.getId());
            orderLinesMap.put(aboutOrder.getId(), orderLines);

            Payment pay = officeService.getPaymentByOrderId(aboutOrder.getId());
            payments.put(aboutOrder.getId(), pay);

            Customer customer = customerService.getAllCustomersByOrderId(aboutOrder);
            customers.put(aboutOrder.getId(), customer);

            Address address = customerService.getAddressByOrderId(aboutOrder);
            addresses.put(aboutOrder.getId(), address);
        }

        model.addAttribute("orderLinesMap", orderLinesMap);
        model.addAttribute("payments", payments);
        model.addAttribute("customers", customers);
        model.addAttribute("addresses", addresses);

        return "order_list";
    }

    // Обробка запиту для оновлення даних про замовлення
    @PostMapping("/order_list/update_order/{orderId}")
    public String updateOrder(@RequestParam(value = "orderStatus", required = false) Optional<String> orderStatus,
                              @RequestParam(value = "paymentStatus", required = false) Optional<String> paymentStatus,
                              @RequestParam(value = "employeeId") int employeeId,
                              @PathVariable("orderId") int orderId) {

        Order order = officeService.getOrder(orderId);
        orderStatus.ifPresent(order::setStatus);
        officeService.updateOrder(order);

        Payment payment = officeService.getPaymentByOrderId(orderId);
        paymentStatus.ifPresent(payment::setStatus);
        officeService.updatePayment(payment);

        return "redirect:/order_list";
    }

    // Обробка запиту для видалення замовлення
    @PostMapping("/order_list/delete_order/{orderId}")
    public String deleteOrder(@PathVariable("orderId") int orderId) {
        officeService.deleteOrder(orderId);
        return "redirect:/order_list";
    }

    // Відображення сторінки зі списком усіх товарів
    @GetMapping("/products_editor")
    public String showListOfProducts(Model model) {
        List<Product> products = productService.showAllProducts();
        List<Category> categories = productService.findAllCategories();
        List<Type> types = productService.findAllTypes();
        List<Image> images = productService.findAllImages();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
        model.addAttribute("images", images);
        return "products_editor";
    }

    // Обробка запиту для оновлення даних про товар
    @PostMapping("/product/update")
    public String updateProduct(@RequestParam("productId")int productId, @RequestParam("name") String name,
                                @RequestParam("price") double price, @RequestParam("quantity") int quantity,
                                @RequestParam("size") String size,
                                @RequestParam("description") String description, @RequestParam("imageURL") String imageURL,
                                @RequestParam("lifespan") int lifespan,  @RequestParam("imageID") int imageID,
                                @RequestParam("origin") String origin) {
        Product product = productService.getProductById(productId);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        product.setQuantity(quantity);
        product.setSize(size);
        product.setDescription(description);
        product.setImage(productService.findImageById(imageID));
        product.setLifespan(lifespan);
        product.setOrigin(origin);
        productService.updateProduct(product);
        return "redirect:/products_editor";
    }

    // Обробка запиту для додавання нового товару
    @PostMapping("/product/add")
    public String addProduct(@RequestParam("nameN") String name, @RequestParam("priceN") double price,
                             @RequestParam("quantityN") int quantity, @RequestParam("sizeN") String size,
                             @RequestParam("descriptionN") String description, @RequestParam("lifespanN") int lifespan,
                             @RequestParam("imageIdN") int imageID, @RequestParam("originN") String origin,
                             @RequestParam("typeIdN") int typeId) {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Product product = new Product();
        product.setName(name);
        product.setType(productService.findTypeById(typeId));
        product.setPrice(BigDecimal.valueOf(price));
        product.setQuantity(quantity);
        product.setSize(size);
        product.setDescription(description);
        product.setCreatedAt(createdAt);
        product.setImage(productService.findImageById(imageID));
        product.setLifespan(lifespan);
        product.setOrigin(origin);
        productService.addProduct(product);
        return "redirect:/products_editor";
    }

    // Обробка запиту для видалення товару за ID
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/products_editor";
    }

    // Обробка запиту для пошуку товарів за назвою
    @GetMapping("/products_editor/search")
    public String searchProducts(@RequestParam String searchTerm, Model model) {
        List<Product> list = productService.findProductsByName(searchTerm);
        model.addAttribute("products", list);
        return "products_editor";
    }


    // Відображення сторінки зі статистикою
    @GetMapping("/statistics")
    public String statistics(Model model) {

        double thisMonthProfit = statisticRepo.getThisMonthProfit().getFirst().getValue();
        model.addAttribute("thisMonthProfit", thisMonthProfit);

        int thisMonthOrders = (int) Math.round(statisticRepo.getThisMonthOrders().getFirst().getValue());
        model.addAttribute("thisMonthOrders", thisMonthOrders);

        int thisMonthProducts =  (int) Math.round(statisticRepo.getThisMonthProducts().getFirst().getValue());
        model.addAttribute("thisMonthProducts", thisMonthProducts);

        List<StatisticRepo.ChartData> getTopMonthsProfit = statisticRepo.getTopMonthsProfit();
        model.addAttribute("getTopMonthsProfit", getTopMonthsProfit);

        List<StatisticRepo.ChartData> getTopMonthsOrders = statisticRepo.getTopMonthsOrders();
        model.addAttribute("getTopMonthsOrders", getTopMonthsOrders);

        List<StatisticRepo.ChartDataInt> getTopProducts = statisticRepo.getTopProducts();
        model.addAttribute("getTopProducts", getTopProducts);

        List<StatisticRepo.ChartDataInt> getTopCity= statisticRepo.getTopCity();
        model.addAttribute("getTopCity", getTopCity);

        List<StatisticRepo.ChartDataInt> getTopType = statisticRepo.getTopType();
        model.addAttribute("getTopType", getTopType);

        return "statistics";
    }

    // Відображення сторінки зі списком співробітників (користувачів)
    @GetMapping("/users")
    public  String showEmployees(Model model){
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "users";
    }

    // Обробка запиту для оновлення даних про співробітника
    @PostMapping("/employee/update")
    public String updateEmployee(@RequestParam("employeeId") int employeeId,
                                 @RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("title") String title,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("email") String email,
                                 @RequestParam("country") String country,
                                 @RequestParam("city") String city,
                                 @RequestParam("hireDate") String hireDate,
                                 @RequestParam("birthDate") String birthDate) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setTitle(title);
        employee.setPhone(phone);
        employee.setEmail(email);
        employee.setCountry(country);
        employee.setCity(city);
        employee.setHireDate(LocalDate.parse(hireDate));
        employee.setBirthDate(LocalDate.parse(birthDate));
        employeeService.saveEmployee(employee);
        return "redirect:/users";
    }

    // Обробка запиту для додавання нового співробітника
    @PostMapping("/employee/add_new")
    public String addNewEmployee(@RequestParam("firstNameN") String firstName,
                                 @RequestParam("lastNameN") String lastName,
                                 @RequestParam("titleN") String title,
                                 @RequestParam("phoneN") String phone,
                                 @RequestParam("emailN") String email,
                                 @RequestParam("countryN") String country,
                                 @RequestParam("cityN") String city,
                                 @RequestParam("hireDateN") String hireDate,
                                 @RequestParam("birthDateN") String birthDate) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setTitle(title);
        employee.setPhone(phone);
        employee.setEmail(email);
        employee.setCountry(country);
        employee.setCity(city);
        employee.setHireDate(LocalDate.parse(hireDate));
        employee.setBirthDate(LocalDate.parse(birthDate));
        employeeService.saveEmployee(employee);
        return "redirect:/users";
    }
}
