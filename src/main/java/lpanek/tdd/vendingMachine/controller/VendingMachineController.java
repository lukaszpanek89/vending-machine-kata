package lpanek.tdd.vendingMachine.controller;

import lpanek.tdd.domain.payment.*;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.physicalParts.*;
import lpanek.tdd.vendingMachine.physicalParts.listeners.*;

public class VendingMachineController implements KeyboardListener, CoinTakerListener, ProductDispenserListener {

    private final Display display;
    private final ProductDispenser productDispenser;
    private final Shelves shelves;
    private Coins totalCoins;

    private int selectedProductShelveNumber = -1;
    private Coins coinsForSelectedProduct = new Coins();

    public VendingMachineController(Display display, Keyboard keyboard,
                                    CoinTaker coinTaker, ProductDispenser productDispenser,
                                    Shelves shelves, Coins totalCoins) {
        this.display = display;
        this.productDispenser = productDispenser;
        this.shelves = shelves;
        this.totalCoins = totalCoins;

        keyboard.addListener(this);
        coinTaker.addListener(this);
        productDispenser.addListener(this);
        this.display.showSelectProduct();
    }

    @Override
    public void onKeyPressed(Key key) {
        try {
            int shelveNumber = keyToShelveNumber(key);
            ProductType productType = shelves.getProductTypeOnShelve(shelveNumber);
            display.showInsertMoney(productType.getPrice());

            selectedProductShelveNumber = shelveNumber;
        } catch (EmptyShelveException e) {
            display.showShelveIsEmpty();
        } catch (RuntimeException e) {
            display.showInternalError();
        }
    }

    @Override
    public void onCoinInserted(Coin coin) {
        try {
            totalCoins = totalCoins.plus(coin);
            coinsForSelectedProduct = coinsForSelectedProduct.plus(coin);

            ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber);
            Money moneyToInsert = productType.getPrice().minus(coinsForSelectedProduct.getValue());
            if (moneyToInsert.equals(new Money(0, 0))) {
                productDispenser.dispenseProductFromShelve(selectedProductShelveNumber);
            } else {
                display.showInsertMoney(moneyToInsert);
            }
        } catch (RuntimeException e) {
            display.showInternalError();
        }
    }

    @Override
    public void onProductDispensed() {
        try {
            shelves.removeProductFromShelve(selectedProductShelveNumber);
            display.showTakeProduct();

            selectedProductShelveNumber = -1;
            coinsForSelectedProduct = new Coins();
        } catch (RuntimeException e) {
            display.showInternalError();
        }
    }

    @Override
    public void onProductTaken() {
        try {
            display.showSelectProduct();
        } catch (RuntimeException e) {
            display.showInternalError();
        }
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public Coins getCoins() {
        return totalCoins;
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public void setSelectedProductShelveNumber(int shelveNumber) {
        this.selectedProductShelveNumber = shelveNumber;
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public void setCoinsForSelectedProduct(Coins coinsForSelectedProduct) {
        this.coinsForSelectedProduct = coinsForSelectedProduct;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %s]", getClass().getSimpleName(), shelves, totalCoins);
    }

    private int keyToShelveNumber(Key key) {
        return key.ordinal() + 1;
    }
}
