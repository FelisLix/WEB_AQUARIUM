package web_aquarium.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_aquarium.data.Category;
import web_aquarium.data.Image;
import web_aquarium.data.Product;
import web_aquarium.data.Type;
import web_aquarium.repos.CategoryRepository;
import web_aquarium.repos.ImageRepository;
import web_aquarium.repos.ProductRepository;
import web_aquarium.repos.TypeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    ImageRepository imageRepository;

    public List<Product> showAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> showProductsByType(int typeId) {
        return productRepository.findProductsByTypeIdIs(typeId);
    }

    public List<Product> showProductsByCategory(int categoryId) {
        return productRepository.findProductsByTypeCategoryIdIs(categoryId);
    }

    public Product showProductById(int id) {
        return productRepository.findProductById(id);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Image> findAllImages() {
        return imageRepository.findAll();
    }

    public Image findImageById(int id) {
        return imageRepository.findImageById(id);
    }

    public List<Type> findAllTypes() {
        return typeRepository.findAll();
    }

    public Type findTypeById(int id) {
        return typeRepository.findTypeById(id);
    }

    public Product getProductById(int id) {
        return productRepository.findProductById(id);
    }

    public void updateProduct(Product product) {
         productRepository.save(product);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(int id) {
        productRepository.deleteProductById(id);
    }

    public List<Product> findProductsByName(String name) {
        return productRepository.findProductsByNameContainsIgnoreCase(name);
    }
}
