package lpanek.tdd.domain.shelves.ex;

public class InvalidProductCountException extends RuntimeException {

    public InvalidProductCountException(String message) {
        super(message);
    }
}
