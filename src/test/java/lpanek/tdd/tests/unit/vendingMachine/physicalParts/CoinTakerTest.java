package lpanek.tdd.tests.unit.vendingMachine.physicalParts;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.domain.payment.Coin;
import lpanek.tdd.vendingMachine.physicalParts.CoinTaker;
import lpanek.tdd.vendingMachine.physicalParts.listeners.CoinTakerListener;

@RunWith(JUnitParamsRunner.class)
public class CoinTakerTest {

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(method = "getTestData_Coin")
    public void should_NotifyListeners_When_CoinInserted(Coin coin) {
        // given
        CoinTakerListener listener1 = mock(CoinTakerListener.class);
        CoinTakerListener listener2 = mock(CoinTakerListener.class);
        CoinTaker coinTaker = new CoinTaker();
        coinTaker.addListener(listener1);
        coinTaker.addListener(listener2);

        // when
        coinTaker.insert(coin);

        // then
        verify(listener1).onCoinInserted(coin);
        verify(listener2).onCoinInserted(coin);
    }

    @SuppressWarnings("unused")
    private Coin[] getTestData_Coin() {
        return new Coin[] {
                Coin._0_1,
                Coin._0_2,
                Coin._0_5,
                Coin._0_1,
                Coin._2_0,
                Coin._5_0
        };
    }
}
