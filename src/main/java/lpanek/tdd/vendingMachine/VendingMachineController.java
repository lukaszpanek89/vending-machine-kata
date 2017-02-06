package lpanek.tdd.vendingMachine;

import lpanek.tdd.payment.*;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class VendingMachineController {

    private final Display display;
    private final ProductDispenser productDispenser;
    private final Shelves shelves;
    private Coins totalCoins;

    private int selectedProductShelveNumber = -1;
    private Coins coinsForSelectedProduct = new Coins();
    private Product paidProductBeforeTake;

    VendingMachineController(Display display, ProductDispenser productDispenser, Shelves shelves, Coins coins) {
        this.display = display;
        this.productDispenser = productDispenser;
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

    public Product takeProduct() {
        display.showSelectProduct();

        Product product = paidProductBeforeTake;
        paidProductBeforeTake = null;
        return product;
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
