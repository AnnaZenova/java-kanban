package model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import status.Status;
import status.TaskType;


public class Epic extends Task {

    public final List<Integer> subTaskIdList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status,LocalDateTime.of(1999,01,01,00,00),Duration.ofMinutes(0));
        type = TaskType.EPIC;
        this.status = Status.DONE;
    }

    public List<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    public void addSubTaskIdToList(int subTaskId) {
        subTaskIdList.add(subTaskId);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
