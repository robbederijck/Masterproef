import java.util.ArrayList;

public class Prestation {

    private int id;
    private int optimizationID;
    private int startTime;
    private int endTime;
    private ArrayList<Operation> operations;

    public Prestation(int id, int optID, int startTime, int endTime) {
        this.id = id;
        this.optimizationID = optID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.operations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getOptimizationID() {
        return optimizationID;
    }

    public void setOptimizationID(int optimizationID) {
        this.optimizationID = optimizationID;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void addOperation(Operation o){
        this.operations.add(o);
    }

    private static void sortOperations(){
        // TODO: implementing this method
    }
}
