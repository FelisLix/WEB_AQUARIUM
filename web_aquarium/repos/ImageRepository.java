package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
  Image findImageById(Integer id);
  }