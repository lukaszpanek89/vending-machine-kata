package lpanek.tdd.vendingMachine.physicalParts;

import java.util.LinkedList;
import java.util.List;

import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.physicalParts.ex.NoProductToTakeException;
import lpanek.tdd.vendingMachine.physicalParts.listeners.ProductDispenserListener;

public class ProductDispenser {

    private Shelves shelves;
    private Product dispensedProduct;
    private List<ProductDispenserListener> listeners = new LinkedList<>();

    public ProductDispenser(Shelves shelves) {
        this.shelves = shelves;
    }

    public void dispenseProductFromShelve(int shelveNumber) {
        // Communication with product dispensing driver should happen here.

        ProductType productType = shelves.getProductTypeOnShelve(shelveNumber);
        dispensedProduct = new Product(productType);

        listeners.stream().forEach(ProductDispenserListener::onProductDispensed);
    }

    public Product takeProduct() throws NoProductToTakeException {
        if (dispensedProduct == null) {
            throw new NoProductToTakeException("There is no product to take.");
        }

        listeners.stream().forEach(ProductDispenserListener::onProductTaken);

        Product productToReturn = dispensedProduct;
        dispensedProduct = null;
        return productToReturn;
    }

    public void addListener(ProductDispenserListener listener) {
        listeners.add(listener);
    }
}
