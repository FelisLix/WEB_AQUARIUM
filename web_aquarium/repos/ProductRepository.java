package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  List<Product> findProductsByTypeIdIs(int typeId);
  List<Product> findProductsByTypeCategoryIdIs(int categoryId);

  Product findProductById(int id);

  void deleteProductById(Integer id);

  List<Product> findProductsByNameContainsIgnoreCase(String name);
}