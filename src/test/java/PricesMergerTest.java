import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        assertTrue(compareCollections(expectedList, resultMerge));
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

        assertTrue(compareCollections(expectedList, resultMerge));
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

        assertTrue(compareCollections(expectedList, resultMerge));
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

        assertTrue(compareCollections(expectedList, resultMerge));
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

        assertTrue(compareCollections(expectedList, resultMerge));
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

        assertTrue(compareCollections(expectedList, resultMerge));
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

        assertTrue(compareCollections(expectedList, resultMerge));
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

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public Date conv(String in) {
        Date result = null;
        try {
            result = sdf.parse(in);
        } catch (ParseException ex) {
            //
        }
        return result;
    }

    @Test
    public void testMergePrices() throws ParseException {
        List<Price> oldPrices = new ArrayList<>();
        oldPrices.add(new Price("12345", 1, 1, conv("01.01.2013 00:00:00"), conv("05.01.2013 23:59:59"), 80));
        oldPrices.add(new Price("12345", 1, 1, conv("06.01.2013 00:00:00"), conv("10.01.2013 23:59:59"), 82));
        oldPrices.add(new Price("12345", 1, 1, conv("11.01.2013 00:00:00"), conv("15.01.2013 23:59:59"), 84));
        oldPrices.add(new Price("12345", 1, 1, conv("16.01.2013 00:00:00"), conv("20.01.2013 23:59:59"), 86));
        oldPrices.add(new Price("12345", 1, 1, conv("21.01.2013 00:00:00"), conv("25.01.2013 23:59:59"), 88));

        List<Price> newPrices = new ArrayList<>();

        newPrices.add(new Price("12345", 1, 1, conv("09.01.2013 23:59:59"), conv("12.01.2013 23:59:59"), 83));
        newPrices.add(new Price("12345", 1, 1, conv("14.01.2013 00:00:00"), conv("17.01.2013 23:59:59"), 85));
        newPrices.add(new Price("12345", 1, 1, conv("04.01.2013 00:00:00"), conv("07.01.2013 23:59:59"), 81));
        newPrices.add(new Price("12345", 1, 1, conv("19.01.2013 23:59:59"), conv("22.01.2013 23:59:59"), 87));
        newPrices.add(new Price("12345", 1, 1, conv("20.01.2013 10:58:00"), conv("20.01.2013 14:36:59"), 81));

        PricesMerger merger = new PricesMerger();
        List<Price> unionPrices = merger.merge(oldPrices, newPrices);
        unionPrices.stream()
                .sorted((p1, p2) -> p1.getBegin().compareTo(p2.getBegin()))
                .forEach(System.out::println);


        assertEquals(11, unionPrices.size());

        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("01.01.2013 00:00:00"), conv("04.01.2013 00:00:00"), 80)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("04.01.2013 00:00:00"), conv("07.01.2013 23:59:59"), 81)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("07.01.2013 23:59:59"), conv("09.01.2013 23:59:59"), 82)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("09.01.2013 23:59:59"), conv("12.01.2013 23:59:59"), 83)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("12.01.2013 23:59:59"), conv("14.01.2013 00:00:00"), 84)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("14.01.2013 00:00:00"), conv("17.01.2013 23:59:59"), 85)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("17.01.2013 23:59:59"), conv("19.01.2013 23:59:59"), 86)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("19.01.2013 23:59:59"), conv("20.01.2013 10:58:00"), 87)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("20.01.2013 10:58:00"), conv("20.01.2013 14:36:59"), 81)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("20.01.2013 14:36:59"), conv("22.01.2013 23:59:59"), 87)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("22.01.2013 23:59:59"), conv("25.01.2013 23:59:59"), 88)));
    }

    @Test
    public void testMergePrices1() throws ParseException {
        List<Price> oldPrices = new ArrayList<>();
        oldPrices.add(new Price("12345", 1, 1, conv("01.01.2013 00:00:00"), conv("05.01.2013 23:59:59"), 80));

        List<Price> newPrices = new ArrayList<>();

        newPrices.add(new Price("789", 1, 1, conv("04.01.2013 00:00:00"), conv("07.01.2013 23:59:59"), 81));

        PricesMerger merger = new PricesMerger();
        List<Price> unionPrices = merger.merge(oldPrices, newPrices);
        unionPrices.stream()
                .sorted((p1, p2) -> p1.getBegin().compareTo(p2.getBegin()))
                .forEach(System.out::println);


        assertEquals(2, unionPrices.size());

        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("01.01.2013 00:00:00"), conv("05.01.2013 23:59:59"), 80)));
        assertTrue(unionPrices.contains(new Price("789", 1, 1, conv("04.01.2013 00:00:00"), conv("07.01.2013 23:59:59"), 81)));
    }

    @Test
    public void testMergePrices2() throws ParseException {
        List<Price> oldPrices = new ArrayList<>();
        oldPrices.add(new Price("12345", 1, 1, conv("16.01.2013 00:00:00"), conv("20.01.2013 23:59:59"), 86));

        List<Price> newPrices = new ArrayList<>();

        newPrices.add(new Price("12345", 1, 1, conv("19.01.2013 23:59:59"), conv("22.01.2013 23:59:59"), 87));
        newPrices.add(new Price("12345", 1, 1, conv("20.01.2013 10:58:00"), conv("20.01.2013 14:36:59"), 81));

        PricesMerger merger = new PricesMerger();
        List<Price> unionPrices = merger.merge(oldPrices, newPrices);
        unionPrices.stream()
                .sorted((p1, p2) -> p1.getBegin().compareTo(p2.getBegin()))
                .forEach(System.out::println);


        assertEquals(4, unionPrices.size());

        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("16.01.2013 00:00:00"), conv("19.01.2013 23:59:59"), 87)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("19.01.2013 23:59:59"), conv("20.01.2013 10:58:00"), 87)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("20.01.2013 10:58:00"), conv("20.01.2013 14:36:59"), 81)));
        assertTrue(unionPrices.contains(new Price("12345", 1, 1, conv("20.01.2013 14:36:59"), conv("22.01.2013 23:59:59"), 87)));
    }


}
