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
    public void should_ReturnNewObjectWithDifference_When_Subtracted() {
        Money[][] testTuples = getTestTuplesConsistingOfMinuendSubtrahendAndDifference();

        for (Money[] testTuple : testTuples) {
            // given
            Money minuend = testTuple[0];
            Money subtrahend = testTuple[1];
            Money expectedDifference = testTuple[2];
            Money minuendBeforeSubtraction = new Money(minuend.getWholes(), minuend.getPennies());

            // when
            Money actualDifference = minuend.subtract(subtrahend);

            // then
            assertThat(actualDifference).isEqualTo(expectedDifference);
            assertThat(minuend).isEqualTo(minuendBeforeSubtraction);
        }
    }

    private Money[][] getTestTuplesConsistingOfMinuendSubtrahendAndDifference() {
        return new Money[][]{
                new Money[] {new Money(4, 34), new Money(2, 10), new Money(2, 24)},
                new Money[] {new Money(1, 30), new Money(0, 30), new Money(1, 0)},
                new Money[] {new Money(6, 27), new Money(6, 0),  new Money(0, 27)},
                new Money[] {new Money(1, 51), new Money(0, 62), new Money(0, 89)}
        };
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
