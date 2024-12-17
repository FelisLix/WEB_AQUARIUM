package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}