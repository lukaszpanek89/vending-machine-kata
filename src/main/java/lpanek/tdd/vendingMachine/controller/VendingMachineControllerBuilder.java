package lpanek.tdd.vendingMachine.controller;

import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.physicalParts.Display;
import lpanek.tdd.vendingMachine.physicalParts.ProductDispenser;

public class VendingMachineControllerBuilder {

    private Display display = new Display();
    private ProductDispenser productDispenser = new ProductDispenser();
    private Shelves shelves = new Shelves();
    private Coins coins = new Coins();

    public VendingMachineControllerBuilder withDisplay(Display display) {
        this.display = display;
        return this;
    }

    public VendingMachineControllerBuilder withProductDispenser(ProductDispenser productDispenser) {
        this.productDispenser = productDispenser;
        return this;
    }

    public VendingMachineControllerBuilder withShelves(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineControllerBuilder withCoins(Coins coins) {
        this.coins = coins;
        return this;
    }

    public VendingMachineController build() {
        return new VendingMachineController(display, productDispenser, shelves, coins);
    }
}
