package lpanek.tdd.vendingMachine;

import java.util.Optional;

import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.ex.InvalidProductCountException;

public class Shelve {

    private Optional<ProductType> productTypeOptional;
    private int productCount;

    public Shelve() {
        productTypeOptional = Optional.empty();
        productCount = 0;
    }

    public Shelve(ProductType productType, int productCount) throws InvalidProductCountException {
        validateProductCount(productCount);
        this.productTypeOptional = Optional.of(productType);
        this.productCount = productCount;
    }

    public void removeProduct() throws EmptyShelveException {
        if (isShelveEmpty()) {
            throw new EmptyShelveException("Cannot remove product from empty shelve.");
        }
        --productCount;
        if (productCount == 0) {
            productTypeOptional = Optional.empty();
        }
    }

    public ProductType getProductType() throws EmptyShelveException {
        if (isShelveEmpty()) {
            throw new EmptyShelveException("Cannot get product type from empty shelve.");
        }
        return productTypeOptional.get();
    }

    public int getProductCount() {
        return productCount;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %d]",
                getClass().getSimpleName(),
                isShelveEmpty() ? "<no product>" : productTypeOptional.get().toString(),
                productCount);
    }

    private void validateProductCount(int productCount) throws InvalidProductCountException {
        if (productCount <= 0) {
            throw new InvalidProductCountException(String.format("%d is an invalid product count.", productCount));
        }
    }

    private boolean isShelveEmpty() {
        return (productCount == 0);
    }
}
