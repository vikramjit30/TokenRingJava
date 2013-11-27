
public class CalendarEntry {
    private static int id = 0;
    private String date;
    private String time;
    private String duration;
    private String header;
    private String comment;

    public CalendarEntry(String date, String time, String duration, String header, String comment) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.header = header;
        this.comment = comment;
        id++;
    }
}
