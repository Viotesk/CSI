import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PricesMergerTest{

    private List<Price> pricesFromBD = new LinkedList<>();
    private List<Price> pricesForAdd = new LinkedList<>();

    private PricesMerger pricesMerger = new PricesMerger();

    @Before
    public void setUp() {
        pricesFromBD.clear();
        pricesForAdd.clear();
    }

    @Test
    public void testMergeWhenOverlaps() {
        Price priceFromBD = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000);
        Price priceForAdd = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()), 15000);

        List<Price> expectedList = new LinkedList<>();
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000));
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000));
        expectedList.add(new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000));


        pricesFromBD.add(priceFromBD);
        pricesForAdd.add(priceForAdd);

        Collection<Price> mergeResult = pricesMerger.merge(pricesFromBD, pricesForAdd);

        Assert.assertEquals(expectedList, mergeResult);

    }

}
