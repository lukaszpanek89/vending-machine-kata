package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.anyPrice;
import static lpanek.tdd.tests.util.ConstructingUtil.productType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.Shelve;
import lpanek.tdd.vendingMachine.ex.InvalidProductCountException;

@RunWith(JUnitParamsRunner.class)
public class ShelveTest {

    @Test
    @Parameters(method = "getTestData_ProductTypeAndCount")
    public void should_HaveSpecifiedProductTypeAndCount_When_ConstructedNotEmpty(ProductType productType, int productCount) {
        // when
        Shelve shelve = new Shelve(productType, productCount);

        // then
        assertThat(shelve.getProductType().get()).isEqualTo(productType);
        assertThat(shelve.getProductCount()).isEqualTo(productCount);
    }

    @Test
    public void should_BeEmpty_When_ConstructedEmpty() {
        // when
        Shelve shelve = new Shelve();

        // then
        assertThat(shelve.getProductType().isPresent()).isEqualTo(false);
        assertThat(shelve.getProductCount()).isEqualTo(0);
    }

    @Test
    @Parameters(method = "getTestData_InvalidProductCountAndExceptionMessage")
    public void should_ThrowException_When_TriesToConstructWithInvalidProductCount(int invalidProductCount, String exceptionMessage) {
        // given
        ProductType blackcurrantJuiceType = productType("Blackcurrant juice", anyPrice());

        // when
        Throwable caughtThrowable = catchThrowable(() -> new Shelve(blackcurrantJuiceType, invalidProductCount));

        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidProductCountException.class)
                .hasMessage(exceptionMessage);
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductTypeAndCount() {
        return new Object[][]{
                new Object[] {productType("Apple juice", anyPrice()),        3},
                new Object[] {productType("Blackcurrant juice", anyPrice()), 5},
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
