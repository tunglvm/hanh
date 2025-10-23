import java.io.IOException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Schedule schedule = new Schedule();

        // Thêm dữ liệu mẫu
        schedule.addEntry(new ScheduleEntry("COMP1010", "Lecture", new DayTime(9, 0, "Monday"), new DayTime(11, 0, "Monday")));
        schedule.addEntry(new ScheduleEntry("COMP1010", "Practical", new DayTime(13, 0, "Wednesday"), new DayTime(15, 0, "Wednesday")));
        schedule.addEntry(new ScheduleEntry("MATH1007", "Lecture", new DayTime(10, 0, "Tuesday"), new DayTime(12, 0, "Tuesday")));

        schedule.calculateSelfStudy();

        // Hiển thị
        schedule.displaySchedule();

        // Lưu file
        System.out.print("\nEnter file name to save (e.g., schedule.csv): ");
        String fileName = scanner.nextLine();
        schedule.saveToCSV(fileName);

        // Đọc lại file
        System.out.println("\nLoading schedule back from file...");
        Schedule loaded = new Schedule();
        loaded.loadFromCSV(fileName);
        loaded.displaySchedule();

        scanner.close();
    }

    private static ScheduleEntry createEntry(String name, String type, String day, int startH, int startM, int durationH) {
        DayTime start = new DayTime(startH, startM, day);
        DayTime end = start.addHours(durationH);
        return new ScheduleEntry(name, type, start, end);
    }
}
