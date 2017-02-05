package lpanek.tdd.tests.unit.vendingMachine;

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
    @Parameters(method = "getTestData_ProductPriceAndDisplayedMessage")
    public void should_ShowInsertValueThatEqualsProductPrice_When_ProductJustSelected(Money productPrice, String displayedMessage) {
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
    public void should_ShowInsertValueThatIsProductPriceLoweredByInsertedCoinsValue_When_ProductSelectedAndCoinsInserted(Money productPrice, Coin coinToInsert, String displayedMessage) {
        // given
        ProductType sandwichType = productType("Sandwich", productPrice);
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(Optional.of(sandwichType));
        Coins coins = coins(Coin._5_0, Coin._2_0);

        VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).withCoins(coins).build();
        vendingMachine.selectProduct(2);

        // when
        vendingMachine.insertCoin(coinToInsert);

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo(displayedMessage);
        assertThat(vendingMachine.getCoins()).isEqualTo(coins.plus(coinToInsert));
    }

    @Test
    public void should_ThrowException_When_ShelvesGetProductTypeOnShelveThrowsInvalidShelveNumberException() {
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
    public void should_ThrowException_When_ShelvesGetProductCountOnShelveThrowsInvalidShelveNumberException() {
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
                new Object[] {price(5, 40), Coin._2_0, "Insert 3.40 zł."},
                new Object[] {price(4, 20), Coin._0_2, "Insert 4.00 zł."},
                new Object[] {price(1, 10), Coin._0_5, "Insert 0.60 zł."}
        };
    }
}
