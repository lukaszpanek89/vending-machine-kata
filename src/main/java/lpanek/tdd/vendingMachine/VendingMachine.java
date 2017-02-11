package lpanek.tdd.vendingMachine;

import lpanek.tdd.vendingMachine.physicalParts.*;

public class VendingMachine {

    private final GlassCase glassCase;
    private final Display display;
    private final Keyboard keyboard;
    private final CoinTaker coinTaker;
    private final CoinsDispenser coinsDispenser;
    private final ProductDispenser productDispenser;

    VendingMachine(GlassCase glassCase, Display display, Keyboard keyboard,
                   CoinTaker coinTaker, CoinsDispenser coinsDispenser, ProductDispenser productDispenser) {
        this.glassCase = glassCase;
        this.display = display;
        this.keyboard = keyboard;
        this.coinTaker = coinTaker;
        this.coinsDispenser = coinsDispenser;
        this.productDispenser = productDispenser;
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
}
