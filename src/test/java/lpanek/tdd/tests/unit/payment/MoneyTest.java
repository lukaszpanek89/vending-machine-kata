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
    public void should_ContainDifference_When_Subtracted() {
        assertThat(new Money(4, 34).subtract(new Money(2, 10))).isEqualTo(new Money(2, 24));
        assertThat(new Money(1, 30).subtract(new Money(0, 30))).isEqualTo(new Money(1, 0));
        assertThat(new Money(6, 27).subtract(new Money(6, 0))).isEqualTo(new Money(0, 27));
        assertThat(new Money(1, 51).subtract(new Money(0, 62))).isEqualTo(new Money(0, 89));
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
