package lpanek.tdd.tests.unit.vendingMachine.controller;

import static lpanek.tdd.domain.payment.Coin.*;
import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static lpanek.tdd.tests.util.VendingMachineControllerBuilder.controllerBuilder;
import static lpanek.tdd.vendingMachine.controller.VendingMachineController.MachineState.ProductAndOptionallyChangeDispensed;
import static lpanek.tdd.vendingMachine.controller.VendingMachineController.MachineState.ProductSelected;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.domain.payment.*;
import lpanek.tdd.domain.payment.strategy.ChangeDeterminingStrategy;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.domain.shelves.ex.InvalidShelveNumberException;
import lpanek.tdd.vendingMachine.controller.VendingMachineController;
import lpanek.tdd.vendingMachine.physicalParts.*;

@RunWith(JUnitParamsRunner.class)
public class VendingMachineControllerTest {

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_RegisterItselfAsListener_When_UnderConstruction() {
        // given
        Keyboard keyboardMock = mock(Keyboard.class);
        CoinTaker coinTakerMock = mock(CoinTaker.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);

        // when
        VendingMachineController controller = controllerBuilder()
                .with(keyboardMock).with(coinTakerMock).with(coinsDispenserMock).with(productDispenserMock).build();

        // then
        verify(keyboardMock).addListener(controller);
        verify(coinTakerMock).addListener(controller);
        verify(coinsDispenserMock).addListener(controller);
        verify(productDispenserMock).addListener(controller);
    }

    @Test
    public void should_ShowSelectProduct_When_Started() {
        // given
        Display displayMock = mock(Display.class);

        // when
        controllerBuilder().with(displayMock).build();

        // then
        verify(displayMock).showSelectProduct();
    }

