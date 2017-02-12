package lpanek.tdd.tests.unit.vendingMachine.physicalParts;

import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.coins;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lpanek.tdd.vendingMachine.domain.payment.Coin;
import lpanek.tdd.vendingMachine.domain.payment.Coins;
import lpanek.tdd.vendingMachine.physicalParts.CoinsDispenser;
import lpanek.tdd.vendingMachine.physicalParts.ex.NoCoinsToTakeException;
import lpanek.tdd.vendingMachine.physicalParts.ex.PreviousCoinsNotYetTakenException;
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
        // given
        CoinsDispenser coinsDispenser = new CoinsDispenser();
        coinsDispenser.setDispensedCoins(coins(Coin._0_1));

        // when
        Throwable caughtThrowable = catchThrowable(() -> coinsDispenser.dispenseCoins(coins(Coin._0_1, Coin._5_0)));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(PreviousCoinsNotYetTakenException.class)
                .hasMessage("Previously dispensed coins were not yet taken.");
    }

    @Test
    public void should_ThrowException_When_TriesToTakeCoinsWithoutDispense() {
        // given
        CoinsDispenser coinsDispenser = new CoinsDispenser();

        // when
        Throwable caughtThrowable = catchThrowable(coinsDispenser::takeCoins);

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(NoCoinsToTakeException.class)
                .hasMessage("There are no coins to take.");
    }

    @Test
    public void should_ThrowException_When_TriesToTakeDispensedCoinsTwice() {
        // given
        CoinsDispenser coinsDispenser = new CoinsDispenser();
        coinsDispenser.setDispensedCoins(coins(Coin._0_1));

        // when
        coinsDispenser.takeCoins();
        Throwable caughtThrowable = catchThrowable(coinsDispenser::takeCoins);

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(NoCoinsToTakeException.class)
                .hasMessage("There are no coins to take.");
    }
}
