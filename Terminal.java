import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.stream.Stream;

public class Terminal {
    Path currentPath;
    Terminal(){
        currentPath = Paths.get(System.getProperty("user.dir"));
    }
    Parser parser;

    // TODO: implement each command in a method, for example:
//    public String pwd(){
//        return " ";
//    }
//
//    public void cd(String[] args){
//
//    }

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


    // this method will choose the suitable command method to be called
    public void chooseCommandAction(){

    }

    public static void main(String[] args){

    }


}
