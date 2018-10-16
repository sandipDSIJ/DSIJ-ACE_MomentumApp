package in.dsij.acemomentum.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class DbUser extends RealmObject {

    public static final String SESSION_TOKEN = "SessionToken";
    public static final String USERNAME = "Username";
    public static final String DISPLAY_NAME = "DisplayName";
    public static final String EMAIL = "Email";

    @PrimaryKey
    private String Username;
    private String SessionToken;
    private String DisplayName;
    private String Email;

    public String getSessionToken() {
        return SessionToken;
    }

    public DbUser setSessionToken(String sessionToken) {
        SessionToken = sessionToken;
        return this;
    }

    public String getUsername() {
        return Username;
    }

    public DbUser setUsername(String username) {
        Username = username;
        return this;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public DbUser setDisplayName(String displayName) {
        DisplayName = displayName;
        return this;
    }

    public String getEmail() {
        return Email;
    }

    public DbUser setEmail(String email) {
        Email = email;
        return this;
    }
}
