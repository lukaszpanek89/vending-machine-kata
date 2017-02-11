package lpanek.tdd.tests.unit.vendingMachine.domain.payment;

import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.money;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.domain.payment.Coin;
import lpanek.tdd.vendingMachine.domain.payment.Money;

@RunWith(JUnitParamsRunner.class)
public class CoinTest {

    @Test
    @Parameters(method = "getTestData_CoinAndItsValue")
    public void should_HaveValueConsistentWithItsDenomination(Coin coin, Money coinValue) {
        assertThat(coin.getValue()).isEqualTo(coinValue);
    }

    @Test
    public void should_ReturnCoinsFromHighestToLowest_When_AskedFor() {
        // when
        Coin[] coinsInOrder = Coin.valuesFromHighestToLowest();

        // then
        assertThat(coinsInOrder.length).isEqualTo(Coin.values().length);
        for (int i = 0; i < coinsInOrder.length - 1; ++i) {
            Money currentCoinValue = coinsInOrder[i].getValue();
            Money nextCoinValue = coinsInOrder[i + 1].getValue();
            assertThat(currentCoinValue.isGreaterThan(nextCoinValue));
        }
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_CoinAndItsValue() {
        return new Object[][] {
                new Object[] {Coin._5_0, money(5, 0)},
                new Object[] {Coin._2_0, money(2, 0)},
                new Object[] {Coin._1_0, money(1, 0)},
                new Object[] {Coin._0_5, money(0, 50)},
                new Object[] {Coin._0_2, money(0, 20)},
                new Object[] {Coin._0_1, money(0, 10)}
        };
    }
}
