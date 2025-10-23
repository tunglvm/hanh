import java.io.*;
import java.util.*;

public class Schedule {
    private final List<ScheduleEntry> entries = new ArrayList<>();
    private static final int TOTAL_WEEKLY_HOURS = 10;
    private static final int MAX_UNITS = 4;

    /** Thêm một buổi học, có kiểm tra trùng giờ */
    public boolean addEntry(ScheduleEntry entry) {
        for (ScheduleEntry existing : entries) {
            if (existing.conflictsWith(entry)) {
                System.err.printf("⚠️ Conflict: %s overlaps with %s on %s.%n",
                    entry.getUnitName(), existing.getUnitName(), entry.getStartTime().getDayOfWeek());
                return false;
            }
        }
        entries.add(entry);
        return true;
    }

    /** Nhập thông tin môn học và tạo lịch Lecture + Practical + Self-study */
    public void enrollUnits(Scanner scanner) {
        System.out.println("\n🎓 UNIT ENROLLMENT");
        System.out.printf("You can enroll in up to %d units.%n", MAX_UNITS);
        System.out.print("Enter number of units to enroll (0–4): ");
        int unitCount = getIntInput(scanner, 0, MAX_UNITS);
        if (unitCount == 0) {
            System.out.println("No units enrolled this semester.");
            return;
        }

        // Dùng danh sách ngày cố định để sắp lịch hợp lý
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        int dayIndex = 0;

        for (int i = 1; i <= unitCount; i++) {
            System.out.printf("%nEnter details for Unit %d:%n", i);
            System.out.print("Unit Name (e.g., COMP1010): ");
            String unit = scanner.nextLine().trim().toUpperCase();

            System.out.print("Lecture Hours (2 or 3): ");
            int lectureH = getIntInput(scanner, 2, 3);

            System.out.print("Practical Hours (1–3): ");
            int practicalH = getIntInput(scanner, 1, 3);

            // 1️⃣ Lecture
            DayTime lecStart = new DayTime(9, 0, days[dayIndex]);
            DayTime lecEnd = lecStart.addHours(lectureH);
            addEntry(new ScheduleEntry(unit, "Lecture", lecStart, lecEnd));

            // 2️⃣ Practical
            DayTime pracStart = new DayTime(lecEnd.getHour() + 1, 0, days[dayIndex]);
            DayTime pracEnd = pracStart.addHours(practicalH);
            addEntry(new ScheduleEntry(unit, "Practical", pracStart, pracEnd));

            // 3️⃣ Self-study
            int selfStudyH = TOTAL_WEEKLY_HOURS - (lectureH + practicalH);
            DayTime studyStart = new DayTime(8, 0, "Sunday");
            DayTime studyEnd = studyStart.addHours(selfStudyH);
            addEntry(new ScheduleEntry(unit, "Self-Study", studyStart, studyEnd));

            System.out.printf("✅ Enrolled %s (Lecture %dh, Practical %dh, Self-Study %dh).%n",
                    unit, lectureH, practicalH, selfStudyH);

            dayIndex = (dayIndex + 1) % days.length; // Chuyển sang ngày kế tiếp
        }
    }

    /** Hàm nhập số nguyên an toàn */
    private int getIntInput(Scanner sc, int min, int max) {
        int val;
        while (true) {
            try {
                val = Integer.parseInt(sc.nextLine().trim());
                if (val < min || val > max) throw new NumberFormatException();
                return val;
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input. Please enter a number between " + min + " and " + max + ": ");
            }
        }
    }

    /** Lưu file CSV */
    public void saveToCSV(String fileName) {
        if (entries.isEmpty()) {
            System.err.println("⚠️ No entries to save.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Unit Name,Activity Type,Day,Start Time,End Time");
            writer.newLine();
            for (ScheduleEntry entry : entries) {
                writer.write(entry.toString());
                writer.newLine();
            }
            System.out.println("✅ Schedule saved to " + fileName);
        } catch (IOException e) {
            System.err.println("❌ Error saving file: " + e.getMessage());
        }
    }

    /** Hiển thị lịch học */
    public void displaySchedule() {
        if (entries.isEmpty()) {
            System.out.println("📭 No schedule entries.");
            return;
        }
        System.out.println("\n📅 Weekly Schedule:");
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-10s | %-12s | %-9s | %-11s | %-8s | %s%n",
                "Unit", "Activity", "Day", "Start", "End", "Hours");
        System.out.println("----------------------------------------------------------");

        for (ScheduleEntry e : entries) {
            System.out.printf("%-10s | %-12s | %-9s | %-11s | %-8s | %d%n",
                    e.getUnitName(),
                    e.getActivityType(),
                    e.getStartTime().getDayOfWeek(),
                    e.getStartTime(),
                    e.getEndTime(),
                    e.getDurationHours());
        }
        System.out.println("----------------------------------------------------------");
    }
}
