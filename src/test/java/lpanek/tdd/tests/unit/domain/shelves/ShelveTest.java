package lpanek.tdd.tests.unit.domain.shelves;

import static lpanek.tdd.tests.util.ConstructingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelve;
import lpanek.tdd.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.domain.shelves.ex.InvalidProductCountException;

@RunWith(JUnitParamsRunner.class)
public class ShelveTest {

    @Test
    @Parameters(method = "getTestData_ProductTypeAndCount")
    public void should_HaveSpecifiedProductTypeAndCount_When_ConstructedNotEmpty(ProductType productType, int productCount) {
        // when
        Shelve shelve = new Shelve(productType, productCount);

        // then
        assertThat(shelve.getProductType()).isEqualTo(productType);
        assertThat(shelve.getProductCount()).isEqualTo(productCount);
    }

    @Test
    public void should_BeEmpty_When_ConstructedEmpty() {
        // when
        Shelve shelve = new Shelve();

        // then
        assertThat(shelve.getProductCount()).isEqualTo(0);

        // when
        Throwable caughtThrowable = catchThrowable(shelve::getProductType);

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(EmptyShelveException.class)
                .hasMessage("Cannot get product type from empty shelve.");
    }

    @Test
    @Parameters(method = "getTestData_InvalidProductCountAndExceptionMessage")
    public void should_ThrowException_When_TriesToConstructShelveWithInvalidProductCount(int invalidProductCount, String exceptionMessage) {
        // when
        Throwable caughtThrowable = catchThrowable(() -> new Shelve(anyProductType(), invalidProductCount));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidProductCountException.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    public void should_RemoveProduct_When_ShelveHasManyProducts() {
        // given
        ProductType productType = anyProductType();
        Shelve shelve = new Shelve(productType, 3);

        // when
        shelve.removeProduct();

        // then
        assertThat(shelve.getProductType()).isEqualTo(productType);
        assertThat(shelve.getProductCount()).isEqualTo(2);
    }

    @Test
    public void should_RemoveProduct_When_ShelveHasOneProduct() {
        // given
        Shelve shelve = new Shelve(anyProductType(), 1);

        // when
        shelve.removeProduct();

        // then
        assertThat(shelve.getProductCount()).isEqualTo(0);
    }

    @Test
    public void should_ThrowException_When_TriesToRemoveProductFromEmptyShelve() {
        // when
        Throwable caughtThrowable = catchThrowable(() -> new Shelve().removeProduct());

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(EmptyShelveException.class)
                .hasMessage("Cannot remove product from empty shelve.");
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductTypeAndCount() {
        return new Object[][]{
                new Object[] {productType("Apple juice", anyPrice()),        3},
                new Object[] {productType("Raspberry juice", anyPrice()),    5},
                new Object[] {productType("Blackcurrant juice", anyPrice()), 9}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_InvalidProductCountAndExceptionMessage() {
        return new Object[][]{
                new Object[] {-1, "-1 is an invalid product count."},
                new Object[] {0, "0 is an invalid product count."}
        };
    }
}
