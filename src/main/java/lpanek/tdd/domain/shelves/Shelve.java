package lpanek.tdd.domain.shelves;

import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.domain.shelves.ex.InvalidProductCountException;

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
