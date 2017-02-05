package lpanek.tdd.tests.unit.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coin;
import lpanek.tdd.payment.Money;

public class CoinTest {

    @Test
    public void should_HaveValueConsistentWithItsDenomination() {
        Object[][] testTuples = getTestTuplesConsistingOfCoinAndItsValue();

        for (Object[] testTuple : testTuples) {
            // given
            Coin coin = (Coin) testTuple[0];
            Money coinValue = (Money) testTuple[1];

            // then
            assertThat(coin.getValue()).isEqualTo(coinValue);
        }
    }

    private Object[][] getTestTuplesConsistingOfCoinAndItsValue() {
        return new Object[][]{
                new Object[] {Coin._5_0, new Money(5, 0)},
                new Object[] {Coin._2_0, new Money(2, 0)},
                new Object[] {Coin._1_0, new Money(1, 0)},
                new Object[] {Coin._0_5, new Money(0, 50)},
                new Object[] {Coin._0_2, new Money(0, 20)},
                new Object[] {Coin._0_1, new Money(0, 10)}
        };
    }
}
