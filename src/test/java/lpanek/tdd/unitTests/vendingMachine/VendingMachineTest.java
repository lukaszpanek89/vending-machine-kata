package lpanek.tdd.unitTests.vendingMachine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lpanek.tdd.vendingMachine.*;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {

    private static final String EXCEPTION_MESSAGE = "exception message";

    @Test
    public void should_ThrowException_When_ShelvesGetProductTypeOnShelveThrowsInvalidShelveNumberException() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        doThrow(new InvalidShelveNumberException(EXCEPTION_MESSAGE)).when(shelvesMock).getProductTypeOnShelve(1);
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

        // when
        Throwable caughtThrowable = catchThrowable(() -> vendingMachine.getProductTypeOnShelve(1));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void should_ThrowException_When_ShelvesGetProductCountOnShelveThrowsInvalidShelveNumberException() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        doThrow(new InvalidShelveNumberException(EXCEPTION_MESSAGE)).when(shelvesMock).getProductCountOnShelve(1);
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

        // when
        Throwable caughtThrowable = catchThrowable(() -> vendingMachine.getProductCountOnShelve(1));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }
}
