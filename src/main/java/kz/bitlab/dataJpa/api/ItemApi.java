package kz.bitlab.dataJpa.api;

import kz.bitlab.dataJpa.model.Category;
import kz.bitlab.dataJpa.model.Country;
import kz.bitlab.dataJpa.model.Item;
import kz.bitlab.dataJpa.repository.CategoryRepository;
import kz.bitlab.dataJpa.repository.CountryRepository;
import kz.bitlab.dataJpa.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemApi {

    private final ItemRepository itemRepository;
    private final CountryRepository countryRepository;
    private final CategoryRepository categoryRepository;

    public ItemApi(ItemRepository itemRepository,
                   CountryRepository countryRepository,
                   CategoryRepository categoryRepository){
        this.itemRepository = itemRepository;
        this.countryRepository = countryRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String getAll(Model model){

        if (!itemRepository.findAll().isEmpty()){

            model.addAttribute("items", itemRepository.findAll());
            model.addAttribute("count", countryRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());

            return "items";
        }

        return "redirect:/error";
    }

    @GetMapping("/{id}")
    public String getItem(@PathVariable("id") Long id, Model model){
        Item item = itemRepository.findById(id).orElse(null);

        List<Category> categories = categoryRepository.findAll();
        categories.removeAll(item.getCategories());

        model.addAttribute("countryyy", countryRepository.findAll());
        model.addAttribute("categoryy", categories);
        model.addAttribute("item", item);

        return "item";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "car-country-id") Long id,
                          @RequestParam(name = "categories") List<Category> categories){

        Country country = countryRepository.findById(id).orElse(null);

        if (country != null){

            Item item = new Item();
            item.setPrice(price);
            item.setNameItem(name);
            item.setCountry(country);
            item.setCategories(categories);

            itemRepository.save(item);
        }
        return "redirect:/item";
    }

    @PostMapping("/update/{id}")
    public String updateItem(@PathVariable("id") Long id,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "price") int price,
                             @RequestParam(name = "country-id") Long cId){
        Item item = itemRepository.findById(id).orElse(null);

        Country country = countryRepository.findById(cId).orElse(null);

        if (country != null){
            if (item != null){

                item.setNameItem(name);
                item.setPrice(price);
                item.setCountry(country);

                itemRepository.save(item);
            }
        }
        return "redirect:/item";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        itemRepository.deleteById(id);

        return "redirect:/item";
    }

    @PostMapping(value = "/unassigncategory")
    public String unassignCategory(@RequestParam(name = "category_id") Long categoryId,
                                   @RequestParam(name = "item_id") Long itemId){

        Item item = itemRepository.findById(itemId).orElse(null);


        if (item != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);

            if (category != null) {

                List<Category> categories = item.getCategories();

                if (categories == null){
                    categories = new ArrayList<>();
                }

                categories.remove(category);
                item.setCategories(categories);
                itemRepository.save(item);
            }

            return "redirect:/item?id=" + item.getId();
        }

        return "redirect:/item";
    }

    @PostMapping(value = "/assigncategory")
    public String assignCategory(@RequestParam(name = "category_id") Long categoryId,
                                 @RequestParam(name = "item_id") Long itemId){

        Item item = itemRepository.findById(itemId).orElse(null);

        if (item != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);

            if (category != null) {
                List<Category> categories = item.getCategories();

                if (categories == null){
                    categories = new ArrayList<>();
                }

                categories.add(category);
                item.setCategories(categories);
                itemRepository.save(item);
            }
            return "redirect:/item?id=" + item.getId();

        }
        return "redirect:/item";
    }

}
