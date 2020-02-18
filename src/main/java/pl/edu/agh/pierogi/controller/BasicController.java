package pl.edu.agh.pierogi.controller;

public abstract class BasicController<T> {
    protected boolean Approved = true;

    protected T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isApproved() {
        return Approved;
    }
}
