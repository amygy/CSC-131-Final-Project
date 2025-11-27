public class Staff extends User {

    private String workId;

    public Staff(String id, String name, String email, String password, String workId) {
        super(id, name, email, password);
        this.workId = workId;
    }

    public String getWorkId() {
        return workId;
    }

    @Override
    public String toString() {
        return "Staff{" + name + "}";
    }
}
