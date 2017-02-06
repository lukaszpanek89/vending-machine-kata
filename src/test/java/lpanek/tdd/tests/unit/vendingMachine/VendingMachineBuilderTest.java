package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lpanek.tdd.payment.Coin;
import lpanek.tdd.payment.Coins;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.*;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineBuilderTest {

    @Test
    public void should_BuildDefaultVendingMachine() {
        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().build();

        // then
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(vendingMachine.getShelveCount()).isEqualTo(0);
        assertThat(vendingMachine.getCoins()).isEqualTo(emptyCoins());
    }

    @Test
    public void should_BuildVendingMachineWithSpecifiedDisplay() {
        // given
        Display displayMock = mock(Display.class);
        when(displayMock.getCurrentMessage()).thenReturn("message on display");

        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().withDisplay(displayMock).build();

        // then
        verify(displayMock).showSelectProduct();
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("message on display");
        assertThat(vendingMachine.getShelveCount()).isEqualTo(0);
        assertThat(vendingMachine.getCoins()).isEqualTo(emptyCoins());
    }

    @Test
    public void should_BuildVendingMachineWithOneNotEmptyShelve() {
        // given
        ProductType orangeJuiceType = productType("Orange juice", anyPrice());
        Shelves shelves = shelves(shelve(orangeJuiceType, 5));

        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelves).build();

        // then
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(vendingMachine.getShelveCount()).isEqualTo(1);
        assertThat(vendingMachine.getProductTypeOnShelve(1)).isEqualTo(orangeJuiceType);
        assertThat(vendingMachine.getProductCountOnShelve(1)).isEqualTo(5);
        assertThat(vendingMachine.getCoins()).isEqualTo(emptyCoins());
    }

    @Test
    public void should_BuildVendingMachineWithSpecifiedCoins() {
        // given
        Coins coins = coins(Coin._0_5, Coin._1_0);

        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().withCoins(coins).build();

        // then
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(vendingMachine.getShelveCount()).isEqualTo(0);
        assertThat(vendingMachine.getCoins()).isEqualTo(coins);
    }
}
