package lpanek.tdd.unitTests.vendingMachine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coins;
import lpanek.tdd.payment.Money;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.VendingMachine;
import lpanek.tdd.vendingMachine.VendingMachineBuilder;

public class VendingMachineBuilderTest {

    @Test
    public void should_BuildVendingMachineWithoutShelvesAndCoins() {
        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().build();

        // then
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getShelveCount()).isEqualTo(0);
        assertThat(vendingMachine.getCoins()).isEqualTo(new Coins());
    }

    @Test
    public void should_BuildVendingMachineWithOneNotEmptyShelveAndWithoutCoins() {
        // given
        ProductType orangeJuiceType = new ProductType("Orange juice 0.3 l", new Money(3));

        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().addShelve(orangeJuiceType, 5).build();

        // then
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getShelveCount()).isEqualTo(1);
        assertThat(vendingMachine.getProductTypeOnShelve(1).get()).isEqualTo(orangeJuiceType);
        assertThat(vendingMachine.getProductCountOnShelve(1)).isEqualTo(5);
        assertThat(vendingMachine.getCoins()).isEqualTo(new Coins());
    }
}
