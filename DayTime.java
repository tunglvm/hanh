import java.util.*;

public class DayTime {
    private int hour;
    private int minute;
    private String dayOfWeek;

    // Danh sách ngày hợp lệ
    private static final List<String> VALID_DAYS = Arrays.asList(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    );

    /**
     * Constructor kiểm tra hợp lệ
     */
    public DayTime(int hour, int minute, String dayOfWeek) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59 || !VALID_DAYS.contains(dayOfWeek)) {
            throw new IllegalArgumentException("Invalid time or day: " + dayOfWeek + " " + hour + ":" + minute);
        }
        this.hour = hour;
        this.minute = minute;
        this.dayOfWeek = dayOfWeek;
    }

    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public String getDayOfWeek() { return dayOfWeek; }

    /**
     * Cộng thêm số giờ (tự động qua ngày nếu cần)
     */
    public DayTime addHours(int hours) {
        int newHour = this.hour + hours;
        String newDay = this.dayOfWeek;
        if (newHour >= 24) {
            newHour %= 24;
            newDay = getNextDayOfWeek(this.dayOfWeek);
        }
        return new DayTime(newHour, this.minute, newDay);
    }

    /**
     * Lấy ngày kế tiếp
     */
    public static String getNextDayOfWeek(String currentDay) {
        int idx = VALID_DAYS.indexOf(currentDay);
        return VALID_DAYS.get((idx + 1) % VALID_DAYS.size());
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
