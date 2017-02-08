package lpanek.tdd.tests.unit.vendingMachine.physicalParts;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.physicalParts.Key;
import lpanek.tdd.vendingMachine.physicalParts.Keyboard;
import lpanek.tdd.vendingMachine.physicalParts.listeners.KeyboardListener;

@RunWith(JUnitParamsRunner.class)
public class KeyboardTest {

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(method = "getTestData_Key")
    public void should_NotifyListeners_When_KeyPressed(Key key) {
        // given
        KeyboardListener listener1Mock = mock(KeyboardListener.class);
        KeyboardListener listener2Mock = mock(KeyboardListener.class);
        Keyboard keyboard = new Keyboard();
        keyboard.addListener(listener1Mock);
        keyboard.addListener(listener2Mock);

        // when
        keyboard.press(key);

        // then
        verify(listener1Mock).onKeyPressed(key);
        verify(listener2Mock).onKeyPressed(key);
    }

    @SuppressWarnings("unused")
    private Key[] getTestData_Key() {
        return new Key[] {
                Key._1,
                Key._2,
                Key._3,
                Key._4,
                Key._5,
                Key._6,
                Key._7,
                Key._8,
                Key._9
        };
    }
}
