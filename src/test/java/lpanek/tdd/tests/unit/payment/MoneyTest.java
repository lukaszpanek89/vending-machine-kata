package lpanek.tdd.tests.unit.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.payment.Money;

@RunWith(JUnitParamsRunner.class)
public class MoneyTest {

    @Test
    public void should_HaveSpecifiedWholesPenniesAndDefaultCurrencySymbol_When_Constructed() {
        // when
        Money money = new Money(2, 99);

        // then
        assertThat(money.getWholes()).isEqualTo(2);
        assertThat(money.getPennies()).isEqualTo(99);
        assertThat(money.getCurrencySymbol()).isEqualTo("zł");
        assertThat(money.toString()).isEqualTo("Money=[wholes=2, pennies=99, currencySymbol='zł']");
    }

    @Test
    @Parameters(method = "getTestData_MoneyObjectsHavingEqualFields")
    public void should_twoMoneyObjectsBeEqual_When_HavingEqualFields(Money money1, Money money2) {
        assertThat(money1).isEqualTo(money2);
        assertThat(money2).isEqualTo(money1);
        assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
    }

    @Test
    @Parameters(method = "getTestData_MoneyObjectsHavingDifferentFields")
    public void should_twoMoneyObjectsNotBeEqual_When_HavingDifferentFields(Money money1, Money money2) {
        assertThat(money1).isNotEqualTo(money2);
        assertThat(money2).isNotEqualTo(money1);
    }

    @Test
    @Parameters(method = "getTestData_AddendsAndSum")
    public void should_ReturnNewObjectWithSum_When_MoneyAdded(Money addend1, Money addend2, Money expectedSum) {
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
    public void should_ReturnNewObjectWithDifference_When_MoneySubtracted(Money minuend, Money subtrahend, Money expectedDifference) {
        // given
        Money minuendBeforeSubtraction = new Money(minuend.getWholes(), minuend.getPennies());

        // when
        Money actualDifference = minuend.minus(subtrahend);

        // then
        assertThat(actualDifference).isEqualTo(expectedDifference);
        assertThat(minuend).isEqualTo(minuendBeforeSubtraction);
    }

    @Test
    @Parameters(method = "getTestData_MultiplicandMultiplierAndProduct")
    public void should_ReturnNewObjectWithProduct_When_MoneyMultiplied(Money multiplicand, int multiplier, Money expectedProduct) {
        // given
        Money multiplicandBeforeMultiplication = new Money(multiplicand.getWholes(), multiplicand.getPennies());

        // when
        Money actualProduct = multiplicand.times(multiplier);

        // then
        assertThat(actualProduct).isEqualTo(expectedProduct);
        assertThat(multiplicand).isEqualTo(multiplicandBeforeMultiplication);
    }

    @SuppressWarnings("unused")
    private Money[][] getTestData_MoneyObjectsHavingEqualFields() {
        return new Money[][]{
                new Money[] {new Money(0, 0),  new Money(0, 0)},
                new Money[] {new Money(0, 20), new Money(0, 20)},
                new Money[] {new Money(5, 0),  new Money(5, 0)},
                new Money[] {new Money(6, 72), new Money(6, 72)}
        };
    }

    @SuppressWarnings("unused")
    private Money[][] getTestData_MoneyObjectsHavingDifferentFields() {
        return new Money[][]{
                new Money[] {new Money(5, 40), new Money(7, 30)},
                new Money[] {new Money(4, 20), new Money(4, 0)},
                new Money[] {new Money(5, 72), new Money(6, 72)}
        };
    }

    @SuppressWarnings("unused")
    private Money[][] getTestData_AddendsAndSum() {
        return new Money[][]{
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
        return new Money[][]{
                new Money[] {new Money(0, 0),  new Money(0, 0),  new Money(0, 0)},
                new Money[] {new Money(5, 12), new Money(0, 0),  new Money(5, 12)},
                new Money[] {new Money(4, 34), new Money(2, 10), new Money(2, 24)},
                new Money[] {new Money(1, 30), new Money(0, 30), new Money(1, 0)},
                new Money[] {new Money(6, 27), new Money(6, 0),  new Money(0, 27)},
                new Money[] {new Money(1, 51), new Money(0, 62), new Money(0, 89)}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_MultiplicandMultiplierAndProduct() {
        return new Object[][]{
                new Object[] {new Money(0, 0),  9, new Money(0, 0)},
                new Object[] {new Money(4, 20), 0, new Money(0, 0)},
                new Object[] {new Money(1, 32), 4, new Money(5, 28)},
                new Object[] {new Money(6, 0),  6, new Money(36, 0)},
                new Object[] {new Money(0, 72), 8, new Money(5, 76)}
        };
    }
}
