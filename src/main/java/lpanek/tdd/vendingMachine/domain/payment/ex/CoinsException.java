package lpanek.tdd.vendingMachine.domain.payment.ex;

public class CoinsException extends RuntimeException {

    public CoinsException(String message) {
        super(message);
    }
}
