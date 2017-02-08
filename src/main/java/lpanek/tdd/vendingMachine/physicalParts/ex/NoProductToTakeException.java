package lpanek.tdd.vendingMachine.physicalParts.ex;

public class NoProductToTakeException extends RuntimeException {

    public NoProductToTakeException(String message) {
        super(message);
    }
}
