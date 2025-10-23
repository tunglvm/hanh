import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Schedule schedule = new Schedule();

        System.out.println("===== UNIVERSITY SCHEDULE SYSTEM =====");

        // 1️⃣ Đăng ký môn học
        schedule.enrollUnits(scanner);

        // 2️⃣ Hiển thị lịch
        schedule.displaySchedule();

        // 3️⃣ Lưu lịch học
        System.out.print("\nEnter filename to save (e.g., schedule.csv): ");
        String fileName = scanner.nextLine().trim();
        schedule.saveToCSV(fileName);

        System.out.println("\n🎉 Schedule setup complete!");
        scanner.close();
    }
}
