package lpanek.tdd.tests.util;

import lpanek.tdd.payment.*;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.Shelve;
import lpanek.tdd.vendingMachine.Shelves;

public class ConstructingUtil {

    public static Shelve shelve(ProductType productType, int productCount) {
        return new Shelve(productType, productCount);
    }

    public static Shelve emptyShelve() {
        return new Shelve();
    }

    public static Shelves shelves(Shelve... shelves) {
        Shelves result = new Shelves();
        for (Shelve shelve : shelves) {
            result.add(shelve);
        }
        return result;
    }

    public static Shelves emptyShelves() {
        return new Shelves();
    }

    public static ProductType productType(String productName, Money productPrice) {
        return new ProductType(productName, productPrice);
    }

    public static Money price(int wholes, int pennies) {
        return new Money(wholes, pennies);
    }

    public static Coins coins(Coin... coins) {
        Coins result = new Coins();
        for (Coin coin : coins) {
            result.add(coin);
        }
        return result;
    }

    public static Shelve anyNotEmptyShelve() {
        return shelve(anyProductType(), 4);
    }

    public static ProductType anyProductType() {
        return productType("Lemon juice 0.3 l", anyPrice());
    }

    public static Money anyPrice() {
        return price(3, 50);
    }
}
