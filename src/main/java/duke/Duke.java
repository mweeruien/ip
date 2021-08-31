/**
 * This function simulates a chat bot who interacts with a user to keep track of a todo list.
 *
 * @author Megan Wee Rui En
 * @version CS2103T AY21/22 Semester 1
 */
package duke;

import duke.command.Command;
import duke.exceptions.CommandDoesNotExist;
import duke.exceptions.DukeExceptions;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.tasklist.TaskList;
import duke.ui.Ui;
import java.util.Scanner;
import java.io.IOException;

public class Duke {

    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;
    private final Command command;

    /**
     * Constructs a Duke object
     *
     * @param filePath The location where information will be saved in the project.
     * @throws IOException
     */
    public Duke(String filePath) throws IOException {
        this.ui = new Ui(); // Performs the self introduction upon successful initialization.
        this.storage = new Storage(filePath);
        this.taskList = new TaskList(storage.load());
        this.command = new Command(storage, ui);
    }

    /**
     * This function is the body of the chat bot, and contains the flow and commands.
     */
    public void run() throws DukeExceptions, IOException {
        ui.showWelcome();
        String userCommand; // this is the container for the full command received from the user
        String cmd; // this is the container for the first word of the command received from the user

        ///// This listens for the commands and interprets them
        // This part listens for user input and repeats until the command "bye" is identified
        Scanner sc = new Scanner(System.in);
        while (true) {
            userCommand = sc.nextLine();
            Parser parser = new Parser(userCommand);
            cmd = parser.getFirstWord();

            // 'bye' : Ends the program
            if (userCommand.equals("bye") || cmd.equals("bye")) {
                command.bye();
                sc.close();
                break;

            // 'list' : Retrieves information from the hard drive and prints it
            } else if (cmd.equals("list")) {
                command.list(taskList);

            // 'done [int]' : marks the corresponding number in the list as done
            } else if (cmd.equals("done")) {
                command.done(userCommand, taskList);

            // 'delete [int]' : delete the corresponding number
            } else if (cmd.equals("delete")) {
                command.delete(userCommand, taskList);

            } else if (cmd.equals("find")) {
                command.find(userCommand, taskList);

            // Else, an item has been added to the chat bot
            // Commands are either todo, deadline or event
            } else {
                switch (cmd) {
                case "todo" :
                    command.addTodo(userCommand, cmd, taskList);
                    break;

                case "deadline" :
                    command.addDeadline(userCommand, cmd, taskList);
                    break;

                case "event" :
                    command.addEvent(userCommand, cmd, taskList);
                    break;

                default :
                    throw new CommandDoesNotExist(userCommand);
                    // Fallthrough
                }
            }
        }
    }

    public static void main(String[] args) throws DukeExceptions, IOException {
        new Duke("data/duke.txt").run();
    }
}
