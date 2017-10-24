package com.iba.schedule.model;

import java.util.concurrent.atomic.AtomicReference;

public class TaskResponseModel extends BaseModel {

    private AtomicReference<String> id = new AtomicReference<>();
    private AtomicReference<String> body = new AtomicReference<>();
    private AtomicReference<String> currentStatus = new AtomicReference<>();

    public TaskResponseModel(){}

    public TaskResponseModel(String id) {
        this.id.set(id);
    }

    public TaskResponseModel(String id, String body) {
        this.id.set(id);
        this.body.set(body);
    }

    public TaskResponseModel(String id, String body, String currentStatus) {
        this.id.set(id);
        this.body.set(body);
        this.currentStatus.set(currentStatus);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getBody() {
        return body.get();
    }

    public void setBody(String body) {
        this.body.set(body);
    }

    public String getCurrentStatus() {
        return currentStatus.get();
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus.set(currentStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskResponseModel taskResponseModel = (TaskResponseModel) o;

        if (id.get() != null ? !id.get().equals(taskResponseModel.id.get()) : taskResponseModel.id.get() != null) return false;
        if (body.get() != null ? !body.get().equals(taskResponseModel.body.get()) : taskResponseModel.body.get() != null) return false;
        return currentStatus.get() != null ? currentStatus.get().equals(taskResponseModel.currentStatus.get()) : taskResponseModel.currentStatus.get() == null;
    }

    @Override
    public int hashCode() {
        int result = id.get() != null ? id.get().hashCode() : 0;
        result = 31 * result + (body.get() != null ? body.get().hashCode() : 0);
        result = 31 * result + (currentStatus.get() != null ? currentStatus.get().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "id='" + id.get() + '\'' +
                ", body='" + body.get() + '\'' +
                ", currentStatus='" + currentStatus.get() + '\'' +
                '}';
    }
}
