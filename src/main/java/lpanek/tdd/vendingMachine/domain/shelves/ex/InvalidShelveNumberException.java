package lpanek.tdd.vendingMachine.domain.shelves.ex;

public class InvalidShelveNumberException extends RuntimeException {

    public InvalidShelveNumberException(String message) {
        super(message);
    }
}
