package lpanek.tdd.vendingMachine.domain.shelves;

import java.util.*;
import java.util.stream.Collectors;

import lpanek.tdd.vendingMachine.domain.product.ProductType;
import lpanek.tdd.vendingMachine.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.vendingMachine.domain.shelves.ex.InvalidShelveNumberException;

/**
 * Representation of vending machine's shelves, each containing products of single type.<br/>
 * <br/>
 * Invariants:
 * <ul>
 * <li>shelve count can be non-negative only (>=0),</li>
 * <li>one cannot check product count, product type or remove product from not existing shelve,</li>
 * <li>product count on each shelve can be non-negative only (>=0),</li>
 * <li>if given shelve is empty (its product count is equal to 0), it has no product type,</li>
 * <li>given shelve, if not empty, contains products of single type only,</li>
 * <li>one cannot remove product from an empty shelve.</li>
 * </ul>
 */
public class Shelves {

    private List<Shelve> shelves;

    public Shelves(Shelve... shelves) {
        this.shelves = new ArrayList<>();
        Collections.addAll(this.shelves, shelves);
    }

    public void removeProductFromShelve(int shelveNumber) throws InvalidShelveNumberException, EmptyShelveException {
        validateShelveNumber(shelveNumber);
        shelves.get(shelveNumber - 1).removeProduct();
    }

    public int getCount() {
        return shelves.size();
    }

    public ProductType getProductTypeOnShelve(int shelveNumber) throws InvalidShelveNumberException, EmptyShelveException {
        validateShelveNumber(shelveNumber);
        return shelves.get(shelveNumber - 1).getProductType();
    }

    public int getProductCountOnShelve(int shelveNumber) throws InvalidShelveNumberException {
        validateShelveNumber(shelveNumber);
        return shelves.get(shelveNumber - 1).getProductCount();
    }

    @Override
    public String toString() {
        return shelves.stream()
                .map(Shelve::toString)
                .collect(Collectors.joining(", ", getClass().getSimpleName() + "=[", "]"));
    }

    private void validateShelveNumber(int shelveNumber) throws InvalidShelveNumberException {
        if ((shelveNumber <= 0) || (shelveNumber > shelves.size())) {
            throw new InvalidShelveNumberException(String.format("%d is an invalid shelve number.", shelveNumber));
        }
    }
}
