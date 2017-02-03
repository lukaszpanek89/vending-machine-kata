package lpanek.tdd.endToEndTests.vendingMachine;

import static org.assertj.core.api.Assertions.assertThat;

import lpanek.tdd.vendingMachine.VendingMachine;
import org.junit.Test;

public class VendingMachineEndToEndTest {

    @Test
    public void should_BuyProduct_When_ClientPaysExactProductPrice() {
        // given
        ProductType chocolateBarType = new ProductType("Chocolate bar", new Money(1, 80));
        ProductType colaDrinkType = new ProductType("Cola drink 0.25 l", new Money(2, 50));
        Coins coinsBeforePurchase = new Coins(Coin.DENOMINATION_5_0, Coin.DENOMINATION_2_0);
        VendingMachine vendingMachine = new VendingMachineBuilder()
                .addShelve(chocolateBarType, 4)
                .addEmptyShelve()
                .addShelve(colaDrinkType, 2)
                .withCoins(coinsBeforePurchase)
                .build();
        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
        // when
        vendingMachine.selectProduct(3);
        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Insert 2.5 zl.");
        // when
        vendingMachine.insertCoin(Coin.DENOMINATION_2_0);
        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Insert 0.5 zl.");
        // when
        vendingMachine.insertCoin(Coin.DENOMINATION_0_5);
        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Take your product.");
        // when
        Product product = vendingMachine.takeProduct();
        // then
        assertThat(product.getType()).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
        assertThat(vendingMachine.getProductTypeOnShelve(1).get()).isEqualTo(chocolateBarType);
        assertThat(vendingMachine.getProductTypeOnShelve(2).isPresent()).isEqualTo(false);
        assertThat(vendingMachine.getProductTypeOnShelve(3).get()).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(vendingMachine.getProductCountOnShelve(2)).isEqualTo(0);
        assertThat(vendingMachine.getProductCountOnShelve(3)).isEqualTo(1);
        Coins coinsAfterPurchase = coinsBeforePurchase.add(Coin.DENOMINATION_2_0, Coin.DENOMINATION_0_5);
        assertThat(vendingMachine.getCoins()).isEqualTo(coinsAfterPurchase);
    }
}
