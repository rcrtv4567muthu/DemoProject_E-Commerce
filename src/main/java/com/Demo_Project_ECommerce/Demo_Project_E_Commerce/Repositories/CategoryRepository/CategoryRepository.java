package com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Repositories.CategoryRepository;

import com.Demo_Project_ECommerce.Demo_Project_E_Commerce.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long>
{
//   private  static Category findByCategoryName(String categoryName);
}