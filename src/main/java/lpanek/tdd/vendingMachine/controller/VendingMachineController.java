package lpanek.tdd.vendingMachine.controller;

import lpanek.tdd.domain.payment.*;
import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.domain.shelves.ex.InvalidShelveNumberException;
import lpanek.tdd.vendingMachine.physicalParts.*;
import lpanek.tdd.vendingMachine.physicalParts.listeners.*;

public class VendingMachineController implements KeyboardListener, CoinTakerListener, ProductDispenserListener {

    private final Display display;
    private final ProductDispenser productDispenser;
    private final Shelves shelves;
    private Coins totalCoins;

    private int selectedProductShelveNumber = -1;
    private Coins coinsForSelectedProduct = new Coins();
    private Product paidProductBeforeTake;

    public VendingMachineController(Display display, Keyboard keyboard,
                                    CoinTaker coinTaker, ProductDispenser productDispenser,
                                    Shelves shelves, Coins coins) {
        this.display = display;
        this.productDispenser = productDispenser;
        this.shelves = shelves;
        this.totalCoins = coins;

        keyboard.addListener(this);
        coinTaker.addListener(this);
        productDispenser.addListener(this);
        this.display.showSelectProduct();
    }

    @Override
    public void onKeyPressed(Key key) {
        int shelveNumber = keyToShelveNumber(key);

        try {
            ProductType productType = shelves.getProductTypeOnShelve(shelveNumber);
            display.showInsertMoney(productType.getPrice());

            selectedProductShelveNumber = shelveNumber;
        } catch (EmptyShelveException e) {
            display.showShelveIsEmpty();
        } catch (InvalidShelveNumberException e) {
            display.showInternalError();
        }
    }

    @Override
    public void onCoinInserted(Coin coin) {
        totalCoins = totalCoins.plus(coin);
        coinsForSelectedProduct = coinsForSelectedProduct.plus(coin);

        ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber);
        Money moneyToInsert = productType.getPrice().minus(coinsForSelectedProduct.getValue());
        if (moneyToInsert.equals(new Money(0, 0))) {
            productDispenser.dispenseProductFromShelve(selectedProductShelveNumber);
            shelves.removeProductFromShelve(selectedProductShelveNumber);
            display.showTakeProduct();

            selectedProductShelveNumber = -1;
            coinsForSelectedProduct = new Coins();
            paidProductBeforeTake = new Product(productType);
        } else {
            display.showInsertMoney(moneyToInsert);
        }

    }

    @Override
    public void onProductDispensedFromShelve(int shelveNumber) {

    }

    public Product takeProduct() {
        display.showSelectProduct();

        Product product = paidProductBeforeTake;
        paidProductBeforeTake = null;
        return product;
    }

    @Override
    public void onProductTaken() {

    }

    public ProductType getProductTypeOnShelve(int shelveNumber) throws InvalidShelveNumberException, EmptyShelveException {
        return shelves.getProductTypeOnShelve(shelveNumber);
    }

    public int getProductCountOnShelve(int shelveNumber) throws InvalidShelveNumberException {
        return shelves.getProductCountOnShelve(shelveNumber);
    }

    public Coins getCoins() {
        return totalCoins;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %s]", getClass().getSimpleName(), shelves, totalCoins);
    }

    private int keyToShelveNumber(Key key) {
        return key.ordinal() + 1;
    }
}
