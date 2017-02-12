package lpanek.tdd.vendingMachine;

import lpanek.tdd.vendingMachine.physicalParts.*;

public class VendingMachine {

    private GlassCase glassCase;
    private Display display;
    private Keyboard keyboard;
    private CoinTaker coinTaker;
    private CoinsDispenser coinsDispenser;
    private ProductDispenser productDispenser;
    private CoinsContainer coinsContainer;

    VendingMachine(GlassCase glassCase, Display display, Keyboard keyboard,
                   CoinTaker coinTaker, CoinsDispenser coinsDispenser, ProductDispenser productDispenser,
                   CoinsContainer coinsContainer) {
        this.glassCase = glassCase;
        this.display = display;
        this.keyboard = keyboard;
        this.coinTaker = coinTaker;
        this.coinsDispenser = coinsDispenser;
        this.productDispenser = productDispenser;
        this.coinsContainer = coinsContainer;
    }

    public GlassCase getGlassCase() {
        return glassCase;
    }

    public Display getDisplay() {
        return display;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public CoinTaker getCoinTaker() {
        return coinTaker;
    }

    public CoinsDispenser getCoinsDispenser() {
        return coinsDispenser;
    }

    public ProductDispenser getProductDispenser() {
        return productDispenser;
    }

    public CoinsContainer getCoinsContainer() {
        return coinsContainer;
    }
}
