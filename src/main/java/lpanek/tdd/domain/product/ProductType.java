package lpanek.tdd.domain.product;

import lpanek.tdd.domain.payment.Money;

import java.util.Objects;

public class ProductType {

    private String name;
    private Money price;

    public ProductType(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }

        ProductType other = (ProductType) object;

        return (other.name.equals(this.name))
                && (other.price.equals(this.price));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %s]", getClass().getSimpleName(), name, price.toString());
    }
}
