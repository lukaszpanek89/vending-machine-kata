package lpanek.tdd.tests.unit.payment;

import static lpanek.tdd.payment.Coin.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.*;

public class CoinsTest {

    @Test
    public void should_CoinsObjectHaveValueBeingSumOfCoinValues() {
        Object[][] testTuples = getTestTuplesConsistingOfCoinsObjectAndItsValue();

        for (Object[] testTuple : testTuples) {
            // given
            Coins coins = (Coins) testTuple[0];
            Money coinsValue = (Money) testTuple[1];

            // then
            assertThat(coins.getValue()).isEqualTo(coinsValue);
        }
    }

    @Test
    public void should_TwoObjectsHaveEqualValues_When_HavingTheSameCollectionsOfCoins() {
        Coins[][] testTuples = getTestTuplesConsistingOfTwoCoinsObjectsHavingTheSameCollectionsOfCoins();

        for (Coins[] testTuple : testTuples) {
            // given
            Coins coins1 = testTuple[0];
            Coins coins2 = testTuple[1];

            // then
            assertThat(coins1.getValue()).isEqualTo(coins2.getValue());
        }
    }

    @Test
    public void should_TwoObjectsBeEqual_When_HavingTheSameCollectionsOfCoins() {
        Coins[][] testTuples = getTestTuplesConsistingOfTwoCoinsObjectsHavingTheSameCollectionsOfCoins();

        for (Coins[] testTuple : testTuples) {
            // given
            Coins coins1 = testTuple[0];
            Coins coins2 = testTuple[1];

            // then
            assertThat(coins1).isEqualTo(coins2);
            assertThat(coins2).isEqualTo(coins1);
            assertThat(coins1.hashCode()).isEqualTo(coins2.hashCode());
        }
    }

    @Test
    public void should_TwoObjectsNotBeEqual_When_HavingDifferentCollectionsOfCoins() {
        Coins[][] testTuples = getTestTuplesConsistingOfTwoCoinsObjectsHavingDifferentCollectionsOfCoins();

        for (Coins[] testTuple : testTuples) {
            // given
            Coins coins1 = testTuple[0];
            Coins coins2 = testTuple[1];

            // then
            assertThat(coins1).isNotEqualTo(coins2);
            assertThat(coins2).isNotEqualTo(coins1);
        }
    }

    @Test
    public void should_TwoObjectsHaveEqualValues_When_BeingEqual() {
        Coins[][] testTuples = getTestTuplesConsistingOfTwoCoinsObjectsHavingTheSameCollectionsOfCoins();

        for (Coins[] testTuple : testTuples) {
            // given
            Coins coins1 = testTuple[0];
            Coins coins2 = testTuple[1];
            assertThat(coins1).isEqualTo(coins2);

            // then
            assertThat(coins1.getValue()).isEqualTo(coins2.getValue());
        }
    }

    @Test
    public void should_ReturnNewCoinsObjectWithSum_When_CoinsAdded() {
        Object[][] testTuples = getTestTuplesConsistingOfAddendsAndSum();

        for (Object[] testTuple : testTuples) {
            // given
            Coin[] addend1AsArray = (Coin[]) testTuple[0];
            Coin[] addend2AsArray = (Coin[]) testTuple[1];
            Coins expectedSum = (Coins) testTuple[2];
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
    }

    private Object[][] getTestTuplesConsistingOfCoinsObjectAndItsValue() {
        return new Object[][]{
                new Object[] {new Coins(), new Money(0, 0)},
                new Object[] {new Coins(_5_0), new Money(5, 0)},
                new Object[] {new Coins(_2_0), new Money(2, 0)},
                new Object[] {new Coins(_1_0), new Money(1, 0)},
                new Object[] {new Coins(_0_5), new Money(0, 50)},
                new Object[] {new Coins(_0_2), new Money(0, 20)},
                new Object[] {new Coins(_0_1), new Money(0, 10)},
                new Object[] {new Coins(_5_0, _2_0), new Money(7, 0)},
                new Object[] {new Coins(_2_0, _0_2), new Money(2, 20)},
                new Object[] {new Coins(_0_2, _0_1), new Money(0, 30)},
                new Object[] {new Coins(_2_0, _2_0, _2_0), new Money(6, 0)},
                new Object[] {new Coins(_5_0, _0_5, _0_1), new Money(5, 60)},
                new Object[] {new Coins(_0_5, _0_5, _0_2), new Money(1, 20)}
        };
    }

    private Coins[][] getTestTuplesConsistingOfTwoCoinsObjectsHavingTheSameCollectionsOfCoins() {
        return new Coins[][]{
                new Coins[] {new Coins(), new Coins()},
                new Coins[] {new Coins(_5_0), new Coins(_5_0)},
                new Coins[] {new Coins(_2_0), new Coins(_2_0)},
                new Coins[] {new Coins(_1_0), new Coins(_1_0)},
                new Coins[] {new Coins(_0_5), new Coins(_0_5)},
                new Coins[] {new Coins(_0_2), new Coins(_0_2)},
                new Coins[] {new Coins(_0_1), new Coins(_0_1)},
                new Coins[] {new Coins(_0_5, _2_0), new Coins(_0_5, _2_0)},
                new Coins[] {new Coins(_0_5, _2_0), new Coins(_2_0, _0_5)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_5_0, _1_0, _0_2)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_1_0, _5_0, _0_2)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_0_2, _1_0, _5_0)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_5_0, _0_2, _1_0)}
        };
    }

    private Coins[][] getTestTuplesConsistingOfTwoCoinsObjectsHavingDifferentCollectionsOfCoins() {
        return new Coins[][]{
                new Coins[] {new Coins(), new Coins(_5_0)},
                new Coins[] {new Coins(), new Coins(_5_0, _2_0)},
                new Coins[] {new Coins(), new Coins(_5_0, _2_0, _0_1)},
                new Coins[] {new Coins(_1_0), new Coins(_2_0)},
                new Coins[] {new Coins(_1_0), new Coins(_2_0, _0_2)},
                new Coins[] {new Coins(_1_0), new Coins(_2_0, _0_2, _0_5)},
                new Coins[] {new Coins(_0_5, _2_0), new Coins(_0_5, _1_0)},
                new Coins[] {new Coins(_0_5, _2_0), new Coins(_0_5, _1_0, _0_1)},
                new Coins[] {new Coins(_5_0, _1_0, _0_2), new Coins(_2_0, _1_0, _0_2)}
        };
    }

    private Object[][] getTestTuplesConsistingOfAddendsAndSum() {
        return new Object[][]{
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
}
