package lpanek.tdd.tests.unit.vendingMachine.physicalParts;

import static lpanek.tdd.tests.vendingMachine.util.ConstructingUtil.money;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.domain.payment.Money;
import lpanek.tdd.vendingMachine.physicalParts.Display;

@RunWith(JUnitParamsRunner.class)
public class DisplayTest {

    @Test
    public void should_ShowSelectProduct_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showSelectProduct();

        // then
        assertThat(display.getMessage()).isEqualTo("Select product.");
    }

    @Test
    public void should_ShowShelveIsEmpty_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showShelveIsEmpty();

        // then
        assertThat(display.getMessage()).isEqualTo("Shelve is empty.");
    }

    @Test
    @Parameters(method = "getTestData_CoinToInsertAndMessage")
    public void should_ShowInsertMoney_When_AskedFor(Money moneyToInsert, String expectedMessage) {
        // given
        Display display = new Display();

        // when
        display.showInsertMoney(moneyToInsert);

        // then
        assertThat(display.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    public void should_ShowTakeProduct_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showTakeProduct();

        // then
        assertThat(display.getMessage()).isEqualTo("Take your product.");
    }

    @Test
    public void should_ShowTakeProductAndChange_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showTakeProductAndChange();

        // then
        assertThat(display.getMessage()).isEqualTo("Take your product and change.");
    }

    @Test
    public void should_ShowUnableToGiveChange_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showUnableToGiveChange();

        // then
        assertThat(display.getMessage()).isEqualTo("Sorry, no coins to give change. Take inserted coins.");
    }

    @Test
    public void should_ShowTakeCoinsAfterCancel_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showTakeCoinsAfterCancel();

        // then
        assertThat(display.getMessage()).isEqualTo("Purchase cancelled. Take inserted coins.");
    }

    @Test
    public void should_ShowInternalError_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showInternalError();

        // then
        assertThat(display.getMessage()).isEqualTo("Internal error...");
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_CoinToInsertAndMessage() {
        return new Object[][] {
                new Object[] {money(3, 40), "Insert 3.40 zł."},
                new Object[] {money(4, 0),  "Insert 4.00 zł."},
                new Object[] {money(0, 60), "Insert 0.60 zł."}
        };
    }
}
