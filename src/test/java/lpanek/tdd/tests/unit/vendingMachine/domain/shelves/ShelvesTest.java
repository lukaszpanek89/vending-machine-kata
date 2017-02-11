package lpanek.tdd.tests.unit.vendingMachine.domain.shelves;

import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.domain.product.ProductType;
import lpanek.tdd.vendingMachine.domain.shelves.Shelve;
import lpanek.tdd.vendingMachine.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.domain.shelves.ex.InvalidShelveNumberException;

@RunWith(JUnitParamsRunner.class)
public class ShelvesTest {

    private static final String EXCEPTION_MESSAGE = "exception message";

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_ContainZeroShelves_When_NoShelveAdded() {
        assertThat(new Shelves().getCount()).isEqualTo(0);
    }

    @Test
    public void should_ContainOneNotEmptyShelve_When_OneNotEmptyShelveAdded() {
        // given
        ProductType productType = anyProductType();
        Shelve shelve = shelve(productType, 4);

        // when
        Shelves shelves = new Shelves(shelve);

        // then
        assertThat(shelves.getCount()).isEqualTo(1);
        assertThat(shelves.getProductTypeOnShelve(1)).isEqualTo(productType);
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
        assertThat(shelves.getProductTypeOnShelve(1)).isEqualTo(lemonJuiceType);
        assertThat(shelves.getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(shelves.getProductTypeOnShelve(2)).isEqualTo(appleJuiceType);
        assertThat(shelves.getProductCountOnShelve(2)).isEqualTo(5);
    }

    @Test
    public void should_RemoveProductFromSpecifiedShelve_When_AskedFor() {
        // given
        Shelve shelve1Mock = mock(Shelve.class);
        Shelve shelve2Mock = mock(Shelve.class);
        Shelves shelves = new Shelves(shelve1Mock, shelve2Mock);

        // when
        shelves.removeProductFromShelve(2);

        // then
        verify(shelve1Mock, never()).removeProduct();
        verify(shelve2Mock).removeProduct();
    }

    @Test
    @Parameters(method = "getTestData_InvalidShelveNumberAndExceptionMessage")
    public void should_ThrowException_When_TriesToRemoveProductUsingInvalidShelveNumber(int invalidShelveNumber, String exceptionMessage) {
        // given
        Shelves shelves = new Shelves(emptyShelve());

        // when
        Throwable caughtThrowable = catchThrowable(() -> shelves.removeProductFromShelve(invalidShelveNumber));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    public void should_ThrowException_When_TriesToRemoveProductFromEmptyShelve() {
        // given
        Shelve shelveMock = mock(Shelve.class);
        doThrow(new EmptyShelveException(EXCEPTION_MESSAGE)).when(shelveMock).removeProduct();
        Shelves shelves = new Shelves(shelveMock);

        // when
        Throwable caughtThrowable = catchThrowable(() -> shelves.removeProductFromShelve(1));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(EmptyShelveException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    @Parameters(method = "getTestData_InvalidShelveNumberAndExceptionMessage")
    public void should_ThrowException_When_TriesToGetProductTypeUsingInvalidShelveNumber(int invalidShelveNumber, String exceptionMessage) {
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
    public void should_ThrowException_When_TriesToGetProductTypeFromEmptyShelve() {
        // given
        Shelve shelveMock = mock(Shelve.class);
        doThrow(new EmptyShelveException(EXCEPTION_MESSAGE)).when(shelveMock).getProductType();
        Shelves shelves = new Shelves(shelveMock);

        // when
        Throwable caughtThrowable = catchThrowable(() -> shelves.getProductTypeOnShelve(1));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(EmptyShelveException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    @Parameters(method = "getTestData_InvalidShelveNumberAndExceptionMessage")
    public void should_ThrowException_When_TriesToGetProductCountUsingInvalidShelveNumber(int invalidShelveNumber, String exceptionMessage) {
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
        return new Object[][] {
                new Object[] {-1, "-1 is an invalid shelve number."},
                new Object[] {0, "0 is an invalid shelve number."},
                new Object[] {2, "2 is an invalid shelve number."}
        };
    }
}
