package lpanek.tdd.vendingMachine.ex;

public class InvalidProductCountException extends RuntimeException {

    public InvalidProductCountException(String message) {
        super(message);
    }
}
