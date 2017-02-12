package lpanek.tdd.vendingMachine.domain;

import lpanek.tdd.vendingMachine.domain.ex.InvalidMachineStateException;
import lpanek.tdd.vendingMachine.domain.payment.*;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ChangeDeterminingStrategy;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ex.UnableToDetermineChangeException;
import lpanek.tdd.vendingMachine.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.domain.shelves.ex.InvalidShelveNumberException;

/**
 * Vending machine model responsible for managing machine's state during purchase process.
 */
public class VendingMachineModel {

    // TODO: This enum is public for testing purposes only. Should be private.
    public enum MachineState {
        PRODUCT_NOT_SELECTED,
        PRODUCT_SELECTED,
        PRODUCT_AND_OR_COINS_DISPENSED
    }

    private MachineState machineState = MachineState.PRODUCT_NOT_SELECTED;

    private Shelves shelves;
    private Coins totalCoins;
    private ChangeDeterminingStrategy changeStrategy;

    private int selectedProductShelveNumber = -1;
    private Coins coinsInsertedForProduct = new Coins();
    private Coins coinsForChange = new Coins();
    private boolean isWaitingForCoinsToBeTaken = false;
    private boolean isWaitingForProductToBeTaken = false;

    /**
     * Constructs new model ready for first product selection.
     */
    public VendingMachineModel(Shelves shelves, Coins totalCoins, ChangeDeterminingStrategy changeStrategy) {
        this.shelves = shelves;
        this.totalCoins = totalCoins;
        this.changeStrategy = changeStrategy;
    }

    /**
     * Selects product from given shelve.<br/>
     * <br/>
     * This method requires that no product is selected yet.<br/>
     * <br/>
     * When this method ends, model is ready for inserting coins.
     */
    public void selectProduct(int shelveNumber) throws InvalidShelveNumberException, EmptyShelveException {
        validateIsInState(MachineState.PRODUCT_NOT_SELECTED);

        shelves.getProductTypeOnShelve(shelveNumber);
        selectedProductShelveNumber = shelveNumber;
        coinsInsertedForProduct = new Coins();

        machineState = MachineState.PRODUCT_SELECTED;
    }

    /**
     * Inserts given coin.<br/>
     * <br/>
     * This method requires that there is product selected.<br/>
     * <br/>
     * When this method ends, model is ready for inserting next coin.
     */
    public void insertCoin(Coin coin) {
        validateIsInState(MachineState.PRODUCT_SELECTED);

        totalCoins = totalCoins.plus(coin);
        coinsInsertedForProduct = coinsInsertedForProduct.plus(coin);
        coinsForChange = new Coins();
    }

    /**
     * Marks paid product (and change, if product is overpaid) as dispensed.<br/>
     * <br/>
     * This method requires that there is product selected and paid. If it is overpaid, then this method additionally requires that coins
     * for change are already determined.<br/>
     * <br/>
     * When this method ends, model waits for product (and change, if product was overpaid) to be taken. Until then it won't accept
     * insertion of new coins or selection of new products.
     */
    public void markProductAndChangeAsDispensed() {
        validateIsInState(MachineState.PRODUCT_SELECTED);
        if (!isEnoughMoneyInserted()) {
            throw new InvalidMachineStateException("Product is not yet paid.");
        }
        if (isTooMuchMoneyInserted() && coinsForChange.isEmpty()) {
            throw new InvalidMachineStateException("Coins for change are not yet determined.");
        }

        if (coinsForChange.isNotEmpty()) {
            totalCoins = totalCoins.minus(coinsForChange);
            isWaitingForCoinsToBeTaken = true;
        } else {
            isWaitingForCoinsToBeTaken = false;
        }

        shelves.removeProductFromShelve(selectedProductShelveNumber);
        isWaitingForProductToBeTaken = true;

        machineState = MachineState.PRODUCT_AND_OR_COINS_DISPENSED;
    }

