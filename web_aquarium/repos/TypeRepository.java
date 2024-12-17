package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.Type;

public interface TypeRepository extends JpaRepository<Type, Integer> {
    Type findTypeById(Integer id);
}