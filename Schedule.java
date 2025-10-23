import java.io.*;
import java.util.*;

public class Schedule {
    private final List<ScheduleEntry> entries = new ArrayList<>();
    private static final int TOTAL_WEEKLY_HOURS = 10;

    /** Thêm buổi học, kiểm tra trùng giờ */
    public boolean addEntry(ScheduleEntry entry) {
        for (ScheduleEntry existing : entries) {
            if (existing.conflictsWith(entry)) {
                System.err.printf(" Conflict: %s overlaps with %s on %s.%n",
                    entry.getUnitName(), existing.getUnitName(), entry.getStartTime().getDayOfWeek());
                return false;
            }
        }
        entries.add(entry);
        return true;
    }

    /** Tính toán & thêm Self-Study cho các môn chưa đủ 10h */
    public void calculateSelfStudy() {
        Map<String, Integer> hoursPerUnit = new HashMap<>();

        // 1️⃣ Tính tổng Lecture + Practical cho từng môn
        for (ScheduleEntry e : entries) {
            if (!e.getActivityType().equalsIgnoreCase("Self-Study")) {
                hoursPerUnit.merge(e.getUnitName(), e.getDurationHours(), Integer::sum);
            }
        }

        // 2️⃣ Tự động thêm Self-Study để đủ 10h
        for (Map.Entry<String, Integer> entry : hoursPerUnit.entrySet()) {
            String unit = entry.getKey();
            int total = entry.getValue();
            int remaining = TOTAL_WEEKLY_HOURS - total;

            if (remaining > 0) {
                DayTime studyStart = new DayTime(8, 0, "Sunday"); // gán mặc định Chủ nhật
                DayTime studyEnd = studyStart.addHours(remaining);
                ScheduleEntry selfStudy = new ScheduleEntry(unit, "Self-Study", studyStart, studyEnd);
                entries.add(selfStudy);

                System.out.printf(" Added %d Self-Study hours for %s (Total = 10h).%n", remaining, unit);
            }
        }
    }

    /** Lưu file CSV */
    public void saveToCSV(String fileName) {
        if (entries.isEmpty()) {
            System.err.println(" No entries to save.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Unit Name,Activity Type,Day,Start Time,End Time");
            writer.newLine();
            for (ScheduleEntry entry : entries) {
                writer.write(entry.toString());
                writer.newLine();
            }
            System.out.println(" Schedule saved to " + fileName);
        } catch (IOException e) {
            System.err.println(" Error saving file: " + e.getMessage());
        }
    }

    /** Đọc file CSV */
    public void loadFromCSV(String fileName) {
        entries.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first && line.toLowerCase().contains("unit name")) { first = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");
                if (p.length < 5) continue;
                try {
                    String unit = p[0].trim();
                    String type = p[1].trim();
                    String day = p[2].trim();
                    String[] sh = p[3].split(":");
                    String[] eh = p[4].split(":");
                    DayTime start = new DayTime(Integer.parseInt(sh[0]), Integer.parseInt(sh[1]), day);
                    DayTime end = new DayTime(Integer.parseInt(eh[0]), Integer.parseInt(eh[1]), day);
                    addEntry(new ScheduleEntry(unit, type, start, end));
                } catch (Exception ex) {
                    System.err.println(" Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println(" Error reading file: " + e.getMessage());
        }
    }

    /** Hiển thị lịch học */
    public void displaySchedule() {
        if (entries.isEmpty()) {
            System.out.println(" No schedule entries.");
            return;
        }
        System.out.println("\n Weekly Schedule:");
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