    /**
     * Marks all coins inserted for selected product as dispensed.<br/>
     * <br/>
     * This method requires that there is at least one coin inserted for selected product.<br/>
     * <br/>
     * When this method ends, model waits for inserted coins to be taken. Until then it won't accept insertion of new coins or selection of
     * new products.
     */
    public void markInsertedCoinsAsDispensed() {
        validateIsInState(MachineState.PRODUCT_SELECTED);
        if (getInsertedCoins().isEmpty()) {
            throw new InvalidMachineStateException("There are no inserted coins.");
        }

        totalCoins = totalCoins.minus(coinsInsertedForProduct);
        isWaitingForCoinsToBeTaken = true;
        isWaitingForProductToBeTaken = false;

        machineState = MachineState.PRODUCT_AND_OR_COINS_DISPENSED;
    }

    /**
     * Marks dispensed coins (which are either coins for change or coins inserted for selected product) as taken.<br/>
     * <br/>
     * This method requires that model is waiting for coins to be taken.<br/>
     * <br/>
     * When this method ends, model:
     * <ul>
     * <li>either waits for dispensed product to be taken (this is the case if taken coins were coins for change and dispensed product is
     * not yet taken); if so model won't accept insertion of new coins or selection of new products until product is taken too,</li>
     * <li>or is ready for new product selection (this is the case (1) if taken coins were coins inserted for selected product, or (2) if
     * taken coins were coins for change and dispensed product is already taken).</li>
     * </ul>
     */
    public void markCoinsTaken() {
        validateIsInState(MachineState.PRODUCT_AND_OR_COINS_DISPENSED);
        if (!isWaitingForCoinsToBeTaken) {
            throw new InvalidMachineStateException("No coins are expected to be taken.");
        }

        isWaitingForCoinsToBeTaken = false;
        if (!isWaitingForProductToBeTaken) {
            machineState = MachineState.PRODUCT_NOT_SELECTED;
        }
    }

    /**
     * Marks dispensed product as taken.<br/>
     * <br/>
     * This method requires that model is waiting for product to be taken.<br/>
     * <br/>
     * When this method ends, model:
     * <ul>
     * <li>either waits for dispensed coins for change to be taken (this is the case if they are not yet taken); if so model won't accept
     * insertion of new coins or selection of new products until coins for change are taken too,</li>
     * <li>or is ready for new product selection (this is the case if dispensed coins for change are already taken).</li>
     * </ul>
     */
    public void markProductTaken() {
        validateIsInState(MachineState.PRODUCT_AND_OR_COINS_DISPENSED);
        if (!isWaitingForProductToBeTaken) {
            throw new InvalidMachineStateException("No product is expected to be taken.");
        }

        isWaitingForProductToBeTaken = false;
        if (!isWaitingForCoinsToBeTaken) {
            machineState = MachineState.PRODUCT_NOT_SELECTED;
        }
    }

    /**
     * Unselects currently selected product.<br/>
     * <br/>
     * This method requires that there is product selected and that no coins were inserted for it. If the latter is not the case, then
     * inserted coins should be first dispensed and marked as dispensed.<br/>
     * <br/>
     * When this method ends, model is ready for product selection.
     */
    public void unselectProduct() {
        validateIsInState(MachineState.PRODUCT_SELECTED);
        if (getInsertedCoins().isNotEmpty()) {
            throw new InvalidMachineStateException("There are coins inserted for selected product.");
        }

        machineState = MachineState.PRODUCT_NOT_SELECTED;
    }

    public boolean canSelectProduct() {
        return isInState(MachineState.PRODUCT_NOT_SELECTED);
    }

    public boolean canInsertCoin() {
        return isInState(MachineState.PRODUCT_SELECTED);
    }

    public boolean canTakeCoins() {
        return isInState(MachineState.PRODUCT_AND_OR_COINS_DISPENSED) && isWaitingForCoinsToBeTaken;
    }

    public boolean canTakeProduct() {
        return isInState(MachineState.PRODUCT_AND_OR_COINS_DISPENSED) && isWaitingForProductToBeTaken;
    }

    public boolean canCancelPurchase() {
        return isInState(MachineState.PRODUCT_SELECTED);
    }

