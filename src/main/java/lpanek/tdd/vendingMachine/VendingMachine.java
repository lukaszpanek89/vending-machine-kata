package lpanek.tdd.vendingMachine;

import lpanek.tdd.payment.*;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class VendingMachine {

    private final Shelves shelves;
    private Coins totalCoins;
    private String displayMessage;

    private int selectedProductShelveNumber = -1;
    private Coins coinsForSelectedProduct = new Coins();

    VendingMachine(Shelves shelves, Coins coins) {
        this.shelves = shelves;
        this.totalCoins = coins;
        this.displayMessage = getSelectProductMessage();
    }

    public void selectProduct(int shelveNumber) throws InvalidShelveNumberException {
        try {
            ProductType productType = shelves.getProductTypeOnShelve(shelveNumber);
            displayMessage = getInsertMoneyMessage(productType.getPrice());
            selectedProductShelveNumber = shelveNumber;
        } catch (EmptyShelveException e) {
            displayMessage = getShelveIsEmptyMessage();
        }
    }

    public void insertCoin(Coin coin) {
        totalCoins = totalCoins.plus(coin);
        coinsForSelectedProduct = coinsForSelectedProduct.plus(coin);

        ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber);
        Money moneyToInsert = productType.getPrice().minus(coinsForSelectedProduct.getValue());
        if (moneyToInsert.equals(new Money(0, 0))) {
            displayMessage = getTakeProductMessage();
        } else {
            displayMessage = getInsertMoneyMessage(moneyToInsert);
        }
    }

    public Product takeProduct() {
        ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber);
        shelves.removeProductFromShelve(selectedProductShelveNumber);
        displayMessage = getSelectProductMessage();

        selectedProductShelveNumber = -1;
        coinsForSelectedProduct = new Coins();

        return new Product(productType);
    }

    public int getShelveCount() {
        return shelves.getCount();
    }

    public ProductType getProductTypeOnShelve(int shelveNumber) throws InvalidShelveNumberException, EmptyShelveException {
        return shelves.getProductTypeOnShelve(shelveNumber);
    }

    public int getProductCountOnShelve(int shelveNumber) throws InvalidShelveNumberException {
        return shelves.getProductCountOnShelve(shelveNumber);
    }

    public String getMessageOnDisplay() {
        return displayMessage;
    }

    public Coins getCoins() {
        return totalCoins;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %s]", getClass().getSimpleName(), shelves, totalCoins);
    }

    private String getSelectProductMessage() {
        return "Select product.";
    }

    private String getShelveIsEmptyMessage() {
        return "Shelve is empty.";
    }

    private String getInsertMoneyMessage(Money moneyToInsert) {
        return String.format("Insert %d.%02d %s.", moneyToInsert.getWholes(), moneyToInsert.getPennies(), moneyToInsert.getCurrencySymbol());
    }

    private String getTakeProductMessage() {
        return "Take your product.";
    }
}
