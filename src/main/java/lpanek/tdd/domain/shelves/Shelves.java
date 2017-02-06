package lpanek.tdd.domain.shelves;

import java.util.*;
import java.util.stream.Collectors;

import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.domain.shelves.ex.InvalidShelveNumberException;

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
