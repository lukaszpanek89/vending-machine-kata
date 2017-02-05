package lpanek.tdd.tests.unit.payment;

import static lpanek.tdd.tests.util.ConstructingUtil.money;
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
    @Parameters(method = "getTestData_CoinAndItsValue")
    public void should_HaveValueConsistentWithItsDenomination(Coin coin, Money coinValue) {
        assertThat(coin.getValue()).isEqualTo(coinValue);
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_CoinAndItsValue() {
        return new Object[][]{
                new Object[] {Coin._5_0, money(5, 0)},
                new Object[] {Coin._2_0, money(2, 0)},
                new Object[] {Coin._1_0, money(1, 0)},
                new Object[] {Coin._0_5, money(0, 50)},
                new Object[] {Coin._0_2, money(0, 20)},
                new Object[] {Coin._0_1, money(0, 10)}
        };
    }
}
