package lpanek.tdd.vendingMachine.ex;

public class InvalidShelveNumberException extends RuntimeException {

    public InvalidShelveNumberException(String message) {
        super(message);
    }
}
