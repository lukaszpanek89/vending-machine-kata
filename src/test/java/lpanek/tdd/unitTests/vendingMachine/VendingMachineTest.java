package lpanek.tdd.unitTests.vendingMachine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import lpanek.tdd.payment.Money;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.VendingMachine;
import lpanek.tdd.vendingMachine.VendingMachineBuilder;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class VendingMachineTest {

    @Test
    public void should_ThrowException_When_TriesToGetProductTypeForInvalidShelveNumber() {
        // given
        VendingMachine vendingMachine = createVendingMachineWithOneShelve();

        // when
        Throwable caughtThrowable = catchThrowable(() -> vendingMachine.getProductTypeOnShelve(-1));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("-1 is an invalid shelve number.");

        // when
        caughtThrowable = catchThrowable(() -> vendingMachine.getProductTypeOnShelve(0));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("0 is an invalid shelve number.");

        // when
        caughtThrowable = catchThrowable(() -> vendingMachine.getProductTypeOnShelve(2));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("2 is an invalid shelve number.");
    }

    @Test
    public void should_ThrowException_When_TriesToGetProductCountForInvalidShelveNumber() {
        // given
        VendingMachine vendingMachine = createVendingMachineWithOneShelve();

        // when
        Throwable caughtThrowable = catchThrowable(() -> vendingMachine.getProductCountOnShelve(-1));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("-1 is an invalid shelve number.");

        // when
        caughtThrowable = catchThrowable(() -> vendingMachine.getProductCountOnShelve(0));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("0 is an invalid shelve number.");

        // when
        caughtThrowable = catchThrowable(() -> vendingMachine.getProductCountOnShelve(2));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("2 is an invalid shelve number.");
    }

    private VendingMachine createVendingMachineWithOneShelve() {
        ProductType snacksType = new ProductType("Snacks", new Money(4));
        return new VendingMachineBuilder().addShelve(snacksType, 7).build();
    }
}
