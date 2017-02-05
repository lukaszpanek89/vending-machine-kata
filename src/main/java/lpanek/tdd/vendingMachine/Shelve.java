package lpanek.tdd.vendingMachine;

import java.util.Optional;

import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.InvalidProductCountException;

public class Shelve {

    private Optional<ProductType> productType;
    private int productCount;

    public Shelve() {

    }

    public Shelve(ProductType productType, int productCount) throws InvalidProductCountException {
        validateProductCount(productCount);
        this.productType = Optional.of(productType);
        this.productCount = productCount;
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
}
