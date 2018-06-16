import java.util.*;

public class PricesMerger {
    public Collection<Price> merge(Collection<Price> pricesFromBD, Collection<Price> pricesForAdd) {
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
        for (Price priceFromBD : pricesFromBD) {
            switch (priceForAdd.intersects(priceFromBD)) {
                case OVERLAPS:
                    if (priceForAdd.getValue() == priceFromBD.getValue()) {
                        priceFromBD.setBegin(priceForAdd.getBegin());
                        priceFromBD.setEnd(priceForAdd.getEnd());
                    } else {
                        priceFromBD.setValue(priceForAdd.getValue());
                        priceFromBD.setBegin(priceForAdd.getBegin());
                        priceFromBD.setEnd(priceForAdd.getEnd());
                    }
                    break;
                case OVERLAPPED:
                    if (priceForAdd.getValue() != priceFromBD.getValue()) {
                        forAdd.addAll(splitPrices(priceForAdd, priceFromBD));
                    }
                    break;
                case OVERLAP_LEFT:
                    if (priceForAdd.getValue() == priceFromBD.getValue()) {
                        priceFromBD.setEnd(priceForAdd.getEnd());
                    } else {
                        priceFromBD.setEnd(priceForAdd.getBegin());
                        forAdd.add(priceForAdd);
                    }
                    break;
                case OVERLAP_RIGHT:
                    if (priceForAdd.getValue() == priceFromBD.getValue()) {
                        priceFromBD.setBegin(priceForAdd.getBegin());
                    } else {
                        priceFromBD.setBegin(priceForAdd.getEnd());
                        forAdd.add(priceForAdd);
                    }
                    break;
                case DONT_INTERSECT:
                    forAdd.add(priceForAdd);
                    break;
            }
        }
        return forAdd;
    }

    private List<Price> splitPrices(Price priceForAdd, Price priceFromBD) {
        Price newPrice = new Price(priceFromBD);
        newPrice.setBegin(priceForAdd.getEnd());
        priceFromBD.setEnd(priceForAdd.getBegin());
        return Arrays.asList(priceForAdd, newPrice);
    }
}
