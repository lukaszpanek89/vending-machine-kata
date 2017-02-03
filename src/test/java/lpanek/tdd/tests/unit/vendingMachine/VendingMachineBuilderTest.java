package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coin;
import lpanek.tdd.payment.Coins;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.*;

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
        ProductType orangeJuiceType = productType("Orange juice", anyPrice());
        Shelves shelves = shelves(shelve(orangeJuiceType, 5));

        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelves).build();

        // then
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getShelveCount()).isEqualTo(1);
        assertThat(vendingMachine.getProductTypeOnShelve(1).get()).isEqualTo(orangeJuiceType);
        assertThat(vendingMachine.getProductCountOnShelve(1)).isEqualTo(5);
        assertThat(vendingMachine.getCoins()).isEqualTo(new Coins());
    }

    @Test
    public void should_BuildVendingMachineWithCoinsAndWithoutShelves() {
        // given
        Coins coins = coins(Coin.DENOMINATION_0_5, Coin.DENOMINATION_1_0);

        // when
        VendingMachine vendingMachine = new VendingMachineBuilder().withCoins(coins).build();

        // then
        assertThat(vendingMachine).isNotNull();
        assertThat(vendingMachine.getShelveCount()).isEqualTo(0);
        assertThat(vendingMachine.getCoins()).isEqualTo(coins);
    }
}
