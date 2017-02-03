package lpanek.tdd.unitTests.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coins;

public class CoinsTest {

    @Test
    public void should_EmptyCoinsEqual() {
        // expect
        assertThat(new Coins()).isEqualTo(new Coins());
    }
}
