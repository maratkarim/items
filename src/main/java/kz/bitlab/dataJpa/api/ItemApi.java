package kz.bitlab.dataJpa.api;

import kz.bitlab.dataJpa.model.Item;
import kz.bitlab.dataJpa.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/item")
public class ItemApi {

    // hello test branch
    private final ItemRepository itemRepository;

    public ItemApi(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String getAll(Model model){

        if (!itemRepository.findAll().isEmpty()){
            model.addAttribute("items", itemRepository.findAll());
            return "items";
        }

        return "redirect:/error";
    }

    @GetMapping("/{id}")
    public String getItem(@PathVariable("id") Long id, Model model){
        Item item = itemRepository.findById(id).orElse(null);

        if (item != null){
            model.addAttribute("item", item);
            return "item";
        }

        return "redirect:/error";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam(name = "name") String name,
                          @RequestParam(name = "price") int price){
        Item item = new Item();
        item.setPrice(price);
        item.setNameItem(name);
        Item addedItem = itemRepository.save(item);

        Item item1 = itemRepository.findById(addedItem.getId()).orElse(null);

        if (item1 != null){
            if (item1.equals(addedItem)){
                return "redirect:/item";
            }
        }

        return "redirect:/error";
    }

    @PostMapping("/update/{id}")
    public String updateItem(@PathVariable("id") Long id,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "price") int price){
        Item item = itemRepository.findById(id).orElse(null);

        if (item != null){
            item.setNameItem(name);
            item.setPrice(price);

            itemRepository.save(item);

            return "redirect:/item";
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
