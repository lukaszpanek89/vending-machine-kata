package lpanek.tdd.vendingMachine.physicalParts;

import lpanek.tdd.vendingMachine.domain.VendingMachineModel;
import lpanek.tdd.vendingMachine.domain.payment.Coins;

public class CoinsContainer {

    private VendingMachineModel model;

    public CoinsContainer(VendingMachineModel model) {
        this.model = model;
    }

    public Coins getCoins() {
        return model.getTotalCoins();
    }
}
