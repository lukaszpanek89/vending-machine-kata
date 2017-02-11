package lpanek.tdd.vendingMachine.domain;

import lpanek.tdd.vendingMachine.domain.payment.*;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ChangeDeterminingStrategy;
import lpanek.tdd.vendingMachine.domain.product.ProductType;
import lpanek.tdd.vendingMachine.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.domain.shelves.ex.InvalidShelveNumberException;

public class VendingMachineModel {

    // TODO: This enum is public for testing purposes only. Should be private.
    public enum MachineState {
        ProductNotSelected,
        ProductSelected,
        ProductAndOptionallyChangeDispensed
    }

    private MachineState machineState;

    private final Shelves shelves;
    private Coins totalCoins;
    private ChangeDeterminingStrategy changeStrategy;

    private int selectedProductShelveNumber = -1;
    private Coins coinsForSelectedProduct = new Coins();
    private boolean isWaitingForCoinsToBeTaken = false;
    private boolean isWaitingForProductToBeTaken = false;

    public VendingMachineModel(Shelves shelves, Coins totalCoins, ChangeDeterminingStrategy changeStrategy) {
        this.shelves = shelves;
        this.totalCoins = totalCoins;
        this.changeStrategy = changeStrategy;
        this.machineState = MachineState.ProductNotSelected;
    }

    public void selectProduct(int shelveNumber) throws InvalidShelveNumberException, EmptyShelveException {
        shelves.getProductTypeOnShelve(shelveNumber);
        selectedProductShelveNumber = shelveNumber;

        machineState = MachineState.ProductSelected;
    }

    public void insertCoin(Coin coin) {
        totalCoins = totalCoins.plus(coin);
        coinsForSelectedProduct = coinsForSelectedProduct.plus(coin);
    }

    public void markChangeAndProductDispensed() {
        if (isTooMuchMoneyInserted()) {
            Coins change = determineCoinsForChange();
            totalCoins = totalCoins.minus(change);
            isWaitingForCoinsToBeTaken = true;
        } else {
            isWaitingForCoinsToBeTaken = false;
        }

        shelves.removeProductFromShelve(selectedProductShelveNumber);
        selectedProductShelveNumber = -1;
        coinsForSelectedProduct = new Coins();
        isWaitingForProductToBeTaken = true;

        machineState = MachineState.ProductAndOptionallyChangeDispensed;
    }

    public void markCoinsTaken() {
        isWaitingForCoinsToBeTaken = false;
        if (!isWaitingForProductToBeTaken) {
            machineState = MachineState.ProductNotSelected;
        }
    }

    public void markProductTaken() {
        isWaitingForProductToBeTaken = false;
        if (!isWaitingForCoinsToBeTaken) {
            machineState = MachineState.ProductNotSelected;
        }
    }

    public boolean canSelectProduct() {
        return isInState(MachineState.ProductNotSelected);
    }

    public boolean canInsertCoin() {
        return isInState(MachineState.ProductSelected);
    }

    public boolean canTakeCoins() {
        return isInState(MachineState.ProductAndOptionallyChangeDispensed) && isWaitingForCoinsToBeTaken;
    }

    public boolean canTakeProduct() {
        return isInState(MachineState.ProductAndOptionallyChangeDispensed) && isWaitingForProductToBeTaken;
    }

    public boolean isPurchaseFinished() {
        return isInState(MachineState.ProductNotSelected);
    }

    public int getSelectedProductShelveNumber() {
        return selectedProductShelveNumber;
    }

    public Money getSelectedProductPrice() {
        return getSelectedProductType().getPrice();
    }

    public Money getMoneyToInsert() {
        Money coinsValue = coinsForSelectedProduct.getValue();
        ProductType productType = getSelectedProductType();
        return productType.getPrice().minus(coinsValue);
    }

    public boolean isEnoughMoneyInserted() {
        Money coinsValue = coinsForSelectedProduct.getValue();
        ProductType productType = getSelectedProductType();
        return coinsValue.isGreaterOrEqualTo(productType.getPrice());
    }

    public boolean isTooMuchMoneyInserted() {
        Money coinsValue = coinsForSelectedProduct.getValue();
        ProductType productType = getSelectedProductType();
        return coinsValue.isGreaterThan(productType.getPrice());
    }

    public Coins determineCoinsForChange() {
        Money coinsValue = coinsForSelectedProduct.getValue();
        ProductType productType = getSelectedProductType();
        Money overpayment = coinsValue.minus(productType.getPrice());
        return changeStrategy.determineChange(totalCoins, overpayment);
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public Coins getTotalCoins() {
        return totalCoins;
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public void setMachineState(MachineState machineState) {
        this.machineState = machineState;
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public void setSelectedProductShelveNumber(int shelveNumber) {
        this.selectedProductShelveNumber = shelveNumber;
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public void setIsWaitingForCoinsToBeTaken(boolean isWaitingForCoinsToBeTaken) {
        this.isWaitingForCoinsToBeTaken = isWaitingForCoinsToBeTaken;
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public void setIsWaitingForProductToBeTaken(boolean isWaitingForProductToBeTaken) {
        this.isWaitingForProductToBeTaken = isWaitingForProductToBeTaken;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %s, %s]", getClass().getSimpleName(), machineState, shelves, totalCoins);
    }

    private ProductType getSelectedProductType() {
        return shelves.getProductTypeOnShelve(selectedProductShelveNumber);
    }

    private boolean isInState(MachineState state) {
        return machineState == state;
    }
}
