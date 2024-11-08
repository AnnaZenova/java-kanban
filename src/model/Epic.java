package model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import status.Status;
import status.TaskType;


public class Epic extends Task {

    public final List<Integer> subTaskIdList = new ArrayList<>();

    public Epic(String name, String description, Status status,LocalDateTime startTime, Duration duration) {
        super(name, description, status,startTime,duration);
        type = TaskType.EPIC;
        this.status = Status.DONE;
        this.startTime=getStartTime();
        this.duration = getDuration();
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
