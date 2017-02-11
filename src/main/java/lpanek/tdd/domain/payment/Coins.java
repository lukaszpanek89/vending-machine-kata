package lpanek.tdd.domain.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Coins {

    private Map<Coin, Integer> coinToCountMap;

    public Coins(Coin... coins) {
        coinToCountMap = createCoinToCountMapWithZeros();
        for (Coin coin : coins) {
            addCoinToMap(coin);
        }
    }

    private Coins(Coins other) {
        coinToCountMap = cloneCoinToCountMap(other.coinToCountMap);
    }

    public Coins plus(Coin... coins) {
        Coins newCoins = new Coins(this);
        for (Coin coin : coins) {
            newCoins.addCoinToMap(coin);
        }
        return newCoins;
    }

    public Coins minus(Coin coin) {
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

    public int getCoinCount(Coin coin) {
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }

        Coins other = (Coins) object;
        return other.coinToCountMap.equals(this.coinToCountMap);
    }

    @Override
    public int hashCode() {
        return coinToCountMap.hashCode();
    }

    @Override
    public String toString() {
        return coinToCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> entry.getValue() + " x " + entry.getKey().toString())
                .collect(Collectors.joining(", ", getClass().getSimpleName() + "=[", "]"));
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

    private static Map<Coin, Integer> cloneCoinToCountMap(Map<Coin, Integer> mapToClone) {
        Map<Coin, Integer> map = new HashMap<>();
        for (Map.Entry<Coin, Integer> entry : mapToClone.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
