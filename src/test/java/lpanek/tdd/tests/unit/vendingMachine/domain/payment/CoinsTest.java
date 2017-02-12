package lpanek.tdd.tests.unit.vendingMachine.domain.payment;

import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.money;
import static lpanek.tdd.vendingMachine.domain.payment.Coin.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.domain.payment.*;
import lpanek.tdd.vendingMachine.domain.payment.ex.CoinsException;

@RunWith(JUnitParamsRunner.class)
public class CoinsTest {

    @Test
    @Parameters(method = "getTestData_Coins")
    public void should_ContainSpecifiedCoins_When_Constructed(Coin[] coinsAsArray) {
        // given
        int[] coinOrdinalToCount = new int[Coin.values().length];
        for (Coin coin : coinsAsArray) {
            ++coinOrdinalToCount[coin.ordinal()];
        }

        // when
        Coins coins = new Coins(coinsAsArray);

        // then
        for (Coin coin : Coin.values()) {
            assertThat(coins.getCoinCount(coin)).isEqualTo(coinOrdinalToCount[coin.ordinal()]);
        }
    }

    @Test
    @Parameters(method = "getTestData_CoinsObjectAndItsValue")
    public void should_HaveValueBeingSumOfCoinValues(Coins coins, Money coinsValue) {
        assertThat(coins.getValue()).isEqualTo(coinsValue);
    }

    @Test
    @Parameters(method = "getTestData_Coins")
    public void should_CorrectlyExamineIsNotEmpty(Coin[] coinsAsArray) {
        // given
        Coins coins = new Coins(coinsAsArray);

        // then
        if (coinsAsArray.length > 0) {
            assertThat(coins.isNotEmpty()).isTrue();
        } else {
            assertThat(coins.isNotEmpty()).isFalse();
        }
    }

    @Test
    @Parameters(method = "getTestData_TwoCoinsObjectsHavingTheSameCollectionsOfCoins")
    public void should_TwoCoinsObjectsHaveEqualValues_When_HavingTheSameCollectionsOfCoins(Coins coins1, Coins coins2) {
        assertThat(coins1.getValue()).isNotNull();
        assertThat(coins1.getValue()).isEqualTo(coins2.getValue());
    }

    @Test
    @Parameters(method = "getTestData_TwoCoinsObjectsHavingTheSameCollectionsOfCoins")
    public void should_TwoCoinsObjectsBeEqual_When_HavingTheSameCollectionsOfCoins(Coins coins1, Coins coins2) {
        assertThat(coins1).isEqualTo(coins2);
        assertThat(coins2).isEqualTo(coins1);
        assertThat(coins1.hashCode()).isEqualTo(coins2.hashCode());
    }

    @Test
    @Parameters(method = "getTestData_TwoCoinsObjectsHavingDifferentCollectionsOfCoins")
    public void should_TwoCoinsObjectsNotBeEqual_When_HavingDifferentCollectionsOfCoins(Coins coins1, Coins coins2) {
        assertThat(coins1).isNotEqualTo(coins2);
        assertThat(coins2).isNotEqualTo(coins1);
    }

    @Test
    @Parameters(method = "getTestData_TwoCoinsObjectsHavingTheSameCollectionsOfCoins")
    public void should_TwoCoinsObjectsHaveEqualValues_When_BeingEqual(Coins coins1, Coins coins2) {
        // given
        assertThat(coins1).isEqualTo(coins2);

        // then
        assertThat(coins1.getValue()).isNotNull();
        assertThat(coins1.getValue()).isEqualTo(coins2.getValue());
    }

    @Test
    @Parameters(method = "getTestData_AddendsAndSum")
    public void should_ReturnNewCoinsObjectWithSum_When_CoinsAdded(Coin[] addend1AsArray, Coin[] addend2AsArray, Coins expectedSum) {
        // given
        Coins addend1 = new Coins(addend1AsArray);
        Coins addend2 = new Coins(addend2AsArray);
        Coins addend1BeforeAddition = new Coins(addend1AsArray);
        Coins addend2BeforeAddition = new Coins(addend2AsArray);

        // when
        Coins sumOf1And2 = addend1.plus(addend2AsArray);
        Coins sumOf2And1 = addend2.plus(addend1AsArray);

        // then
        assertThat(sumOf1And2).isEqualTo(expectedSum);
        assertThat(sumOf2And1).isEqualTo(expectedSum);
        assertThat(addend1).isEqualTo(addend1BeforeAddition);
        assertThat(addend2).isEqualTo(addend2BeforeAddition);
    }

