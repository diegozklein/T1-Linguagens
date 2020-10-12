public class main {
    public static void main(String args[]){

        // You should manually set the files paths here.
        // Ideally they should be in the root directory of the project.
        // The OF file will be created, so you can choose any name you want.
        String sourceFile = "source";
        String ofFile = "of";
        String ifFile = "if.txt";

        // com IF file
        //Interpreter machine = new Interpreter(sourceFile, ifFile, ofFile);

        // sem IF file
        Interpreter machine = new Interpreter(sourceFile, ofFile);

        // Checks if the program has any errors and runs it
        int errorCode = machine.run();
        if (errorCode == -1)
            System.out.println("Error. The program is badly written. It has at least one unpaired [ or ].");
        else if (errorCode == -2) System.out.println("Error. The program is badly written. It doesn´t end with $.");
        else if (errorCode == -3) System.out.println("Error. There is a command to read from IF file in the source code, but you have not provided an IF file.");
        else System.out.println("\nProgram succesfully ended.");


    }
}
