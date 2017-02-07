package lpanek.tdd.vendingMachine;

import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.controller.VendingMachineControllerBuilder;
import lpanek.tdd.vendingMachine.physicalParts.*;

public class VendingMachineBuilder {

    private GlassCase glassCase = new GlassCase();
    private Display display = new Display();
    private Keyboard keyboard = new Keyboard();
    private CoinTaker coinTaker = new CoinTaker();
    private ProductDispenser productDispenser = new ProductDispenser();

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
        new VendingMachineControllerBuilder().withDisplay(display).withProductDispenser(productDispenser)
                .withShelves(shelves).withCoins(coins).build();
        return new VendingMachine(glassCase, display, keyboard, coinTaker, productDispenser);
    }
}
