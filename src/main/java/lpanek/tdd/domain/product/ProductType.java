package lpanek.tdd.domain.product;

import lpanek.tdd.domain.payment.Money;

public class ProductType {

    private Money price;

    public ProductType(String name, Money price) {
        this.price = price;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s]", getClass().getSimpleName(), price.toString());
    }
}
