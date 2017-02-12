package lpanek.tdd.vendingMachine.controller;

import lpanek.tdd.vendingMachine.domain.VendingMachineModel;
import lpanek.tdd.vendingMachine.domain.payment.*;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ex.UnableToDetermineChangeException;
import lpanek.tdd.vendingMachine.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.physicalParts.*;
import lpanek.tdd.vendingMachine.physicalParts.listeners.*;

public class VendingMachineController implements KeyboardListener, CoinTakerListener, CoinsDispenserListener, ProductDispenserListener {

    private final Display display;
    private final CoinsDispenser coinsDispenser;
    private final ProductDispenser productDispenser;
    private VendingMachineModel model;

    public VendingMachineController(Display display, Keyboard keyboard,
                                    CoinTaker coinTaker, CoinsDispenser coinsDispenser, ProductDispenser productDispenser,
                                    VendingMachineModel model) {
        this.display = display;
        this.coinsDispenser = coinsDispenser;
        this.productDispenser = productDispenser;
        this.model = model;

        keyboard.addListener(this);
        coinTaker.addListener(this);
        coinsDispenser.addListener(this);
        productDispenser.addListener(this);

        this.display.showSelectProduct();
    }

    @Override
    public void onKeyPressed(Key key) {
        try {
            if (isNumeric(key)) {
                handleNumericKeyPressed(key);
            } else if (isCancel(key)) {
                handleCancelKeyPressed();
            } else {
                throw new RuntimeException("Unknown key pressed: " + key);
            }
        } catch (RuntimeException e) {
            handleUnexpectedException(e);
        }
    }

    @Override
    public void onCoinInserted(Coin coin) {
        try {
            if (!model.canInsertCoin()) {
                coinsDispenser.dispenseCoins(new Coins(coin));
                return;
            }

            model.insertCoin(coin);
            if (model.isEnoughMoneyInserted()) {
                try {
                    dispenseChangeAndProductAndShowMessage();
                } catch (UnableToDetermineChangeException e) {
                    rejectInsertedCoinsAndShowMessage();
                }
            } else {
                showInsertMoney();
            }
        } catch (RuntimeException e) {
            handleUnexpectedException(e);
        }
    }

    @Override
    public void onCoinsTaken() {
        try {
            if (!model.canTakeCoins()) {
                return;
            }

            model.markCoinsTaken();
            if (model.isPurchaseFinished()) {
                display.showSelectProduct();
            }
        } catch (RuntimeException e) {
            handleUnexpectedException(e);
        }
    }

    @Override
    public void onProductTaken() {
        try {
            if (!model.canTakeProduct()) {
                throw new RuntimeException("Unexpected product taken notification.");
            }

            model.markProductTaken();
            if (model.isPurchaseFinished()) {
                display.showSelectProduct();
            }
        } catch (RuntimeException e) {
            handleUnexpectedException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s=[%s]", getClass().getSimpleName(), model);
    }

    private boolean isNumeric(Key key) {
        return (key.ordinal() >= Key._1.ordinal()) && (key.ordinal() <= Key._9.ordinal());
    }

    private boolean isCancel(Key key) {
        return key == Key.CANCEL;
    }

    private void handleNumericKeyPressed(Key key) {
        if (!model.canSelectProduct()) {
            return;
        }

        try {
            int shelveNumber = numericKeyToShelveNumber(key);
            model.selectProduct(shelveNumber);
            showInsertMoney();
        } catch (EmptyShelveException e) {
            display.showShelveIsEmpty();
        }
    }

    private int numericKeyToShelveNumber(Key numericKey) {
        return numericKey.ordinal() - Key._1.ordinal() + 1;
    }

    private void handleCancelKeyPressed() {
        if (!model.canCancelPurchase()) {
            return;
        }

        Coins insertedCoins = model.getInsertedCoins();
        if (insertedCoins.isNotEmpty()) {
            coinsDispenser.dispenseCoins(insertedCoins);
            // We simplify things a little, and assume that coins dispense happens immediately.
            model.markInsertedCoinsDispensed();
            display.showTakeCoinsAfterCancel();
        } else {
            model.resetPurchase();
            display.showSelectProduct();
        }
    }

    private void showInsertMoney() {
        Money moneyToInsert = model.getMoneyToInsert();
        display.showInsertMoney(moneyToInsert);
    }

    private void dispenseChangeAndProductAndShowMessage() throws UnableToDetermineChangeException {
        boolean tooMuchMoneyInserted = model.isTooMuchMoneyInserted();
        if (tooMuchMoneyInserted) {
            model.determineCoinsForChange();
            coinsDispenser.dispenseCoins(model.getCoinsForChange());
        }

        int shelveNumber = model.getSelectedProductShelveNumber();
        productDispenser.dispenseProductFromShelve(shelveNumber);

        // We simplify things a little, and assume that product and coins dispense happens immediately.
        model.markChangeAndProductDispensed();

        if (tooMuchMoneyInserted) {
            display.showTakeProductAndChange();
        } else {
            display.showTakeProduct();
        }
    }

    private void rejectInsertedCoinsAndShowMessage() {
        Coins insertedCoins = model.getInsertedCoins();
        coinsDispenser.dispenseCoins(insertedCoins);
        // We simplify things a little, and assume that coins dispense happens immediately.
        model.markInsertedCoinsDispensed();
        display.showUnableToGiveChange();
    }

    private void handleUnexpectedException(Exception e) {
        display.showInternalError();
        e.printStackTrace();
    }
}
