package kz.bitlab.dataJpa.api;

import kz.bitlab.dataJpa.model.Country;
import kz.bitlab.dataJpa.model.Item;
import kz.bitlab.dataJpa.repository.CountryRepository;
import kz.bitlab.dataJpa.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/item")
public class ItemApi {

    private final ItemRepository itemRepository;
    private final CountryRepository countryRepository;

    public ItemApi(ItemRepository itemRepository, CountryRepository countryRepository){
        this.itemRepository = itemRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public String getAll(Model model){

        if (!itemRepository.findAll().isEmpty()){
            model.addAttribute("items", itemRepository.findAll());
            model.addAttribute("count", countryRepository.findAll());

            return "items";
        }

        return "redirect:/error";
    }

    @GetMapping("/{id}")
    public String getItem(@PathVariable("id") Long id, Model model){
        Item item = itemRepository.findById(id).orElse(null);

        model.addAttribute("countryyy", countryRepository.findAll());

        if (item != null){
            model.addAttribute("item", item);
            return "item";
        }

        return "redirect:/error";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price,
                          @RequestParam(name = "car-country-id") Long id){

        Country country = countryRepository.findById(id).orElse(null);

        if (country != null){

            Item item = new Item();
            item.setPrice(price);
            item.setNameItem(name);
            item.setCountry(country);

            Item addedItem = itemRepository.save(item);

            Item item1 = itemRepository.findById(addedItem.getId()).orElse(null);

            if (item1 != null){
                if (item1.equals(addedItem)){
                    return "redirect:/item";
                }
            }

        }

        return "redirect:/error";
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

                return "redirect:/item";
            }
        }

        return "redirect:/error";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        itemRepository.deleteById(id);

        return "redirect:/item";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }

}
