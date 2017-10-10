package com.iba.schedule.model;


import java.io.Serializable;

public class BaseModel implements Serializable {

    private static final long SerialVersionUID = 1L;

        private String id;
        private String body;
        private String currentStatus;

    public BaseModel(){}

    public BaseModel(String id) {
        this.id = id;
    }

    public BaseModel(String id, String body) {
        this.id = id;
        this.body = body;
    }

    public BaseModel(String id, String body, String currentStatus) {
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

        BaseModel baseModel = (BaseModel) o;

        if (id != null ? !id.equals(baseModel.id) : baseModel.id != null) return false;
        if (body != null ? !body.equals(baseModel.body) : baseModel.body != null) return false;
        return currentStatus != null ? currentStatus.equals(baseModel.currentStatus) : baseModel.currentStatus == null;
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
