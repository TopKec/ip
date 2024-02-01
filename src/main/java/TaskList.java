import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import exceptions.*;
import tasks.*;

/**
 * Class which holds on to the list of tasks used in Steven
 */
public class TaskList {
    public ArrayList<Task> taskList;
    private Storage storage;

    /**
     * Constructor class for TaskList
     * @param storage Class which assists in reading and writing to the hard disk.
     */
    TaskList(Storage storage) {
        taskList = new ArrayList<>();
        this.storage = storage;
    }

    public void readFile(File newFile) throws FileNotFoundException{
        storage.readFile(newFile, this.taskList);
    }

    /**
     * Adds the specified task to the list
     * @param t Task to be added
     * @throws IOException If the file used for refreshing the list cannot be found
     */
    public void addToList (Task t) throws IOException{
        taskList.add(t);
        storage.refreshFile(taskList);
    }

    /**
     * Removes the index of the specified task to the list
     * @param index Index of task to be removed
     * @throws IOException If the file used for refreshing the list cannot be found
     */
    public void removeFromList (int index) throws IOException{
        taskList.remove(index);
        storage.refreshFile(taskList);
    }

    /**
     * Prints the current list
     * It will add a counter to the list such that each task is enumerated.
     */
    public void printList() {
        int counter = 1;
        for (Task t : taskList) {
            System.out.printf("%d. %s%n", counter, t.toString());
            counter++;
        }
    }

    /**
     * Marks an item with a particular index.
     * @param index Index of item to be marked.
     * @throws IncompatibleMarkException If the item to be marked is already marked.
     * @throws IOException If the file used for refreshing the list cannot be found.
     */
    public void markList(int index) throws IncompatibleMarkException, IOException{
        if (taskList.get(index).getCompletionStatus()) {
            taskList.get(index).toggleCompletion();
            storage.refreshFile(taskList);
            return;
        }
        throw new IncompatibleMarkException();
    }

    /**
     * Unmarks an item with a particular index.
     * @param index Index of item to be unmarked.
     * @throws IncompatibleMarkException If the item to be marked is already unmarked.
     * @throws IOException If the file used for refreshing the list cannot be found.
     */
    public void unmarkList(int index) throws IncompatibleMarkException, IOException{
        if (!taskList.get(index).getCompletionStatus()) {
            taskList.get(index).toggleCompletion();
            storage.refreshFile(taskList);
            return;
        }
        throw new IncompatibleMarkException();
    }
    public int size() {
        return taskList.size();
    }

    public Task get(int i) {
        return taskList.get(i);
    }
}
