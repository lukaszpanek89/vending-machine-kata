package lpanek.tdd.unitTests.vendingMachine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import lpanek.tdd.payment.Money;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.Shelve;
import lpanek.tdd.vendingMachine.ex.InvalidProductCountException;

public class ShelveTest {

    @Test
    public void should_ContainSpecifiedProductTypeAndCount_When_SuccessfullyConstructed() {
        // given
        ProductType blackcurrantJuiceType = new ProductType("Blackcurrant juice 0.3 l", new Money(3, 2));

        // when
        Shelve shelve = new Shelve(blackcurrantJuiceType, 8);

        // then
        assertThat(shelve.getProductType().get()).isEqualTo(blackcurrantJuiceType);
        assertThat(shelve.getProductCount()).isEqualTo(8);
    }

    @Test
    public void should_ThrowException_When_TriesToConstructWithInvalidProductCount() {
        // given
        ProductType blackcurrantJuiceType = new ProductType("Blackcurrant juice 0.3 l", new Money(3, 2));

        // when
        Throwable caughtThrowable = catchThrowable(() -> new Shelve(blackcurrantJuiceType, -1));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidProductCountException.class)
                .hasMessage("-1 is an invalid product count.");

        // when
        caughtThrowable = catchThrowable(() -> new Shelve(blackcurrantJuiceType, 0));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidProductCountException.class)
                .hasMessage("0 is an invalid product count.");
    }
}
