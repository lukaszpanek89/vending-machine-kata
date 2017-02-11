package lpanek.tdd.vendingMachine.domain.shelves.ex;

public class InvalidProductCountException extends RuntimeException {

    public InvalidProductCountException(String message) {
        super(message);
    }
}
