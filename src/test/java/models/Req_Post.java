package models;

public class Req_Post extends Request {

    private String title;
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void init() {
        super.init(Res_Post.class);
    }

    @Override
    public String getJsonSchema() {
        return "posts.json";
    }
}
