package lpanek.tdd.vendingMachine;

import java.util.Optional;

import lpanek.tdd.payment.*;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class VendingMachine {

    private Shelves shelves = new Shelves();
    private Coins coins = new Coins();

    ProductType selectedProductType;

    VendingMachine(Shelves shelves) {
        this.shelves = shelves;
    }

    public void selectProduct(int shelveNumber) throws InvalidShelveNumberException {
        selectedProductType = shelves.getProductTypeOnShelve(shelveNumber).get();
    }

    public void insertCoin(Coin coin) {

    }

    public Product takeProduct() {
        return null;
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
        if (selectedProductType == null) {
            return "Select product.";
        }

        Money price = selectedProductType.getPrice();
        return String.format("Insert %d.%2d %s.", price.getWholes(), price.getPennies(), price.getCurrencySymbol());
    }

    public Coins getCoins() {
        return coins;
    }
}