    @Test
    @Parameters(method = "getTestData_MinuendAndSingleCoinSubtrahendAndDifference")
    public void should_ReturnNewCoinsObjectWithDifference_When_SingleCoinSubtracted(Coin[] minuendAsArray, Coin subtrahend, Coins expectedDifference) {
        // given
        Coins minuend = new Coins(minuendAsArray);
        Coins minuendBeforeSubtraction = new Coins(minuendAsArray);

        // when
        Coins actualDifference = minuend.minus(subtrahend);

        // then
        assertThat(actualDifference).isEqualTo(expectedDifference);
        assertThat(minuend).isEqualTo(minuendBeforeSubtraction);
    }

    @Test
    @Parameters(method = "getTestData_MinuendAndNotPresentSingleCoinSubtrahend")
    public void should_ThrowException_When_TriesToSubtractSingleCoinNotPresentInCoinsObject(Coins minuend, Coin subtrahend) {
        // when
        Throwable caughtThrowable = catchThrowable(() -> minuend.minus(subtrahend));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(CoinsException.class)
                .hasMessage("Cannot remove not present coin.");
    }

    @Test
    @Parameters(method = "getTestData_MinuendAndCoinsSubtrahendAndDifference")
    public void should_ReturnNewCoinsObjectWithDifference_When_CoinsSubtracted(Coin[] minuendAsArray, Coins subtrahend, Coins expectedDifference) {
        // given
        Coins minuend = new Coins(minuendAsArray);
        Coins minuendBeforeSubtraction = new Coins(minuendAsArray);

        // when
        Coins actualDifference = minuend.minus(subtrahend);

        // then
        assertThat(actualDifference).isEqualTo(expectedDifference);
        assertThat(minuend).isEqualTo(minuendBeforeSubtraction);
    }

