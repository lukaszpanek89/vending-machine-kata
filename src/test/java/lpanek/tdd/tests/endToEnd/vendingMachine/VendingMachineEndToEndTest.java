package lpanek.tdd.tests.endToEnd.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.domain.payment.Coin;
import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.controller.VendingMachineController;
import lpanek.tdd.vendingMachine.controller.VendingMachineControllerBuilder;

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
        VendingMachineController controller = new VendingMachineControllerBuilder()
                .withShelves(shelves)
                .withCoins(coinsBeforePurchase)
                .build();
        
        // then
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Select product.");

        // when
        controller.selectProduct(3);
        // then
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Insert 2.50 zł.");

        // when
        controller.insertCoin(Coin._2_0);
        // then
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Insert 0.50 zł.");

        // when
        controller.insertCoin(Coin._0_5);
        // then
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Take your product.");

        // when
        Product product = controller.takeProduct();
        // then
        assertThat(product).isNotNull();
        assertThat(product.getType()).isEqualTo(colaDrinkType);
        assertThat(controller.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(controller.getShelveCount()).isEqualTo(3);
        assertThat(controller.getProductTypeOnShelve(1)).isEqualTo(chocolateBarType);
        assertThat(controller.getProductTypeOnShelve(3)).isEqualTo(colaDrinkType);
        assertThat(controller.getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(controller.getProductCountOnShelve(2)).isEqualTo(0);
        assertThat(controller.getProductCountOnShelve(3)).isEqualTo(1);
        Coins coinsAfterPurchase = coinsBeforePurchase.plus(Coin._2_0, Coin._0_5);
        assertThat(controller.getCoins()).isEqualTo(coinsAfterPurchase);
    }
}