    @Test
    @Parameters(method = "getTestData_ProductPrice")
    public void should_ShowInsertMoney_When_StartedAndFirstProductJustSelected(Money productPrice) {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));

        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock).build();

        // when
        controller.onKeyPressed(Key._2);

        // then
        verify(displayMock).showInsertMoney(productPrice);
    }

    @Test
    @Parameters(method = "getTestData_ProductPrice")
    public void should_ShowInsertMoney_When_PreviousPurchaseFinishedAndNextProductJustSelected(Money productPrice) {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));

        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        controller.onCoinsTaken();
        controller.onProductTaken();

        reset(displayMock);

        // when
        controller.onKeyPressed(Key._2);

        // then
        verify(displayMock).showInsertMoney(productPrice);
    }

    @Test
    public void should_ShowShelveIsEmpty_When_TriesToSelectProductFromEmptyShelve() {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelves = shelves(emptyShelve());
        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelves).build();

        // when
        controller.onKeyPressed(Key._1);

        // then
        verify(displayMock).showShelveIsEmpty();
    }

    @Test
    public void should_ShowError_When_TriesToSelectProductUsingInvalidShelveNumber() {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelvesMock = mock(Shelves.class);
        doThrow(InvalidShelveNumberException.class).when(shelvesMock).getProductTypeOnShelve(1);
        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock).build();

        // when
        controller.onKeyPressed(Key._1);

        // then
        verify(displayMock).showInternalError();
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinToInsert")
    public void should_ShowInsertMoney_When_FirstCoinInserted(Money productPrice, Coin coinToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_5_0, _2_0);
        Coins coinsAfterInsertion = initialCoins.plus(coinToInsert);

        VendingMachineController controller = controllerBuilder()
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).with(initialCoins)
                .withState(ProductSelected)
                .withSelectedShelveNumber(2).build();

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verify(displayMock).showInsertMoney(productPrice.minus(coinToInsert.getValue()));
        verify(coinsDispenserMock, never()).dispenseCoins(any(Coins.class));
        verify(productDispenserMock, never()).dispenseProductFromShelve(anyInt());
        assertThat(controller.getCoins()).isNotNull();
        assertThat(controller.getCoins()).isEqualTo(coinsAfterInsertion);
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndExactCoinsToInsert")
    public void exactPayment_should_DispenseProductAndShowTakeIt_When_ExactCoinsInserted(Money productPrice, Coin[] coinsToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_2_0, _0_5);
        Coins coinsAfterInsertions = initialCoins.plus(coinsToInsert);

        VendingMachineController controller = controllerBuilder()
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).with(initialCoins)
                .withState(ProductSelected)
                .withSelectedShelveNumber(2).build();

        // when
        for (Coin coin : coinsToInsert) {
            controller.onCoinInserted(coin);
        }

        // then
        verify(displayMock, times(coinsToInsert.length - 1)).showInsertMoney(any(Money.class));
        verify(displayMock).showTakeProduct();
        verify(coinsDispenserMock, never()).dispenseCoins(any(Coins.class));
        verify(productDispenserMock).dispenseProductFromShelve(2);
        verify(shelvesMock).removeProductFromShelve(2);
        assertThat(controller.getCoins()).isNotNull();
        assertThat(controller.getCoins()).isEqualTo(coinsAfterInsertions);
    }

    @Test
    public void exactPayment_should_ShowSelectProduct_When_ProductTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(false)
                .withWaitingForProductToBeTaken(true).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verify(displayMock).showSelectProduct();
    }

    @Test
    @Parameters(method = "getTestData_InitialCoinsAndProductPriceAndExcessiveCoinsToInsertAndChange")
    public void overPayment_should_DispenseProductAndChangeAndShowTakeThem_When_ExcessiveCoinsInserted(Coins initialCoins, Money productPrice, Coin[] coinsToInsert, Coin[] changeAsArray) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);

        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));

        Coins change = coins(changeAsArray);
        Coins coinsAfterInsertions = initialCoins.plus(coinsToInsert);
        Coins coinsAfterChangeDispense = coinsAfterInsertions.minus(change);

        ChangeDeterminingStrategy changeStrategyMock = mock(ChangeDeterminingStrategy.class);
        when(changeStrategyMock.determineChange(coinsAfterInsertions, change.getValue())).thenReturn(change);

        VendingMachineController controller = controllerBuilder()
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).with(initialCoins).with(changeStrategyMock)
                .withState(ProductSelected)
                .withSelectedShelveNumber(2).build();

        reset(displayMock);

        // when
        for (Coin coin : coinsToInsert) {
            controller.onCoinInserted(coin);
        }

        // then
        verify(displayMock, times(coinsToInsert.length - 1)).showInsertMoney(any(Money.class));
        verify(displayMock).showTakeProductAndChange();
        verify(changeStrategyMock).determineChange(coinsAfterInsertions, change.getValue());
        verify(coinsDispenserMock).dispenseCoins(change);
        verify(productDispenserMock).dispenseProductFromShelve(2);
        verify(shelvesMock).removeProductFromShelve(2);
        assertThat(controller.getCoins()).isNotNull();
        assertThat(controller.getCoins()).isEqualTo(coinsAfterChangeDispense);
    }

    @Test
    public void overPayment_should_ShowSelectProduct_When_ProductAndChangeTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        reset(displayMock);

        // when
        controller.onProductTaken();
        controller.onCoinsTaken();

        // then
        verify(displayMock).showSelectProduct();
    }

    @Test
    public void overPayment_shouldNot_ShowSelectProduct_When_OnlyProductTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void overPayment_shouldNot_ShowSelectProduct_When_OnlyChangeTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        reset(displayMock);

        // when
        controller.onCoinsTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreProductSelection_When_ProductAlreadySelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductSelected).build();
        reset(displayMock);

        // when
        controller.onKeyPressed(Key._1);

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreProductSelection_When_WaitingForProductToBeTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForProductToBeTaken(true).build();
        reset(displayMock);

        // when
        controller.onKeyPressed(Key._1);

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreProductSelection_When_WaitingForCoinsToBeTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true).build();
        reset(displayMock);

        // when
        controller.onKeyPressed(Key._1);

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    @Parameters(method = "getTestData_CoinToInsert")
    public void should_RejectInsertedCoin_When_ProductNotSelected(Coin coinToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        Coins initialCoins = coins(_0_5, _1_0);
        VendingMachineController controller = controllerBuilder()
                .with(displayMock).with(coinsDispenserMock).with(initialCoins).build();
        reset(displayMock);

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verifyZeroInteractions(displayMock);
        verify(coinsDispenserMock).dispenseCoins(coins(coinToInsert));
        assertThat(controller.getCoins()).isNotNull();
        assertThat(controller.getCoins()).isEqualTo(initialCoins);
    }

    @Test
    @Parameters(method = "getTestData_CoinToInsert")
    public void should_RejectInsertedCoin_When_WaitingForProductToBeTaken(Coin coinToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        Coins initialCoins = coins(_0_5, _1_0);
        VendingMachineController controller = controllerBuilder()
                .with(displayMock).with(coinsDispenserMock).with(initialCoins)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForProductToBeTaken(true).build();
        reset(displayMock);

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verifyZeroInteractions(displayMock);
        verify(coinsDispenserMock).dispenseCoins(coins(coinToInsert));
        assertThat(controller.getCoins()).isNotNull();
        assertThat(controller.getCoins()).isEqualTo(initialCoins);
    }

    @Test
    @Parameters(method = "getTestData_CoinToInsert")
    public void should_RejectInsertedCoin_When_WaitingForCoinsToBeTaken(Coin coinToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        Coins initialCoins = coins(_0_5, _1_0);
        VendingMachineController controller = controllerBuilder()
                .with(displayMock).with(coinsDispenserMock).with(initialCoins)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true).build();
        reset(displayMock);

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verifyZeroInteractions(displayMock);
        verify(coinsDispenserMock).dispenseCoins(coins(coinToInsert));
        assertThat(controller.getCoins()).isNotNull();
        assertThat(controller.getCoins()).isEqualTo(initialCoins);
    }

    @Test
    public void should_IgnoreProductTaken_When_ProductNotSelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreProductTaken_When_ProductSelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductSelected).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreProductTaken_When_ProductAlreadyTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForProductToBeTaken(false).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreCoinsTaken_When_ProductNotSelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock).build();
        reset(displayMock);

        // when
        controller.onCoinsTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreCoinsTaken_When_ProductSelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductSelected).build();
        reset(displayMock);

        // when
        controller.onCoinsTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreCoinsTaken_When_CoinsAlreadyTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineController controller = controllerBuilder().with(displayMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(false).build();
        reset(displayMock);

        // when
        controller.onCoinsTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductPrice() {
        return new Object[][] {
                new Object[] {price(5, 40)},
                new Object[] {price(3, 0)},
                new Object[] {price(0, 60)}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductPriceAndCoinToInsert() {
        return new Object[][] {
                new Object[] {price(5, 40), _2_0},
                new Object[] {price(4, 20), _0_2},
                new Object[] {price(1, 10), _0_5}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductPriceAndExactCoinsToInsert() {
        return new Object[][] {
                new Object[] {price(2, 0),  new Coin[] {_2_0}},
                new Object[] {price(0, 50), new Coin[] {_0_5}},
                new Object[] {price(5, 40), new Coin[] {_5_0, _0_2, _0_2}},
                new Object[] {price(5, 40), new Coin[] {_2_0, _2_0, _1_0, _0_2, _0_1, _0_1}},
                new Object[] {price(5, 40), new Coin[] {_1_0, _0_1, _0_1, _2_0, _2_0, _0_2}}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_InitialCoinsAndProductPriceAndExcessiveCoinsToInsertAndChange() {
        return new Object[][] {
                //            initial coins                            | product price | excessive coins to insert    | change
                new Object[] {coins(_0_1, _0_2, _5_0),                   price(1, 90),   new Coin[] {_2_0},             new Coin[] {_0_1}},
                new Object[] {coins(_0_1, _0_1,       _0_5),             price(1, 80),   new Coin[] {_2_0},             new Coin[] {_0_1, _0_1}},
                new Object[] {coins(            _0_2, _0_5),             price(1, 80),   new Coin[] {_2_0},             new Coin[] {_0_2}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_5),             price(1, 80),   new Coin[] {_2_0},             new Coin[] {_0_2}},
                new Object[] {coins(_0_1, _0_1, _0_1,             _2_0), price(1, 70),   new Coin[] {_2_0},             new Coin[] {_0_1, _0_1, _0_1}},
                new Object[] {coins(                  _0_1, _0_2, _2_0), price(1, 70),   new Coin[] {_2_0},             new Coin[] {_0_1, _0_2}},
                new Object[] {coins(_0_1, _0_1, _0_1, _0_1, _0_2, _2_0), price(1, 70),   new Coin[] {_2_0},             new Coin[] {_0_1, _0_2}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _2_0, _5_0), price(1, 60),   new Coin[] {_2_0},             new Coin[] {_0_2, _0_2}},
                new Object[] {coins(_0_1, _0_2, _0_2, _0_5, _2_0),       price(1, 50),   new Coin[] {_2_0},             new Coin[] {_0_5}},
                new Object[] {coins(_0_1, _0_2, _0_2, _0_2, _0_5, _5_0), price(1, 40),   new Coin[] {_2_0},             new Coin[] {_0_1, _0_5}},
                new Object[] {coins(_0_5, _0_5, _1_0),                   price(1, 0),    new Coin[] {_2_0},             new Coin[] {_1_0}},
                new Object[] {coins(_0_5, _0_5, _1_0),                   price(0, 50),   new Coin[] {_2_0},             new Coin[] {_0_5, _1_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), price(3, 10),   new Coin[] {_2_0, _1_0, _0_5}, new Coin[] {_0_2, _0_2}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), price(3, 10),   new Coin[] {_1_0, _2_0, _0_5}, new Coin[] {_0_2, _0_2}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), price(3, 10),   new Coin[] {_0_5, _1_0, _2_0}, new Coin[] {_0_2, _0_2}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), price(3, 10),   new Coin[] {_2_0, _0_5, _1_0}, new Coin[] {_0_2, _0_2}},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), price(0, 90),   new Coin[] {_5_0},             new Coin[] {_0_1, _2_0, _2_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), price(0, 90),   new Coin[] {_0_1, _5_0},       new Coin[] {_0_2, _2_0, _2_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), price(0, 90),   new Coin[] {_0_1, _0_2, _5_0}, new Coin[] {_0_1, _0_1, _0_2, _2_0, _2_0}}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_CoinToInsert() {
        return new Object[][] {
                new Object[] {Coin._5_0},
                new Object[] {Coin._2_0},
                new Object[] {Coin._1_0},
                new Object[] {Coin._0_5},
                new Object[] {Coin._0_2},
                new Object[] {Coin._0_1}
        };
    }
}
