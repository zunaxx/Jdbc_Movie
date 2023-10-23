package peaksoft.models;

public class Theater {
    private static Long id;
    private static String name;
    private static String location;

    public Theater(Long theaterId, String theaterName, String location){

    }

    public Theater() {

    }

    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
        Theater.id = id;
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Theater{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