    public boolean isPurchaseFinished() {
        return isInState(MachineState.PRODUCT_NOT_SELECTED);
    }

    /**
     * Gets shelve number of selected product.<br/>
     * <br/>
     * This method requires that there is product selected. (Note that product marked as dispensed is not deemed selected.)
     */
    public int getSelectedProductShelveNumber() {
        validateIsInState(MachineState.PRODUCT_SELECTED);

        return selectedProductShelveNumber;
    }

    /**
     * Gets coins inserted for product.<br/>
     * <br/>
     * This method requires that there is product selected. (Note that product marked as dispensed is not deemed selected.)
     */
    public Coins getInsertedCoins() {
        validateIsInState(MachineState.PRODUCT_SELECTED);

        return coinsInsertedForProduct;
    }

    /**
     * Gets money that is still to insert for product.<br/>
     * <br/>
     * This method requires that there is product selected. (Note that product marked as dispensed is not deemed selected.)
     */
    public Money getMoneyToInsert() {
        validateIsInState(MachineState.PRODUCT_SELECTED);

        if (isTooMuchMoneyInserted()) {
            return Money.ZERO;
        }
        Money productPrice = getSelectedProductPrice();
        Money coinsValue = coinsInsertedForProduct.getValue();
        return productPrice.minus(coinsValue);
    }

    /**
     * Informs whether selected product is already paid (or overpaid).<br/>
     * <br/>
     * This method requires that there is product selected. (Note that product marked as dispensed is not deemed selected.)
     */
    public boolean isEnoughMoneyInserted() {
        validateIsInState(MachineState.PRODUCT_SELECTED);

        Money productPrice = getSelectedProductPrice();
        Money coinsValue = coinsInsertedForProduct.getValue();
        return coinsValue.isGreaterOrEqualTo(productPrice);
    }

    /**
     * Informs whether selected product is already overpaid, in other words: whether purchase will involve change.<br/>
     * <br/>
     * This method requires that there is product selected. (Note that product marked as dispensed is not deemed selected.)
     */
    public boolean isTooMuchMoneyInserted() {
        validateIsInState(MachineState.PRODUCT_SELECTED);

        Money coinsValue = coinsInsertedForProduct.getValue();
        Money productPrice = getSelectedProductPrice();
        return coinsValue.isGreaterThan(productPrice);
    }

    /**
     * Determines coins for change.<br/>
     * <br/>
     * This method requires that there is product selected and overpaid. (Note that product marked as dispensed is not deemed selected.)
     */
    public void determineCoinsForChange() throws UnableToDetermineChangeException {
        validateIsInState(MachineState.PRODUCT_SELECTED);
        if (!isTooMuchMoneyInserted()) {
            throw new InvalidMachineStateException("Product is not overpaid.");
        }

        Money coinsValue = coinsInsertedForProduct.getValue();
        Money productPrice = getSelectedProductPrice();
        Money overpayment = coinsValue.minus(productPrice);
        coinsForChange = changeStrategy.determineChange(totalCoins, overpayment);
    }

    /**
     * Gets coins determined for change.<br/>
     * <br/>
     * This method requires that there is product selected and overpaid, and that coins for change are already determined. (Note that
     * product marked as dispensed is not deemed selected.)
     */
    public Coins getCoinsForChange() {
        validateIsInState(MachineState.PRODUCT_SELECTED);
        if (!isTooMuchMoneyInserted()) {
            throw new InvalidMachineStateException("Product is not overpaid.");
        }
        if (coinsForChange.isEmpty()) {
            throw new InvalidMachineStateException("Coins for change are not yet determined.");
        }

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

    private void validateIsInState(MachineState state) throws InvalidMachineStateException {
        if (!isInState(state)) {
            throw new InvalidMachineStateException(String.format("Machine is in state other than %s.", state));
        }
    }

    private boolean isInState(MachineState state) {
        return machineState == state;
    }

    private Money getSelectedProductPrice() {
        return shelves.getProductTypeOnShelve(selectedProductShelveNumber).getPrice();
    }
}
