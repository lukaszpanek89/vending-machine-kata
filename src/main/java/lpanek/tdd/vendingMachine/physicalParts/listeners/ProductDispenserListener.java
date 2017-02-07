package lpanek.tdd.vendingMachine.physicalParts.listeners;

public interface ProductDispenserListener {

    void onProductDispensedFromShelve(int shelveNumber);

    void onProductTaken();
}
