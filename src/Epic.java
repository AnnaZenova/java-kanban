import java.util.ArrayList;

public class Epic extends Task {

    public ArrayList<Integer> subTaskIdList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        type = TaskType.EPIC;
        this.status = Status.NEW;
        this.subTaskIdList = subTaskIdList;

    }

    public void addSubTaskIdToLisT (int subTaskId) {
        subTaskIdList.add(subTaskId);
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        this.status = status;
    }
}
