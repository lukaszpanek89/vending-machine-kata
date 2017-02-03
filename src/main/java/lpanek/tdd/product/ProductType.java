package lpanek.tdd.product;

import lpanek.tdd.payment.Money;

public class ProductType {

    private Money price;

    public ProductType(String name, Money price) {
        this.price = price;
    }

    public Money getPrice() {
        return price;
    }
}
