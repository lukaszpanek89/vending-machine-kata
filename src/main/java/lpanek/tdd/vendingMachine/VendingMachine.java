package lpanek.tdd.vendingMachine;

import lpanek.tdd.payment.*;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class VendingMachine {

    private Display display;
    private final Shelves shelves;
    private Coins totalCoins;

    private int selectedProductShelveNumber = -1;
    private Coins coinsForSelectedProduct = new Coins();

    VendingMachine(Shelves shelves, Coins coins) {
        this.display = new Display();
        this.shelves = shelves;
        this.totalCoins = coins;

        this.display.showSelectProduct();
    }

    public void selectProduct(int shelveNumber) throws InvalidShelveNumberException {
        try {
            ProductType productType = shelves.getProductTypeOnShelve(shelveNumber);
            display.showInsertMoney(productType.getPrice());
            selectedProductShelveNumber = shelveNumber;
        } catch (EmptyShelveException e) {
            display.showShelveIsEmpty();
        }
    }

    public void insertCoin(Coin coin) {
        totalCoins = totalCoins.plus(coin);
        coinsForSelectedProduct = coinsForSelectedProduct.plus(coin);

        ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber);
        Money moneyToInsert = productType.getPrice().minus(coinsForSelectedProduct.getValue());
        if (moneyToInsert.equals(new Money(0, 0))) {
            display.showTakeProduct();
        } else {
            display.showInsertMoney(moneyToInsert);
        }
    }

    public Product takeProduct() {
        ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber);
        shelves.removeProductFromShelve(selectedProductShelveNumber);
        display.showSelectProduct();

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
        return display.getCurrentMessage();
    }

    public Coins getCoins() {
        return totalCoins;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %s]", getClass().getSimpleName(), shelves, totalCoins);
    }
}
