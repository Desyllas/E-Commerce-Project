package dev.desyllas.E_CommerceProject.repository;

import dev.desyllas.E_CommerceProject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
