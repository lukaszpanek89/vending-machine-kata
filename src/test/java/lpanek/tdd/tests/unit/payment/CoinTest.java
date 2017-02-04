package lpanek.tdd.tests.unit.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coin;
import lpanek.tdd.payment.Money;

public class CoinTest {

    @Test
    public void coinShouldHaveValueConsistentWithItsDenomination() {
        assertThat(Coin._5_0.getValue()).isEqualTo(new Money(5, 0));
        assertThat(Coin._2_0.getValue()).isEqualTo(new Money(2, 0));
        assertThat(Coin._1_0.getValue()).isEqualTo(new Money(1, 0));
        assertThat(Coin._0_5.getValue()).isEqualTo(new Money(0, 50));
        assertThat(Coin._0_2.getValue()).isEqualTo(new Money(0, 20));
        assertThat(Coin._0_1.getValue()).isEqualTo(new Money(0, 10));
    }
}
