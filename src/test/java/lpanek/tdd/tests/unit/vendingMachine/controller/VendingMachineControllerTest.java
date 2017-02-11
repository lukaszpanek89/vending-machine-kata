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
import lpanek.tdd.domain.payment.strategy.HighestDenominationFirstStrategy;
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
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_5_0, _2_0);

        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock).with(initialCoins)
                .withProductSelected(2).build();

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verify(displayMock).showInsertMoney(productPrice.minus(coinToInsert.getValue()));
        assertThat(controller.getCoins()).isEqualTo(initialCoins.plus(coinToInsert));
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinsForProduct")
    public void should_DispenseProduct_When_ExactProductPriceInserted(Money productPrice, Coin[] coinsToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_2_0, _0_5);

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
        verify(productDispenserMock).dispenseProductFromShelve(2);
        verify(coinsDispenserMock, never()).dispenseCoins(any(Coins.class));
        assertThat(controller.getCoins()).isEqualTo(initialCoins.plus(coinsToInsert));
    }

    @Test
    @Parameters(method = "getTestData_InitialCoinsAndProductPriceAndCoinsForProduct")
    public void should_DispenseProductAndChange_When_MoreMoneyThanProductPriceInserted(Coins initialCoins, Money productPrice, Coin[] coinsToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);

        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));

        Coins expectedCoins = initialCoins.plus(coinsToInsert);
        Money expectedChangeValue = coins(coinsToInsert).getValue().minus(productPrice);
        HighestDenominationFirstStrategy changeStrategy = new HighestDenominationFirstStrategy();
        Coins expectedChange = changeStrategy.determineChange(expectedCoins, expectedChangeValue);

        VendingMachineController controller = controllerBuilder()
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).with(initialCoins).with(changeStrategy)
                .withProductSelected(2).build();

        // when
        for (Coin coin : coinsToInsert) {
            controller.onCoinInserted(coin);
        }

        // then
        verify(displayMock, times(coinsToInsert.length - 1)).showInsertMoney(any(Money.class));
        verify(productDispenserMock).dispenseProductFromShelve(2);
        verify(coinsDispenserMock).dispenseCoins(expectedChange);
        assertThat(controller.getCoins()).isEqualTo(expectedCoins);
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinsForProduct")
    public void should_ShowTakeProduct_When_ProductDispensed(Money productPrice, Coin[] coinsInserted) {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_2_0, _0_5).plus(coinsInserted);

        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock).with(initialCoins)
                .withProductSelected(2)
                .withCoinsForSelectedProductInserted(coins(coinsInserted)).build();

        // when
        controller.onProductDispensed();

        // then
        verify(displayMock).showTakeProduct();
        verify(shelvesMock).removeProductFromShelve(2);
        assertThat(controller.getCoins()).isEqualTo(initialCoins);
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinsForProduct")
    public void should_ShowSelectProduct_When_ProductTaken(Money productPrice, Coin[] coinsInserted) {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_2_0, _0_5).plus(coinsInserted);

        VendingMachineController controller = controllerBuilder().with(displayMock).with(shelvesMock).with(initialCoins)
                .withProductSelected(2)
                .withCoinsForSelectedProductInserted(coins(coinsInserted)).build();

        // when
        controller.onProductTaken();

        // then
        verify(displayMock, times(2)).showSelectProduct();
        assertThat(controller.getCoins()).isEqualTo(initialCoins);
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
    private Object[][] getTestData_ProductPriceAndCoinsForProduct() {
        return new Object[][] {
                new Object[] {price(2, 0),  new Coin[] {_2_0}},
                new Object[] {price(0, 50), new Coin[] {_0_5}},
                new Object[] {price(5, 40), new Coin[] {_5_0, _0_2, _0_2}},
                new Object[] {price(5, 40), new Coin[] {_2_0, _2_0, _1_0, _0_2, _0_1, _0_1}},
                new Object[] {price(5, 40), new Coin[] {_1_0, _0_1, _0_1, _2_0, _2_0, _0_2}}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_InitialCoinsAndProductPriceAndCoinsForProduct() {
        return new Object[][] {
                new Object[] {coins(_0_1, _0_2, _5_0),                   price(1, 90), new Coin[] {_2_0}},
                new Object[] {coins(_0_1, _0_1,       _0_5),             price(1, 80), new Coin[] {_2_0}},
                new Object[] {coins(            _0_2, _0_5),             price(1, 80), new Coin[] {_2_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_5),             price(1, 80), new Coin[] {_2_0}},
                new Object[] {coins(_0_1, _0_1, _0_1,             _2_0), price(1, 70), new Coin[] {_2_0}},
                new Object[] {coins(                  _0_1, _0_2, _2_0), price(1, 70), new Coin[] {_2_0}},
                new Object[] {coins(_0_1, _0_1, _0_1, _0_1, _0_2, _2_0), price(1, 70), new Coin[] {_2_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _2_0, _5_0), price(1, 60), new Coin[] {_2_0}},
                new Object[] {coins(_0_1, _0_2, _0_2, _0_5, _2_0),       price(1, 50), new Coin[] {_2_0}},
                new Object[] {coins(_0_1, _0_2, _0_2, _0_2, _0_5, _5_0), price(1, 40), new Coin[] {_2_0}},
                new Object[] {coins(_0_5, _0_5, _1_0),                   price(1, 0),  new Coin[] {_2_0}},
                new Object[] {coins(_0_5, _0_5, _1_0),                   price(0, 50), new Coin[] {_2_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), price(3, 10), new Coin[] {_2_0, _1_0, _0_5}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), price(3, 10), new Coin[] {_1_0, _2_0, _0_5}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), price(3, 10), new Coin[] {_0_5, _2_0, _2_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _0_2, _0_5, _5_0), price(3, 10), new Coin[] {_2_0, _0_5, _1_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), price(0, 90), new Coin[] {_5_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), price(0, 90), new Coin[] {_0_1, _5_0}},
                new Object[] {coins(_0_1, _0_1, _0_2, _2_0, _2_0, _5_0), price(0, 90), new Coin[] {_0_1, _0_2, _5_0}}
        };
    }
}
