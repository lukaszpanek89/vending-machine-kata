package lpanek.tdd.vendingMachine;

import lpanek.tdd.payment.Coins;
import lpanek.tdd.product.ProductType;

public class VendingMachineBuilder {

    private Shelves shelves = new Shelves();

    public VendingMachineBuilder addShelve(ProductType productType, int productCount) {
        Shelve shelve = new Shelve(productType, productCount);
        shelves.add(shelve);
        return this;
    }

    public VendingMachineBuilder addEmptyShelve() {
        return null;
    }

    public VendingMachineBuilder withCoins(Coins coins) {
        return null;
    }

    public VendingMachine build() {
        return new VendingMachine(shelves);
    }
}
