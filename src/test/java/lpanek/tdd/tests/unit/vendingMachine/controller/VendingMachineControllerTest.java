package lpanek.tdd.tests.unit.vendingMachine.controller;

import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.*;
import static lpanek.tdd.tests.vendingMachine.util.VendingMachineControllerBuilder.controllerBuilder;
import static lpanek.tdd.tests.vendingMachine.util.VendingMachineModelBuilder.modelBuilder;
import static lpanek.tdd.vendingMachine.domain.VendingMachineModel.MachineState.InsertedCoinsDispensed;
import static lpanek.tdd.vendingMachine.domain.VendingMachineModel.MachineState.ProductAndOptionallyChangeDispensed;
import static lpanek.tdd.vendingMachine.domain.VendingMachineModel.MachineState.ProductSelected;
import static lpanek.tdd.vendingMachine.domain.payment.Coin.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.controller.VendingMachineController;
import lpanek.tdd.vendingMachine.domain.VendingMachineModel;
import lpanek.tdd.vendingMachine.domain.payment.*;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ChangeDeterminingStrategy;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ex.UnableToDetermineChangeException;
import lpanek.tdd.vendingMachine.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.domain.shelves.ex.InvalidShelveNumberException;
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
        VendingMachineModel model = modelBuilder().build();
        VendingMachineController controller = controllerBuilder(model)
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
        VendingMachineModel model = modelBuilder().build();
        controllerBuilder(model).with(displayMock).build();

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

        VendingMachineModel model = modelBuilder().with(shelvesMock).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).with(shelvesMock).build();

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

        VendingMachineModel model = modelBuilder().with(shelvesMock)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).with(shelvesMock).build();
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
        VendingMachineModel model = modelBuilder().with(shelves).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).with(shelves).build();

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
        VendingMachineModel model = modelBuilder().with(shelvesMock).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).with(shelvesMock).build();

        // when
        controller.onKeyPressed(Key._1);

        // then
        verify(displayMock).showInternalError();
    }

    @Test
    @Parameters(method = "getTestData_ProductPrice_CoinToInsert")
    public void should_ShowInsertMoney_When_FirstCoinInserted(Money productPrice, Coin coinToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_5_0, _2_0);
        Coins coinsAfterInsertion = initialCoins.plus(coinToInsert);

        VendingMachineModel model = modelBuilder().with(shelvesMock).with(initialCoins)
                .withState(ProductSelected)
                .withSelectedShelveNumber(2).build();
        VendingMachineController controller = controllerBuilder(model)
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).build();

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verify(displayMock).showInsertMoney(productPrice.minus(coinToInsert.getValue()));
        verify(coinsDispenserMock, never()).dispenseCoins(any(Coins.class));
        verify(productDispenserMock, never()).dispenseProductFromShelve(anyInt());
        assertThat(model.getTotalCoins()).isNotNull();
        assertThat(model.getTotalCoins()).isEqualTo(coinsAfterInsertion);
    }

    @Test
    @Parameters(method = "getTestData_ProductPrice_ExactCoinsToInsert")
    public void exactPayment_should_DispenseProductAndShowTakeIt_When_ExactCoinsInserted(Money productPrice, Coin[] coinsToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));
        Coins initialCoins = coins(_2_0, _0_5);
        Coins coinsAfterInsertions = initialCoins.plus(coinsToInsert);

        VendingMachineModel model = modelBuilder().with(shelvesMock).with(initialCoins)
                .withState(ProductSelected)
                .withSelectedShelveNumber(2).build();
        VendingMachineController controller = controllerBuilder(model)
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).build();

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
        assertThat(model.getTotalCoins()).isNotNull();
        assertThat(model.getTotalCoins()).isEqualTo(coinsAfterInsertions);
    }

    @Test
    public void exactPayment_should_ShowSelectProduct_When_ProductTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder()
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(false)
                .withWaitingForProductToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verify(displayMock).showSelectProduct();
    }

    @Test
    @Parameters(method = "getTestData_InitialCoins_ProductPrice_ExcessiveCoinsToInsert_Change")
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

        VendingMachineModel model = modelBuilder().with(shelvesMock).with(initialCoins).with(changeStrategyMock)
                .withState(ProductSelected)
                .withSelectedShelveNumber(2).build();
        VendingMachineController controller = controllerBuilder(model)
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).build();

        reset(displayMock);

        // when
        for (Coin coin : coinsToInsert) {
            controller.onCoinInserted(coin);
        }

        // then
        verify(displayMock, times(coinsToInsert.length - 1)).showInsertMoney(any(Money.class));
        verify(displayMock).showTakeProductAndChange();
        verify(changeStrategyMock, atLeastOnce()).determineChange(coinsAfterInsertions, change.getValue());
        verify(coinsDispenserMock).dispenseCoins(change);
        verify(productDispenserMock).dispenseProductFromShelve(2);
        verify(shelvesMock).removeProductFromShelve(2);
        assertThat(model.getTotalCoins()).isNotNull();
        assertThat(model.getTotalCoins()).isEqualTo(coinsAfterChangeDispense);
    }

    @Test
    public void overPayment_should_ShowSelectProduct_When_ProductAndChangeTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder()
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
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
        VendingMachineModel model = modelBuilder()
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
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
        VendingMachineModel model = modelBuilder()
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onCoinsTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    @Parameters(method = "getTestData_InitialCoins_ProductPrice_ExcessiveCoinsToInsert_IndeterminableChangeValue")
    public void overPayment_should_RejectInsertedCoins_When_UnableToGiveChange(Coins initialCoins, Money productPrice, Coin[] coinsToInsert, Money indeterminableChangeValue) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);

        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(anyProductTypeWithPrice(productPrice));

        Coins coinsAfterInsertions = initialCoins.plus(coinsToInsert);

        ChangeDeterminingStrategy changeStrategyMock = mock(ChangeDeterminingStrategy.class);
        doThrow(UnableToDetermineChangeException.class).when(changeStrategyMock).determineChange(coinsAfterInsertions, indeterminableChangeValue);

        VendingMachineModel model = modelBuilder().with(shelvesMock).with(initialCoins).with(changeStrategyMock)
                .withState(ProductSelected)
                .withSelectedShelveNumber(2).build();
        VendingMachineController controller = controllerBuilder(model)
                .with(displayMock).with(coinsDispenserMock).with(productDispenserMock)
                .with(shelvesMock).build();

        reset(displayMock);

        // when
        for (Coin coin : coinsToInsert) {
            controller.onCoinInserted(coin);
        }

        // then
        verify(displayMock, times(coinsToInsert.length - 1)).showInsertMoney(any(Money.class));
        verify(displayMock).showUnableToGiveChange();
        verify(changeStrategyMock, atLeastOnce()).determineChange(coinsAfterInsertions, indeterminableChangeValue);
        verify(coinsDispenserMock).dispenseCoins(new Coins(coinsToInsert));
        verify(productDispenserMock, never()).dispenseProductFromShelve(2);
        verify(shelvesMock, never()).removeProductFromShelve(2);
        assertThat(model.getTotalCoins()).isNotNull();
        assertThat(model.getTotalCoins()).isEqualTo(initialCoins);
    }

    @Test
    public void should_IgnoreProductSelection_When_ProductAlreadySelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder()
                .withState(ProductSelected).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onKeyPressed(Key._1);

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreProductSelection_When_WaitingForProductAndChangeToBeTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder()
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onKeyPressed(Key._1);

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreProductSelection_When_WaitingForInsertedCoinsToBeTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder()
                .withState(InsertedCoinsDispensed)
                .withWaitingForCoinsToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
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
        VendingMachineModel model = modelBuilder().with(initialCoins).build();
        VendingMachineController controller = controllerBuilder(model)
                .with(displayMock).with(coinsDispenserMock).build();
        reset(displayMock);

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verifyZeroInteractions(displayMock);
        verify(coinsDispenserMock).dispenseCoins(coins(coinToInsert));
        assertThat(model.getTotalCoins()).isNotNull();
        assertThat(model.getTotalCoins()).isEqualTo(initialCoins);
    }

    @Test
    @Parameters(method = "getTestData_CoinToInsert")
    public void should_RejectInsertedCoin_When_WaitingForProductAndChangeToBeTaken(Coin coinToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        Coins initialCoins = coins(_0_5, _1_0);
        VendingMachineModel model = modelBuilder().with(initialCoins)
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).
                with(displayMock).with(coinsDispenserMock).build();
        reset(displayMock);

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verifyZeroInteractions(displayMock);
        verify(coinsDispenserMock).dispenseCoins(coins(coinToInsert));
        assertThat(model.getTotalCoins()).isNotNull();
        assertThat(model.getTotalCoins()).isEqualTo(initialCoins);
    }

    @Test
    @Parameters(method = "getTestData_CoinToInsert")
    public void should_RejectInsertedCoin_When_WaitingForInsertedCoinsToBeTaken(Coin coinToInsert) {
        // given
        Display displayMock = mock(Display.class);
        CoinsDispenser coinsDispenserMock = mock(CoinsDispenser.class);
        Coins initialCoins = coins(_0_5, _1_0);
        VendingMachineModel model = modelBuilder().with(initialCoins)
                .withState(InsertedCoinsDispensed)
                .withWaitingForCoinsToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model)
                .with(displayMock).with(coinsDispenserMock).build();
        reset(displayMock);

        // when
        controller.onCoinInserted(coinToInsert);

        // then
        verifyZeroInteractions(displayMock);
        verify(coinsDispenserMock).dispenseCoins(coins(coinToInsert));
        assertThat(model.getTotalCoins()).isNotNull();
        assertThat(model.getTotalCoins()).isEqualTo(initialCoins);
    }

    @Test
    public void should_IgnoreCoinsTaken_When_ProductNotSelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder().build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
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
        VendingMachineModel model = modelBuilder()
                .withState(ProductSelected).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onCoinsTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_IgnoreCoinsTaken_When_ChangeAlreadyTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder()
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(false)
                .withWaitingForProductToBeTaken(true).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onCoinsTaken();

        // then
        verifyZeroInteractions(displayMock);
    }

    @Test
    public void should_ShowError_WhenProductTakenWhileProductNotSelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder().build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verify(displayMock).showInternalError();
    }

    @Test
    public void should_ShowError_WhenProductTakenWhileProductSelected() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder()
                .withState(ProductSelected).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verify(displayMock).showInternalError();
    }

    @Test
    public void should_ShowError_WhenProductTakenWhileProductAlreadyTaken() {
        // given
        Display displayMock = mock(Display.class);
        VendingMachineModel model = modelBuilder()
                .withState(ProductAndOptionallyChangeDispensed)
                .withWaitingForCoinsToBeTaken(true)
                .withWaitingForProductToBeTaken(false).build();
        VendingMachineController controller = controllerBuilder(model).with(displayMock).build();
        reset(displayMock);

        // when
        controller.onProductTaken();

        // then
        verify(displayMock).showInternalError();
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
    private Object[][] getTestData_ProductPrice_CoinToInsert() {
        return new Object[][] {
                new Object[] {price(5, 40), _2_0},
                new Object[] {price(4, 20), _0_2},
                new Object[] {price(1, 10), _0_5}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductPrice_ExactCoinsToInsert() {
        return new Object[][] {
                new Object[] {price(2, 0),  new Coin[] {_2_0}},
                new Object[] {price(0, 50), new Coin[] {_0_5}},
                new Object[] {price(5, 40), new Coin[] {_5_0, _0_2, _0_2}},
                new Object[] {price(5, 40), new Coin[] {_2_0, _2_0, _1_0, _0_2, _0_1, _0_1}},
                new Object[] {price(5, 40), new Coin[] {_1_0, _0_1, _0_1, _2_0, _2_0, _0_2}}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_InitialCoins_ProductPrice_ExcessiveCoinsToInsert_Change() {
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
    private Object[][] getTestData_InitialCoins_ProductPrice_ExcessiveCoinsToInsert_IndeterminableChangeValue() {
        return new Object[][] {
                //            initial coins           | product price | excessive coins to insert | indeterminable change value
                new Object[] {coins(_0_1),              price(1, 90),   new Coin[] {_5_0},          money(3, 10)},
                new Object[] {coins(_0_5),              price(1, 80),   new Coin[] {_5_0},          money(3, 20)},
                new Object[] {coins(_0_5),              price(1, 80),   new Coin[] {_2_0},          money(0, 20)},
                new Object[] {coins(_0_1, _0_5, _1_0),  price(1, 80),   new Coin[] {_2_0},          money(0, 20)},
                new Object[] {coins(_0_1, _0_5, _2_0),  price(1, 0),    new Coin[] {_2_0},          money(1, 0)},
                new Object[] {coins(_0_1, _0_5, _2_0),  price(0, 50),   new Coin[] {_2_0},          money(1, 50)}
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
