package lpanek.tdd.vendingMachine.controller;

import lpanek.tdd.domain.payment.*;
import lpanek.tdd.domain.payment.strategy.ChangeDeterminingStrategy;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.physicalParts.*;
import lpanek.tdd.vendingMachine.physicalParts.listeners.*;

public class VendingMachineController implements KeyboardListener, CoinTakerListener, CoinsDispenserListener, ProductDispenserListener {

    private final Display display;
    private final CoinsDispenser coinsDispenser;
    private final ProductDispenser productDispenser;
    private final Shelves shelves;
    private Coins totalCoins;

    private ChangeDeterminingStrategy changeStrategy;

    private int selectedProductShelveNumber = -1;
    private Coins coinsForSelectedProduct = new Coins();

    public VendingMachineController(Display display, Keyboard keyboard,
                                    CoinTaker coinTaker, CoinsDispenser coinsDispenser, ProductDispenser productDispenser,
                                    Shelves shelves, Coins totalCoins,
                                    ChangeDeterminingStrategy changeStrategy) {
        this.display = display;
        this.coinsDispenser = coinsDispenser;
        this.productDispenser = productDispenser;
        this.shelves = shelves;
        this.totalCoins = totalCoins;
        this.changeStrategy = changeStrategy;

        keyboard.addListener(this);
        coinTaker.addListener(this);
        coinsDispenser.addListener(this);
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

            Money coinsValue = coinsForSelectedProduct.getValue();
            if (coinsValue.isGreaterOrEqualTo(productType.getPrice())) {
                productDispenser.dispenseProductFromShelve(selectedProductShelveNumber);

                Money overpayment = coinsValue.minus(productType.getPrice());
                if (overpayment.isGreaterThan(Money.ZERO)) {
                    Coins change = changeStrategy.determineChange(totalCoins, overpayment);
                    coinsDispenser.dispenseCoins(change);
                }
            } else {
                Money moneyToInsert = productType.getPrice().minus(coinsValue);
                display.showInsertMoney(moneyToInsert);
            }
        } catch (RuntimeException e) {
            display.showInternalError();
        }
    }

    @Override
    public void onCoinsDispensed() {

    }

    @Override
    public void onCoinsTaken() {

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
