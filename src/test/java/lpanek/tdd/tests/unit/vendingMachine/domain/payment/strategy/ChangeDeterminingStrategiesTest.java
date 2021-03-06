package lpanek.tdd.tests.unit.vendingMachine.domain.payment.strategy;

import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.coins;
import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.money;
import static lpanek.tdd.vendingMachine.domain.payment.Coin.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.domain.payment.*;
import lpanek.tdd.vendingMachine.domain.payment.strategy.HighestDenominationFirstStrategy;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ex.UnableToDetermineChangeException;

@RunWith(JUnitParamsRunner.class)
public class ChangeDeterminingStrategiesTest {

    @Test
    @Parameters(method = "getTestData_AccessibleCoinsAndDeterminableChangeValue")
    public void should_CorrectlyDetermineChange_When_ChangeDeterminable_TestOf_HighestDenominationFirstStrategy(Coins accessibleCoins, Money changeValue) {
        // when
        Coins change = new HighestDenominationFirstStrategy().determineChange(accessibleCoins, changeValue);

        // then
        assertThat(change.getValue()).isEqualTo(changeValue);
        assertThatChangeIsSubsetOfAccessibleCoins(change, accessibleCoins);
    }

    @Test
    @Parameters(method = "getTestData_AccessibleCoinsAndDeterminableChangeValueAndExpectedChangeFromHighestDenominationFirstStrategy")
    public void should_PreferHigherCoins_When_ChangeDeterminable_TestOf_HighestDenominationFirstStrategy(Coins accessibleCoins, Money changeValue, Coins expectedChange) {
        // when
        Coins actualChange = new HighestDenominationFirstStrategy().determineChange(accessibleCoins, changeValue);

        // then
        assertThat(actualChange).isEqualTo(expectedChange);
    }

    @Test
    @Parameters(method = "getTestData_AccessibleCoinsAndIndeterminableChangeValue")
    public void should_ThrowException_When_ChangeIndeterminable_TestOf_HighestDenominationFirstStrategy(Coins accessibleCoins, Money changeValue) {
        // when
        Throwable caughtThrowable = catchThrowable(() -> new HighestDenominationFirstStrategy().determineChange(accessibleCoins, changeValue));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(UnableToDetermineChangeException.class);
    }

    private void assertThatChangeIsSubsetOfAccessibleCoins(Coins change, Coins accessibleCoins) {
        for (Coin coin : Coin.values()) {
            assertThat(accessibleCoins.getCoinCount(coin) >= change.getCoinCount(coin)).isTrue();
        }
    }

    /**
     * Basic positive test set for all change determining strategies. <br/>
     * <br/>
     * <b>Note:</b><br/>
     * There are more sophisticated test cases for which highest-denomination-first strategy fails, but I omitted them on purpose, since
     * designing robust change determining strategy is not the point of this kata.<br/>
     * <br/>
     * An example of such case is:
     * <ul>
     * <li>accessible coins: 2 x 0.50 PLN, and 3 x 0.20 PLN,</li>
     * <li>change value: 1.10 PLN.</li>
     * </ul>
     * Robust strategy should pick 1 x 0.50 PLN, and 3 x 0.20 PLN. However, highest-denomination-first strategy will start with picking 2 x
     * 0.50 PLN, and so it then fails.
     */
    @SuppressWarnings("unused")
    private Object[][] getTestData_AccessibleCoinsAndDeterminableChangeValue() {
        return new Object[][] {
                new Object[] {coins(_0_1, _0_2, _5_0),                   money(0, 10)},
                new Object[] {coins(_0_1, _0_1,       _0_5),             money(0, 20)},
                new Object[] {coins(            _0_2, _0_5),             money(0, 20)},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_5),             money(0, 20)},
                new Object[] {coins(_0_1, _0_1, _0_1,             _2_0), money(0, 30)},
                new Object[] {coins(                  _0_1, _0_2, _2_0), money(0, 30)},
                new Object[] {coins(_0_1, _0_1, _0_1, _0_1, _0_2, _2_0), money(0, 30)},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _2_0, _5_0), money(0, 40)},
                new Object[] {coins(_0_1, _0_2, _0_2, _0_5, _2_0),       money(0, 50)},
                new Object[] {coins(_0_1, _0_2, _0_2, _0_2, _0_5, _5_0), money(0, 60)},
                new Object[] {coins(_0_5, _0_5, _1_0),                   money(1, 0),},
                new Object[] {coins(_0_5, _0_5, _1_0),                   money(1, 50)},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), money(0, 40)},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), money(4, 10)},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), money(4, 20)},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), money(4, 40)}
        };
    }

    /**
     * Basic negative test set for all change determining strategies.
     */
    @SuppressWarnings("unused")
    private Object[][] getTestData_AccessibleCoinsAndIndeterminableChangeValue() {
        return new Object[][] {
                new Object[] {coins(_0_2, _1_0),                         money(0, 10)},
                new Object[] {coins(_0_2, _1_0),                         money(0, 30)},
                new Object[] {coins(_0_2, _1_0),                         money(0, 90)},
                new Object[] {coins(_0_2, _1_0),                         money(1, 10)},
                new Object[] {coins(_0_1, _0_1, _0_5, _2_0, _5_0),       money(2, 40)},
                new Object[] {coins(_0_1, _0_1, _0_5, _2_0, _5_0),       money(2, 80)},
                new Object[] {coins(_0_1, _0_1, _0_5, _2_0, _5_0),       money(2, 90)},
                new Object[] {coins(_0_1, _0_1, _0_5, _2_0, _5_0),       money(7, 80)}
        };
    }

    /**
     * Positive test set designed specifically for {@link HighestDenominationFirstStrategy}.
     */
    @SuppressWarnings("unused")
    private Object[][] getTestData_AccessibleCoinsAndDeterminableChangeValueAndExpectedChangeFromHighestDenominationFirstStrategy() {
        return new Object[][] {
                new Object[] {coins(_0_1, _0_1, _0_2, _0_5),             money(0, 20), coins(_0_2)},
                new Object[] {coins(_0_1, _0_1, _0_1, _0_1, _0_2, _2_0), money(0, 30), coins(_0_1, _0_2)},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _2_0, _5_0), money(0, 40), coins(_0_2, _0_2)},
                new Object[] {coins(_0_1, _0_2, _0_2, _0_5, _2_0),       money(0, 50), coins(_0_5)},
                new Object[] {coins(_0_1, _0_2, _0_2, _0_2, _0_5, _5_0), money(0, 60), coins(_0_1, _0_5)},
                new Object[] {coins(_0_5, _0_5, _1_0),                   money(1, 0),  coins(_1_0)},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), money(0, 40), coins(_0_2, _0_2)},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), money(4, 20), coins(_0_2, _2_0, _2_0)},
                new Object[] {coins(_0_1, _0_1, _1_0, _2_0, _2_0, _5_0), money(5, 10), coins(_0_1, _5_0)}
        };
    }
}
