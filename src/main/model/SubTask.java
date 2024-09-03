package main.model;
import main.status.Status;
import main.status.TaskType;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description,status);
        type = TaskType.SUBTASK;
        this.epicId = epicId;

    }

    @Override
    public String toString() {
        return "SubTask{" +
                "taskId=" + getTaskId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                "status=" + getStatus() +
                ", epicId=" + getEpicId() +
                ", type=" + getType() +
                '}';
    }

    public int getEpicId() {
        return epicId;
    }
}
