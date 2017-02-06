package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.payment.Coin.*;
import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.payment.*;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.*;
import lpanek.tdd.vendingMachine.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

@RunWith(JUnitParamsRunner.class)
public class VendingMachineTest {

    private static final String EXCEPTION_MESSAGE = "exception message";

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_ShowSelectProduct_When_MachineJustStarted() {
        // given
        Display displayMock = mock(Display.class);

        // when
        new VendingMachineBuilder().withDisplay(displayMock).build();

        // then
        verify(displayMock).showSelectProduct();
    }

    @Test
    public void should_ShowShelveIsEmpty_When_TriesToSelectProductFromEmptyShelve() {
        // given
        Display displayMock = mock(Display.class);
        Shelves shelves = shelves(emptyShelve());
        VendingMachine vendingMachine = new VendingMachineBuilder().withDisplay(displayMock).withShelves(shelves).build();

        // when
        vendingMachine.selectProduct(1);

        // then
        verify(displayMock).showShelveIsEmpty();
    }

    @Test
    public void should_ThrowException_When_TriesToSelectProductUsingInvalidShelveNumber() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        doThrow(new InvalidShelveNumberException(EXCEPTION_MESSAGE)).when(shelvesMock).getProductTypeOnShelve(1);
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

        // when
        Throwable caughtThrowable = catchThrowable(() -> vendingMachine.selectProduct(1));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    @Parameters(method = "getTestData_ProductPrice")
    public void should_ShowInsertMoney_When_ProductJustSelected(Money productPrice) {
        // given
        Display displayMock = mock(Display.class);
        ProductType productType = anyProductTypeWithPrice(productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(productType);

        VendingMachine vendingMachine = new VendingMachineBuilder().withDisplay(displayMock).withShelves(shelvesMock).build();

        // when
        vendingMachine.selectProduct(2);

        // then
        verify(displayMock).showInsertMoney(productPrice);
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinToInsert")
    public void should_ShowInsertMoney_When_FirstCoinInserted(Money productPrice, Coin coinToInsert) {
        // given
        Display displayMock = mock(Display.class);
        ProductType productType = anyProductTypeWithPrice(productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(productType);
        Coins coins = coins(_5_0, _2_0);

        VendingMachine vendingMachine = new VendingMachineBuilder().withDisplay(displayMock).withShelves(shelvesMock).withCoins(coins).build();
        vendingMachine.selectProduct(2);

        // when
        vendingMachine.insertCoin(coinToInsert);

        // then
        verify(displayMock).showInsertMoney(productPrice.minus(coinToInsert.getValue()));
        assertThat(vendingMachine.getCoins()).isEqualTo(coins.plus(coinToInsert));
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinsToInsert")
    public void should_DispenseProductAndShowTakeProduct_When_ExactProductPriceInserted(Money productPrice, Coin[] coinsToInsert) {
        // given
        Display displayMock = mock(Display.class);
        ProductDispenser productDispenserMock = mock(ProductDispenser.class);
        ProductType productType = anyProductTypeWithPrice(productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(productType);
        Coins coins = coins(_2_0, _0_5);

        VendingMachine vendingMachine = new VendingMachineBuilder()
                .withDisplay(displayMock).withProductDispenser(productDispenserMock).withShelves(shelvesMock).withCoins(coins)
                .build();
        vendingMachine.selectProduct(2);

        // when
        for (Coin coin : coinsToInsert) {
            vendingMachine.insertCoin(coin);
        }

        // then
        verify(displayMock, times(coinsToInsert.length)).showInsertMoney(any(Money.class));
        verify(displayMock).showTakeProduct();
        verify(productDispenserMock).dispenseProductFromShelve(2);
        assertThat(vendingMachine.getCoins()).isEqualTo(coins.plus(coinsToInsert));
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinsToInsert")
    public void should_ReturnRightProductAndShowSelectProduct_When_BoughtProductTaken(Money productPrice, Coin[] coinsToInsert) {
        // given
        Display displayMock = mock(Display.class);
        ProductType productType = anyProductTypeWithPrice(productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(productType);
        Coins coins = coins(_2_0, _0_5);

        VendingMachine vendingMachine = new VendingMachineBuilder().withDisplay(displayMock).withShelves(shelvesMock).withCoins(coins).build();
        vendingMachine.selectProduct(2);
        for (Coin coin : coinsToInsert) {
            vendingMachine.insertCoin(coin);
        }

        // when
        Product product = vendingMachine.takeProduct();

        // then
        assertThat(product).isNotNull();
        assertThat(product.getType()).isEqualTo(productType);
        verify(displayMock, times(2)).showSelectProduct();
        verify(shelvesMock).removeProductFromShelve(2);
        assertThat(vendingMachine.getCoins()).isEqualTo(coins.plus(coinsToInsert));
    }

    @Test
    public void should_ThrowException_When_TriesToGetProductTypeUsingInvalidShelveNumber() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        doThrow(new InvalidShelveNumberException(EXCEPTION_MESSAGE)).when(shelvesMock).getProductTypeOnShelve(1);
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

        // when
        Throwable caughtThrowable = catchThrowable(() -> vendingMachine.getProductTypeOnShelve(1));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void should_ThrowException_When_TriesToGetProductTypeFromEmptyShelve() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        doThrow(new EmptyShelveException(EXCEPTION_MESSAGE)).when(shelvesMock).getProductTypeOnShelve(1);
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

        // when
        Throwable caughtThrowable = catchThrowable(() -> vendingMachine.getProductTypeOnShelve(1));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(EmptyShelveException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void should_ThrowException_When_TriesToGetProductCountUsingInvalidShelveNumber() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        doThrow(new InvalidShelveNumberException(EXCEPTION_MESSAGE)).when(shelvesMock).getProductCountOnShelve(1);
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

        // when
        Throwable caughtThrowable = catchThrowable(() -> vendingMachine.getProductCountOnShelve(1));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage(EXCEPTION_MESSAGE);
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductPrice() {
        return new Object[][]{
                new Object[] {price(5, 40)},
                new Object[] {price(3, 0)},
                new Object[] {price(0, 60)}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductPriceAndCoinToInsert() {
        return new Object[][]{
                new Object[] {price(5, 40), _2_0},
                new Object[] {price(4, 20), _0_2},
                new Object[] {price(1, 10), _0_5}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductPriceAndCoinsToInsert() {
        return new Object[][]{
                new Object[] {price(2, 0),  new Coin[] {_2_0}},
                new Object[] {price(0, 50), new Coin[] {_0_5}},
                new Object[] {price(5, 40), new Coin[] {_5_0, _0_2, _0_2}},
                new Object[] {price(5, 40), new Coin[] {_2_0, _2_0, _1_0, _0_2, _0_1, _0_1}},
                new Object[] {price(5, 40), new Coin[] {_1_0, _0_1, _0_1, _2_0, _2_0, _0_2}}
        };
    }
}
