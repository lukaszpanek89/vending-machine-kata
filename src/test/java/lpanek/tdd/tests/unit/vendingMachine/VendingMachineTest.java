package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.payment.Coin.*;
import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import java.util.Optional;

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
        VendingMachine vendingMachine = new VendingMachineBuilder().build();

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
    }

    @Test
    public void should_ShowShelveIsEmpty_When_TriesToSelectProductFromEmptyShelve() {
        // given
        Shelves shelves = shelves(emptyShelve());
        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelves).build();

        // when
        vendingMachine.selectProduct(1);

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Shelve is empty.");
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
    @Parameters(method = "getTestData_ProductPriceAndDisplayedMessage")
    public void should_ShowInsertMoney_When_ProductJustSelected(Money productPrice, String displayedMessage) {
        // given
        ProductType sandwichType = productType("Sandwich", productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(Optional.of(sandwichType));

        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

        // when
        vendingMachine.selectProduct(2);

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo(displayedMessage);
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinToInsertAndDisplayedMessage")
    public void should_ShowInsertMoney_When_FirstCoinInserted(Money productPrice, Coin coinToInsert, String displayedMessage) {
        // given
        ProductType sandwichType = productType("Sandwich", productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(Optional.of(sandwichType));
        Coins coins = coins(_5_0, _2_0);

        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).withCoins(coins).build();
        vendingMachine.selectProduct(2);

        // when
        vendingMachine.insertCoin(coinToInsert);

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo(displayedMessage);
        assertThat(vendingMachine.getCoins()).isEqualTo(coins.plus(coinToInsert));
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinsToInsert")
    public void should_ShowTakeYourProduct_When_ExactProductPriceInserted(Money productPrice, Coin[] coinsToInsert) {
        // given
        ProductType sandwichType = productType("Sandwich", productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(Optional.of(sandwichType));
        Coins coins = coins(_2_0, _0_5);

        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).withCoins(coins).build();
        vendingMachine.selectProduct(2);

        // when
        for (Coin coin : coinsToInsert) {
            vendingMachine.insertCoin(coin);
        }

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Take your product.");
        assertThat(vendingMachine.getCoins()).isEqualTo(coins.plus(coinsToInsert));
    }

    @Test
    @Parameters(method = "getTestData_ProductPriceAndCoinsToInsert")
    public void should_ReturnRightProductAndShowSelectProduct_When_BoughtProductTaken(Money productPrice, Coin[] coinsToInsert) {
        // given
        ProductType sandwichType = productType("Sandwich", productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(Optional.of(sandwichType));
        Coins coins = coins(_2_0, _0_5);

        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).withCoins(coins).build();
        vendingMachine.selectProduct(2);
        for (Coin coin : coinsToInsert) {
            vendingMachine.insertCoin(coin);
        }

        // when
        Product product = vendingMachine.takeProduct();

        // then
        verify(shelvesMock).removeProductFromShelve(2);
        assertThat(product).isNotNull();
        assertThat(product.getType()).isEqualTo(sandwichType);
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
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
    private Object[][] getTestData_ProductPriceAndDisplayedMessage() {
        return new Object[][]{
                new Object[] {price(5, 40), "Insert 5.40 zł."},
                new Object[] {price(3, 0),  "Insert 3.00 zł."},
                new Object[] {price(0, 60), "Insert 0.60 zł."}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductPriceAndCoinToInsertAndDisplayedMessage() {
        return new Object[][]{
                new Object[] {price(5, 40), _2_0, "Insert 3.40 zł."},
                new Object[] {price(4, 20), _0_2, "Insert 4.00 zł."},
                new Object[] {price(1, 10), _0_5, "Insert 0.60 zł."}
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
