import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.Scanner;

public class Terminal {
    Parser parser;
    Path currentPath;
    ArrayList<String> terminalHistory;

    Terminal(){
        currentPath = Paths.get(System.getProperty("user.dir"));
    }

    // TODO: implement each command in a method, for example:
   public void pwd(){
    String currentDir = currentPath.toAbsolutePath().toString();
       System.out.println(currentDir);
   }

    public void mkdir(String[] args){
        for (String path : args){
            File directory = new File(path);
            if (!directory.exists()){
                boolean success = directory.mkdirs();
                if (success){
                    System.out.println("Directory created successfully at " + directory.getAbsolutePath());
                } else {
                    System.err.println("Failed to create directory");
                }
            } else {
                System.err.println("Directory already exists");
            }
        }
    }


    // precondition: arg is a directory
    public void rmdir(String arg) throws IOException {
        if (arg.equals("*")){
            Files.walkFileTree(currentPath, new SimpleFileVisitor<Path>(){
                public FileVisitResult afterVisitingDirectory(Path directory, IOException exception) throws IOException{
                    if (Files.isDirectory(directory)){
                        try(Stream<Path> directoryContents = Files.list(directory)){
                            if (directoryContents.findAny().isEmpty()){
                                Files.delete(directory);
                            }
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
        });
        }
        else {
            Path filePath = Paths.get(arg);
            try (Stream<Path> directoryContents = Files.list(filePath)){
                if (directoryContents.findFirst().isPresent()){
                    System.err.println("Directory not empty\n");
                    return;
                }
            }

            try{
                Files.delete(filePath);
                System.out.println("Directory deleted successfully");
            } catch(IOException e){
                System.err.println("Failed to delete the directory: " + e.getMessage());
            }
        }
    }

    public void terminalHistory(){
       for (String command:terminalHistory){
           System.out.println(command);
       }
    }

    public void cp(String[] arg){
            Path sourcePath = Paths.get(currentPath.toString(), arg[0]);
            Path destinationPath = Paths.get(currentPath.toString(), arg[1]);
            try {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied: " + sourcePath + " -> " + destinationPath);
            } catch (IOException e) {
                System.out.println("Failed to copy file: " + sourcePath + " -> " + destinationPath);
            }
        
    }
   
    public void rm(String arg){
             Path filePath = Paths.get(currentPath.toString(), arg);
        try {
            Files.delete(filePath);
            System.out.println("File removed: " + filePath);
        } catch (NoSuchFileException e) {
            System.out.println("No such file!");
        } 
         catch (IOException e) {
            System.out.println("Failed to remove file: " + filePath);
        }
    }

    public void echo(String arg)
    {
        System.out.println(arg);
    }

    public void ls()
    {
        try
        {
            Stream<Path> stream = Files.list(currentPath);

            // Use the map() function to transform the Path objects into their respective file or directory names
            // The getFileName() method extracts the name of each item without the full path
            stream.map(Path::getFileName)
                  .sorted() // Sort the names alphabetically
                  .forEach(p -> System.out.println(p));

            stream.close();
        }
        catch(IOException e)
        {
            System.out.println("Failed to list the contents of the current directory");
        }
    }

    public void lsReversed()
    {
        try
        {
            Stream<Path> stream = Files.list(currentPath);

            // Use the map() function to transform the Path objects into their respective file or directory names
            // The getFileName() method extracts the name of each item without the full path
            stream.map(Path::getFileName)
                  .sorted(Comparator.reverseOrder()) // Sort the names in reverse order
                  .forEach(p -> System.out.println(p));

            stream.close();
        }
        catch(IOException e)
        {
            System.out.println("Failed to list the contents of the current directory");
        }

    }


    // this method will choose the suitable command method to be called
    public void chooseCommandAction(){



    }

    public static void main(String[] args){

        


    }
}