    @Test
    @Parameters(method = "getTestData_MinuendAndNotPresentCoinsSubtrahend")
    public void should_ThrowException_When_TriesToSubtractCoinsNotPresentInCoinsObject(Coins minuend, Coins subtrahend) {
        // when
        Throwable caughtThrowable = catchThrowable(() -> minuend.minus(subtrahend));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(CoinsException.class)
                .hasMessage("Cannot remove not present coin.");
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_Coins() {
        return new Object[][] {
                new Object[] {new Coin[] {}},
                new Object[] {new Coin[] {_0_1}},
                new Object[] {new Coin[] {_0_2}},
                new Object[] {new Coin[] {_0_5}},
                new Object[] {new Coin[] {_1_0}},
                new Object[] {new Coin[] {_2_0}},
                new Object[] {new Coin[] {_5_0}},
                new Object[] {new Coin[] {_0_1, _0_2, _0_2, _0_5, _0_5, _0_5}},
                new Object[] {new Coin[] {_0_2, _0_1, _0_2, _0_5, _0_5, _0_5}},
                new Object[] {new Coin[] {_0_2, _0_2, _0_1, _0_5, _0_5, _0_5}},
                new Object[] {new Coin[] {_0_5, _0_2, _0_2, _0_1, _0_5, _0_5}},
                new Object[] {new Coin[] {_0_5, _0_2, _0_2, _0_5, _0_1, _0_5}},
                new Object[] {new Coin[] {_0_5, _0_2, _0_2, _0_5, _0_5, _0_1}},
                new Object[] {new Coin[] {_0_5, _0_5, _0_5, _0_2, _0_2, _0_1}},
                new Object[] {new Coin[] {_0_5, _0_5, _0_5, _0_1, _0_2, _0_2}},
                new Object[] {new Coin[] {_0_5, _0_2, _0_5, _0_2, _0_5, _0_1}}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_CoinsObjectAndItsValue() {
        return new Object[][] {
                new Object[] {new Coins(),                 money(0, 0)},
                new Object[] {new Coins(_5_0),             money(5, 0)},
                new Object[] {new Coins(_2_0),             money(2, 0)},
                new Object[] {new Coins(_1_0),             money(1, 0)},
                new Object[] {new Coins(_0_5),             money(0, 50)},
                new Object[] {new Coins(_0_2),             money(0, 20)},
                new Object[] {new Coins(_0_1),             money(0, 10)},
                new Object[] {new Coins(_5_0, _2_0),       money(7, 0)},
                new Object[] {new Coins(_2_0, _0_2),       money(2, 20)},
                new Object[] {new Coins(_0_2, _0_1),       money(0, 30)},
                new Object[] {new Coins(_2_0, _2_0, _2_0), money(6, 0)},
                new Object[] {new Coins(_5_0, _0_5, _0_1), money(5, 60)},
                new Object[] {new Coins(_0_5, _0_5, _0_2), money(1, 20)}
        };
    }

    @SuppressWarnings("unused")
    private Coins[][] getTestData_TwoCoinsObjectsHavingTheSameCollectionsOfCoins() {
        return new Coins[][] {
                new Coins[] {new Coins(),                 new Coins()},
                new Coins[] {new Coins(_5_0),             new Coins(_5_0)},
                new Coins[] {new Coins(_2_0),             new Coins(_2_0)},
                new Coins[] {new Coins(_1_0),             new Coins(_1_0)},
                new Coins[] {new Coins(_0_5),             new Coins(_0_5)},
                new Coins[] {new Coins(_0_2),             new Coins(_0_2)},
                new Coins[] {new Coins(_0_1),             new Coins(_0_1)},
                new Coins[] {new Coins(_0_5, _2_0),       new Coins(_0_5, _2_0)},
                new Coins[] {new Coins(_0_5, _2_0),       new Coins(_2_0, _0_5)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_5_0, _1_0, _0_2)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_1_0, _5_0, _0_2)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_0_2, _1_0, _5_0)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_5_0, _0_2, _1_0)}
        };
    }

    @SuppressWarnings("unused")
    private Coins[][] getTestData_TwoCoinsObjectsHavingDifferentCollectionsOfCoins() {
        return new Coins[][] {
                new Coins[] {new Coins(),                 new Coins(_5_0)},
                new Coins[] {new Coins(),                 new Coins(_5_0, _2_0)},
                new Coins[] {new Coins(),                 new Coins(_5_0, _2_0, _0_1)},
                new Coins[] {new Coins(_1_0),             new Coins(_2_0)},
                new Coins[] {new Coins(_1_0),             new Coins(_2_0, _0_2)},
                new Coins[] {new Coins(_1_0),             new Coins(_2_0, _0_2, _0_5)},
                new Coins[] {new Coins(_0_5, _2_0),       new Coins(_0_5, _1_0)},
                new Coins[] {new Coins(_0_5, _2_0),       new Coins(_0_5, _1_0, _0_1)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_2_0, _1_0, _0_2)}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_AddendsAndSum() {
        return new Object[][] {
                new Object[] {new Coin[] {},           new Coin[] {_0_2},             new Coins(_0_2)},
                new Object[] {new Coin[] {},           new Coin[] {_0_2, _5_0},       new Coins(_0_2, _5_0)},
                new Object[] {new Coin[] {},           new Coin[] {_0_2, _5_0, _1_0}, new Coins(_0_2, _5_0, _1_0)},
                new Object[] {new Coin[] {},           new Coin[] {_0_2, _5_0, _5_0}, new Coins(_0_2, _5_0, _5_0)},
                new Object[] {new Coin[] {_5_0},       new Coin[] {_0_2},             new Coins(_5_0, _0_2)},
                new Object[] {new Coin[] {_5_0},       new Coin[] {_0_2, _1_0},       new Coins(_5_0, _0_2, _1_0)},
                new Object[] {new Coin[] {_5_0},       new Coin[] {_0_2, _1_0, _0_1}, new Coins(_5_0, _0_2, _1_0, _0_1)},
                new Object[] {new Coin[] {_5_0},       new Coin[] {_0_2, _1_0, _1_0}, new Coins(_5_0, _0_2, _1_0, _1_0)},
                new Object[] {new Coin[] {_2_0, _0_5}, new Coin[] {_1_0},             new Coins(_2_0, _0_5, _1_0)},
                new Object[] {new Coin[] {_2_0, _0_5}, new Coin[] {_1_0, _5_0},       new Coins(_2_0, _0_5, _1_0, _5_0)},
                new Object[] {new Coin[] {_2_0, _0_5}, new Coin[] {_1_0, _5_0, _0_2}, new Coins(_2_0, _0_5, _1_0, _5_0, _0_2)},
                new Object[] {new Coin[] {_2_0, _0_5}, new Coin[] {_1_0, _5_0, _5_0}, new Coins(_2_0, _0_5, _1_0, _5_0, _5_0)},
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_MinuendAndSingleCoinSubtrahendAndDifference() {
        return new Object[][] {
                new Object[] {new Coin[] {_0_2},             _0_2, new Coins()},
                new Object[] {new Coin[] {_0_2, _5_0},       _0_2, new Coins(_5_0)},
                new Object[] {new Coin[] {_0_2, _5_0, _1_0}, _0_2, new Coins(_5_0, _1_0)},
                new Object[] {new Coin[] {_0_2, _5_0, _5_0}, _0_2, new Coins(_5_0, _5_0)},
                new Object[] {new Coin[] {_0_2, _5_0},       _5_0, new Coins(_0_2)},
                new Object[] {new Coin[] {_0_2, _5_0, _1_0}, _5_0, new Coins(_0_2, _1_0)},
                new Object[] {new Coin[] {_0_2, _5_0, _5_0}, _5_0, new Coins(_0_2, _5_0)},
                new Object[] {new Coin[] {_0_2, _5_0, _1_0}, _1_0, new Coins(_0_2, _5_0)}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_MinuendAndNotPresentSingleCoinSubtrahend() {
        return new Object[][] {
                new Object[] {new Coins(),                 _0_2},
                new Object[] {new Coins(_0_2),             _0_1},
                new Object[] {new Coins(_0_2),             _0_5},
                new Object[] {new Coins(_0_2),             _1_0},
                new Object[] {new Coins(_0_2),             _2_0},
                new Object[] {new Coins(_0_2),             _5_0},
                new Object[] {new Coins(_0_2, _5_0),       _0_1},
                new Object[] {new Coins(_0_2, _5_0, _1_0), _0_1},
                new Object[] {new Coins(_0_2, _5_0, _5_0), _0_1}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_MinuendAndCoinsSubtrahendAndDifference() {
        return new Object[][] {
                new Object[] {new Coin[] {_0_2},                   new Coins(_0_2),              new Coins()},
                new Object[] {new Coin[] {_0_2, _5_0},             new Coins(_0_2),              new Coins(_5_0)},
                new Object[] {new Coin[] {_0_2, _5_0, _1_0},       new Coins(_0_2),              new Coins(_5_0, _1_0)},
                new Object[] {new Coin[] {_0_2, _5_0},             new Coins(_0_2, _5_0),        new Coins()},
                new Object[] {new Coin[] {_0_2, _5_0, _1_0},       new Coins(_0_2, _5_0),        new Coins(_1_0)},
                new Object[] {new Coin[] {_0_2, _5_0, _1_0},       new Coins(_0_2, _5_0, _1_0),  new Coins()},
                new Object[] {new Coin[] {_0_2, _5_0, _1_0, _1_0}, new Coins(_0_2, _5_0, _1_0),  new Coins(_1_0)}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_MinuendAndNotPresentCoinsSubtrahend() {
        return new Object[][] {
                new Object[] {new Coins(),                 new Coins(_0_2)},
                new Object[] {new Coins(_0_2),             new Coins(_0_1)},
                new Object[] {new Coins(_0_2),             new Coins(_0_5)},
                new Object[] {new Coins(_0_2),             new Coins(_1_0)},
                new Object[] {new Coins(_0_2),             new Coins(_2_0)},
                new Object[] {new Coins(_0_2),             new Coins(_5_0)},
                new Object[] {new Coins(_0_2, _5_0),       new Coins(_0_1)},
                new Object[] {new Coins(_0_2, _5_0, _1_0), new Coins(_0_1)},
                new Object[] {new Coins(_0_2, _5_0, _5_0), new Coins(_0_1)},
                new Object[] {new Coins(_0_2),             new Coins(_0_2, _0_1)},
                new Object[] {new Coins(_0_2, _5_0),       new Coins(_0_2, _5_0, _0_1)},
                new Object[] {new Coins(_0_2, _5_0, _5_0), new Coins(_0_2, _5_0, _0_1)}
        };
    }
}
