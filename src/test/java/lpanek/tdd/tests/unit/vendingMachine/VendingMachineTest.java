package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lpanek.tdd.payment.*;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.*;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {

    private static final String EXCEPTION_MESSAGE = "exception message";

    @Test
    public void should_ShowSelectProduct_When_MachineJustStarted() {
        // given
        VendingMachine vendingMachine = new VendingMachineBuilder().build();

        // then
        assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo("Select product.");
    }

    @Test
    public void should_ShowInsertValueThatEqualsProductPrice_When_ProductJustSelected() {
        Object[][] testTuples = getTestTuplesConsistingOfProductPriceAndDisplayedMessage();

        for (Object[] testTuple : testTuples) {
            // given
            Money productPrice = (Money) testTuple[0];
            String displayedMessage = (String) testTuple[1];

            ProductType sandwichType = productType("Sandwich", productPrice);
            Shelves shelvesMock = mock(Shelves.class);
            when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(Optional.of(sandwichType));

            VendingMachine vendingMachine = new VendingMachineBuilder().withShelves(shelvesMock).build();

            // when
            vendingMachine.selectProduct(2);

            // then
            assertThat(vendingMachine.getMessageOnDisplay()).isEqualTo(displayedMessage);
        }
    }

    private Object[][] getTestTuplesConsistingOfProductPriceAndDisplayedMessage() {
        return new Object[][]{
                new Object[] {price(5, 40), "Insert 5.40 zł."},
                new Object[] {price(3, 0),  "Insert 3.00 zł."},
                new Object[] {price(0, 60), "Insert 0.60 zł."}
        };
    }

    @Test
    public void should_ShowInsertValueThatIsProductPriceLowerByInsertedCoinsValue_When_ProductSelectedAndCoinsInserted() {
        Object[][] testTuples = getTestTuplesConsistingOfProductPriceAndCoinToInsertAndDisplayedMessage();

        for (Object[] testTuple : testTuples) {
            // given
            Money productPrice = (Money) testTuple[0];
            Coin coinToInsert = (Coin) testTuple[1];
            String displayedMessage = (String) testTuple[2];

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
    }

    private Object[][] getTestTuplesConsistingOfProductPriceAndCoinToInsertAndDisplayedMessage() {
        return new Object[][]{
                new Object[] {price(5, 40), Coin._2_0, "Insert 3.40 zł."},
                new Object[] {price(4, 20), Coin._0_2, "Insert 4.00 zł."},
                new Object[] {price(1, 10), Coin._0_5, "Insert 0.60 zł."}
        };
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
}
