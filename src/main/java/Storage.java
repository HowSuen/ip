import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Storage {
    private final Path path;

    public Storage() {
        this.path = Paths.get(System.getProperty("user.dir"), "data", "duke.txt");
        try {
            initialiseSaveFile(this.path);
        } catch (DukeException e) {
            System.out.println(e);
        }
    }

    /**
     * Creates save file if it does not already exist.
     *
     * @param path The path to the save file
     * @throws DukeException if error occurs while creating save directory or save file
     */
    public void initialiseSaveFile(Path path) throws DukeException {
        Path dataPath = Paths.get(System.getProperty("user.dir"), "data");
        if (!Files.exists(dataPath)) {
            // Create 'data' directory if it does not exist
            try {
                Files.createDirectory(dataPath);
            } catch (IOException e) {
                throw new DukeException("I/O Error occurred when creating save directory!");
            }
        }
        if (!Files.exists(this.path)) {
            // Create 'save.txt' file in 'data' directory if it does not exist
            try {
                Files.createFile(this.path);
            } catch (IOException e) {
                throw new DukeException("I/O Error occurred when creating save file!");
            }
        }
    }

    /**
     * Creates TaskList with history of commands from save file.
     *
     * @return TaskList from history of commands
     * @throws DukeException if error occurs in reading file or when adding previous commands
     */
    public TasksList createTaskList() throws DukeException {
        try {
            List<String> history = Files.readAllLines(this.path);
            TasksList taskList = new TasksList();
            for (String s : history) {
                taskList.loadFromSave(s);
            }
            return taskList;
        } catch (IOException e) {
            throw new DukeException("I/O Error occurred when reading from save file!");
        }
    }

    /**
     * Overwrites the user's save file with the current tasks in the list.
     *
     * @param tasksList The TasksList to save.
     * @throws DukeException if error occurs while writing to save file.
     */
    public void writeToSave(TasksList tasksList) throws DukeException {
        try {
            if (tasksList.isEmpty()) {
                Files.write(this.path, new byte[0]);
                return;
            }
            StringBuilder sb = tasksList.toStringBuilder();
            byte[] byteArray = sb.toString().getBytes();
            Files.write(this.path, byteArray);
        } catch (IOException e) {
            throw new DukeException("I/O Error occurred while overwriting save file!");
        }
    }

    /**
     * Add task to save file.
     *
     * @param task The task to be added to the save file.
     * @throws DukeException if error occurs while reading from or writing to save file.
     */
    public void addTaskToSave(Task task) throws DukeException {
        try {
            List<String> history = Files.readAllLines(this.path);
            history.add(task.toCommand());
            StringBuilder sb = new StringBuilder();
            for (String s : history) {
                sb.append(s + "\n");
            }
            Files.write(this.path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new DukeException("I/O Error occurred when reading from save file!");
        }
    }

    /**
     * Remove task from save file.
     *
     * @param id The id of the task to be removed.
     * @throws DukeException if error occurs while reading from or writing to save file.
     */
    public void deleteTaskFromSave(int id) throws DukeException {
        try {
            List<String> history = Files.readAllLines(this.path);
            history.remove(id);
            StringBuilder sb = new StringBuilder();
            for (String s : history) {
                sb.append(s + "\n");
            }
            Files.write(this.path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new DukeException("I/O Error occurred when reading from save file!");
        }
    }

    /**
     * Marks the Task in the save file.
     *
     * @param id The id of the task to be marked in the save file.
     * @throws DukeException if error occurs while reading from or writing to save file.
     */
    public void markTaskInSave(int id) throws DukeException {
        try {
            List<String> history = Files.readAllLines(this.path);
            String[] command = history.get(id).split(" \\| ", 3);
            String newLine = command[0] + " | 1 | " + command[2];
            history.set(id, newLine);
            StringBuilder sb = new StringBuilder();
            for (String s : history) {
                sb.append(s + "\n");
            }
            Files.write(this.path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new DukeException("I/O Error occurred when reading from save file!");
        }
    }

    /**
     * Unmarks the Task in the save file.
     *
     * @param id The id of the task to be unmarked in the save file.
     * @throws DukeException if error occurs while reading from or writing to save file.
     */
    public void unmarkTaskInSave(int id) throws DukeException {
        try {
            List<String> history = Files.readAllLines(this.path);
            String[] command = history.get(id).split(" \\| ", 3);
            String newLine = command[0] + " | 0 | " + command[2];
            history.set(id, newLine);
            StringBuilder sb = new StringBuilder();
            for (String s : history) {
                sb.append(s + "\n");
            }
            Files.write(this.path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new DukeException("I/O Error occurred when reading from save file!");
        }
    }
}
