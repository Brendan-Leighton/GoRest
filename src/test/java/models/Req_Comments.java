package models;

public class Req_Comments extends Request{

    private String name;
    private String email;
    private String body;

    public void init() {
        super.init(Res_Comments.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getJsonSchema() {
        return "JsonSchemas/comments.json";
    }
}
