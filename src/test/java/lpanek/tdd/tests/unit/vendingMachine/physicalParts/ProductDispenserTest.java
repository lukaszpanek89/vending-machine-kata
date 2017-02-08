package lpanek.tdd.tests.unit.vendingMachine.physicalParts;

import static lpanek.tdd.tests.util.ConstructingUtil.anyProductType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.physicalParts.ProductDispenser;
import lpanek.tdd.vendingMachine.physicalParts.ex.NoProductToTakeException;
import lpanek.tdd.vendingMachine.physicalParts.ex.PreviousProductNotYetTakenException;
import lpanek.tdd.vendingMachine.physicalParts.listeners.ProductDispenserListener;

@RunWith(JUnitParamsRunner.class)
public class ProductDispenserTest {

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(method = "getTestData_ShelveNumber")
    public void should_NotifyListeners_When_ProductDispensed(int shelveNumber) {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(shelveNumber)).thenReturn(anyProductType());
        ProductDispenserListener listener1 = mock(ProductDispenserListener.class);
        ProductDispenserListener listener2 = mock(ProductDispenserListener.class);

        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);
        productDispenser.addListener(listener1);
        productDispenser.addListener(listener2);

        // when
        productDispenser.dispenseProductFromShelve(shelveNumber);

        // then
        verify(listener1).onProductDispensed();
        verify(listener2).onProductDispensed();
    }

    @Test
    public void should_ThrowException_When_TriesToDispenseProductWhilePreviousProductWasNotYetTaken() {
        // given
        ProductType productType = anyProductType();
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(productType);
        Product previousProduct = new Product(productType);

        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);
        productDispenser.setDispensedProduct(previousProduct);

        // when
        Throwable caughtThrowable = catchThrowable(() -> productDispenser.dispenseProductFromShelve(2));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(PreviousProductNotYetTakenException.class)
                .hasMessage("Previously dispensed product was not yet taken.");
    }

    @Test
    public void should_NotifyListeners_When_ProductTaken() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        Product product = new Product(anyProductType());
        ProductDispenserListener listener1 = mock(ProductDispenserListener.class);
        ProductDispenserListener listener2 = mock(ProductDispenserListener.class);

        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);
        productDispenser.setDispensedProduct(product);
        productDispenser.addListener(listener1);
        productDispenser.addListener(listener2);

        // when
        productDispenser.takeProduct();

        // then
        verify(listener1).onProductTaken();
        verify(listener2).onProductTaken();
    }

    @Test
    public void should_ReturnRightProduct_When_ProductTaken() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        Product expectedProduct = new Product(anyProductType());

        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);
        productDispenser.setDispensedProduct(expectedProduct);

        // when
        Product actualProduct = productDispenser.takeProduct();

        // then
        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    public void should_ThrowException_When_TriesToTakeProductWithoutDispense() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);

        // when
        Throwable caughtThrowable = catchThrowable(productDispenser::takeProduct);

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(NoProductToTakeException.class)
                .hasMessage("There is no product to take.");
    }

    @Test
    public void should_ThrowException_When_TriesToTakeDispensedProductTwice() {
        // given
        Shelves shelvesMock = mock(Shelves.class);
        Product product = new Product(anyProductType());

        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);
        productDispenser.setDispensedProduct(product);

        // when
        productDispenser.takeProduct();
        Throwable caughtThrowable = catchThrowable(productDispenser::takeProduct);

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(NoProductToTakeException.class)
                .hasMessage("There is no product to take.");
    }

    @SuppressWarnings("unused")
    private Integer[] getTestData_ShelveNumber() {
        return new Integer[] {
                1,
                2,
                3,
                4,
                5,
                6
        };
    }
}
