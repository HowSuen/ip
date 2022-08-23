package duke.command;

import duke.DukeException;
import duke.Storage;
import duke.task.TasksList;

public class UnmarkCommand extends Command {
    private TasksList tasksList;
    private Storage storage;
    private String[] words;

    public UnmarkCommand(TasksList tasksList, Storage storage, String[] words) {
        this.tasksList = tasksList;
        this.storage = storage;
        this.words = words;
    }

    @Override
    public void execute() throws DukeException {
        tasksList.unmarkTask(words, storage);
    }

    public static boolean isCommand(String s) {
        return s.equals("unmark");
    }
}
