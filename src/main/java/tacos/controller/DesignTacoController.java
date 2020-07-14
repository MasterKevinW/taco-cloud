package tacos.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.dto.Ingredient;
import tacos.dto.Ingredient.Type;
import tacos.dto.Order;
import tacos.dto.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private TacoRepository designRepo;

    @Autowired
    public DesignTacoController(
            IngredientRepository ingredientRepo,
            TacoRepository designRepo){
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public  Taco taco(){
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for(Type type: types){
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        return "design";
    }

    @PostMapping
    public String processDesign(
            @Valid Taco design, Errors errors,
            @ModelAttribute Order order){

        if(errors.hasErrors()){
            return "design";
        }

        Taco saved = designRepo.save(design);
        order.addDesign(saved);

        log.info("Taco Submitted: " + design);

        return "redirect:/orders/current";
    }


//    @ModelAttribute
//    public void addIngredientsToModel(Model model) {
//        List<Ingredient> ingredients = Arrays.asList(
//                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
//                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
//                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
//                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
//                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
//                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
//                new Ingredient("CHED", "Cheddar", Type.CHEESE),
//                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
//                new Ingredient("SLSA", "Salsa", Type.SAUCE),
//                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
//        );
//
//        Type[] types = Ingredient.Type.values();
//        for (Type type : types) {
//            model.addAttribute(type.toString().toLowerCase(),
//                    filterByType(ingredients, type));
//        }
//    }
//
//    //tag::showDesignForm[]
//    @GetMapping
//    public String showDesignForm(Model model) {
//        model.addAttribute("design", new Taco());
//        return "design";
//    }
//
    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type){
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
//
//    @PostMapping
//    public String processDesign(@Valid @ModelAttribute("design") Taco design, Errors errors, Model model){
//        if(errors.hasErrors()){
//            return "design";
//        }
//        log.info("Process design: " + design);
//        return "redirect:/orders/current";
//    }
}
