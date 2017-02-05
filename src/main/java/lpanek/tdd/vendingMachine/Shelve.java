package lpanek.tdd.vendingMachine;

import java.util.Optional;

import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.ex.InvalidProductCountException;

public class Shelve {

    private Optional<ProductType> productType;
    private int productCount;

    public Shelve() {
        productType = Optional.empty();
        productCount = 0;
    }

    public Shelve(ProductType productType, int productCount) throws InvalidProductCountException {
        validateProductCount(productCount);
        this.productType = Optional.of(productType);
        this.productCount = productCount;
    }

    public void removeProduct() throws EmptyShelveException {
        validateShelveIsNotEmpty();
        --productCount;
        if (productCount == 0) {
            productType = Optional.empty();
        }
    }

    public Optional<ProductType> getProductType() {
        return productType;
    }

    public int getProductCount() {
        return productCount;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %d]",
                getClass().getSimpleName(),
                productType.isPresent() ? productType.get().toString() : "<no product>",
                productCount);
    }

    private void validateProductCount(int productCount) throws InvalidProductCountException {
        if (productCount <= 0) {
            throw new InvalidProductCountException(String.format("%d is an invalid product count.", productCount));
        }
    }

    private void validateShelveIsNotEmpty() throws EmptyShelveException {
        if (productCount == 0) {
            throw new EmptyShelveException("Cannot remove product from empty shelve.");
        }
    }
}
