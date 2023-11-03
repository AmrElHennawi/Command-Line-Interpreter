import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

public class Terminal {
    Parser parser;
    Path currentPath;
    ArrayList<String> terminalHistory;

    Terminal() {
        currentPath = Paths.get(System.getProperty("user.dir"));
        parser = new Parser();
        terminalHistory = new ArrayList<String>();
    }

    // TODO: implement each command in a method, for example:
    public void pwd() {
        String currentDir = currentPath.toAbsolutePath().toString();
        System.out.println(currentDir);
    }

    public void mkdir(String[] args) {
        for (String path : args) {
            File directory = new File(path);
            if (!directory.exists()) {
                boolean success = directory.mkdirs();
                if (success) {
                    System.out.println("Directory created successfully at " + directory.getAbsolutePath());
                } else {
                    System.err.println("Failed to create directory");
                }
            } else {
                System.err.println("Directory already exists");
            }
        }
    }

    
    // public void rmdir(String arg) throws IOException {
    //     if (arg.equals("*")) {
    //         Files.walkFileTree(currentPath, new SimpleFileVisitor<Path>() {
    //             public FileVisitResult afterVisitingDirectory(Path directory, IOException exception)
    //                     throws IOException {
    //                 if (Files.isDirectory(directory)) {
    //                     try (Stream<Path> directoryContents = Files.list(directory)) {
    //                         if (directoryContents.findAny().isEmpty()) {
    //                             Files.delete(directory);
    //                         }
    //                     }
    //                 }
    //                 return FileVisitResult.CONTINUE;
    //             }
    //         });
    //     } else {
    //         Path filePath = Paths.get(arg);
    //         if (Files.exists(filePath) && Files.isDirectory(filePath)){
    //             try (Stream<Path> directoryContents = Files.list(filePath)) {
    //                 if (directoryContents.findFirst().isPresent()) {
    //                     System.err.println("Directory not empty\n");
    //                     return;
    //                 }
    //             }

    //             try {
    //                 Files.delete(filePath);
    //                 System.out.println("Directory deleted successfully");
    //             } catch (IOException e) {
    //                 System.err.println("Failed to delete the directory: " + e.getMessage());
    //             }
    //         } else {
    //             System.out.println("File does not exist or is not a directory");
    //         }
    //     }
    // }

    public void addToHistory()
    {
        String command = "";
        command += parser.commandName;
        for (String arg : parser.args) {
            command = command + " " + arg;
        }

        terminalHistory.add(command);
    }

    public void printTerminalHistory() {
        for (String command : terminalHistory) {
            System.out.println(command);
        }
    }

