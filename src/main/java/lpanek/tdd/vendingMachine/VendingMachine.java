package lpanek.tdd.vendingMachine;

import java.util.Optional;

import lpanek.tdd.payment.*;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class VendingMachine {

    private final Shelves shelves;
    private Coins coins = new Coins();
    private String displayMessage;

    private int selectedProductShelveNumber = -1;
    private Coins coinsForCurrentlySelectedProduct = new Coins();

    VendingMachine(Shelves shelves, Coins coins) {
        this.shelves = shelves;
        this.coins = coins;
        this.displayMessage = getSelectProductMessage();
    }

    public void selectProduct(int shelveNumber) throws InvalidShelveNumberException {
        ProductType productType = shelves.getProductTypeOnShelve(shelveNumber).get();
        displayMessage = getInsertMoneyMessage(productType.getPrice());
        selectedProductShelveNumber = shelveNumber;
    }

    public void insertCoin(Coin coin) {
        coins = coins.plus(coin);
        coinsForCurrentlySelectedProduct = coinsForCurrentlySelectedProduct.plus(coin);

        ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber).get();
        Money moneyToInsert = productType.getPrice().minus(coinsForCurrentlySelectedProduct.getValue());
        if (moneyToInsert.equals(new Money(0, 0))) {
            displayMessage = getTakeProductMessage();
        } else {
            displayMessage = getInsertMoneyMessage(moneyToInsert);
        }
    }

    public Product takeProduct() {
        ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber).get();
        shelves.removeProductFromShelve(selectedProductShelveNumber);
        displayMessage = getSelectProductMessage();

        selectedProductShelveNumber = -1;
        coinsForCurrentlySelectedProduct = new Coins();

        return new Product(productType);
    }

    public int getShelveCount() {
        return shelves.getCount();
    }

    public Optional<ProductType> getProductTypeOnShelve(int shelveNumber) throws InvalidShelveNumberException {
        return shelves.getProductTypeOnShelve(shelveNumber);
    }

    public int getProductCountOnShelve(int shelveNumber) throws InvalidShelveNumberException {
        return shelves.getProductCountOnShelve(shelveNumber);
    }

    public String getMessageOnDisplay() {
        return displayMessage;
    }

    public Coins getCoins() {
        return coins;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %s]", getClass().getSimpleName(), shelves, coins);
    }

    private String getSelectProductMessage() {
        return "Select product.";
    }

    private String getInsertMoneyMessage(Money moneyToInsert) {
        return String.format("Insert %d.%02d %s.", moneyToInsert.getWholes(), moneyToInsert.getPennies(), moneyToInsert.getCurrencySymbol());
    }

    private String getTakeProductMessage() {
        return "Take your product.";
    }
}
