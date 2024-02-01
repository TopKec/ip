import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
public class Steven {

    public static void main(String[] args) {
        String corrputed = "Oh dear, looks like the file used to handle the data I'm supposed to store is corrupted...\nSteven's Advice: You may need to re-create the file!";
        String formatError = "My, it would appear as though you didn't format your instruction properly!\n";
        String line = "========\n";
        String bootMsg = ("This is Steven!\nHow can I advise?\n");
        //String blankFieldMsg = "Just to let you know, I can't accept a task with missing details.\nSteven's advice: Make sure you're leaving no blanks in your instructions!";
        System.out.print(line + bootMsg + line);
        System.out.println("Steven's advice: Don't know what commands I understand? Use \"help\"!");
        System.out.print(line);
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            new File("./data").mkdirs();
            File newFile = new File("./data/Steven.txt");
            if (!newFile.createNewFile()) {
                taskList = readFile(newFile);
            }
        } catch (IOException e) {
            System.out.println("Whoops, looks like something went wrong, it really shouldn't!");
            e.printStackTrace();
        }

        boolean exit = false;
        while (!exit) {
            Scanner Input = new Scanner(System.in);
            while (Input.hasNextLine()) {
                UserInput command = new UserInput(Input.nextLine());
                switch (command.getInputName()) {
                    case "list":
                        System.out.println("This is your list so far:");
                        int counter = 1;
                        for (Task t : taskList) {
                            System.out.printf("%d. %s%n", counter, t.toString());
                            counter++;
                        }
                        break;
                    case "unmark":
                        try {
                            if (command.arg1Empty()) {
                                throw new InsufficientArgException();
                            }
                            if (!command.arg2Empty() || !command.arg3Empty()) {
                                throw new ExcessiveArgException();
                            }
                            int index = Integer.parseInt(command.getArg1());
                            if (!taskList.get(index - 1).getCompletionStatus()) {
                                System.out.println("Ah, hold on. Seems like this one's still incomplete. If you meant to mark this as complete instead, use \"mark\".\nDo note that this is the current status of the task:");
                            } else {
                                taskList.get(index - 1).toggleCompletion();
                                System.out.println("Sure, I'll mark this as incomplete for the time being.");
                            }
                            System.out.println(taskList.get(index - 1).toString());
                            refreshFile(taskList);
                        } catch (InsufficientArgException|ExcessiveArgException error) {
                            System.out.println(formatError + "Steven's advice: The format of \"mark\" is as follows:\nmark (x) - x is an number corresponding with the index of an item in the list.");
                        } catch (NumberFormatException error) {
                            System.out.println("Hmm... Seems like you want me to mark, something, but you didn't provide a valid number for me to work off.\nSteven's Advice: Use a number instead.");
                        } catch (IndexOutOfBoundsException error) {
                            System.out.println("Ah, a pity... Seems like you don't have that many tasks.\nSteven's advice: Use a number which corresponds to a task number. If you need to know what number corresponds to what task, use \"list\".");
                        } catch (IOException e) {
                            System.out.println(corrputed);
                        }
                        break;
                    case "mark":
                        try {
                            if (command.arg1Empty()) {
                                throw new InsufficientArgException();
                            }
                            if (!command.arg2Empty() || !command.arg3Empty()) {
                                throw new ExcessiveArgException();
                            }
                            int index = Integer.parseInt(command.getArg1());
                            if (taskList.get(index - 1).getCompletionStatus()) {
                                System.out.println("Oh, this one's already cleared! Remember that you can use \"unmark\" to mark this as incomplete if that was your intention.\nRegardless, here is the current status of that task:");
                            } else {
                                taskList.get(index - 1).toggleCompletion();
                                System.out.println("As you wish, this will me marked as complete then!");
                            }
                            System.out.println(taskList.get(index - 1).toString());
                            refreshFile(taskList);
                        } catch (InsufficientArgException|ExcessiveArgException error) {
                            System.out.println(formatError + "Steven's advice: The format of \"unmark\" is as follows:\nunmark (x) - x is an number corresponding with the index of an item in the list.");
                        } catch (NumberFormatException error) {
                            System.out.println("Hmm... Seems like you want me to unmark something, but you didn't provide a valid number for me to work off.\nSteven's Advice: Use a number instead.");
                        } catch (IndexOutOfBoundsException error) {
                            System.out.println("Ah, a pity... Seems like you don't have that many tasks.\nSteven's advice: Use a number which corresponds to a task number. If you need to know what number corresponds to what task, use \"list\".");
                        } catch (IOException e) {
                            System.out.println(corrputed);
                        }
                        break;
                    case "todo":
                        try {
                            if (command.arg1Empty()) {
                                throw new InsufficientArgException();
                            }
                            if (!command.arg2Empty() || !command.arg3Empty()) {
                                throw new ExcessiveArgException();
                            }
                            String name = command.getArg1();
                            taskList.add(new Todo(name));
                            refreshFile(taskList);
                            System.out.print(line);
                            System.out.println("I see. I shall add the following to the list of tasks:");
                            System.out.println(taskList.get(taskList.size() - 1));
                            System.out.printf("Do bear in mind that you now have %d tasks in the list.%n", taskList.size());
                        } catch (InsufficientArgException | ExcessiveArgException error) {
                            System.out.println(formatError + "Steven's advice: The format of \"Todo\" is as follows:\nTodo (item) - item is the name of an item that you want to add to the list as a todo.");
                        } catch (IOException e) {
                            System.out.println(corrputed);
                        }
                        break;
                    case "deadline":
                        try {
                            if (command.arg1Empty() || command.arg2Empty()) {
                                throw new InsufficientArgException();
                            }
                            if (!command.arg3Empty()) {
                                throw new ExcessiveArgException();
                            }
                            taskList.add(new Deadline(command.getArg1(), command.getArg2()));
                            refreshFile(taskList);
                            System.out.print(line);
                            System.out.println("I see. I shall add the following to the list of tasks:");
                            System.out.println(taskList.get(taskList.size() - 1));
                            System.out.printf("Do bear in mind that you now have %d tasks in the list.%n", taskList.size());
                        } catch (InsufficientArgException | ExcessiveArgException error) {
                            System.out.println(formatError + "Steven's advice: The format of \"Deadline\" is as follows:\nDeadline (item) /by (date) - item is the name of an item that you want to add to the list as a deadline. date must be a date.");
                        } catch (IOException e) {
                            System.out.println(corrputed);
                        }
                        break;
                    case "event":
                        try {
                            if (command.arg1Empty() || command.arg2Empty() || command.arg3Empty()) {
                                throw new InsufficientArgException();
                            }
                            taskList.add(new Event(command.getArg1(), command.getArg2(), command.getArg3()));
                            refreshFile(taskList);
                            System.out.print(line);
                            System.out.println("I see. I shall add the following to the list of tasks:");
                            System.out.println(taskList.get(taskList.size() - 1));
                            System.out.printf("Do bear in mind that you now have %d tasks in the list.%n", taskList.size());
                        } catch (InsufficientArgException error) {
                            System.out.println(formatError + "Steven's advice: The format of \"Event\" is as follows:\nDeadline (item) /from (date1) /to (date2) - item is the name of an item that you want to add to the list as a deadline. date1 amd date2 must be a dates.");
                        } catch (IOException e) {
                            System.out.println(corrputed);
                        }
                        break;
                    case "delete":
                        try {
                            if (command.arg1Empty()) {
                                throw new InsufficientArgException();
                            }
                            if (!command.arg2Empty() || !command.arg3Empty()) {
                                throw new ExcessiveArgException();
                            }
                            int index = Integer.parseInt(command.getArg1());
                            index -= 1;
                            Task removedItem = taskList.get(index);
                            taskList.remove(index);
                            refreshFile(taskList);
                            System.out.println(line + "Very well, the following item has been removed from the list:\n" + removedItem.toString() + "\n");
                            System.out.println("For the sake of completeness, this is the current list, do take note if any of your items have been moved around in order.");
                            int counter1 = 1;
                            for (Task t : taskList) {
                                System.out.printf("%d. %s%n", counter1, t.toString());
                                counter1++;
                            }
                        } catch (InsufficientArgException|ExcessiveArgException error) {
                            System.out.println(formatError + "Steven's advice: The format of \"Delete\" is as follows:\ndelete (x) - x is an number corresponding with the index of an item in the list.");
                        } catch (NumberFormatException error) {
                            System.out.println("Oh, I can't  delete that - I need a number of an item in the list to delete it.\nSteven's Advice: Use a number instead.");
                        } catch (IndexOutOfBoundsException error) {
                            System.out.println("Apologies, you don't have a task of this number, so I can't delete it.\nSteven's advice: Use a number which corresponds to a task number. If you need to know what number corresponds to what task, use \"list\".");
                        } catch (IOException e) {
                            System.out.println(corrputed);
                        }
                        break;
                    case "help":
                        System.out.println("The following are commands that I recgonise, and their respective formats:");
                        System.out.println("bye - terminates the program");
                        System.out.println("list - provides a list of events that you have added prior");
                        System.out.println("mark (x) - marks the xth item on the list as complete. Note that an item marked complete cannot be marked complete again.");
                        System.out.println("unmark (x) - marks the xth item on the list as incomplete. Note than an item marked incomplete cannot be marked incomplete again.");
                        System.out.println("todo (item) - adds a todo item to the list.");
                        System.out.println("deadline (item) /by (date1) - adds a deadline item to the list which is due on date1.");
                        System.out.println("event (item) /from (date1) /to (date2) - adds an event item to the list which begins on date1 and ends on date2.");
                        System.out.println("delete (x) - delete the xth item from the list. Do note that this may affect the positioning of some of the items.");
                        break;
                    case "bye":
                        exit = true;
                        break;
                    default:
                        System.out.println("Hm, this doesn't seem like something can do for you. Try something else?\nSteven's advice: try typing \"help\" to see what commands are available.");
                }
                System.out.print(line);
                if (exit) {
                    break;
                }
            }
        }

