package lpanek.tdd.tests.util;

import lpanek.tdd.domain.VendingMachineModel;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.controller.VendingMachineController;
import lpanek.tdd.vendingMachine.physicalParts.*;

public class VendingMachineControllerBuilder {

    private Display display = new Display();
    private Keyboard keyboard = new Keyboard();
    private CoinTaker coinTaker = new CoinTaker();
    private CoinsDispenser coinsDispenser = new CoinsDispenser();
    private ProductDispenser productDispenser;

    private Shelves shelves = new Shelves();

    private VendingMachineModel model;

    public static VendingMachineControllerBuilder controllerBuilder(VendingMachineModel model) {
        return new VendingMachineControllerBuilder(model);
    }

    private VendingMachineControllerBuilder(VendingMachineModel model) {
        this.model = model;
    }

    public VendingMachineControllerBuilder with(Display display) {
        this.display = display;
        return this;
    }

    public VendingMachineControllerBuilder with(Keyboard keyboard) {
        this.keyboard = keyboard;
        return this;
    }

    public VendingMachineControllerBuilder with(CoinTaker coinTaker) {
        this.coinTaker = coinTaker;
        return this;
    }

    public VendingMachineControllerBuilder with(CoinsDispenser coinsDispenser) {
        this.coinsDispenser = coinsDispenser;
        return this;
    }

    public VendingMachineControllerBuilder with(ProductDispenser productDispenser) {
        this.productDispenser = productDispenser;
        return this;
    }

    public VendingMachineControllerBuilder with(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineController build() {
        if (productDispenser == null) {
            productDispenser = new ProductDispenser(shelves);
        }

        return new VendingMachineController(display, keyboard, coinTaker, coinsDispenser, productDispenser, model);
    }
}
