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
    public void should_HaveSpecifiedProductTypeAndCount_When_Constructed() {
        // given
        ProductType blackcurrantJuiceType = productType("Blackcurrant juice", anyPrice());

        // when
        Shelve shelve = new Shelve(blackcurrantJuiceType, 8);

        // then
        assertThat(shelve.getProductType().get()).isEqualTo(blackcurrantJuiceType);
        assertThat(shelve.getProductCount()).isEqualTo(8);
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
    private Object[][] getTestData_InvalidProductCountAndExceptionMessage() {
        return new Object[][]{
                new Object[] {-1, "-1 is an invalid product count."},
                new Object[] {0, "0 is an invalid product count."}
        };
    }
}
