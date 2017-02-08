package lpanek.tdd.tests.unit.vendingMachine.physicalParts;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.vendingMachine.physicalParts.ProductDispenser;
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
        ProductDispenserListener listener1 = mock(ProductDispenserListener.class);
        ProductDispenserListener listener2 = mock(ProductDispenserListener.class);
        ProductDispenser productDispenser = new ProductDispenser();
        productDispenser.addListener(listener1);
        productDispenser.addListener(listener2);

        // when
        productDispenser.dispenseProductFromShelve(shelveNumber);

        // then
        verify(listener1).onProductDispensed();
        verify(listener2).onProductDispensed();
    }

    @Test
    public void should_NotifyListeners_When_ProductTaken() {
        // given
        ProductDispenserListener listener1 = mock(ProductDispenserListener.class);
        ProductDispenserListener listener2 = mock(ProductDispenserListener.class);
        ProductDispenser productDispenser = new ProductDispenser();
        productDispenser.addListener(listener1);
        productDispenser.addListener(listener2);

        // when
        productDispenser.takeProduct();

        // then
        verify(listener1).onProductTaken();
        verify(listener2).onProductTaken();
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
