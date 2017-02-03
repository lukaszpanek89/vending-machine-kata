package lpanek.tdd.vendingMachine;

import java.util.Optional;

import lpanek.tdd.payment.Coin;
import lpanek.tdd.payment.Coins;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class VendingMachine {

    private Shelves shelves = new Shelves();
    private Coins coins = new Coins();

    VendingMachine(Shelves shelves) {
        this.shelves = shelves;
    }

    public void selectProduct(int shelveNumber) {

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
        return "Select product.";
    }

    public Coins getCoins() {
        return coins;
    }
}
