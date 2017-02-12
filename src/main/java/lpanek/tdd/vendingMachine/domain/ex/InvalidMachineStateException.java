package lpanek.tdd.vendingMachine.domain.ex;

public class InvalidMachineStateException extends RuntimeException {

    public InvalidMachineStateException(String message) {
        super(message);
    }
}
