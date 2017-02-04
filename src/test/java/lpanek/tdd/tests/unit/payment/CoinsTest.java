package lpanek.tdd.tests.unit.payment;

import static lpanek.tdd.payment.Coin.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coins;
import lpanek.tdd.payment.Money;

public class CoinsTest {

    @Test
    public void coinsObjectShouldHaveValueBeingSumOfCoinValues() {
        assertThat(new Coins().getValue()).isEqualTo(new Money(0, 0));

        assertThat(new Coins(DENOMINATION_5_0).getValue()).isEqualTo(new Money(5, 0));
        assertThat(new Coins(DENOMINATION_2_0).getValue()).isEqualTo(new Money(2, 0));
        assertThat(new Coins(DENOMINATION_1_0).getValue()).isEqualTo(new Money(1, 0));
        assertThat(new Coins(DENOMINATION_0_5).getValue()).isEqualTo(new Money(0, 50));
        assertThat(new Coins(DENOMINATION_0_2).getValue()).isEqualTo(new Money(0, 20));
        assertThat(new Coins(DENOMINATION_0_1).getValue()).isEqualTo(new Money(0, 10));

        assertThat(new Coins(DENOMINATION_5_0, DENOMINATION_2_0).getValue()).isEqualTo(new Money(7, 0));
        assertThat(new Coins(DENOMINATION_2_0, DENOMINATION_0_2).getValue()).isEqualTo(new Money(2, 20));
        assertThat(new Coins(DENOMINATION_0_2, DENOMINATION_0_1).getValue()).isEqualTo(new Money(0, 30));

        assertThat(new Coins(DENOMINATION_2_0, DENOMINATION_2_0, DENOMINATION_2_0).getValue()).isEqualTo(new Money(6, 0));
        assertThat(new Coins(DENOMINATION_5_0, DENOMINATION_0_5, DENOMINATION_0_1).getValue()).isEqualTo(new Money(5, 60));
        assertThat(new Coins(DENOMINATION_0_5, DENOMINATION_0_5, DENOMINATION_0_2).getValue()).isEqualTo(new Money(1, 20));
    }

    @Test
    public void twoCoinsObjectsHavingTheSameCollectionOfCoinsShouldHaveEqualValues() {
        assertThat(        new Coins(DENOMINATION_0_5, DENOMINATION_2_0).getValue())
                .isEqualTo(new Coins(DENOMINATION_0_5, DENOMINATION_2_0).getValue());
        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_2_0).getValue())
                .isEqualTo(new Coins(DENOMINATION_2_0, DENOMINATION_5_0).getValue());

        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2).getValue())
                .isEqualTo(new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2).getValue());
        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2).getValue())
                .isEqualTo(new Coins(DENOMINATION_1_0, DENOMINATION_5_0, DENOMINATION_0_2).getValue());
        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2).getValue())
                .isEqualTo(new Coins(DENOMINATION_0_2, DENOMINATION_1_0, DENOMINATION_5_0).getValue());
        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2).getValue())
                .isEqualTo(new Coins(DENOMINATION_5_0, DENOMINATION_0_2, DENOMINATION_1_0).getValue());
    }

    @Test
    public void twoCoinsObjectsHavingTheSameCollectionOfCoinsShouldBeEqual() {
        assertThat(        new Coins())
                .isEqualTo(new Coins());

        assertThat(        new Coins(DENOMINATION_5_0))
                .isEqualTo(new Coins(DENOMINATION_5_0));
        assertThat(        new Coins(DENOMINATION_2_0))
                .isEqualTo(new Coins(DENOMINATION_2_0));
        assertThat(        new Coins(DENOMINATION_1_0))
                .isEqualTo(new Coins(DENOMINATION_1_0));
        assertThat(        new Coins(DENOMINATION_0_5))
                .isEqualTo(new Coins(DENOMINATION_0_5));
        assertThat(        new Coins(DENOMINATION_0_2))
                .isEqualTo(new Coins(DENOMINATION_0_2));
        assertThat(        new Coins(DENOMINATION_0_1))
                .isEqualTo(new Coins(DENOMINATION_0_1));

        assertThat(        new Coins(DENOMINATION_0_5, DENOMINATION_2_0))
                .isEqualTo(new Coins(DENOMINATION_0_5, DENOMINATION_2_0));
        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_2_0))
                .isEqualTo(new Coins(DENOMINATION_2_0, DENOMINATION_5_0));

        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2))
                .isEqualTo(new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2));
        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2))
                .isEqualTo(new Coins(DENOMINATION_1_0, DENOMINATION_5_0, DENOMINATION_0_2));
        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2))
                .isEqualTo(new Coins(DENOMINATION_0_2, DENOMINATION_1_0, DENOMINATION_5_0));
        assertThat(        new Coins(DENOMINATION_5_0, DENOMINATION_1_0, DENOMINATION_0_2))
                .isEqualTo(new Coins(DENOMINATION_5_0, DENOMINATION_0_2, DENOMINATION_1_0));
    }
}
