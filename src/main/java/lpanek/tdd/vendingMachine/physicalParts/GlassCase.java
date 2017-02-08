package lpanek.tdd.vendingMachine.physicalParts;

import lpanek.tdd.domain.product.ProductType;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.domain.shelves.ex.EmptyShelveException;
import lpanek.tdd.domain.shelves.ex.InvalidShelveNumberException;

public class GlassCase {

    private Shelves shelves;

    public GlassCase(Shelves shelves) {
        this.shelves = shelves;
    }

    public int getShelveCount() {
        return shelves.getCount();
    }

    public ProductType getProductTypeOnShelve(int shelveNumber) throws InvalidShelveNumberException, EmptyShelveException {
        return shelves.getProductTypeOnShelve(shelveNumber);
    }

    public int getProductCountOnShelve(int shelveNumber) throws InvalidShelveNumberException {
        return shelves.getProductCountOnShelve(shelveNumber);
    }
}
