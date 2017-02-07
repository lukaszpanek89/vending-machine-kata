package lpanek.tdd.vendingMachine.physicalParts;

import lpanek.tdd.domain.product.Product;
import lpanek.tdd.vendingMachine.physicalParts.listeners.ProductDispenserListener;

public class ProductDispenser {

    public void dispenseProductFromShelve(int shelveNumber) {
        // Communication with product dispensing driver should happen here.
    }

    public Product takeProduct() {
        return null;
    }

    public void addListener(ProductDispenserListener listener) {

    }
}
