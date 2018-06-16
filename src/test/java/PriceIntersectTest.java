import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PriceIntersectTest {
    private Price price1;
    private Price price2;
    private Price price3;
    private Price price4;

    @Before
    public void setUp() {
        price1 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000);
        price2 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000);
        price3 = new Price("122856", 1, 1, Date.from(LocalDate.of(2013, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2013, 8, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000);
        price4 = new Price("122856", 1, 1, Date.from(LocalDate.of(2018, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
                , Date.from(LocalDate.of(2018, 10, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), 11000);
    }

    @Test
    public void testOverlapsIntersect() {
        Assert.assertEquals(price1.intersects(price2), Price.IntersectType.OVERLAPS);
        Assert.assertEquals(price2.intersects(price1), Price.IntersectType.OVERLAPPED);
        Assert.assertEquals(price2.intersects(price3), Price.IntersectType.OVERLAP_RIGHT);
        Assert.assertEquals(price3.intersects(price2), Price.IntersectType.OVERLAP_LEFT);
        Assert.assertEquals(price1.intersects(price4), Price.IntersectType.DONT_INTERSECT);
    }
}
