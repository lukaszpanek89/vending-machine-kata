package lpanek.tdd.tests.unit.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Money;

public class MoneyTest {

    @Test
    public void twoMoneyObjectsWithEqualValuesShouldBeEqual() {
        assertThat(new Money(5, 60)).isEqualTo(new Money(5, 60));
    }

    @Test
    public void twoMoneyObjectsWithDifferentWholesShouldNotBeEqual() {
        assertThat(new Money(1, 60)).isNotEqualTo(new Money(5, 60));
    }

    @Test
    public void twoMoneyObjectsWithDifferentPenniesShouldNotBeEqual() {
        assertThat(new Money(5, 20)).isNotEqualTo(new Money(5, 60));
    }

    @Test
    public void twoMoneyObjectsWithDifferentWholesAndPenniesShouldNotBeEqual() {
        assertThat(new Money(1, 20)).isNotEqualTo(new Money(5, 60));
    }
}
