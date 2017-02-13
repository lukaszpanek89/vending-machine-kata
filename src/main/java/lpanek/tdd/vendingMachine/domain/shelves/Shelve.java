package lpanek.tdd.vendingMachine.domain.shelves;

import lpanek.tdd.vendingMachine.domain.product.ProductType;
import lpanek.tdd.vendingMachine.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.domain.shelves.ex.InvalidProductCountException;

/**
 * Representation of vending machine's shelve.<br/>
 * <br/>
 * Invariants:
 * <ul>
 * <li>product count can be non-negative only (>=0),</li>
 * <li>if shelve is empty (its product count is equal to 0), it has no product type,</li>
 * <li>shelve, if not empty, contains products of single type only,</li>
 * <li>one cannot remove product from an empty shelve.</li>
 * </ul>
 */
public class Shelve {

    private ProductType productType;
    private int productCount;

    public Shelve() {
        productType = null;
        productCount = 0;
    }

    public Shelve(ProductType productType, int productCount) throws InvalidProductCountException {
        validateProductCount(productCount);
        this.productType = productType;
        this.productCount = productCount;
    }

    public void removeProduct() throws EmptyShelveException {
        if (isShelveEmpty()) {
            throw new EmptyShelveException("Cannot remove product from empty shelve.");
        }
        --productCount;
        if (productCount == 0) {
            productType = null;
        }
    }

    public ProductType getProductType() throws EmptyShelveException {
        if (isShelveEmpty()) {
            throw new EmptyShelveException("Cannot get product type from empty shelve.");
        }
        return productType;
    }

    public int getProductCount() {
        return productCount;
    }

    @Override
    public String toString() {
        return String.format("%s=[%s, %d]",
                getClass().getSimpleName(),
                isShelveEmpty() ? "<no product>" : productType.toString(),
                productCount);
    }

    private void validateProductCount(int productCount) throws InvalidProductCountException {
        if (productCount <= 0) {
            throw new InvalidProductCountException(String.format("%d is an invalid product count.", productCount));
        }
    }

    private boolean isShelveEmpty() {
        return productCount == 0;
    }
}
