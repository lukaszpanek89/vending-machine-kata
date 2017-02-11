package lpanek.tdd.tests.vendingMachine.util;

import lpanek.tdd.vendingMachine.domain.VendingMachineModel;
import lpanek.tdd.vendingMachine.domain.payment.Coins;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ChangeDeterminingStrategy;
import lpanek.tdd.vendingMachine.domain.shelves.Shelves;

public class VendingMachineModelBuilder {

    private Shelves shelves = new Shelves();
    private Coins totalCoins = new Coins();
    private ChangeDeterminingStrategy changeStrategy = (accessibleCoins, changeValue) -> null;

    private VendingMachineModel.MachineState machineState;
    private Integer selectedProductShelveNumber;
    private Boolean isWaitingForCoinsToBeTaken;
    private Boolean isWaitingForProductToBeTaken;

    public static VendingMachineModelBuilder modelBuilder() {
        return new VendingMachineModelBuilder();
    }

    public VendingMachineModelBuilder with(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineModelBuilder with(Coins totalCoins) {
        this.totalCoins = totalCoins;
        return this;
    }

    public VendingMachineModelBuilder with(ChangeDeterminingStrategy changeStrategy) {
        this.changeStrategy = changeStrategy;
        return this;
    }

    public VendingMachineModelBuilder withState(VendingMachineModel.MachineState machineState) {
        this.machineState = machineState;
        return this;
    }

    public VendingMachineModelBuilder withSelectedShelveNumber(int selectedShelveNumber) {
        this.selectedProductShelveNumber = selectedShelveNumber;
        return this;
    }

    public VendingMachineModelBuilder withWaitingForCoinsToBeTaken(boolean isWaiting) {
        this.isWaitingForCoinsToBeTaken = isWaiting;
        return this;
    }

    public VendingMachineModelBuilder withWaitingForProductToBeTaken(boolean isWaiting) {
        this.isWaitingForProductToBeTaken = isWaiting;
        return this;
    }

    public VendingMachineModel build() {
        VendingMachineModel model = new VendingMachineModel(shelves, totalCoins, changeStrategy);
        if (machineState != null) {
            model.setMachineState(machineState);
        }
        if (selectedProductShelveNumber != null) {
            model.setSelectedProductShelveNumber(selectedProductShelveNumber);
        }
        if (isWaitingForCoinsToBeTaken != null) {
            model.setIsWaitingForCoinsToBeTaken(isWaitingForCoinsToBeTaken);
        }
        if (isWaitingForProductToBeTaken != null) {
            model.setIsWaitingForProductToBeTaken(isWaitingForProductToBeTaken);
        }
        return model;
    }
}
