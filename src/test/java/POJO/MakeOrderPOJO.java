package POJO;

import java.util.List;

public class MakeOrderPOJO {
    private List<String> ingredients;

    public MakeOrderPOJO(){
    }

    public MakeOrderPOJO(List ingredients){
        this.ingredients=ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
