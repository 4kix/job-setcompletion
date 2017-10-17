package com.iba.schedule.model;

public class TaskResponseModel extends BaseModel {

    private volatile String id;
    private volatile String body;
    private volatile String currentStatus;

    public TaskResponseModel(){}

    public TaskResponseModel(String id) {
        this.id = id;
    }

    public TaskResponseModel(String id, String body) {
        this.id = id;
        this.body = body;
    }

    public TaskResponseModel(String id, String body, String currentStatus) {
        this.id = id;
        this.body = body;
        this.currentStatus = currentStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TaskResponseModel taskResponseModel = (TaskResponseModel) o;

        if (id != null ? !id.equals(taskResponseModel.id) : taskResponseModel.id != null) return false;
        if (body != null ? !body.equals(taskResponseModel.body) : taskResponseModel.body != null) return false;
        return currentStatus != null ? currentStatus.equals(taskResponseModel.currentStatus) : taskResponseModel.currentStatus == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (currentStatus != null ? currentStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                '}';
    }
}
