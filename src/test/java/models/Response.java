package models;

import java.util.List;

public abstract class Response<T extends Request> {
    private String meta;
    private T data;
    private List<T> dataLast;

    public List<T> getDataLast() {
        return dataLast;
    }

    public void setDataLast(List<T> dataLast) {
        this.dataLast = dataLast;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
