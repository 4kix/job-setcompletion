package com.iba.schedule.model;

import java.util.concurrent.atomic.AtomicReference;

public class TaskResponseModel extends BaseModel {

    private volatile String UUID;
    private AtomicReference<String> body = new AtomicReference<>();
    private AtomicReference<String> currentStatus = new AtomicReference<>();

    public TaskResponseModel(){}

    public TaskResponseModel(String UUID) {
        this.UUID = UUID;
    }

    public TaskResponseModel(String UUID, String body) {
        this.UUID = UUID;
        this.body.set(body);
    }

    public TaskResponseModel(String UUID, String body, String currentStatus) {
        this.UUID = UUID;
        this.body.set(body);
        this.currentStatus.set(currentStatus);
    }

    public String getUUID() {
        return UUID;
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

        if (UUID != null ? !UUID.equals(taskResponseModel.UUID) : taskResponseModel.UUID != null) return false;
        if (body.get() != null ? !body.get().equals(taskResponseModel.body.get()) : taskResponseModel.body.get() != null) return false;
        return currentStatus.get() != null ? currentStatus.get().equals(taskResponseModel.currentStatus.get()) : taskResponseModel.currentStatus.get() == null;
    }

    @Override
    public int hashCode() {
        int result = UUID != null ? UUID.hashCode() : 0;
        result = 31 * result + (body.get() != null ? body.get().hashCode() : 0);
        result = 31 * result + (currentStatus.get() != null ? currentStatus.get().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "UUID='" + UUID + '\'' +
                ", body='" + body.get() + '\'' +
                ", currentStatus='" + currentStatus.get() + '\'' +
                '}';
    }
}
