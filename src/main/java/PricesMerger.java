import java.util.*;

public class PricesMerger {
    public List<Price> merge(Collection<Price> pricesFromBD, Collection<Price> pricesForAdd) {
        List<Price> mergeResultList = new LinkedList<>();
        Map<Price, List<Price>> pricesMap = new HashMap<>();

        pricesFromBD.forEach(price -> {
            if (!pricesMap.containsKey(price))
                pricesMap.put(price, new LinkedList<>());
            pricesMap.get(price).add(price);
        });

        for (Price price : pricesForAdd) {
            if (!pricesMap.containsKey(price)) {
                mergeResultList.add(price);
            } else {
                pricesMap.get(price).addAll(checkPrices(price, pricesMap.get(price)));
            }
        }

        for (List<Price> prices : pricesMap.values()) {
            mergeResultList.addAll(prices);
        }

        return mergeResultList;
    }

    private List<Price> checkPrices(Price priceForAdd, List<Price> pricesFromBD) {
        List<Price> forAdd = new LinkedList<>();
        LinkedList<Integer> indexesForRemove = new LinkedList<>();
        forAdd.add(priceForAdd);
        for (int i = 0; i < pricesFromBD.size(); i++) {
            switch (priceForAdd.intersects(pricesFromBD.get(i))) {
                case OVERLAPS:
                    indexesForRemove.addFirst(i);
                    break;
                case OVERLAPPED:
                    if (pricesFromBD.get(i).getValue() == priceForAdd.getValue()) {
                        return new LinkedList<>();
                    } else {
                        forAdd.add(splitPrices(priceForAdd, pricesFromBD.get(i)));
                    }
                    break;
                case OVERLAP_LEFT:
                    if (priceForAdd.getValue() == pricesFromBD.get(i).getValue()) {
                        priceForAdd.setBegin(pricesFromBD.get(i).getBegin());
                        indexesForRemove.addFirst(i);
                    } else {
                        pricesFromBD.get(i).setEnd(priceForAdd.getBegin());
                    }
                    break;
                case OVERLAP_RIGHT:
                    if (priceForAdd.getValue() == pricesFromBD.get(i).getValue()) {
                        priceForAdd.setEnd(pricesFromBD.get(i).getBegin());
                        indexesForRemove.addFirst(i);
                    } else {
                        pricesFromBD.get(i).setBegin(priceForAdd.getEnd());
                    }
                    break;
                case DONT_INTERSECT:
                    break;
            }
        }
        for (int index : indexesForRemove) {
            pricesFromBD.remove(index);
        }
        return forAdd;
    }

    private Price splitPrices(Price priceForAdd, Price priceFromBD) {
        Price newPrice = new Price(priceFromBD);
        newPrice.setBegin(priceForAdd.getEnd());
        priceFromBD.setEnd(priceForAdd.getBegin());
        return newPrice;
    }
}
