package lpanek.tdd.tests.endToEnd.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.domain.payment.Coin;
import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.VendingMachine;
import lpanek.tdd.vendingMachine.VendingMachineBuilder;
import lpanek.tdd.vendingMachine.physicalParts.Key;

public class VendingMachineEndToEndTest {

    @Test
    public void buyProductAfterInsertingExactProductPrice() {
        // given
        ProductType chocolateBarType = productType("Chocolate bar", price(1, 80));
        ProductType colaDrinkType = productType("Cola drink", price(2, 50));
        Shelves shelves = shelves(
                shelve(chocolateBarType, 4),
                emptyShelve(),
                shelve(colaDrinkType, 2));
        Coins coins = coins(
                Coin._5_0,
                Coin._2_0);
        VendingMachine vendingMachine = new VendingMachineBuilder()
                .withShelves(shelves)
                .withCoins(coins)
                .build();
        
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");

        // when
        vendingMachine.getKeyboard().press(Key._3);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 2.50 zł.");

        // when
        vendingMachine.getCoinTaker().insert(Coin._2_0);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 0.50 zł.");

        // when
        vendingMachine.getCoinTaker().insert(Coin._0_5);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Take your product.");

        // when
        Product product = vendingMachine.getProductDispenser().takeProduct();
        // then
        assertThat(product).isNotNull();
        assertThat(product.getType()).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");
        assertThat(vendingMachine.getGlassCase().getShelveCount()).isEqualTo(3);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(1)).isEqualTo(chocolateBarType);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(3)).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(2)).isEqualTo(0);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(3)).isEqualTo(1);
    }
}
