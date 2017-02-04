package lpanek.tdd.payment;

public enum Coin {
    DENOMINATION_5_0(5, 0),
    DENOMINATION_2_0(2, 0),
    DENOMINATION_1_0(1, 0),
    DENOMINATION_0_5(0, 50),
    DENOMINATION_0_2(0, 20),
    DENOMINATION_0_1(0, 10);

    private Money value;

    Coin(int wholes, int pennies) {
        value = new Money(wholes, pennies);
    }

    public Money getValue() {
        return value;
    }
}
