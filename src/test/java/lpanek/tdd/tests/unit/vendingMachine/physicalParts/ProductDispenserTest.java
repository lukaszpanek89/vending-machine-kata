package lpanek.tdd.tests.unit.vendingMachine.physicalParts;

import static lpanek.tdd.tests.util.ConstructingUtil.anyProductType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.physicalParts.ProductDispenser;
import lpanek.tdd.vendingMachine.physicalParts.ex.NoProductToTakeException;
import lpanek.tdd.vendingMachine.physicalParts.ex.PreviousProductNotYetTakenException;
import lpanek.tdd.vendingMachine.physicalParts.listeners.ProductDispenserListener;

@RunWith(MockitoJUnitRunner.class)
public class ProductDispenserTest {

    @Test
    public void should_ReturnRightProductAndNotifyListeners_When_ProductTaken() {
        // given
        ProductType productType = anyProductType();
        Shelves shelvesMock = mock(Shelves.class);
        when(shelvesMock.getProductTypeOnShelve(2)).thenReturn(productType);
        Product expectedProduct = new Product(productType);

        ProductDispenserListener listener1Mock = mock(ProductDispenserListener.class);
        ProductDispenserListener listener2Mock = mock(ProductDispenserListener.class);

        ProductDispenser productDispenser = new ProductDispenser(shelvesMock);
        productDispenser.addListener(listener1Mock);
        productDispenser.addListener(listener2Mock);

        // when
        productDispenser.dispenseProductFromShelve(2);
        Product actualProduct = productDispenser.takeProduct();

        // then
        verify(listener1Mock).onProductTaken();
        verify(listener2Mock).onProductTaken();
        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    public void should_ThrowException_When_TriesToDispenseProductWhilePreviousProductWasNotYetTaken() {
        // given
        ProductType productType = anyProductType();
        Shelves shelvesMock = mock(Shelves.class);
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
}
