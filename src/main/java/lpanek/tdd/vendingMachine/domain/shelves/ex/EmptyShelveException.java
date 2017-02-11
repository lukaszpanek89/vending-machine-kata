package lpanek.tdd.vendingMachine.domain.shelves.ex;

public class EmptyShelveException extends RuntimeException {

    public EmptyShelveException(String message) {
        super(message);
    }
}
