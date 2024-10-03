package model;
import java.util.ArrayList;
import java.util.List;
import status.Status;
import status.TaskType;


public class Epic extends Task {

    public final List<Integer> subTaskIdList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        type = TaskType.EPIC;
        this.status = Status.NEW;
    }

    public List<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    public void addSubTaskIdToList(int subTaskId) {
        subTaskIdList.add(subTaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskId=" + getTaskId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", subTaskIdList=" + getSubTaskIdList() +
                ", status=" + getStatus() +
                ", type=" + getType() +
                '}';
    }
}
