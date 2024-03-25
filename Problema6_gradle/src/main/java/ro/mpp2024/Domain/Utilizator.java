package ro.mpp2024.Domain;

public class Utilizator implements IEntity<Integer> {

    private String username;
    private String password;
    private int id;
    public Utilizator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Utilizator: " +
                "ID: " + id +
                "\nUsername: " + username +
                "\nPassword: " + password;
    }

}
