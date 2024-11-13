package model;
import status.Status;
import status.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        type = TaskType.SUBTASK;
        this.epicId = epicId;

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "taskId=" + getTaskId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + getEpicId() +
                ", type=" + getType() +
                ",startTime=" + getStartTime() + '\'' +
                ",duration=" + getDuration() + '\'' +
                ",endTime=" + getCalculatedEndTime(getStartTime(), getDuration()) + '\'' +
                '}';
    }
}
