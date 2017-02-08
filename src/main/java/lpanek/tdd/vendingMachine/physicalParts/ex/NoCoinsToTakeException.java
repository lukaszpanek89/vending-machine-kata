package lpanek.tdd.vendingMachine.physicalParts.ex;

public class NoCoinsToTakeException extends RuntimeException {

    public NoCoinsToTakeException(String message) {
        super(message);
    }
}
