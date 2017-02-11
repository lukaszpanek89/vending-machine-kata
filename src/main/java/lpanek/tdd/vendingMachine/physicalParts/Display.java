package lpanek.tdd.vendingMachine.physicalParts;

import lpanek.tdd.vendingMachine.domain.payment.Money;

public class Display {

    private String message = "";

    public void showSelectProduct() {
        message = "Select product.";
    }

    public void showShelveIsEmpty() {
        message = "Shelve is empty.";
    }

    public void showInsertMoney(Money moneyToInsert) {
        message = String.format("Insert %d.%02d %s.", moneyToInsert.getWholes(), moneyToInsert.getPennies(), moneyToInsert.getCurrencySymbol());
    }

    public void showTakeProduct() {
        message = "Take your product.";
    }

    public void showTakeProductAndChange() {
        message = "Take your product and change.";
    }

    public void showInternalError() {
        message = "Internal error...";
    }

    public String getMessage() {
        return message;
    }
}
