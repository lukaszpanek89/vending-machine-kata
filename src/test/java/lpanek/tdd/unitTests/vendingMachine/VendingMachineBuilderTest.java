package lpanek.tdd.unitTests.vendingMachine;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import lpanek.tdd.payment.Money;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.VendingMachine;
import lpanek.tdd.vendingMachine.VendingMachineBuilder;

public class VendingMachineBuilderTest {

    @Test
    public void should_BuildVendingMachineWithOneNotEmptyShelve() {
        // given
        ProductType orangeJuiceType = new ProductType("Orange juice 0.3 l", new Money(3));

        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().addShelve(orangeJuiceType, 5).build();

        // then
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getShelveCount()).isEqualTo(1);
        assertThat(vendingMachine.getProductTypeOnShelve(1)).isEqualTo(orangeJuiceType);
        assertThat(vendingMachine.getProductCountOnShelve(1)).isEqualTo(5);
    }
}
