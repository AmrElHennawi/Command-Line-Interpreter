public class Parser {
    String commandName;
    String[] args;

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean parse (String input){
        if (input.isEmpty()){
            commandName = "";
            args = new String[0];
            return false;
        }

        String[] tokens = input.split("\\s+");

        // at this point we are sure that there exists at least one token since the input string is not empty
        commandName = tokens[0];

        // if there exists more than one token then the rest are arguments
        if (tokens.length > 1){
            args = new String[tokens.length - 1];
            System.arraycopy(tokens, 1, args, 0, args.length);
        } else {
            // if no more token, then arg is empty
            args = new String[0];
        }
        return true;
    }
}
