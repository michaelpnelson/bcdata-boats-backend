package ca.bc.gov.bcdata.boats;

public class Boat {

    private final long id;
    private final String name;
    private final String status;

    public Boat(long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}