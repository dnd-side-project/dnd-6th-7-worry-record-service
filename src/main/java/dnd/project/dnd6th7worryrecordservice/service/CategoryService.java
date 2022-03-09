package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findCategoryByCategoryId(Long categoryId){
        Category category = categoryRepository.findByCategoryId(categoryId);
        return category;
    }


}
