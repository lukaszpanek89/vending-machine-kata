package lpanek.tdd.payment;

import java.util.HashMap;
import java.util.Map;

public class Coins {

    private Map<Coin, Integer> coinToCountMap = createCoinToCountMapWithZeros();

    public Coins() {
        // Intentionally left empty.
    }

    public Coins(Coin... coins) {
        for (Coin coin : coins) {
            addCoinToMap(coin);
        }
    }

    public Coins plus(Coin... coins) {
        return null;
    }

    public Money getValue() {
        Money coinsValue = new Money(0, 0);
        for (Map.Entry<Coin, Integer> entry : coinToCountMap.entrySet()) {
            Money coinValue = entry.getKey().getValue().times(entry.getValue());
            coinsValue = coinsValue.plus(coinValue);
        }
        return coinsValue;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    private void addCoinToMap(Coin coin) {
        coinToCountMap.put(coin, coinToCountMap.get(coin) + 1);
    }

    private static Map<Coin, Integer> createCoinToCountMapWithZeros() {
        Map<Coin, Integer> map = new HashMap<>();
        for (Coin coin : Coin.values()) {
            map.put(coin, 0);
        }
        return map;
    }
}