    public void cp(String[] arg) {
        Path sourcePath = Paths.get(currentPath.toString(), arg[0]);
        Path destinationPath = Paths.get(currentPath.toString(), arg[1]);
        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied: " + sourcePath + " -> " + destinationPath);
        } catch (IOException e) {
            System.out.println("Failed to copy file: " + sourcePath + " -> " + destinationPath);
        }

    }

    public void rm(String arg) {
        Path filePath = Paths.get(currentPath.toString(), arg);
        try {
            Files.delete(filePath);
            System.out.println("File removed: " + filePath);
        } catch (NoSuchFileException e) {
            System.out.println("No such file!");
        } catch (IOException e) {
            System.out.println("Failed to remove file: " + filePath);
        }
    }

    public void echo(String[] args) {
        
        for (String arg : args) {
            System.out.print(arg + " ");
        }

        System.out.println();
    }

    public void ls() {
        try {
            Stream<Path> stream = Files.list(currentPath);

            // Use the map() function to transform the Path objects into their respective
            // file or directory names
            // The getFileName() method extracts the name of each item without the full path
            stream.map(Path::getFileName)
                    .sorted() // Sort the names alphabetically
                    .forEach(p -> System.out.println(p));

            stream.close();
        } catch (IOException e) {
            System.out.println("Failed to list the contents of the current directory");
        }
    }

    public void lsReversed() {
        try {
            Stream<Path> stream = Files.list(currentPath);

            // Use the map() function to transform the Path objects into their respective
            // file or directory names
            // The getFileName() method extracts the name of each item without the full path
            stream.map(Path::getFileName)
                    .sorted(Comparator.reverseOrder()) // Sort the names in reverse order
                    .forEach(p -> System.out.println(p));

            stream.close();
        } catch (IOException e) {
            System.out.println("Failed to list the contents of the current directory");
        }

    }

    public void cd() // go to the home directory
    {
        currentPath = Paths.get(System.getProperty("user.home"));
        // System.out.println(currentPath);
    }

    public void cd(String arg) {
        if (arg.equals("..")) {
            Path parentPath = currentPath.getParent();
            if (parentPath != null) {
                currentPath = parentPath;
            } else {
                System.out.println("No previous paths available!");
            }

        } else // if the input is a path
        {
            Path newPath;

            if (Paths.get(arg).isAbsolute()) // if its a full absolute path
            {
                newPath = Paths.get(arg);
            } else // if its relative (short) path
            {
                newPath = currentPath.resolve(arg).normalize();
            }

            // to check if it's a valid directory
            if (newPath.toFile().isDirectory()) {
                currentPath = newPath;
            } else {
                System.out.println("Path does not exist!");
            }
        }

    }

    public void cat(String arg1) {
        Path filePath = Paths.get(currentPath.toString(), arg1);
        try {
            Files.lines(filePath).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Failed to read the file: " + filePath);
        }
    }

    public void cat(String arg1, String arg2) {
        Path filePath1 = Paths.get(currentPath.toString(), arg1);
        Path filePath2 = Paths.get(currentPath.toString(), arg2);
        try {
            Stream<String> lines1 = Files.lines(filePath1);
            Stream<String> lines2 = Files.lines(filePath2);
            Stream.concat(lines1, lines2).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Failed to read the files: " + filePath1 + " or " + filePath2);
        }
    }

    public void wc(String arg) {
        Path filePath = Paths.get(currentPath.toString(), arg);
        try {
            long lineCount = Files.lines(filePath).count();
            long wordCount = Files.lines(filePath)
                    .mapToLong(line -> line.split("\\s+").length)
                    .sum();
            long charCount = Files.lines(filePath).mapToLong(String::length).sum();
            System.out.println(lineCount + " " + wordCount + " " + charCount + " " + filePath.getFileName());
        } catch (IOException e) {
            System.out.println("Failed to count in file: " + filePath);
        }
    }

    public void touch(String arg) {
        Path filePath = Paths.get(currentPath.toString(), arg);
        try {
            Files.createFile(filePath);
            System.out.println("File created: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to create file: " + filePath);
        }
    }

    // this method will choose the suitable command method to be called
    public void chooseCommandAction() {
        addToHistory();
        switch (parser.commandName) {
            case "echo":
                echo(parser.args);
                break;

            case "pwd":
                pwd();
                break;

            case "cd":
                if(parser.args.length == 0)
                {
                    cd();
                    break;
                }
                else
                {
                    cd(parser.args[0]);
                    break;
                }

            case "ls":
                if(parser.args.length == 0)
                {
                    ls();
                    break;
                }
                else if(parser.args[0].equals("-r"))
                {
                    lsReversed();;
                    break;
                }
            
            case "cp":
                cp(parser.args);
                break;
            
            case "rm":
                rm(parser.args[0]);
                break;

            case "cat":
                if(parser.args.length == 1)
                {
                    cat(parser.args[0]);
                    break;
                }
                else if(parser.args.length == 2)
                {
                    cat(parser.args[0],parser.args[1]);
                    break;
                }
                else
                {
                    System.out.println("Invalid Command!");
                    break;
                }

            case "wc":
                wc(parser.args[0]);
                break;
            
            case "touch":
                touch(parser.args[0]);
                break;
                
            case "mkdir":
                mkdir(parser.args);
                break;
            
            case "history":
                printTerminalHistory();
                break;
                
            case "exit":
                break;

            default:
                System.out.println("Invalid Command!");
                break;
        }


    }

    public static void main(String[] args) throws IOException {

        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);

        //taking the first input
        System.out.print(terminal.currentPath + "> ");
        String input = scanner.nextLine();
        terminal.parser.parse(input);
        terminal.chooseCommandAction();

        while(!input.equals("exit"))
        {
            System.out.print(terminal.currentPath + "> ");
            input = scanner.nextLine();
            terminal.parser.parse(input);
            terminal.chooseCommandAction();
        }

    }
}