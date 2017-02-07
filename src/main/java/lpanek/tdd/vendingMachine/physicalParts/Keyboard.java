package lpanek.tdd.vendingMachine.physicalParts;

import java.util.LinkedList;
import java.util.List;

import lpanek.tdd.vendingMachine.physicalParts.listeners.KeyboardListener;

public class Keyboard {

    private List<KeyboardListener> listeners = new LinkedList<>();

    public void press(Key key) {
        listeners.stream().forEach(listener -> listener.onKeyPressed(key));
    }

    public void addListener(KeyboardListener listener) {
        listeners.add(listener);
    }
}
