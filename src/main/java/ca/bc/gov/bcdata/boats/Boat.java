package ca.bc.gov.bcdata.boats;

public class Boat {

    private final Long id;
    private final String name;
    private final String status;

    public Boat(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}