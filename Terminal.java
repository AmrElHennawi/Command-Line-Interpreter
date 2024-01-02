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

    // ----------------------------------------------------------------------------------------
    public static void main(String[] args) throws IOException {

        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);

        // taking the first input
        System.out.print(terminal.currentPath + "> ");
        String input = scanner.nextLine();
        terminal.parser.parse(input);
        terminal.chooseCommandAction();

        while (!input.equals("exit")) {
            System.out.print(terminal.currentPath + "> ");
            input = scanner.nextLine();
            terminal.parser.parse(input);
            terminal.chooseCommandAction();
        }
        scanner.close();
    }

    // ----------------------------------------------------------------------------------------
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
                if (parser.args.length == 0) {
                    cd();
                    break;
                } else {
                    cd(parser.args[0]);
                    break;
                }

            case "ls":
                if (parser.args.length == 0) {
                    ls();
                    break;
                } else if (parser.args[0].equals("-r")) {
                    lsReversed();
                    ;
                    break;
                }

            case "cp":
                cp(parser.args);
                break;

            case "rm":
                rm(parser.args[0]);
                break;

            case "cat":
                cat(parser.args);
                break;

            case "wc":
                wc(parser.args[0]);
                break;

            case "touch":
                touch(parser.args[0]);
                break;

            case "mkdir":
                mkdir(parser.args);
                break;

            case "rmdir":
                rmdir(parser.args[0]);
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

    // ----------------------------------------------------------------------------------------
    Terminal() {
        currentPath = Paths.get(System.getProperty("user.dir"));
        parser = new Parser();
        terminalHistory = new ArrayList<String>();
    }

    // ----------------------------------------------------------------------------------------
    public void pwd() {
        String currentDir = currentPath.toAbsolutePath().toString();
        System.out.println(currentDir);
    }

    // ----------------------------------------------------------------------------------------
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

    // ----------------------------------------------------------------------------------------
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

    // ----------------------------------------------------------------------------------------
    public void cat(String[] args) {
        Path filePath = Paths.get(currentPath.toString(), args[0]);
        if (args.length == 1) {
            try {
                Files.lines(filePath).forEach(System.out::println);
            } catch (IOException e) {
                System.out.println("Failed to read the file: " + filePath);
            }
        } else if (parser.args.length == 2) {
            Path filePath2 = Paths.get(currentPath.toString(), args[1]);
            try {
                Stream<String> lines1 = Files.lines(filePath);
                Stream<String> lines2 = Files.lines(filePath2);
                Stream.concat(lines1, lines2).forEach(System.out::println);
            } catch (IOException e) {
                System.out.println("Failed to read the files: " + filePath + " or " + filePath2);
            }
        } else {
            System.out.println("Invalid Command!");
        }

    }

    // ----------------------------------------------------------------------------------------
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

    // ----------------------------------------------------------------------------------------
    public void touch(String arg) {
        Path filePath = Paths.get(currentPath.toString(), arg);
        try {
            Files.createFile(filePath);
            System.out.println("File created: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to create file: " + filePath);
        }
    }

    // ----------------------------------------------------------------------------------------
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

    // ----------------------------------------------------------------------------------------
    public void rmdir(String arg) {
        if (arg.equals("*")) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentPath)) {
                for (Path dirPath : directoryStream) {
                    if (Files.isDirectory(dirPath) && isDirectoryEmpty(dirPath)) {
                        Files.delete(dirPath);
                        System.out.println("Empty directory removed: " + dirPath);
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to remove empty directories: " + e.getMessage());
            }
        } else {
            Path directoryPath = Paths.get(currentPath.toString(), arg);
            if (Files.isDirectory(directoryPath) && isDirectoryEmpty(directoryPath)) {
                try {
                    Files.delete(directoryPath);
                    System.out.println("Empty directory removed: " + directoryPath);
                } catch (IOException e) {
                    System.err.println("Failed to remove directory: " + e.getMessage());
                }
            } else {
                System.err.println(
                        "Invalid argument. Use 'rmdir *' to remove all empty directories in the current directory.");
            }
        }
    }

    // ----------------------------------------------------------------------------------------
    private boolean isDirectoryEmpty(Path dirPath) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
            return !directoryStream.iterator().hasNext();
        } catch (IOException e) {
            return false;
        }
    }

    // ----------------------------------------------------------------------------------------
    public void addToHistory() {
        String command = "";
        command += parser.commandName;
        for (String arg : parser.args) {
            command = command + " " + arg;
        }

        terminalHistory.add(command);
    }

    // ----------------------------------------------------------------------------------------
    public void printTerminalHistory() {
        for (String command : terminalHistory) {
            System.out.println(command);
        }
    }

    // ----------------------------------------------------------------------------------------
    public void echo(String[] args) {

        for (String arg : args) {
            System.out.print(arg + " ");
        }

        System.out.println();
    }

    // ----------------------------------------------------------------------------------------
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

    // ----------------------------------------------------------------------------------------
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

    // ----------------------------------------------------------------------------------------
    public void cd() // go to the home directory
    {
        currentPath = Paths.get(System.getProperty("user.home"));
    }

    // ----------------------------------------------------------------------------------------
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
}