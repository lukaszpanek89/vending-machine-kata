package lpanek.tdd.vendingMachine.physicalParts;

import java.util.LinkedList;
import java.util.List;

import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.physicalParts.ex.NoProductToTakeException;
import lpanek.tdd.vendingMachine.physicalParts.ex.PreviousProductNotYetTakenException;
import lpanek.tdd.vendingMachine.physicalParts.listeners.ProductDispenserListener;

public class ProductDispenser {

    private Shelves shelves;
    private Product dispensedProduct;
    private List<ProductDispenserListener> listeners = new LinkedList<>();

    public ProductDispenser(Shelves shelves) {
        this.shelves = shelves;
    }

    public void dispenseProductFromShelve(int shelveNumber) throws PreviousProductNotYetTakenException {
        if (dispensedProduct != null) {
            throw new PreviousProductNotYetTakenException("Previously dispensed product was not yet taken.");
        }

        // Communication with product dispensing driver should happen here.

        ProductType productType = shelves.getProductTypeOnShelve(shelveNumber);
        dispensedProduct = new Product(productType);
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

    // TODO: This method is for testing purposes only. Should not be here.
    public void setDispensedProduct(Product dispensedProduct) {
        this.dispensedProduct = dispensedProduct;
    }
}
