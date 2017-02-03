package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.price;
import static lpanek.tdd.tests.util.ConstructingUtil.productType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.*;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {

    private static final String EXCEPTION_MESSAGE = "exception message";

    @Test
    public void should_ShowSelectProduct_When_MachineJustStarted() {
        // given
        VendingMachine vendingMachine = new VendingMachineBuilder().build();

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
    }

    @Test
    public void should_ShowInsertValueThatEqualsProductPrice_When_ProductJustSelected() {
        // given
        ProductType sandwichType = productType("Sandwich", price(5, 40));
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(Optional.of(sandwichType));
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

        // when
        vendingMachine.selectProduct(2);

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Insert 5.40 zl.");
    }

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
