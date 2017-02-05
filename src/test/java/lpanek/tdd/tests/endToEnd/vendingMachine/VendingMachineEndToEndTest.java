package lpanek.tdd.tests.endToEnd.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Coin;
import lpanek.tdd.payment.Coins;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.*;

public class VendingMachineEndToEndTest {

    @Test
    public void should_BuyProduct_When_ClientPaysExactProductPrice() {
        // given
        ProductType chocolateBarType = productType("Chocolate bar", price(1, 80));
        ProductType colaDrinkType = productType("Cola drink", price(2, 50));
        Shelves shelves = shelves(
                shelve(chocolateBarType, 4),
                emptyShelve(),
                shelve(colaDrinkType, 2));
        Coins coinsBeforePurchase = coins(
                Coin._5_0,
                Coin._2_0);
        VendingMachine vendingMachine = new VendingMachineBuilder()
                .withShelves(shelves)
                .withCoins(coinsBeforePurchase)
                .build();
        
        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");

        // when
        vendingMachine.selectProduct(3);
        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Insert 2.50 zł.");

        // when
        vendingMachine.insertCoin(Coin._2_0);
        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Insert 0.50 zł.");

        // when
        vendingMachine.insertCoin(Coin._0_5);
        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Take your product.");

        // when
        Product product = vendingMachine.takeProduct();
        // then
        assertThat(product).isNotNull();
        assertThat(product.getType()).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(vendingMachine.getShelveCount()).isEqualTo(3);
        assertThat(vendingMachine.getProductTypeOnShelve(1).get()).isEqualTo(chocolateBarType);
        assertThat(vendingMachine.getProductTypeOnShelve(2).isPresent()).isEqualTo(false);
        assertThat(vendingMachine.getProductTypeOnShelve(3).get()).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(vendingMachine.getProductCountOnShelve(2)).isEqualTo(0);
        assertThat(vendingMachine.getProductCountOnShelve(3)).isEqualTo(1);
        Coins coinsAfterPurchase = coinsBeforePurchase.plus(Coin._2_0, Coin._0_5);
        assertThat(vendingMachine.getCoins()).isEqualTo(coinsAfterPurchase);
    }
}
