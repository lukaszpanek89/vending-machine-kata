package lpanek.tdd.vendingMachine.domain.payment.ex;

public class MoneyException extends RuntimeException {

    public MoneyException(String message) {
        super(message);
    }
}
