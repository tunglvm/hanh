public class ScheduleEntry {
    private String unitName;
    private String activityType;
    private DayTime startTime;
    private DayTime endTime;

    public ScheduleEntry(String unitName, String activityType, DayTime startTime, DayTime endTime) {
        if (unitName == null || unitName.isBlank())
            throw new IllegalArgumentException("Unit name cannot be empty.");
        if (activityType == null || activityType.isBlank())
            throw new IllegalArgumentException("Activity type cannot be empty.");
        if (startTime == null || endTime == null)
            throw new IllegalArgumentException("Start or end time cannot be null.");
        if (!startTime.getDayOfWeek().equals(endTime.getDayOfWeek()))
            throw new IllegalArgumentException("Start and end must be on the same day.");
        if (startTime.getHour() > endTime.getHour())
            throw new IllegalArgumentException("Start time must be before end time.");

        this.unitName = unitName;
        this.activityType = activityType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getUnitName() { return unitName; }
    public String getActivityType() { return activityType; }
    public DayTime getStartTime() { return startTime; }
    public DayTime getEndTime() { return endTime; }

    public int getDurationHours() {
        return (endTime.getHour() - startTime.getHour() + 24) % 24;
    }

    public boolean conflictsWith(ScheduleEntry other) {
        if (!this.startTime.getDayOfWeek().equals(other.startTime.getDayOfWeek())) return false;
        return this.startTime.getHour() < other.endTime.getHour() &&
               this.endTime.getHour() > other.startTime.getHour();
    }

    @Override
    public String toString() {
        return String.join(",",
            unitName,
            activityType,
            startTime.getDayOfWeek(),
            startTime.toString(),
            endTime.toString());
    }
}
