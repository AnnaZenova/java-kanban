public class Epic extends Task {


    public Epic(String name, String description, Status status) {
        super(name, description, status);
        type = TaskType.EPIC;
        this.status = Status.NEW;

    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        this.status = status;


    }
}
