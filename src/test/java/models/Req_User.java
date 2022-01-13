package models;

import com.fasterxml.jackson.annotation.JsonInclude;


public class Req_User extends Request{

    private String name;
    private String gender;
    private String email;
    private String status;

    public void init() {
        super.init(Res_User.class);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailUnique(String email) {
        this.email = makeEmailUnique(email);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String makeEmailUnique(String email) {
        // MAKE EMAIL UNIQUE
        String[] emailArr = email.split("@");
        return emailArr[0] + System.currentTimeMillis() +'@'+ emailArr[1];
    }

    @Override
    public String getJsonSchema() {
        return "users.json";
    }
}
