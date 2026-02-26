package kz.bitlab.dataJpa.api;

import kz.bitlab.dataJpa.model.Category;
import kz.bitlab.dataJpa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryApi {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public String all(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    @GetMapping("/{id}")
    public String getId(@PathVariable("id") Long id, Model model){
        model.addAttribute("category", categoryRepository.findById(id).orElseThrow());
        return "category";
    }

    @PostMapping
    public String add(@RequestParam("name") String name){
        categoryRepository.save(Category.builder().name(name).build());
        return "redirect:/category";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @RequestParam("name") String name){

        Category category = categoryRepository.findById(id).orElse(null);
        category.setName(name);
        categoryRepository.save(category);

//        categoryRepository.save(categoryRepository.findById(id).orElse(null).builder().name(name).build());

        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        categoryRepository.deleteById(id);
        return "redirect:/category";
    }

}
