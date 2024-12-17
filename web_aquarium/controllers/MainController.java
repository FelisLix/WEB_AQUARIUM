package web_aquarium.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import web_aquarium.data.Product;
import web_aquarium.services.ProductService;


import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    @Autowired
    private ProductService productService;

    @GetMapping("/main")
    public String showMain(Model model){
        return "main";
    }

    @GetMapping("/contacts")
    public String showContacts(Model model){
        return "contacts";
    }

    // Відображення сторінки з товарами відповідно параметрів sortField та sortDir
    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productService.showAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    // Відображення сторінки з товарами за певною категорією
    @GetMapping("/products/category/{categoryId}")
    public String showProductsByCategory(@PathVariable("categoryId") int categoryId, Model model) {
        List<Product> products = productService.showProductsByCategory(categoryId);
        model.addAttribute("products", products);
        return "products";
    }

    // Відображення сторінки з товарами за певною підкатегорією
    @GetMapping("/products/type/{typeId}")
    public String showProductsByType(@PathVariable("typeId") int typeId, Model model) {
        List<Product> products = productService.showProductsByType(typeId);
        model.addAttribute("products", products);
        return "products";
    }

    // Відображення сторінки з повною інформацією про товар
    @GetMapping("/products/{id}")
    public String showProductDetail(@PathVariable("id") int id, Model model) {
        Product product = productService.showProductById(id);
        model.addAttribute("product", product);
        return "product_details";
    }

}
