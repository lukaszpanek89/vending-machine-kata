package lpanek.tdd.vendingMachine.controller;

import lpanek.tdd.vendingMachine.domain.VendingMachineModel;
import lpanek.tdd.vendingMachine.domain.payment.*;
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
            if (!model.canSelectProduct()) {
                return;
            }

            int shelveNumber = keyToShelveNumber(key);
            model.selectProduct(shelveNumber);

            Money productPrice = model.getSelectedProductPrice();
            display.showInsertMoney(productPrice);
        } catch (EmptyShelveException e) {
            display.showShelveIsEmpty();
        } catch (RuntimeException e) {
            display.showInternalError();
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
                boolean isTooMuchMoneyInserted = model.isTooMuchMoneyInserted();
                if (isTooMuchMoneyInserted) {
                    Coins change = model.determineCoinsForChange();
                    coinsDispenser.dispenseCoins(change);
                }
                productDispenser.dispenseProductFromShelve(model.getSelectedProductShelveNumber());

                // Here we simplify things a little, and assume that product and coins dispense happened immediately,
                // and so we instantly inform client that they are ready to be taken.

                model.markChangeAndProductDispensed();

                if (isTooMuchMoneyInserted) {
                    display.showTakeProductAndChange();
                } else {
                    display.showTakeProduct();
                }
            } else {
                Money moneyToInsert = model.getMoneyToInsert();
                display.showInsertMoney(moneyToInsert);
            }
        } catch (RuntimeException e) {
            display.showInternalError();
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
            display.showInternalError();
        }
    }

    @Override
    public void onProductTaken() {
        try {
            if (!model.canTakeProduct()) {
                return;
            }

            model.markProductTaken();
            if (model.isPurchaseFinished()) {
                display.showSelectProduct();
            }
        } catch (RuntimeException e) {
            display.showInternalError();
        }
    }

    @Override
    public String toString() {
        return String.format("%s=[%s]", getClass().getSimpleName(), model);
    }

    private int keyToShelveNumber(Key key) {
        return key.ordinal() + 1;
    }
}
