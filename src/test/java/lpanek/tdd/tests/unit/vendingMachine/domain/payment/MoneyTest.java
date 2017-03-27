package lpanek.tdd.tests.unit.vendingMachine.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.domain.payment.Money;
import lpanek.tdd.vendingMachine.domain.payment.ex.MoneyException;

@RunWith(JUnitParamsRunner.class)
public class MoneyTest {

    @Test
    @Parameters(method = "getTestData_WholesPenniesAndCurrencySymbol")
    public void should_HaveSpecifiedWholesPenniesAndDefaultCurrencySymbol_When_Constructed(int wholes, int pennies, String currencySymbol) {
        // when
        Money money = new Money(wholes, pennies);

        // then
        assertThat(money.getWholes()).isEqualTo(wholes);
        assertThat(money.getPennies()).isEqualTo(pennies);
        assertThat(money.getCurrencySymbol()).isEqualTo(currencySymbol);
    }

    @Test
    @Parameters(method = "getTestData_InvalidWholesPenniesAndExceptionMessage")
    public void should_ThrowException_When_TriesToConstructMoneyWithInvalidWholesOrPennies(int wholes, int pennies, String exceptionMessage) {
        // when
        Throwable caughtThrowable = catchThrowable(() -> new Money(wholes, pennies));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(MoneyException.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    @Parameters(method = "getTestData_MoneyObjectsHavingEqualFields")
    public void should_TwoMoneyObjectsBeEqual_When_HavingEqualFields(Money money1, Money money2) {
        assertThat(money1).isEqualTo(money2);
        assertThat(money2).isEqualTo(money1);
        assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
    }

    @Test
    @Parameters(method = "getTestData_MoneyObjectsHavingDifferentFields")
    public void should_TwoMoneyObjectsNotBeEqual_When_HavingDifferentFields(Money money1, Money money2) {
        assertThat(money1).isNotEqualTo(money2);
        assertThat(money2).isNotEqualTo(money1);
    }

    @Test
    @Parameters(method = "getTestData_AddendsAndSum")
    public void should_ReturnNewMoneyObjectWithSum_When_MoneyAdded(Money addend1, Money addend2, Money expectedSum) {
        // given
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

    @Test
    @Parameters(method = "getTestData_MinuendSubtrahendAndDifference")
    public void should_ReturnNewMoneyObjectWithDifference_When_MoneySubtracted(Money minuend, Money subtrahend, Money expectedDifference) {
        // given
        Money minuendBeforeSubtraction = new Money(minuend.getWholes(), minuend.getPennies());

        // when
        Money actualDifference = minuend.minus(subtrahend);

        // then
        assertThat(actualDifference).isEqualTo(expectedDifference);
        assertThat(minuend).isEqualTo(minuendBeforeSubtraction);
    }

    @Test
    @Parameters(method = "getTestData_LesserMinuendAndGreaterSubtrahend")
    public void should_ThrowException_When_TriesToSubtractGreaterMoneyFromLesserMoney(Money lesserMinuend, Money greaterSubtrahend) {
        // when
        Throwable caughtThrowable = catchThrowable(() -> lesserMinuend.minus(greaterSubtrahend));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(MoneyException.class)
                .hasMessage("Cannot subtract greater money from lesser money.");
    }

    @Test
    @Parameters(method = "getTestData_MultiplicandMultiplierAndProduct")
    public void should_ReturnNewMoneyObjectWithProduct_When_Multiplied(Money multiplicand, int multiplier, Money expectedProduct) {
        // given
        Money multiplicandBeforeMultiplication = new Money(multiplicand.getWholes(), multiplicand.getPennies());

        // when
        Money actualProduct = multiplicand.times(multiplier);

        // then
        assertThat(actualProduct).isEqualTo(expectedProduct);
        assertThat(multiplicand).isEqualTo(multiplicandBeforeMultiplication);
    }

    @Test
    @Parameters(method = "getTestData_MultiplicandAndNegativeMultiplier")
    public void should_ThrowException_When_TriesToMultiplyByNegativeNumber(Money multiplicand, int negativeMultiplier) {
        // when
        Throwable caughtThrowable = catchThrowable(() -> multiplicand.times(negativeMultiplier));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(MoneyException.class)
                .hasMessage("Cannot multiply by negative number.");
    }

    @Test
    @Parameters(method = "getTestData_MoneyObjectsHavingEqualFields")
    public void should_CorrectlyCompareTwoMoneyObjects_When_AreEqual(Money money1, Money money2) {
        assertThat(money1.isGreaterThan(money2)).isFalse();
        assertThat(money2.isGreaterThan(money1)).isFalse();

        assertThat(money1.isGreaterOrEqualTo(money2)).isTrue();
        assertThat(money2.isGreaterOrEqualTo(money1)).isTrue();
    }

    @Test
    @Parameters(method = "getTestData_GreaterAndLesserMoneyObjects")
    public void should_CorrectlyCompareTwoMoneyObjects_When_FirstIsGreaterThanSecond(Money greater, Money lesser) {
        assertThat(greater.isGreaterThan(lesser)).isTrue();
        assertThat(lesser.isGreaterThan(greater)).isFalse();

        assertThat(greater.isGreaterOrEqualTo(lesser)).isTrue();
        assertThat(lesser.isGreaterOrEqualTo(greater)).isFalse();
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_WholesPenniesAndCurrencySymbol() {
        return new Object[][] {
                new Object[] {3, 79, "PLN"},
                new Object[] {8, 0,  "PLN"},
                new Object[] {0, 63, "PLN"},
                new Object[] {0, 0,  "PLN"}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_InvalidWholesPenniesAndExceptionMessage() {
        return new Object[][] {
                new Object[] {-1, 79,  "Wholes must not be negative."},
                new Object[] {8,  -1,  "Pennies must not be negative."},
                new Object[] {7,  101, "Pennies must not be greater than 100."},
                new Object[] {-3, -20, "Wholes must not be negative. Pennies must not be negative."},
                new Object[] {-3, 111, "Wholes must not be negative. Pennies must not be greater than 100."}
        };
    }

    @SuppressWarnings("unused")
    private Money[][] getTestData_MoneyObjectsHavingEqualFields() {
        return new Money[][] {
                new Money[] {new Money(0, 0),  new Money(0, 0)},
                new Money[] {new Money(0, 20), new Money(0, 20)},
                new Money[] {new Money(5, 0),  new Money(5, 0)},
                new Money[] {new Money(6, 72), new Money(6, 72)}
        };
    }

    @SuppressWarnings("unused")
    private Money[][] getTestData_MoneyObjectsHavingDifferentFields() {
        return new Money[][] {
                new Money[] {new Money(5, 40), new Money(7, 30)},
                new Money[] {new Money(4, 20), new Money(4, 0)},
                new Money[] {new Money(5, 72), new Money(6, 72)}
        };
    }

    @SuppressWarnings("unused")
    private Money[][] getTestData_AddendsAndSum() {
        return new Money[][] {
                new Money[] {new Money(0, 0),  new Money(0, 0),  new Money(0, 0)},
                new Money[] {new Money(5, 12), new Money(0, 0),  new Money(5, 12)},
                new Money[] {new Money(4, 34), new Money(2, 10), new Money(6, 44)},
                new Money[] {new Money(1, 30), new Money(0, 30), new Money(1, 60)},
                new Money[] {new Money(6, 27), new Money(6, 0),  new Money(12, 27)},
                new Money[] {new Money(1, 51), new Money(0, 62), new Money(2, 13)}
        };
    }

    @SuppressWarnings("unused")
    private Money[][] getTestData_MinuendSubtrahendAndDifference() {
        return new Money[][] {
                new Money[] {new Money(0, 0),  new Money(0, 0),  new Money(0, 0)},
                new Money[] {new Money(5, 12), new Money(0, 0),  new Money(5, 12)},
                new Money[] {new Money(5, 12), new Money(5, 12), new Money(0, 0)},
                new Money[] {new Money(4, 34), new Money(2, 10), new Money(2, 24)},
                new Money[] {new Money(1, 30), new Money(0, 30), new Money(1, 0)},
                new Money[] {new Money(6, 27), new Money(6, 0),  new Money(0, 27)},
                new Money[] {new Money(1, 51), new Money(0, 62), new Money(0, 89)}
        };
    }

    @SuppressWarnings("unused")
    private Money[][] getTestData_LesserMinuendAndGreaterSubtrahend() {
        return new Money[][] {
                new Money[] {new Money(0, 0),  new Money(0, 1)},
                new Money[] {new Money(0, 99), new Money(1, 0)},
                new Money[] {new Money(5, 12), new Money(5, 13)},
                new Money[] {new Money(0, 0),  new Money(1, 0)},
                new Money[] {new Money(5, 44), new Money(6, 44)},
                new Money[] {new Money(6, 37), new Money(7, 36)},
                new Money[] {new Money(7, 55), new Money(8, 56)}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_MultiplicandMultiplierAndProduct() {
        return new Object[][] {
                new Object[] {new Money(0, 0),  0, new Money(0, 0)},
                new Object[] {new Money(0, 0),  9, new Money(0, 0)},
                new Object[] {new Money(4, 20), 0, new Money(0, 0)},
                new Object[] {new Money(1, 32), 4, new Money(5, 28)},
                new Object[] {new Money(6, 0),  6, new Money(36, 0)},
                new Object[] {new Money(0, 72), 8, new Money(5, 76)}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_MultiplicandAndNegativeMultiplier() {
        return new Object[][] {
                new Object[] {new Money(0, 0),  -1},
                new Object[] {new Money(0, 1),  -2},
                new Object[] {new Money(1, 0),  -3},
                new Object[] {new Money(6, 32), -4}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_GreaterAndLesserMoneyObjects() {
        return new Object[][] {
                new Object[] {new Money(0, 1),  new Money(0, 0)},
                new Object[] {new Money(1, 0),  new Money(0, 0)},
                new Object[] {new Money(5, 68), new Money(4, 67)},
                new Object[] {new Money(5, 68), new Money(3, 68)},
                new Object[] {new Money(5, 68), new Money(3, 67)}
        };
    }
}
