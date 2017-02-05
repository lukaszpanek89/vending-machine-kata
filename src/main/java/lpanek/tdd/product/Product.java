package lpanek.tdd.product;

public class Product {

    public ProductType getType() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s", getClass().getSimpleName());
    }
}
