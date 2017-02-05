package lpanek.tdd.vendingMachine;

import java.util.Optional;

import lpanek.tdd.payment.*;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class VendingMachine {

    private final Shelves shelves;
    private Coins coins = new Coins();

    private int selectedProductShelveNumber = -1;
    private Coins coinsForCurrentlySelectedProduct = new Coins();

    VendingMachine(Shelves shelves, Coins coins) {
        this.shelves = shelves;
        this.coins = coins;
    }

    public void selectProduct(int shelveNumber) throws InvalidShelveNumberException {
        selectedProductShelveNumber = shelveNumber;
    }

    public void insertCoin(Coin coin) {
        coins = coins.plus(coin);
        coinsForCurrentlySelectedProduct = coinsForCurrentlySelectedProduct.plus(coin);
    }

    public Product takeProduct() {
        ProductType productType = shelves.getProductTypeOnShelve(selectedProductShelveNumber).get();
        Product product = new Product(productType);

        shelves.removeProductFromShelve(selectedProductShelveNumber);
        selectedProductShelveNumber = -1;
        coinsForCurrentlySelectedProduct = new Coins();

        return product;
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
        if (selectedProductShelveNumber <= 0) {
            return "Select product.";
        }

        Money productPrice = shelves.getProductTypeOnShelve(selectedProductShelveNumber).get().getPrice();
        Money moneyToInsert = productPrice.minus(coinsForCurrentlySelectedProduct.getValue());
        if (!moneyToInsert.equals(new Money(0, 0))) {
            return String.format("Insert %d.%02d %s.", moneyToInsert.getWholes(), moneyToInsert.getPennies(), moneyToInsert.getCurrencySymbol());
        }
        return "Take your product.";
    }

    public Coins getCoins() {
        return coins;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %s]", getClass().getSimpleName(), shelves, coins);
    }
}
