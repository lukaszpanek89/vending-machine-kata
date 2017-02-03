package lpanek.tdd.vendingMachine;

import lpanek.tdd.payment.Coins;

public class VendingMachineBuilder {

    private Shelves shelves = new Shelves();
    private Coins coins = new Coins();

    public VendingMachineBuilder withShelves(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineBuilder withCoins(Coins coins) {
        this.coins = coins;
        return this;
    }

    public VendingMachine build() {
        return new VendingMachine(shelves);
    }
}
