import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class PricesMergerTest {

    private List<Price> pricesFromBD = new LinkedList<>();
    private List<Price> pricesForAdd = new LinkedList<>();

    private PricesMerger pricesMerger = new PricesMerger();

    @Before
    public void setUp() {
        pricesFromBD.clear();
        pricesForAdd.clear();
    }

    @Test
    public void testWhenOverlapsSameValue() {
        Price priceFromBD = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 5000);
        Price priceForAdd = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 5000);
        pricesFromBD.add(priceFromBD);
        pricesForAdd.add(priceForAdd);

        List<Price> expectedList = new LinkedList<>();
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 5000));

        List<Price> resultMerge = pricesMerger.merge(pricesFromBD, pricesForAdd);

        Assert.assertTrue(compareCollections(expectedList, resultMerge));
    }


    @Test
    public void testWhenOverlapsDifferentValue() {
        Price priceFromBD = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10000);
        Price priceForAdd = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 9000);
        pricesFromBD.add(priceFromBD);
        pricesForAdd.add(priceForAdd);

        List<Price> expectedList = new LinkedList<>();
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 9000));

        List<Price> resultMerge = pricesMerger.merge(pricesFromBD, pricesForAdd);

        Assert.assertTrue(compareCollections(expectedList, resultMerge));
    }

    @Test
    public void testWhenOverlappedSameValue() {
        Price priceFromBD = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10000);
        Price priceForAdd = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10000);
        pricesFromBD.add(priceFromBD);
        pricesForAdd.add(priceForAdd);

        List<Price> expectedList = new LinkedList<>();
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10000));

        List<Price> resultMerge = pricesMerger.merge(pricesFromBD, pricesForAdd);

        Assert.assertTrue(compareCollections(expectedList, resultMerge));
    }

    @Test
    public void testWhenOverlappedDifferentValue() {
        Price priceFromBD = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 3000);
        Price priceForAdd = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 4000);
        pricesFromBD.add(priceFromBD);
        pricesForAdd.add(priceForAdd);

        List<Price> expectedList = new LinkedList<>();
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 3000));
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 4000));
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 3000));

        List<Price> resultMerge = pricesMerger.merge(pricesFromBD, pricesForAdd);

        Assert.assertTrue(compareCollections(expectedList, resultMerge));
    }


    @Test
    public void testMergeWhenOverlapsRightAndLeft() {
        Price priceFromBD1 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10000);
        Price priceFromBD2 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 12000);

        Price priceForAdd = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000);
        pricesFromBD.addAll(Arrays.asList(priceFromBD1, priceFromBD2));
        pricesForAdd.add(priceForAdd);

        List<Price> expectedList = new LinkedList<>();
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10000));
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000));
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 12000));

        List<Price> resultMerge = pricesMerger.merge(pricesFromBD, pricesForAdd);

        Assert.assertTrue(compareCollections(expectedList, resultMerge));
    }

    @Test
    public void test1() {
        Price priceFromBD1 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 80);
        Price priceFromBD2 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 87);
        Price priceFromBD3 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 90);

        Price priceForAdd1 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 4, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 80);
        Price priceForAdd2 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 4, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 85);
        pricesFromBD.addAll(Arrays.asList(priceFromBD1, priceFromBD2, priceFromBD3));
        pricesForAdd.addAll(Arrays.asList(priceForAdd1, priceForAdd2));

        List<Price> expectedList = new LinkedList<>();
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 4, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 80));
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 4, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 85));
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 90));

        List<Price> resultMerge = pricesMerger.merge(pricesFromBD, pricesForAdd);

        Assert.assertTrue(compareCollections(expectedList, resultMerge));
    }

    @Test
    public void test2() {
        Price priceFromBD1 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000);
        Price priceFromBD2 = new Price("122856", 2, 1, Date.from(LocalDate.of(2013, 1, 10).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()), 99000);
        Price priceFromBD3 = new Price("6654", 1, 2, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()), 5000);

        Price priceForAdd1 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 20).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 2, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000);
        Price priceForAdd2 = new Price("122856", 2, 1, Date.from(LocalDate.of(2013, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), 92000);
        Price priceForAdd3 = new Price("6654", 1, 2, Date.from(LocalDate.of(2013, 1, 12).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 13).atStartOfDay(ZoneId.systemDefault()).toInstant()), 4000);
        pricesFromBD.addAll(Arrays.asList(priceFromBD1, priceFromBD2, priceFromBD3));
        pricesForAdd.addAll(Arrays.asList(priceForAdd1, priceForAdd2, priceForAdd3));

        List<Price> expectedList = new LinkedList<>();
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 2, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000));
        expectedList.add(new Price("122856", 2, 1, Date.from(LocalDate.of(2013, 1, 10).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), 99000));
        expectedList.add(new Price("122856", 2, 1, Date.from(LocalDate.of(2013, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), 92000));
        expectedList.add(new Price("6654", 1, 2, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()), 5000));
        expectedList.add(new Price("6654", 1, 2, Date.from(LocalDate.of(2013, 1, 12).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 13).atStartOfDay(ZoneId.systemDefault()).toInstant()), 4000));
        expectedList.add(new Price("6654", 1, 2, Date.from(LocalDate.of(2013, 1, 13).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()), 5000));

        List<Price> resultMerge = pricesMerger.merge(pricesFromBD, pricesForAdd);

        Assert.assertTrue(compareCollections(expectedList, resultMerge));
    }



    private boolean compareCollections(List<Price> expected, List<Price> result) {
        Collections.sort(expected);
        Collections.sort(result);

        if (expected.size() != result.size())
            return false;

        for (int i = 0; i < expected.size(); i++) {
            Price expectedPrice = expected.get(i);
            Price resultPrice = result.get(i);
            if (expectedPrice.getNumber() != resultPrice.getNumber() ||
                    expectedPrice.getDepart() != resultPrice.getDepart() ||
                    expectedPrice.getValue() != resultPrice.getValue() ||
                    !Objects.equals(expectedPrice.getProductCode(), resultPrice.getProductCode()) ||
                    !Objects.equals(expectedPrice.getBegin(), resultPrice.getBegin()) ||
                    !Objects.equals(expectedPrice.getEnd(), resultPrice.getEnd())) {
                return false;
            }
        }
        return true;
    }

}
