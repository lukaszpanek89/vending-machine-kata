package lpanek.tdd.vendingMachine.physicalParts.ex;

public class PreviousProductNotYetTakenException extends RuntimeException {

    public PreviousProductNotYetTakenException(String message) {
        super(message);
    }
}