        System.out.println("I'll see you soon then!\n" + line);
    }

    private static ArrayList<Task> readFile(File newFile) throws FileNotFoundException {
        ArrayList<Task> list = new ArrayList<>();
        Scanner s = new Scanner(newFile);
        while (s.hasNextLine()) {
            String readLine = s.nextLine();
            String eventType = readLine.split(" \\| ")[0];
            boolean cleared = readLine.split(" \\| ")[1].equals("1");
            String description = readLine.split(" \\| ", 3)[2];
            Task t;
            switch (eventType) {
                case "T":
                    t = new Todo(description);
                    break;
                case "D":
                    t = new Deadline(description.split(" \\| ")[0], description.split(" \\| ")[1]);
                    break;
                case "E":
                    t = new Event(description.split(" \\| ")[0], description.split(" \\| ")[1], description.split(" \\| ")[2]);
                    break;
                default:
                    t = new Task("errortemp");
            }
            if (cleared) {
                t.toggleCompletion();
            }
            list.add(t);
        }
        return list;
    }

    private static void refreshFile(ArrayList<Task> list) throws IOException{
        FileWriter fw = new FileWriter("data/Steven.txt");
        for(Task t : list) {
            String description = t.storeFormat() + "\n";
            fw.write(description);
        }
        fw.close();
    }
}
