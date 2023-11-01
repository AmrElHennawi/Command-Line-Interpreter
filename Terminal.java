import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Terminal {
    Parser parser;
    // final => const , to keep home directory unchangable
    private static final String HOME_DIRECTORY = System.getProperty("user.home");

    private static String currentDirectory = HOME_DIRECTORY;

    // TODO: implement each command in a method, for example:

   public String pwd(){
       return currentDirectory;
   }
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

    public void rmdir(String arg){

    }
    public void cp(String[] arg){
            Path sourcePath = Paths.get(currentDirectory, arg[0]);
            Path destinationPath = Paths.get(currentDirectory, arg[1]);
            try {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied: " + sourcePath + " -> " + destinationPath);
            } catch (IOException e) {
                System.out.println("Failed to copy file: " + sourcePath + " -> " + destinationPath);
            }
        
    }
   
    public void rm(String arg){
             Path filePath = Paths.get(currentDirectory, arg);
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


    // this method will choose the suitable command method to be called
    public void chooseCommandAction(){

    }

    public static void main(String[] args){
        


    }
}
