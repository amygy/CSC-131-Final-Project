import java.util.List;
import java.util.UUID;

public class AuthService {

    private final InMemoryDatabase db = InMemoryDatabase.getInstance();

    public User registerUser(String name, String email, String password) {
        if (emailExists(email)) {
            return null;
        }
        User u = new User(UUID.randomUUID().toString(), name, email, password);
        db.addUser(u);
        return u;
    }

    public Staff registerStaff(String name, String email, String password, String workId) {
        if (emailExists(email)) {
            return null;
        }
        // Very simple validation: workId must be 5 digits
        if (workId == null || !workId.matches("\\d{5}")) {
            return null;
        }
        Staff s = new Staff(UUID.randomUUID().toString(), name, email, password, workId);
        db.addUser(s);
        return s;
    }

    public User login(String email, String password) {
        List<User> users = db.getUsers();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    private boolean emailExists(String email) {
        for (User u : db.getUsers()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
