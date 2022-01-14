package models;

import java.util.Date;

public class Req_Todos extends Request{

    private String title;
    private String status;
    private Date date;

    public void init() {
        super.init(Res_Todos.class);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getJsonSchema() {
        return "JsonSchemas/todos.json";
    }
}
