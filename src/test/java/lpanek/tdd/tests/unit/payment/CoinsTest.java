package lpanek.tdd.tests.unit.payment;

import static lpanek.tdd.payment.Coin.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coins;
import lpanek.tdd.payment.Money;

public class CoinsTest {

    @Test
    public void coinsObjectShouldHaveValueBeingSumOfCoinValues() {
        Object[][] testTuples = getTestTuplesConsistingOfCoinsObjectAndItsValue();

        for (Object[] testTuple : testTuples) {
            // given
            Coins coins = (Coins) testTuple[0];
            Money coinsValue = (Money) testTuple[1];

            // then
            assertThat(coins.getValue()).isEqualTo(coinsValue);
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

    @Test
    public void twoCoinsObjectsHavingTheSameCollectionOfCoinsShouldHaveEqualValues() {
        Coins[][] testTuples = getTestTuplesConsistingOfTwoEqualCoinsObjects();

        for (Coins[] testTuple : testTuples) {
            // given
            Coins coins1 = testTuple[0];
            Coins coins2 = testTuple[1];

            // then
            assertThat(coins1.getValue()).isEqualTo(coins2.getValue());
        }
    }

    @Test
    public void twoCoinsObjectsHavingTheSameCollectionOfCoinsShouldBeEqual() {
        Coins[][] testTuples = getTestTuplesConsistingOfTwoEqualCoinsObjects();

        for (Coins[] testTuple : testTuples) {
            // given
            Coins coins1 = testTuple[0];
            Coins coins2 = testTuple[1];

            // then
            assertThat(coins1).isEqualTo(coins2);
        }
    }

    @Test
    public void twoCoinsObjectsBeingEqualShouldHaveEqualValues() {
        Coins[][] testTuples = getTestTuplesConsistingOfTwoEqualCoinsObjects();

        for (Coins[] testTuple : testTuples) {
            // given
            Coins coins1 = testTuple[0];
            Coins coins2 = testTuple[1];
            assertThat(coins1).isEqualTo(coins2);

            // then
            assertThat(coins1.getValue()).isEqualTo(coins2.getValue());
        }
    }

    private Coins[][] getTestTuplesConsistingOfTwoEqualCoinsObjects() {
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
}
