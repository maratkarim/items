package kz.bitlab.dataJpa.api;

import kz.bitlab.dataJpa.model.Country;
import kz.bitlab.dataJpa.repository.CountryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/country")
public class CountryApi {

    private final CountryRepository countryRepository;

    public CountryApi(CountryRepository countryRepository){
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public String getAll(Model model){
        model.addAttribute("countries", countryRepository.findAll());
        return "countries";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable("id") Long id){
        model.addAttribute("country", countryRepository.findById(id).orElseThrow());
        return "country";
    }

    @PostMapping
    public String add(@RequestParam("name") String name,
                      @RequestParam("code") String code){

        Country country = new Country(null, name, code);

        countryRepository.save(country);

        return "redirect:/country";
    }

    @PostMapping("/{id}")
    public String update(Model model, @PathVariable("id") Long id,
                         @RequestParam(name = "name") String name,
                         @RequestParam(name = "code") String code){

        Country country = countryRepository.findById(id).orElseThrow();

        country.setName(name);
        country.setCode(code);

        countryRepository.save(country);

        return "redirect:/country";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        countryRepository.deleteById(id);
        return "redirect:/country";
    }

}
