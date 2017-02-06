package lpanek.tdd.tests.unit.vendingMachine;

import static lpanek.tdd.tests.util.ConstructingUtil.money;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.payment.Money;
import lpanek.tdd.vendingMachine.Display;

@RunWith(JUnitParamsRunner.class)
public class DisplayTest {

    @Test
    public void should_ShowSelectProduct_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showSelectProduct();

        // then
        assertThat(display.getCurrentMessage()).isEqualTo("Select product.");
    }

    @Test
    public void should_ShowShelveIsEmpty_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showShelveIsEmpty();

        // then
        assertThat(display.getCurrentMessage()).isEqualTo("Shelve is empty.");
    }

    @Test
    @Parameters(method = "getTestData_CoinToInsertAndMessage")
    public void should_ShowInsertMoney_When_AskedFor(Money moneyToInsert, String expectedMessage) {
        // given
        Display display = new Display();

        // when
        display.showInsertMoney(moneyToInsert);

        // then
        assertThat(display.getCurrentMessage()).isEqualTo(expectedMessage);
    }

    @Test
    public void should_ShowTakeProduct_When_AskedFor() {
        // given
        Display display = new Display();

        // when
        display.showTakeProduct();

        // then
        assertThat(display.getCurrentMessage()).isEqualTo("Take your product.");
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_CoinToInsertAndMessage() {
        return new Object[][]{
                new Object[] {money(3, 40), "Insert 3.40 zł."},
                new Object[] {money(4, 0),  "Insert 4.00 zł."},
                new Object[] {money(0, 60), "Insert 0.60 zł."}
        };
    }
}