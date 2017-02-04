package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.anyPrice;
import static lpanek.tdd.tests.util.ConstructingUtil.productType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.Shelve;
import lpanek.tdd.vendingMachine.ex.InvalidProductCountException;

public class ShelveTest {

    @Test
    public void should_ContainSpecifiedProductTypeAndCount_When_SuccessfullyConstructed() {
        // given
        ProductType blackcurrantJuiceType = productType("Blackcurrant juice", anyPrice());

        // when
        Shelve shelve = new Shelve(blackcurrantJuiceType, 8);

        // then
        assertThat(shelve.getProductType().get()).isEqualTo(blackcurrantJuiceType);
        assertThat(shelve.getProductCount()).isEqualTo(8);
    }

    @Test
    public void should_ThrowException_When_TriesToConstructWithInvalidProductCount() {
        Object[][] testTuples = getTestTuplesConsistingOfInvalidProductCountAndExceptionMessage();

        for (Object[] testTuple : testTuples) {
            // given
            ProductType blackcurrantJuiceType = productType("Blackcurrant juice", anyPrice());
            int invalidProductCount = (int) testTuple[0];
            String exceptionMessage = (String) testTuple[1];

            // when
            Throwable caughtThrowable = catchThrowable(() -> new Shelve(blackcurrantJuiceType, invalidProductCount));

            // then
            assertThat(caughtThrowable)
                    .isNotNull()
                    .isInstanceOf(InvalidProductCountException.class)
                    .hasMessage(exceptionMessage);
        }
    }

    private Object[][] getTestTuplesConsistingOfInvalidProductCountAndExceptionMessage() {
        return new Object[][]{
                new Object[] {-1, "-1 is an invalid product count."},
                new Object[] {0, "0 is an invalid product count."}
        };
    }
}
