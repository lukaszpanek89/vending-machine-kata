package lpanek.tdd.payment;

public enum Coin {
    _5_0(5, 0),
    _2_0(2, 0),
    _1_0(1, 0),
    _0_5(0, 50),
    _0_2(0, 20),
    _0_1(0, 10);

    private Money value;

    Coin(int wholes, int pennies) {
        value = new Money(wholes, pennies);
    }

    public Money getValue() {
        return value;
    }
}
