package lpanek.tdd.tests.unit.vendingMachine.controller;

import static lpanek.tdd.domain.payment.Coin.*;
import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static lpanek.tdd.tests.util.VendingMachineControllerBuilder.controllerBuilder;
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

    private static final String EXCEPTION_MESSAGE = "exception message";

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
                .with(keyboardMock).with(coinTakerMock).with(coinsDispenserMock).with(productDispenserMock)
                .build();

        // then
        verify(keyboardMock).addListener(controller);
        verify(coinTakerMock).addListener(controller);
        verify(coinsDispenserMock).addListener(controller);
        verify(productDispenserMock).addListener(controller);
    }

    @Test
    public void should_ShowSelectProduct_When_JustConstructed() {
        // given
        Display displayMock = mock(Display.class);

        // when
        controllerBuilder().with(displayMock).build();

        // then
        verify(displayMock).showSelectProduct();
    }

    @Test
    @Parameters(method = "getTestData_ProductPrice")
    public void should_ShowInsertMoney_When_ProductJustSelected(Money productPrice) {
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
        doThrow(new InvalidShelveNumberException(EXCEPTION_MESSAGE)).when(shelvesMock).getProductTypeOnShelve(1);
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
                .withProductSelected(2).build();

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verify(displayMock).showInsertMoney(productPrice.minus(coinToInsert.getValue()));
        verify(coinsDispenserMock, never()).dispenseCoins(any(Coins.class));
        verify(productDispenserMock, never()).dispenseProductFromShelve(anyInt());
        assertThat(controller.getCoins()).isEqualTo(coinsAfterInsertion);
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndExactCoinsToInsert")
    public void exactPayment_should_DispenseProduct_When_ExactCoinsInserted(Money productPrice, Coin[] coinsToInsert) {
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
                .withProductSelected(2).build();

        // when
        for (Coin coin : coinsToInsert) {
            controller.onCoinInserted(coin);
        }

        // then
        verify(displayMock, times(coinsToInsert.length - 1)).showInsertMoney(any(Money.class));
        verify(coinsDispenserMock, never()).dispenseCoins(any(Coins.class));
        verify(productDispenserMock).dispenseProductFromShelve(2);
        assertThat(controller.getCoins()).isEqualTo(coinsAfterInsertions);
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndExactCoinsToInsert")
    public void exactPayment_should_ShowTakeProduct_When_ProductDispensed(Money productPrice, Coin[] coinsToInsert) {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_2_0, _0_5).plus(coinsToInsert);

        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock).with(initialCoins)
                .withProductSelected(2)
                .withCoinsForSelectedProductInserted(coins(coinsToInsert)).build();

        // when
        controller.onProductDispensed();

        // then
        verify(displayMock).showTakeProduct();
        verify(shelvesMock).removeProductFromShelve(2);
        assertThat(controller.getCoins()).isEqualTo(initialCoins);
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndExactCoinsToInsert")
    public void exactPayment_should_ShowSelectProduct_When_ProductTaken(Money productPrice, Coin[] coinsToInsert) {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_2_0, _0_5).plus(coinsToInsert);

        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock).with(initialCoins)
                .withProductSelected(2)
                .withCoinsForSelectedProductInserted(coins(coinsToInsert)).build();

        // when
        controller.onProductTaken();

        // then
        verify(displayMock, times(2)).showSelectProduct();
        assertThat(controller.getCoins()).isEqualTo(initialCoins);
    }

    @Test
    @Parameters(method = "getTestData_InitialCoinsAndProductPriceAndExcessiveCoinsToInsertAndChange")
    public void overPayment_should_DispenseProductAndChange_When_ExcessiveCoinsInserted(Coins initialCoins, Money productPrice, Coin[] coinsToInsert, Coin[] changeAsArray) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);

        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));

        Coins coinsAfterInsertions = initialCoins.plus(coinsToInsert);
        Coins change = coins(changeAsArray);
        ChangeDeterminingStrategy changeStrategyMock = mock(ChangeDeterminingStrategy.class);
        when(changeStrategyMock.determineChange(coinsAfterInsertions, change.getValue())).thenReturn(change);

        VendingMachineController controller = controllerBuilder()
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).with(initialCoins).with(changeStrategyMock)
                .withProductSelected(2).build();

        // when
        for (Coin coin : coinsToInsert) {
            controller.onCoinInserted(coin);
        }

        // then
        verify(displayMock, times(coinsToInsert.length - 1)).showInsertMoney(any(Money.class));
        verify(changeStrategyMock).determineChange(coinsAfterInsertions, change.getValue());
        verify(coinsDispenserMock).dispenseCoins(change);
        verify(productDispenserMock).dispenseProductFromShelve(2);
        assertThat(controller.getCoins()).isEqualTo(coinsAfterInsertions);
    }

    @Test
    @Parameters(method = "getTestData_InitialCoinsAndProductPriceAndExcessiveCoinsToInsertAndChange")
    public void overPayment_should_ShowTakeProductAndChange_When_ProductAndChangeDispensed(Coins initialCoins, Money productPrice, Coin[] coinsToInsert, Coin[] changeAsArray) {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins coinsAfterInsertions = initialCoins.plus(coinsToInsert);
        Coins coinsAfterChangeDispense = firstMinusSecond(coinsAfterInsertions, changeAsArray);

        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock).with(coinsAfterInsertions)
                .withProductSelected(2)
                .withCoinsForSelectedProductInserted(coins(coinsToInsert)).build();

        // when
        controller.onProductDispensed();
        controller.onCoinsDispensed();

        // then
        verify(displayMock).showTakeProductAndChange();
        verify(shelvesMock).removeProductFromShelve(2);
        assertThat(controller.getCoins()).isEqualTo(coinsAfterChangeDispense);
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
                //            initial coins                            | product price | excessive coins for product  | change
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
}
