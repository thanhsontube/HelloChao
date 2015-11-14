package son.nt.hellochao.dto;

/**
 * Created by Sonnt on 11/14/15.
 */
public class UserDto {
    String email;
    String password;
    String name;
    String gender;

    public UserDto(String email, String password, String name, String gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
