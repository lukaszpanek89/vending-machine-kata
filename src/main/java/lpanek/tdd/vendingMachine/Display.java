package lpanek.tdd.vendingMachine;

import lpanek.tdd.payment.Money;

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

    public String getCurrentMessage() {
        return message;
    }
}