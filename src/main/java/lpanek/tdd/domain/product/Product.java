package lpanek.tdd.domain.product;

public class Product {

    private ProductType productType;

    public Product(ProductType productType) {
        this.productType = productType;
    }

    public ProductType getType() {
        return productType;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }

        Product other = (Product) object;

        return other.productType.equals(this.productType);
    }

    @Override
    public int hashCode() {
        return productType.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s=[%s]", getClass().getSimpleName(), productType);
    }
}
