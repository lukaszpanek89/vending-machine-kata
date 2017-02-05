package lpanek.tdd.vendingMachine;

import java.util.*;

import lpanek.tdd.product.ProductType;
import lpanek.tdd.vendingMachine.ex.InvalidShelveNumberException;

public class Shelves {

    private List<Shelve> shelves;

    public Shelves(Shelve... shelves) {
        this.shelves = new ArrayList<>();
        Collections.addAll(this.shelves, shelves);
    }

    public int getCount() {
        return shelves.size();
    }

    public Optional<ProductType> getProductTypeOnShelve(int shelveNumber) throws InvalidShelveNumberException {
        validateShelveNumber(shelveNumber);
        return shelves.get(shelveNumber - 1).getProductType();
    }

    public int getProductCountOnShelve(int shelveNumber) throws InvalidShelveNumberException {
        validateShelveNumber(shelveNumber);
        return shelves.get(shelveNumber - 1).getProductCount();
    }

    private void validateShelveNumber(int shelveNumber) throws InvalidShelveNumberException {
        if ((shelveNumber <= 0) || (shelveNumber > shelves.size())) {
            throw new InvalidShelveNumberException(String.format("%d is an invalid shelve number.", shelveNumber));
        }
    }
}
