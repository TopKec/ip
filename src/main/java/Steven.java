import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import mainfiles.Parser;
import mainfiles.Storage;
import mainfiles.TaskList;
import mainfiles.UserInput;

/**
 * Main code for running Steven
 */
public class Steven {

    private static final String LINE = "========\n";
    private static final String MESSAGE_WHEN_BOOTING = ("This is Steven!\nHow can I advise?\n");
    private Storage storage;
    private TaskList tasks;

    /**
     * Constructor class for Steven
     */
    public Steven() {
        this.storage = new Storage();
        this.tasks = new TaskList(storage);
        try {
            new File("./data").mkdirs();
            File newFile = new File("./data/Steven.txt");
            if (!newFile.createNewFile()) {
                tasks.readFile(newFile);
            }
        } catch (IOException e) {
            System.out.println("Whoops, looks like something went wrong, it really shouldn't!");
            e.printStackTrace();
        }
        System.out.print(LINE + MESSAGE_WHEN_BOOTING + LINE);
        System.out.println("Steven's advice: Don't know what commands I understand? Use \"help\"!");
        System.out.print(LINE);
    }

    /**
     * Driver code for Steven. It will run the entire program
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            Scanner input = new Scanner(System.in);
            while (input.hasNextLine()) {
                UserInput command = new UserInput(input.nextLine());
                exit = new Parser(command, this.tasks).processInput();
                if (exit) {
                    System.out.print(LINE);
                    break;
                }
            }
        }
        System.out.println("I'll see you soon then!\n" + LINE);
    }
    public static void main(String[] args) {
        new Steven().run();
    }
}
