import java.util.Date;
import java.util.Objects;

public class Price implements Comparable<Price>{
    private long id;
    private String productCode;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    public Price(String productCode, int number, int depart, Date begin, Date end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public Price(Price other) {
        this.productCode = other.productCode;
        this.number = other.number;
        this.depart = other.depart;
        this.begin = other.begin;
        this.end = other.end;
        this.value = other.value;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getNumber() {
        return number;
    }

    public int getDepart() {
        return depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return getNumber() == price.getNumber() &&
                getDepart() == price.getDepart() &&
                Objects.equals(getProductCode(), price.getProductCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductCode(), getNumber(), getDepart());
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }

    @Override
    public int compareTo(Price o) {
        if (getBegin().equals(o.getBegin()))
            return 0;
        if (getBegin().after(o.getBegin()))
            return 1;
        else
            return -1;
    }

    public IntersectType intersects(Price other) {
        Date ourBegin = getBegin();
        Date ourEnd = getEnd();
        Date otherBegin = other.getBegin();
        Date otherEnd = other.getEnd();


        if(ourBegin.equals(otherBegin)) {
            if(ourEnd.after(otherEnd) || ourEnd.equals(otherEnd)) {
                return IntersectType.OVERLAPS;
            } else {
                return IntersectType.OVERLAPPED;
            }
        } else if (ourBegin.before(otherBegin)) {
            if(ourEnd.before(otherBegin)) {
                return IntersectType.DONT_INTERSECT;
            } else if (ourEnd.after(otherEnd) || ourEnd.equals(otherEnd)) {
                return IntersectType.OVERLAPS;
            } else {
                return IntersectType.OVERLAP_RIGHT;
            }
        } else {
            if(ourBegin.after(otherEnd)) {
                return IntersectType.DONT_INTERSECT;
            } else if (ourEnd.before(otherEnd)) {
                return IntersectType.OVERLAPPED;
            } else {
                return IntersectType.OVERLAP_LEFT;
            }
        }
    }
    public enum IntersectType {
        OVERLAPS, OVERLAPPED, OVERLAP_RIGHT, OVERLAP_LEFT, DONT_INTERSECT

    }
}
