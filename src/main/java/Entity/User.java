package Entity;

import java.util.List;

public class User {
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public String email;
    //username = userid
    public String username;
    public String password;
    public Interests interestedProducts;

    // TODO: 2023-05-23 Remove constructors
    public User(String firstName, String lastName, String dateOfBirth, String email, String username, String password, Interests interests) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.username = username;
        this.password = password;
        this.interestedProducts = interests;
    }

    public User() {
    }
}
