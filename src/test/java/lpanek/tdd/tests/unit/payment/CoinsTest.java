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

        assertThat(new Coins(_5_0).getValue()).isEqualTo(new Money(5, 0));
        assertThat(new Coins(_2_0).getValue()).isEqualTo(new Money(2, 0));
        assertThat(new Coins(_1_0).getValue()).isEqualTo(new Money(1, 0));
        assertThat(new Coins(_0_5).getValue()).isEqualTo(new Money(0, 50));
        assertThat(new Coins(_0_2).getValue()).isEqualTo(new Money(0, 20));
        assertThat(new Coins(_0_1).getValue()).isEqualTo(new Money(0, 10));

        assertThat(new Coins(_5_0, _2_0).getValue()).isEqualTo(new Money(7, 0));
        assertThat(new Coins(_2_0, _0_2).getValue()).isEqualTo(new Money(2, 20));
        assertThat(new Coins(_0_2, _0_1).getValue()).isEqualTo(new Money(0, 30));

        assertThat(new Coins(_2_0, _2_0, _2_0).getValue()).isEqualTo(new Money(6, 0));
        assertThat(new Coins(_5_0, _0_5, _0_1).getValue()).isEqualTo(new Money(5, 60));
        assertThat(new Coins(_0_5, _0_5, _0_2).getValue()).isEqualTo(new Money(1, 20));
    }

    @Test
    public void twoCoinsObjectsHavingTheSameCollectionOfCoinsShouldHaveEqualValues() {
        assertThat(        new Coins(_0_5, _2_0).getValue())
                .isEqualTo(new Coins(_0_5, _2_0).getValue());
        assertThat(        new Coins(_5_0, _2_0).getValue())
                .isEqualTo(new Coins(_2_0, _5_0).getValue());

        assertThat(        new Coins(_5_0, _1_0, _0_2).getValue())
                .isEqualTo(new Coins(_5_0, _1_0, _0_2).getValue());
        assertThat(        new Coins(_5_0, _1_0, _0_2).getValue())
                .isEqualTo(new Coins(_1_0, _5_0, _0_2).getValue());
        assertThat(        new Coins(_5_0, _1_0, _0_2).getValue())
                .isEqualTo(new Coins(_0_2, _1_0, _5_0).getValue());
        assertThat(        new Coins(_5_0, _1_0, _0_2).getValue())
                .isEqualTo(new Coins(_5_0, _0_2, _1_0).getValue());
    }

    @Test
    public void twoCoinsObjectsHavingTheSameCollectionOfCoinsShouldBeEqual() {
        assertThat(        new Coins())
                .isEqualTo(new Coins());

        assertThat(        new Coins(_5_0))
                .isEqualTo(new Coins(_5_0));
        assertThat(        new Coins(_2_0))
                .isEqualTo(new Coins(_2_0));
        assertThat(        new Coins(_1_0))
                .isEqualTo(new Coins(_1_0));
        assertThat(        new Coins(_0_5))
                .isEqualTo(new Coins(_0_5));
        assertThat(        new Coins(_0_2))
                .isEqualTo(new Coins(_0_2));
        assertThat(        new Coins(_0_1))
                .isEqualTo(new Coins(_0_1));

        assertThat(        new Coins(_0_5, _2_0))
                .isEqualTo(new Coins(_0_5, _2_0));
        assertThat(        new Coins(_5_0, _2_0))
                .isEqualTo(new Coins(_2_0, _5_0));

        assertThat(        new Coins(_5_0, _1_0, _0_2))
                .isEqualTo(new Coins(_5_0, _1_0, _0_2));
        assertThat(        new Coins(_5_0, _1_0, _0_2))
                .isEqualTo(new Coins(_1_0, _5_0, _0_2));
        assertThat(        new Coins(_5_0, _1_0, _0_2))
                .isEqualTo(new Coins(_0_2, _1_0, _5_0));
        assertThat(        new Coins(_5_0, _1_0, _0_2))
                .isEqualTo(new Coins(_5_0, _0_2, _1_0));
    }
}
