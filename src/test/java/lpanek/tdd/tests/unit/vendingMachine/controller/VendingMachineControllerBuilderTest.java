package lpanek.tdd.tests.unit.vendingMachine.controller;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lpanek.tdd.domain.payment.Coin;
import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.controller.VendingMachineController;
import lpanek.tdd.vendingMachine.controller.VendingMachineControllerBuilder;
import lpanek.tdd.vendingMachine.physicalParts.Display;
import lpanek.tdd.vendingMachine.physicalParts.ProductDispenser;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineControllerBuilderTest {

    @Test
    public void should_BuildDefaultVendingMachineController() {
        // when
        VendingMachineController controller = new VendingMachineControllerBuilder().build();

        // then
        assertThat(controller).isNotNull();
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(controller.getShelveCount()).isEqualTo(0);
        assertThat(controller.getCoins()).isEqualTo(emptyCoins());
    }

    @Test
    public void should_BuildVendingMachineControllerWithSpecifiedDisplay() {
        // given
        Display displayMock = mock(Display.class);
        when(displayMock.getMessage()).thenReturn("message on display");

        // when
        VendingMachineController controller = new VendingMachineControllerBuilder().withDisplay(displayMock).build();

        // then
        verify(displayMock).showSelectProduct();
        assertThat(controller).isNotNull();
        assertThat(controller.getMessageOnDisplay()).isEqualTo("message on display");
        assertThat(controller.getShelveCount()).isEqualTo(0);
        assertThat(controller.getCoins()).isEqualTo(emptyCoins());
    }

    @Test
    public void should_BuildVendingMachineControllerWithSpecifiedProductDispenser() {
        // given
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);

        // when
        VendingMachineController controller = new VendingMachineControllerBuilder().withProductDispenser(productDispenserMock).build();

        // then
        // TODO: How to test that productDispenserMock was indeed used during VendingMachineController's construction?
        assertThat(controller).isNotNull();
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(controller.getShelveCount()).isEqualTo(0);
        assertThat(controller.getCoins()).isEqualTo(emptyCoins());
    }

    @Test
    public void should_BuildVendingMachineControllerWithOneNotEmptyShelve() {
        // given
        ProductType productType = anyProductType();
        Shelves shelves = shelves(shelve(productType, 5));

        // when
        VendingMachineController controller = new VendingMachineControllerBuilder().withShelves(shelves).build();

        // then
        assertThat(controller).isNotNull();
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(controller.getShelveCount()).isEqualTo(1);
        assertThat(controller.getProductTypeOnShelve(1)).isEqualTo(productType);
        assertThat(controller.getProductCountOnShelve(1)).isEqualTo(5);
        assertThat(controller.getCoins()).isEqualTo(emptyCoins());
    }

    @Test
    public void should_BuildVendingMachineControllerWithSpecifiedCoins() {
        // given
        Coins coins = coins(Coin._0_5, Coin._1_0);

        // when
        VendingMachineController controller = new VendingMachineControllerBuilder().withCoins(coins).build();

        // then
        assertThat(controller).isNotNull();
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(controller.getShelveCount()).isEqualTo(0);
        assertThat(controller.getCoins()).isEqualTo(coins);
    }
}
