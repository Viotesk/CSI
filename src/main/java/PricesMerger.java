import java.util.*;

public class PricesMerger {
    public List<Price> merge(Collection<Price> oldPrices, Collection<Price> newPrices) {

        Map<Price, List<Price>> sortedNewPricesMap = new HashMap<>();
        Map<Price, List<Price>> sortedOldPricesMap = new HashMap<>();
        List<Price> mergeResultList = new LinkedList<>();

        newPrices.forEach(newPrice -> {
            if (!sortedNewPricesMap.containsKey(newPrice)) {
                sortedNewPricesMap.put(newPrice, new LinkedList<>());
                sortedNewPricesMap.get(newPrice).add(newPrice);
            } else {
                sortedNewPricesMap.get(newPrice).addAll(checkPrices(newPrice, sortedNewPricesMap.get(newPrice), true));
            }
        });

        oldPrices.forEach(oldPrice -> {
            if (!sortedOldPricesMap.containsKey(oldPrice))
                sortedOldPricesMap.put(oldPrice, new LinkedList<>());
            sortedOldPricesMap.get(oldPrice).add(oldPrice);
        });

        for (Price newPrice : sortedNewPricesMap.keySet()) {
            if(sortedOldPricesMap.containsKey(newPrice)) {
                for (Price price : sortedNewPricesMap.get(newPrice)) {
                    sortedOldPricesMap.get(newPrice).addAll(checkPrices(price, sortedOldPricesMap.get(newPrice), false));
                }
            } else
                mergeResultList.addAll(sortedNewPricesMap.get(newPrice));
        }

        for (List<Price> prices : sortedOldPricesMap.values()) {
            mergeResultList.addAll(prices);
        }

        return mergeResultList;
    }

    private List<Price> checkPrices(Price newPrice, List<Price> oldPrices, boolean itsNewPricesList) {
        List<Price> forAdd = new LinkedList<>();
        LinkedList<Integer> indexesForRemove = new LinkedList<>();
        forAdd.add(newPrice);
        for (int i = 0; i < oldPrices.size(); i++) {
            switch (newPrice.intersects(oldPrices.get(i))) {
                case OVERLAPS:
                    if (!itsNewPricesList || newPrice.getValue() == oldPrices.get(i).getValue())
                        indexesForRemove.addFirst(i);
                    else
                        forAdd.add(splitPrices(oldPrices.get(i), newPrice));
                    break;
                case OVERLAPPED:
                    if (oldPrices.get(i).getValue() == newPrice.getValue()) {
                        return new LinkedList<>();
                    } else {
                        forAdd.add(splitPrices(newPrice, oldPrices.get(i)));
                    }
                    break;
                case OVERLAP_LEFT:
                    if (newPrice.getValue() == oldPrices.get(i).getValue()) {
                        newPrice.setBegin(oldPrices.get(i).getBegin());
                        indexesForRemove.addFirst(i);
                    } else {
                        oldPrices.get(i).setEnd(newPrice.getBegin());
                    }
                    break;
                case OVERLAP_RIGHT:
                    if (newPrice.getValue() == oldPrices.get(i).getValue()) {
                        newPrice.setEnd(oldPrices.get(i).getBegin());
                        indexesForRemove.addFirst(i);
                    } else {
                        oldPrices.get(i).setBegin(newPrice.getEnd());
                    }
                    break;
                case DONT_INTERSECT:
                    break;
            }
        }
        for (int index : indexesForRemove) {
            oldPrices.remove(index);
        }
        return forAdd;
    }

    private Price splitPrices(Price priceOverlapped, Price priceOverlaps) {
        Price newPrice = new Price(priceOverlaps);
        newPrice.setBegin(priceOverlapped.getEnd());
        priceOverlaps.setEnd(priceOverlapped.getBegin());
        return newPrice;
    }
}
