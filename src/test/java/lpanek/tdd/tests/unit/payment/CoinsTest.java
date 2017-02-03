package lpanek.tdd.tests.unit.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coins;

public class CoinsTest {

    @Test
    public void should_EmptyCoinsBeEqual() {
        // expect
        assertThat(new Coins()).isEqualTo(new Coins());
    }
}
