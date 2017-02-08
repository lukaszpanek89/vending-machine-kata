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
    public void should_NotifyListenersAndReturnRightProduct_When_ProductTaken() {
        // given
        ProductType productType = anyProductType();
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(productType);
        ProductDispenserListener listener1 = mock(ProductDispenserListener.class);
        ProductDispenserListener listener2 = mock(ProductDispenserListener.class);

        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);
        productDispenser.addListener(listener1);
        productDispenser.addListener(listener2);
        productDispenser.dispenseProductFromShelve(2);

        // when
        Product product = productDispenser.takeProduct();

        // then
        verify(listener1).onProductTaken();
        verify(listener2).onProductTaken();
        assertThat(product).isNotNull();
        assertThat(product.getType()).isEqualTo(productType);
    }

    @Test
    public void should_ThrowException_When_TriesToTakeTheSameProductTwice() {
        // given
        ProductType productType = anyProductType();
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(productType);

        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);
        productDispenser.dispenseProductFromShelve(2);
        productDispenser.takeProduct();

        // when
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
