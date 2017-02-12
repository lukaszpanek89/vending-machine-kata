package lpanek.tdd.tests.endToEnd.vendingMachine;

import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.*;
import static lpanek.tdd.vendingMachine.domain.payment.Coin.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.vendingMachine.VendingMachine;
import lpanek.tdd.vendingMachine.VendingMachineBuilder;
import lpanek.tdd.vendingMachine.domain.payment.Coins;
import lpanek.tdd.vendingMachine.domain.product.Product;
import lpanek.tdd.vendingMachine.domain.product.ProductType;
import lpanek.tdd.vendingMachine.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.physicalParts.Key;

public class VendingMachineEndToEndTest {

    @Test
    public void buyProduct_After_InsertingExactProductPrice() {
        // given
        ProductType chocolateBarType = productType("Chocolate bar", price(1, 80));
        ProductType colaDrinkType = productType("Cola drink", price(2, 50));
        Shelves shelves = shelves(
                shelve(chocolateBarType, 4),
                emptyShelve(),
                shelve(colaDrinkType, 2));
        Coins coins = coins(_5_0, _2_0);
        VendingMachine vendingMachine = new VendingMachineBuilder().with(shelves).with(coins).build();
        
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");

        // when
        vendingMachine.getKeyboard().press(Key._3);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 2.50 zł.");

        // when
        vendingMachine.getCoinTaker().insert(_2_0);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 0.50 zł.");

        // when
        vendingMachine.getCoinTaker().insert(_0_5);
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
        // TODO: Coins should be tested as well.
    }

    @Test
    public void buyProductAndTakeChange_After_InsertingMoreMoneyThanProductPrice() {
        // given
        ProductType chocolateBarType = productType("Chocolate bar", price(1, 80));
        ProductType colaDrinkType = productType("Cola drink", price(2, 50));
        Shelves shelves = shelves(
                shelve(chocolateBarType, 4),
                emptyShelve(),
                shelve(colaDrinkType, 2));
        Coins coins = coins(_5_0, _2_0, _1_0, _0_5, _0_2, _0_1);
        VendingMachine vendingMachine = new VendingMachineBuilder().with(shelves).with(coins).build();

        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");

        // when
        vendingMachine.getKeyboard().press(Key._1);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 1.80 zł.");

        // when
        vendingMachine.getCoinTaker().insert(_5_0);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Take your product and change.");

        // when
        Coins change = vendingMachine.getCoinsDispenser().takeCoins();
        Product product = vendingMachine.getProductDispenser().takeProduct();
        // then
        assertThat(change).isNotNull();
        assertThat(change.getValue()).isEqualTo(money(3, 20));
        assertThat(product).isNotNull();
        assertThat(product.getType()).isEqualTo(chocolateBarType);

        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");
        assertThat(vendingMachine.getGlassCase().getShelveCount()).isEqualTo(3);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(1)).isEqualTo(chocolateBarType);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(3)).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(1)).isEqualTo(3);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(2)).isEqualTo(0);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(3)).isEqualTo(2);
        // TODO: Coins should be tested as well.
    }

    @Test
    public void takeRejectedCoins_After_MachineBeingUnableToGiveChange() {
        // given
        ProductType chocolateBarType = productType("Chocolate bar", price(1, 80));
        ProductType colaDrinkType = productType("Cola drink", price(2, 50));
        Shelves shelves = shelves(
                shelve(chocolateBarType, 4),
                emptyShelve(),
                shelve(colaDrinkType, 2));
        Coins coins = coins(_5_0, _2_0, _1_0);
        VendingMachine vendingMachine = new VendingMachineBuilder().with(shelves).with(coins).build();

        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");

        // when
        vendingMachine.getKeyboard().press(Key._1);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 1.80 zł.");

        // when
        vendingMachine.getCoinTaker().insert(_5_0);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Sorry, no coins to give change. Take inserted coins.");

        // when
        Coins rejectedCoins = vendingMachine.getCoinsDispenser().takeCoins();
        // then
        assertThat(rejectedCoins).isNotNull();
        assertThat(rejectedCoins).isEqualTo(coins(_5_0));

        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");
        assertThat(vendingMachine.getGlassCase().getShelveCount()).isEqualTo(3);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(1)).isEqualTo(chocolateBarType);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(3)).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(2)).isEqualTo(0);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(3)).isEqualTo(2);
        // TODO: Coins should be tested as well.
    }

    @Test
    public void cancelPurchase_After_SelectingProduct() {
        // given
        ProductType chocolateBarType = productType("Chocolate bar", price(1, 80));
        ProductType colaDrinkType = productType("Cola drink", price(2, 50));
        Shelves shelves = shelves(
                shelve(chocolateBarType, 4),
                emptyShelve(),
                shelve(colaDrinkType, 2));
        Coins coins = coins(_5_0, _2_0, _1_0);
        VendingMachine vendingMachine = new VendingMachineBuilder().with(shelves).with(coins).build();

        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");

        // when
        vendingMachine.getKeyboard().press(Key._1);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 1.80 zł.");

        // when
        vendingMachine.getKeyboard().press(Key.CANCEL);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");
        assertThat(vendingMachine.getGlassCase().getShelveCount()).isEqualTo(3);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(1)).isEqualTo(chocolateBarType);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(3)).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(2)).isEqualTo(0);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(3)).isEqualTo(2);
        // TODO: Coins should be tested as well.
    }

    @Test
    public void cancelPurchaseAndTakeReturnedCoins_After_InsertingCoinsForSelectedProduct() {
        // given
        ProductType chocolateBarType = productType("Chocolate bar", price(1, 80));
        ProductType colaDrinkType = productType("Cola drink", price(2, 50));
        Shelves shelves = shelves(
                shelve(chocolateBarType, 4),
                emptyShelve(),
                shelve(colaDrinkType, 2));
        Coins coins = coins(_5_0, _2_0, _1_0);
        VendingMachine vendingMachine = new VendingMachineBuilder().with(shelves).with(coins).build();

        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");

        // when
        vendingMachine.getKeyboard().press(Key._3);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 2.50 zł.");

        // when
        vendingMachine.getCoinTaker().insert(_1_0);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 1.50 zł.");

        // when
        vendingMachine.getCoinTaker().insert(_0_2);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Insert 1.30 zł.");

        // when
        vendingMachine.getKeyboard().press(Key.CANCEL);
        // then
        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Purchase cancelled. Take inserted coins.");

        // when
        Coins returnedCoins = vendingMachine.getCoinsDispenser().takeCoins();
        // then
        assertThat(returnedCoins).isNotNull();
        assertThat(returnedCoins).isEqualTo(coins(_1_0, _0_2));

        assertThat(vendingMachine.getDisplay().getMessage()).isEqualTo("Select product.");
        assertThat(vendingMachine.getGlassCase().getShelveCount()).isEqualTo(3);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(1)).isEqualTo(chocolateBarType);
        assertThat(vendingMachine.getGlassCase().getProductTypeOnShelve(3)).isEqualTo(colaDrinkType);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(2)).isEqualTo(0);
        assertThat(vendingMachine.getGlassCase().getProductCountOnShelve(3)).isEqualTo(1);
        // TODO: Coins should be tested as well.
    }
}
