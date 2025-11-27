public class User {
    protected String id;
    protected String name;
    protected String email;
    protected String password; // plain text for simplicity

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return "User{" + name + "}";
    }
}
