import java.util.Objects;

public class Task {

    private int taskId; //Уникальный идентификационный номер задачи
    private String name; //Название, кратко описывающее суть задачи
    private String description; //Описание, в котором раскрываются детали.
    public Status status; //статус задачи
    public TaskType type;//Тип задачи

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        type = TaskType.TASK;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taskId);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}









