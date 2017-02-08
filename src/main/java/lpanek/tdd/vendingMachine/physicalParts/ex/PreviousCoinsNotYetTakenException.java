package lpanek.tdd.vendingMachine.physicalParts.ex;

public class PreviousCoinsNotYetTakenException extends RuntimeException {

    public PreviousCoinsNotYetTakenException(String message) {
        super(message);
    }
}
