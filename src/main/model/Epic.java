package main.model;

import java.util.ArrayList;
import main.status.Status;
import main.status.TaskType;


public class Epic extends Task {

    public final ArrayList<Integer> subTaskIdList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        type = TaskType.EPIC;
        this.status = Status.NEW;
    }

     public ArrayList<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    public void addSubTaskIdToLisT (int subTaskId) {
        subTaskIdList.add(subTaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskId=" + getTaskId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                "subTaskIdList=" + getSubTaskIdList() +
                ", status=" + getStatus() +
                ", type=" + getType() +
                '}';
    }
}
