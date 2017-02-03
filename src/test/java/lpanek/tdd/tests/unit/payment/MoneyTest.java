package lpanek.tdd.tests.unit.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Money;

public class MoneyTest {

    @Test
    public void should_ContainSpecifiedWholesAndPenniesAndDefaultCurrencySymbol_When_SuccessfullyConstructed() {
        // when
        Money money = new Money(2, 99);

        // then
        assertThat(money.getWholes()).isEqualTo(2);
        assertThat(money.getPennies()).isEqualTo(99);
        assertThat(money.getCurrencySymbol()).isEqualTo("zł");
    }

    @Test
    public void twoMoneyObjectsWithEqualFieldsShouldBeEqual() {
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
