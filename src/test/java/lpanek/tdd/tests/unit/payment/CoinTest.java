package lpanek.tdd.tests.unit.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.payment.Coin;
import lpanek.tdd.payment.Money;

@RunWith(JUnitParamsRunner.class)
public class CoinTest {

    @Test
    @Parameters(method = "getTestTuplesConsistingOfCoinAndItsValue")
    public void should_HaveValueConsistentWithItsDenomination(Coin coin, Money coinValue) {
        assertThat(coin.getValue()).isEqualTo(coinValue);
    }

    @SuppressWarnings("unused")
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
