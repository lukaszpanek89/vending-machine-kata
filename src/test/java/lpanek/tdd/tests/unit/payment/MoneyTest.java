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
        assertThat(money.toString()).isEqualTo("Money=[wholes=2, pennies=99, currencySymbol='zł']");
    }

    @Test
    public void should_ReturnNewObjectWithSum_When_Added() {
        Money[][] testTuples = getTestTuplesConsistingOfAddendsAndSum();

        for (Money[] testTuple : testTuples) {
            // given
            Money addend1 = testTuple[0];
            Money addend2 = testTuple[1];
            Money expectedSum = testTuple[2];
            Money addend1BeforeAddition = new Money(addend1.getWholes(), addend1.getPennies());
            Money addend2BeforeAddition = new Money(addend2.getWholes(), addend2.getPennies());

            // when
            Money sumOf1And2 = addend1.plus(addend2);
            Money sumOf2And1 = addend2.plus(addend1);

            // then
            assertThat(sumOf1And2).isEqualTo(expectedSum);
            assertThat(sumOf2And1).isEqualTo(expectedSum);
            assertThat(addend1).isEqualTo(addend1BeforeAddition);
            assertThat(addend2).isEqualTo(addend2BeforeAddition);
        }
    }

    private Money[][] getTestTuplesConsistingOfAddendsAndSum() {
        return new Money[][]{
                new Money[] {new Money(0, 0),  new Money(0, 0),  new Money(0, 0)},
                new Money[] {new Money(5, 12), new Money(0, 0),  new Money(5, 12)},
                new Money[] {new Money(4, 34), new Money(2, 10), new Money(6, 44)},
                new Money[] {new Money(1, 30), new Money(0, 30), new Money(1, 60)},
                new Money[] {new Money(6, 27), new Money(6, 0),  new Money(12, 27)},
                new Money[] {new Money(1, 51), new Money(0, 62), new Money(2, 13)}
        };
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
            Money actualDifference = minuend.minus(subtrahend);

            // then
            assertThat(actualDifference).isEqualTo(expectedDifference);
            assertThat(minuend).isEqualTo(minuendBeforeSubtraction);
        }
    }

    private Money[][] getTestTuplesConsistingOfMinuendSubtrahendAndDifference() {
        return new Money[][]{
                new Money[] {new Money(0, 0),  new Money(0, 0),  new Money(0, 0)},
                new Money[] {new Money(5, 12), new Money(0, 0),  new Money(5, 12)},
                new Money[] {new Money(4, 34), new Money(2, 10), new Money(2, 24)},
                new Money[] {new Money(1, 30), new Money(0, 30), new Money(1, 0)},
                new Money[] {new Money(6, 27), new Money(6, 0),  new Money(0, 27)},
                new Money[] {new Money(1, 51), new Money(0, 62), new Money(0, 89)}
        };
    }

    @Test
    public void should_ReturnNewObjectWithProduct_When_Multiplied() {
        Object[][] testTuples = getTestTuplesConsistingOfMultiplicandMultiplierAndProduct();

        for (Object[] testTuple : testTuples) {
            // given
            Money multiplicand = (Money) testTuple[0];
            int multiplier = (int) testTuple[1];
            Money expectedProduct = (Money) testTuple[2];
            Money multiplicandBeforeMultiplication = new Money(multiplicand.getWholes(), multiplicand.getPennies());

            // when
            Money actualProduct = multiplicand.times(multiplier);

            // then
            assertThat(actualProduct).isEqualTo(expectedProduct);
            assertThat(multiplicand).isEqualTo(multiplicandBeforeMultiplication);
        }
    }

    private Object[][] getTestTuplesConsistingOfMultiplicandMultiplierAndProduct() {
        return new Object[][]{
                new Object[] {new Money(0, 0),  9, new Money(0, 0)},
                new Object[] {new Money(4, 20), 0, new Money(0, 0)},
                new Object[] {new Money(1, 32), 4, new Money(5, 28)},
                new Object[] {new Money(6, 0),  6, new Money(36, 0)},
                new Object[] {new Money(0, 72), 8, new Money(5, 76)}
        };
    }

    @Test
    public void twoMoneyObjectsWithEqualFieldsShouldBeEqual() {
        Money[][] testTuples = getTestTuplesConsistingOfMoneyObjectsHavingEqualFields();

        for (Money[] testTuple : testTuples) {
            // given
            Money money1 = testTuple[0];
            Money money2 = testTuple[1];

            // then
            assertThat(money1).isEqualTo(money2);
            assertThat(money2).isEqualTo(money1);
            assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
        }
    }

    private Money[][] getTestTuplesConsistingOfMoneyObjectsHavingEqualFields() {
        return new Money[][]{
                new Money[] {new Money(0, 0),  new Money(0, 0)},
                new Money[] {new Money(0, 20), new Money(0, 20)},
                new Money[] {new Money(5, 0),  new Money(5, 0)},
                new Money[] {new Money(6, 72), new Money(6, 72)}
        };
    }

    @Test
    public void twoMoneyObjectsWithDifferentFieldsShouldNotBeEqual() {
        Money[][] testTuples = getTestTuplesConsistingOfMoneyObjectsHavingDifferentFields();

        for (Money[] testTuple : testTuples) {
            // given
            Money money1 = testTuple[0];
            Money money2 = testTuple[1];

            // then
            assertThat(money1).isNotEqualTo(money2);
            assertThat(money2).isNotEqualTo(money1);
        }
    }

    private Money[][] getTestTuplesConsistingOfMoneyObjectsHavingDifferentFields() {
        return new Money[][]{
                new Money[] {new Money(5, 40), new Money(7, 30)},
                new Money[] {new Money(4, 20), new Money(4, 0)},
                new Money[] {new Money(5, 72), new Money(6, 72)}
        };
    }
}
