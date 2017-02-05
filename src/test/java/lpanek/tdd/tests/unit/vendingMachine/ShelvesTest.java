package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.Shelve;
import lpanek.tdd.vendingMachine.Shelves;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

@RunWith(JUnitParamsRunner.class)
public class ShelvesTest {

    @Test
    public void should_ContainZeroShelves_When_NoShelveAdded() {
        assertThat(new Shelves().getCount()).isEqualTo(0);
    }

    @Test
    public void should_ContainOneNotEmptyShelve_When_OneNotEmptyShelveAdded() {
        // given
        ProductType lemonJuiceType = productType("Lemon juice", anyPrice());
        Shelve shelve = shelve(lemonJuiceType, 4);

        // when
        Shelves shelves = new Shelves(shelve);

        // then
        assertThat(shelves.getCount()).isEqualTo(1);
        assertThat(shelves.getProductTypeOnShelve(1).get()).isEqualTo(lemonJuiceType);
        assertThat(shelves.getProductCountOnShelve(1)).isEqualTo(4);
    }

    @Test
    public void should_ContainTwoNotEmptyShelves_When_TwoNotEmptyShelvesAdded() {
        // given
        ProductType lemonJuiceType = productType("Lemon juice", anyPrice());
        ProductType appleJuiceType = productType("Apple juice", anyPrice());
        Shelve shelve1 = shelve(lemonJuiceType, 4);
        Shelve shelve2 = shelve(appleJuiceType, 5);

        // when
        Shelves shelves = new Shelves(shelve1, shelve2);

        // then
        assertThat(shelves.getCount()).isEqualTo(2);
        assertThat(shelves.getProductTypeOnShelve(1).get()).isEqualTo(lemonJuiceType);
        assertThat(shelves.getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(shelves.getProductTypeOnShelve(2).get()).isEqualTo(appleJuiceType);
        assertThat(shelves.getProductCountOnShelve(2)).isEqualTo(5);
    }

    @Test
    @Parameters(method = "getTestData_InvalidShelveNumberAndExceptionMessage")
    public void should_ThrowException_When_TriesToGetProductTypeForInvalidShelveNumber(int invalidShelveNumber, String exceptionMessage) {
        // given
        Shelves shelves = new Shelves(emptyShelve());

        // when
        Throwable caughtThrowable = catchThrowable(() -> shelves.getProductTypeOnShelve(invalidShelveNumber));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    @Parameters(method = "getTestData_InvalidShelveNumberAndExceptionMessage")
    public void should_ThrowException_When_TriesToGetProductCountForInvalidShelveNumber(int invalidShelveNumber, String exceptionMessage) {
        // given
        Shelves shelves = new Shelves(emptyShelve());

        // when
        Throwable caughtThrowable = catchThrowable(() -> shelves.getProductCountOnShelve(invalidShelveNumber));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage(exceptionMessage);
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_InvalidShelveNumberAndExceptionMessage() {
        return new Object[][]{
                new Object[] {-1, "-1 is an invalid shelve number."},
                new Object[] {0, "0 is an invalid shelve number."},
                new Object[] {2, "2 is an invalid shelve number."}
        };
    }
}
