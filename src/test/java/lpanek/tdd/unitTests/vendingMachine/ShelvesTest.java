package lpanek.tdd.unitTests.vendingMachine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import lpanek.tdd.payment.Money;
import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.Shelve;
import lpanek.tdd.vendingMachine.Shelves;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class ShelvesTest {

    @Test
    public void should_ContainZeroShelves_When_NoShelveAdded() {
        // expect
        assertThat(new Shelves().getCount()).isEqualTo(0);
    }

    @Test
    public void should_ContainOneNotEmptyShelve_When_OneNotEmptyShelveAdded() {
        // given
        ProductType lemonJuiceType = new ProductType("Lemon juice 0.3 l", new Money(3));
        Shelve shelve = new Shelve(lemonJuiceType, 4);
        Shelves shelves = new Shelves();

        // when
        shelves.add(shelve);

        // then
        assertThat(shelves.getCount()).isEqualTo(1);
        assertThat(shelves.getProductTypeOnShelve(1).get()).isEqualTo(lemonJuiceType);
        assertThat(shelves.getProductCountOnShelve(1)).isEqualTo(4);
    }

    @Test
    public void should_ContainTwoNotEmptyShelves_When_TwoNotEmptyShelvesAdded() {
        // given
        ProductType lemonJuiceType = new ProductType("Lemon juice 0.3 l", new Money(3));
        Shelve shelve1 = new Shelve(lemonJuiceType, 4);
        ProductType appleJuiceType = new ProductType("Apple juice 0.3 l", new Money(3));
        Shelve shelve2 = new Shelve(lemonJuiceType, 5);
        Shelves shelves = new Shelves();

        // when
        shelves.add(shelve1);
        shelves.add(shelve2);

        // then
        assertThat(shelves.getCount()).isEqualTo(2);
        assertThat(shelves.getProductTypeOnShelve(1).get()).isEqualTo(lemonJuiceType);
        assertThat(shelves.getProductCountOnShelve(1)).isEqualTo(4);
        assertThat(shelves.getProductTypeOnShelve(2).get()).isEqualTo(appleJuiceType);
        assertThat(shelves.getProductCountOnShelve(2)).isEqualTo(5);
    }

    @Test
    public void should_ThrowException_When_TriesToGetProductTypeForInvalidShelveNumber() {
        // given
        Shelve shelve = createNotEmptyShelve();
        Shelves shelves = new Shelves();
        shelves.add(shelve);

        // when
        Throwable caughtThrowable = catchThrowable(() -> shelves.getProductTypeOnShelve(-1));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("-1 is an invalid shelve number.");

        // when
        caughtThrowable = catchThrowable(() -> shelves.getProductTypeOnShelve(0));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("0 is an invalid shelve number.");

        // when
        caughtThrowable = catchThrowable(() -> shelves.getProductTypeOnShelve(2));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("2 is an invalid shelve number.");
    }

    @Test
    public void should_ThrowException_When_TriesToGetProductCountForInvalidShelveNumber() {
        // given
        Shelve shelve = createNotEmptyShelve();
        Shelves shelves = new Shelves();
        shelves.add(shelve);

        // when
        Throwable caughtThrowable = catchThrowable(() -> shelves.getProductCountOnShelve(-1));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("-1 is an invalid shelve number.");

        // when
        caughtThrowable = catchThrowable(() -> shelves.getProductCountOnShelve(0));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("0 is an invalid shelve number.");

        // when
        caughtThrowable = catchThrowable(() -> shelves.getProductCountOnShelve(2));
        // then
        assertThat(caughtThrowable)
                .isNotNull()
                .isInstanceOf(InvalidShelveNumberException.class)
                .hasMessage("2 is an invalid shelve number.");
    }

    private Shelve createNotEmptyShelve() {
        ProductType snacksType = new ProductType("Snacks", new Money(4));
        return new Shelve(snacksType, 4);
    }
}
