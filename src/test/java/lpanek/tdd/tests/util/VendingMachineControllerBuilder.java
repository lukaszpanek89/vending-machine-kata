package lpanek.tdd.tests.util;

import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.controller.VendingMachineController;
import lpanek.tdd.vendingMachine.physicalParts.*;

public class VendingMachineControllerBuilder {

    private Display display = new Display();
    private Keyboard keyboard = new Keyboard();
    private CoinTaker coinTaker = new CoinTaker();
    private ProductDispenser productDispenser = new ProductDispenser();
    private Shelves shelves = new Shelves();
    private Coins totalCoins = new Coins();

    private Integer selectedProductShelveNumber;
    private Coins coinsForSelectedProduct;

    public static VendingMachineControllerBuilder controllerBuilder() {
        return new VendingMachineControllerBuilder();
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

    public VendingMachineControllerBuilder with(ProductDispenser productDispenser) {
        this.productDispenser = productDispenser;
        return this;
    }

    public VendingMachineControllerBuilder with(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineControllerBuilder with(Coins totalCoins) {
        this.totalCoins = totalCoins;
        return this;
    }

    public VendingMachineControllerBuilder withProductSelected(int selectedProductShelveNumber) {
        this.selectedProductShelveNumber = selectedProductShelveNumber;
        return this;
    }

    public VendingMachineControllerBuilder withCoinsForSelectedProductInserted(Coins coinsForSelectedProduct) {
        this.coinsForSelectedProduct = coinsForSelectedProduct;
        return this;
    }

    public VendingMachineController build() {
        VendingMachineController controller = new VendingMachineController(display, keyboard, coinTaker, productDispenser, shelves, totalCoins);
        if (selectedProductShelveNumber != null) {
            controller.setSelectedProductShelveNumber(selectedProductShelveNumber);
            if (coinsForSelectedProduct != null) {
                controller.setCoinsForSelectedProduct(coinsForSelectedProduct);
            }
        }
        return controller;
    }
}
