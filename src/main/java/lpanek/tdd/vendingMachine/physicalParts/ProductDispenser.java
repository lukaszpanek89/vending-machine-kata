package lpanek.tdd.vendingMachine.physicalParts;

import java.util.LinkedList;
import java.util.List;

import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.physicalParts.ex.NoProductToTakeException;
import lpanek.tdd.vendingMachine.physicalParts.listeners.ProductDispenserListener;

public class ProductDispenser {

    private List<ProductDispenserListener> listeners = new LinkedList<>();

    public ProductDispenser(Shelves shelves) {

    }

    public void dispenseProductFromShelve(int shelveNumber) {
        // Communication with product dispensing driver should happen here.

        listeners.stream().forEach(ProductDispenserListener::onProductDispensed);
    }

    public Product takeProduct() throws NoProductToTakeException {
        listeners.stream().forEach(ProductDispenserListener::onProductTaken);

        return null;
    }

    public void addListener(ProductDispenserListener listener) {
        listeners.add(listener);
    }
}
