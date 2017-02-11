package lpanek.tdd.tests.unit.vendingMachine.physicalParts;

import static lpanek.tdd.tests.util.ConstructingUtil.coins;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lpanek.tdd.domain.payment.Coin;
import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.vendingMachine.physicalParts.CoinsDispenser;
import lpanek.tdd.vendingMachine.physicalParts.listeners.CoinsDispenserListener;

@RunWith(MockitoJUnitRunner.class)
public class CoinsDispenserTest {

    @Test
    public void should_ReturnRightCoins_When_CoinsTaken() {
        // given
        Coins expectedCoins = coins(Coin._0_1, Coin._0_2);
        CoinsDispenserListener listener1Mock = mock(CoinsDispenserListener.class);
        CoinsDispenserListener listener2Mock = mock(CoinsDispenserListener.class);

        CoinsDispenser coinsDispenser = new CoinsDispenser();
        coinsDispenser.addListener(listener1Mock);
        coinsDispenser.addListener(listener2Mock);

        // when
        coinsDispenser.dispenseCoins(expectedCoins);
        Coins actualCoins = coinsDispenser.takeCoins();

        // then
        verify(listener1Mock).onCoinsTaken();
        verify(listener2Mock).onCoinsTaken();
        assertThat(actualCoins).isEqualTo(expectedCoins);
    }

    @Test
    public void should_ThrowException_When_TriesToDispenseCoinsWhilePreviousCoinsWereNotYetTaken() {
        // TODO: To implement.
    }

    @Test
    public void should_ThrowException_When_TriesToTakeCoinsWithoutDispense() {
        // TODO: To implement.
    }

    @Test
    public void should_ThrowException_When_TriesToTakeDispensedCoinsTwice() {
        // TODO: To implement.
    }
}
