package lpanek.tdd.vendingMachine;

import lpanek.tdd.payment.Coins;
import lpanek.tdd.product.ProductType;

public class VendingMachineBuilder {

    private Shelves shelves = new Shelves();

    public VendingMachineBuilder withShelves(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineBuilder withCoins(Coins coins) {
        return null;
    }

    public VendingMachine build() {
        return new VendingMachine(shelves);
    }
}
