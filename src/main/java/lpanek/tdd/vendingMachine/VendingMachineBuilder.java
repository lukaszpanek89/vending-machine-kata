package lpanek.tdd.vendingMachine;

import lpanek.tdd.payment.Coins;

public class VendingMachineBuilder {

    private Display display = new Display();
    private ProductDispenser productDispenser = new ProductDispenser();
    private Shelves shelves = new Shelves();
    private Coins coins = new Coins();

    public VendingMachineBuilder withDisplay(Display display) {
        this.display = display;
        return this;
    }

    public VendingMachineBuilder withProductDispenser(ProductDispenser productDispenser) {
        this.productDispenser = productDispenser;
        return this;
    }

    public VendingMachineBuilder withShelves(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineBuilder withCoins(Coins coins) {
        this.coins = coins;
        return this;
    }

    public VendingMachine build() {
        return new VendingMachine(display, productDispenser, shelves, coins);
    }
}
