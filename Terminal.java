import java.io.File;

public class Terminal {
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

    public void rmdir(String arg){

    }


    // this method will choose the suitable command method to be called
    public void chooseCommandAction(){

    }

    public static void main(String[] args){

    }


}
