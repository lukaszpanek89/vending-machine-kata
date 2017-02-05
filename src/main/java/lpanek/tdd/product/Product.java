package lpanek.tdd.product;

public class Product {

    private ProductType productType;

    public Product(ProductType productType) {
        this.productType = productType;
    }

    public ProductType getType() {
        return productType;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s]", getClass().getSimpleName(), productType);
    }
}
