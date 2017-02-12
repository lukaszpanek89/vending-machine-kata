package lpanek.tdd.vendingMachine.domain;

import lpanek.tdd.vendingMachine.domain.payment.*;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ChangeDeterminingStrategy;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ex.UnableToDetermineChangeException;
import lpanek.tdd.vendingMachine.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.domain.shelves.ex.InvalidShelveNumberException;

public class VendingMachineModel {

    // TODO: This enum is public for testing purposes only. Should be private.
    public enum MachineState {
        ProductNotSelected,
        ProductSelected,
        ProductAndOptionallyChangeDispensed,
        InsertedCoinsDispensed
    }

    private MachineState machineState;

    private final Shelves shelves;
    private Coins totalCoins;
    private ChangeDeterminingStrategy changeStrategy;

    private int selectedProductShelveNumber = -1;
    private Coins coinsInsertedForProduct = new Coins();
    private Coins coinsForChange = new Coins();
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
        coinsInsertedForProduct = coinsInsertedForProduct.plus(coin);
    }

    public void markChangeAndProductDispensed() {
        if (coinsForChange.getValue().isGreaterThan(Money.ZERO)) {
            totalCoins = totalCoins.minus(coinsForChange);
            coinsForChange = new Coins();
            isWaitingForCoinsToBeTaken = true;
        } else {
            isWaitingForCoinsToBeTaken = false;
        }

        shelves.removeProductFromShelve(selectedProductShelveNumber);
        selectedProductShelveNumber = -1;
        coinsInsertedForProduct = new Coins();
        isWaitingForProductToBeTaken = true;

        machineState = MachineState.ProductAndOptionallyChangeDispensed;
    }

    public void markInsertedCoinsDispensed() {
        totalCoins = totalCoins.minus(coinsInsertedForProduct);
        isWaitingForCoinsToBeTaken = true;

        selectedProductShelveNumber = -1;
        coinsInsertedForProduct = new Coins();
        isWaitingForProductToBeTaken = false;

        machineState = MachineState.InsertedCoinsDispensed;
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
        return (isInState(MachineState.ProductAndOptionallyChangeDispensed) || isInState(MachineState.InsertedCoinsDispensed))
                && isWaitingForCoinsToBeTaken;
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

    public Coins getInsertedCoins() {
        return coinsInsertedForProduct;
    }

    public Money getMoneyToInsert() {
        Money productPrice = getSelectedProductPrice();
        Money coinsValue = coinsInsertedForProduct.getValue();
        return productPrice.minus(coinsValue);
    }

    public boolean isEnoughMoneyInserted() {
        Money productPrice = getSelectedProductPrice();
        Money coinsValue = coinsInsertedForProduct.getValue();
        return coinsValue.isGreaterOrEqualTo(productPrice);
    }

    public boolean isTooMuchMoneyInserted() {
        Money coinsValue = coinsInsertedForProduct.getValue();
        Money productPrice = getSelectedProductPrice();
        return coinsValue.isGreaterThan(productPrice);
    }

    public void determineCoinsForChange() throws UnableToDetermineChangeException {
        Money coinsValue = coinsInsertedForProduct.getValue();
        Money productPrice = getSelectedProductPrice();
        Money overpayment = coinsValue.minus(productPrice);
        coinsForChange = changeStrategy.determineChange(totalCoins, overpayment);
    }

    public Coins getCoinsForChange() {
        return coinsForChange;
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
    public void setCoinsInsertedForProduct(Coins coins) {
        this.coinsInsertedForProduct = coins;
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

    private boolean isInState(MachineState state) {
        return machineState == state;
    }

    private Money getSelectedProductPrice() {
        return shelves.getProductTypeOnShelve(selectedProductShelveNumber).getPrice();
    }
}
