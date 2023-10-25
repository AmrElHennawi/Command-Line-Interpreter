# Command Line Interpreter (CLI) 


Welcome to our Command Line Interpreter (CLI) project.

**Project Overview:**
This repository contains a Java-based Command Line Interpreter (CLI) designed to operate on your computer's file system. Users can enter commands through the keyboard, and the CLI parses and executes these commands. The CLI keeps accepting different commands from the user until the user enters "exit," at which point the CLI terminates.

**Program Structure:**
Our project is organized into two major classes: `Parser` and `Terminal`. The `Parser` class handles the parsing of user input into command names and arguments, while the `Terminal` class contains implementations of various commands, including `echo`, `pwd`, `cd`, `ls`, `mkdir`, `rmdir`, `touch`, `cp`, `rm`, `cat`, `wc`, `>` (output redirection), `>>` (append to file), and `history`.

**Features:**
- Execute a variety of file system operations through a user-friendly command-line interface.
- Implements commonly used commands and file manipulation tasks.
- Handles incorrect inputs and parameters gracefully, providing helpful error messages.

**Assignment Rules:**
- The project is written in Java.
- We have adhered to the given class structure for `Parser` and `Terminal`.
- We have not used "exec" to implement any of the commands.

**Authors:**
- [Amr Khaled El-Hennawi](https://github.com/AmrElhennawi)
- [Abdelrahman Wael Hanafy](https://github.com/abwael)
- [Yusuf Elsayed Badr](https://github.com/yusufbadr)
- [Mazen Mahmoud Adly](https://github.com/mazenmahmoudadly)

Explore our code and start experimenting with the Command Line Interpreter!

**Note:** For file and folder manipulation, the CLI interacts with your operating system. Make sure to exercise caution when working with real files and directories.

