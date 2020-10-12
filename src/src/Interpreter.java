import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;

/**
 * This class receives three files: SOURCE, IF and OF, and then uses them
 * The SOURCE file contains the program to be run.
 * The IF file cntains the values in the memory.
 * THE OF file will be used to write the final values in the memory.
 * as if it were an interpreter for a fictitious machine.
 * At the end of the running the content of the memory is displayed on the screen.
 *
 * @author            Diego Klein
 * @author            Lucas Gavirachi Cardoso
 * @author            Pedro Maia Rogoski
 * @author            Roberto Luís Rezende
 * @version           1.0
 * @since             2020-09-27
 */
public class Interpreter {
    private int dataPointer;
    private int programPointer;
    private int[] memory;
    private int memorySize = 1000;

    private String sourceFile;
    private String ofFile;
    private String ifFile;

    private String program;
    private int[] ifArray;

    private int[][] specialCharMap;
    private int IFPointer;

    /**
     * Interpreter class constructor
     *
     * @param sourceFile  the SOURCE file
     * @param ifFile      the IF file
     * @param ofFile      the OF file
     */
    public Interpreter(String sourceFile, String ifFile, String ofFile) {
        dataPointer = 0;
        programPointer = 0;
        IFPointer = 0;

        memory = new int[memorySize]; //The memory will be 1000, 3 is just for testing pourposes

        // sets the files
        this.sourceFile = sourceFile;
        this.ifFile = ifFile;
        this.ofFile = ofFile;

        // turns the SOURCE file into a single String
        this.program = readSource(sourceFile);

        // turns the IF file into an array of int
        if (turnIFFileIntoArray(ifFile)==null) return;
        else this.ifArray = turnIFFileIntoArray(ifFile);
    }

    /**
     * Interpreter class constructor without IF file use
     *
     * @param sourceFile  the SOURCE file
     * @param ofFile      the OF file
     */
    public Interpreter(String sourceFile, String ofFile) {
        dataPointer = 0;
        programPointer = 0;

        memory = new int[memorySize]; //The memory will be 1000, 3 is just for testing pourposes

        // sets the files
        this.sourceFile = sourceFile;
        this.ofFile = ofFile;

        // turns the SOURCE file into a single String
        this.program = readSource(sourceFile);
    }


    /**
     * This method runs the program.
     *
     * The commands are:
     * >    moves the data pointer to the next cell.
     * <    moves the data pointer to the previous cell.
     * +    adds 1 to the value of the cell referenced by the data pointer.
     * -    subtracts 1 to the value of the cell referenced by the data pointer.
     * [    if the position of the memory referenced by the data pointer is 0, then sends the program pointer
     *      to the next command following the correspondig ]. Otherwise, advances the program pointer.
     * ]    if the position of the memory referenced by the data pointer is not 0,
     *      then sends the program pointer to the previous corresponding [.
     * ,    reads an entry from the IF file and stores it at the memory position referenced by the data pointer.
     * .    writes in the OF file the byte referenced by the data pointer.
     * $    ends the program and prints its content in the OF file.
     *
     *      Any other command is ignored and the program pointer is incremented.
     *
     * @return             0 if the program run successfully
     *                    -1 if it has unpaired [ and ]
     *                    -2 if it doesn´t end with $
     *                    -3 if used the command "," without providing an IF file
     */
    public int run() {
        if(!checkSpecialChars(program)) return -1;
        if(program.charAt(program.length()-1)!='$') return -2;
        mapSpecialChars(program);
        while (true) {
            char command = program.charAt(programPointer);
            switch (command){
                case '>':
                    dataPointer++;
                    programPointer++;
                    break;
                case '<':
                    dataPointer--;
                    programPointer++;
                    break;
                case '+':
                    memory[dataPointer]++;
                    programPointer++;
                    break;
                case '-':
                    memory[dataPointer]--;
                    programPointer++;
                    break;
                case '[':
                    if(memory[dataPointer] == 0){
                        for(int i=0; i<specialCharMap.length; i++){
                            if(specialCharMap[i][0] == programPointer){
                                programPointer = specialCharMap[i][1] + 1;
                            }
                        }
                    }else{
                        programPointer++;
                    }
                    break;
                case ']':
                    if(memory[dataPointer] != 0){
                        for(int i=0; i<specialCharMap.length; i++){
                            if(specialCharMap[i][1] == programPointer){
                                programPointer = specialCharMap[i][0];
                            }
                        }
                    }else{
                        programPointer++;
                    }
                    break;
                case ',':
                    // In case you reach this command but there was no IF file provided by the user
                    if (ifFile==null) {
                        return -3;
                    }
                    readIF();
                    programPointer++;
                    break;
                case '.':
                    writeInOF();
                    programPointer++;
                    break;
                case '$':
                    memoryDump();
                    return 0;
                default:
                    programPointer++;
            }
        }
    }

    /**
     * This function checks if the program has an equal amount of [ and ].
     * If the program doesn´t has an equal amount of [ and ] it cannot run properly.
     *
     * @param program     the program that was read from the SOURCE file
     * @return            true if the number of [ and ] is equal, otherwise it returns false
     */
    private boolean checkSpecialChars(String program){
        int count = 0;

        for(int i=0; i<program.length(); i++){
            if(program.charAt(i) == '[')
                count++;

            if(program.charAt(i) == ']')
                count--;
        }
        return count == 0;
    }

    /**
     * This function will map which '[' belongs to each ']', keeping the position in the string
     *
     * @param program     the program that was read from the SOURCE file
     */
    private void mapSpecialChars(String program){
        Stack<Integer> openChars = new Stack<Integer>();
        int count = 0;

        for(int i=0; i<program.length(); i++){
            if(program.charAt(i) == '[')
                count++;
        }

        specialCharMap = new int[count][2];
        int matrixRow = 0;

        for(int i=0; i<program.length(); i++){
            if(program.charAt(i) == '['){
                openChars.push(i);
            }
            if(program.charAt(i) == ']'){
                specialCharMap[matrixRow][0] = openChars.pop();
                specialCharMap[matrixRow][1] = i;
                matrixRow++;
            }
        }
    }

    /**
     * Writes the current position of the memory in the OF file
     *
     * @exception IOException           on any I/O error
     */
    private void writeInOF(){
        Path pathTexto = Paths.get(ofFile);
        System.out.println("chamou write " + memory[dataPointer]);

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(pathTexto.getFileName(), Charset.forName("utf8")))) {
            writer.println(memory[dataPointer] + "\n");
        } catch (IOException x) {
            System.err.format("I/O Error: %s%n\n", x);
        }
    }

    /**
     * Reads the SOURCE file and turns it into a single String for easier manuipulation
     *
     * @param source                    the SOURCE file
     * @exception NoSuchFileException   on file not found error
     * @exception IOException           on any other error
     * @return                          a String with the content of the SOURCE file
     */
    public String readSource(String source) {
        Path path1 = Paths.get(source);
        String sourceStringyfied ="";

        try (BufferedReader reader = Files.newBufferedReader(path1.getFileName(), Charset.forName("utf8"))) {
            String line = null;

            while ((line = reader.readLine()) != null) {

                if (!line.isEmpty()) {
                    line = line.trim();
                    sourceStringyfied = sourceStringyfied + line;
                }
            }

            return sourceStringyfied;

        } catch (NoSuchFileException x) {
            System.err.format("SOURCE File not found.\n", x);
        } catch (IOException x) {
            System.err.format("I/O Error %s%n\n", x);
        }
        return null;
    }

    /**
     * Reads the file IF and turns it into an array of int for easier manipulation
     *
     * @param ifFile                    the IF file
     * @exception NoSuchFileException   on file not found error
     * @exception NumberFormatException if found anything else other than integer values in the IF file
     * @exception IOException           on any other error
     * @return                          an array with the IF file values
     */
    public int [] turnIFFileIntoArray(String ifFile) {
        Path path1 = Paths.get(ifFile);
        int [] IFArray;

        try (BufferedReader reader = Files.newBufferedReader(path1.getFileName(), Charset.forName("utf8"))) {
            String line = null;

            // checks the size of the future array for the IF file
            int size = getSizeForTheIfFileArray(ifFile);

            IFArray = new int[size];

            int index = 0;
            int IFValue;

            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    line = line.trim();
                    IFValue = Integer.parseInt(line);
                    IFArray[index] = IFValue;
                    index++;
                }
            }

            return IFArray;
        } catch (NoSuchFileException x) {
            System.err.format("IF File not found.\n", x);
        } catch (NumberFormatException x) {
            System.err.print("IF file must contain only integer numbers.\n");
        } catch (IOException x) {
            System.err.format("I/O error: %s%n\n", x);
        }
        return null;
    }

// para eu testar os erros. estou aprendendo. não apaga - KK beleza
/*
    catch (ArrayIndexOutOfBoundsException e) {
        System.err.println("Uso: AppExcecao3 <num1> <num2>");
        e.printStackTrace();
        System.out.println("Mensagem: "+e.getMessage());
    } catch (NumberFormatException e) {
        System.err.println("Valores devem ser inteiros");
    } catch (ArithmeticException e) {
        System.err.println("Não posso dividir por zero!");
    }



    if (i2==0){
        // throw new ArithmeticException("Dividir por zero não pode!");
        throw new IllegalArgumentException("Dividir por zero não pode!");
    }


    catch (NoSuchFileException x) {
        System.err.format("Arquivo não existe!!", x);
    } catch (IOException e) { //esse catch é exigido pelo newBufferedReader
        e.printStackTrace();
*/


    /**
     * reads the IF file to check how many values it has
     *
     * @param ifFile                the IF file
     * @exception IOException       on any I/O error
     * @return                      the number os values in the IF file
     */
    public int getSizeForTheIfFileArray (String ifFile) {
        Path path1 = Paths.get(ifFile);
        int size=0;

        try (BufferedReader reader = Files.newBufferedReader(path1.getFileName(), Charset.forName("utf8"))) {
            String line = null;

            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()){
                    size++;
                }
            }

            return size;

        } catch (IOException x) {
            System.err.format("I/O Error: %s%n\n", x);
        }
        return 0;
    }

    /**
     * Prints in the screen the values stored in the memory
     *
     */
    private void memoryDump(){
        dataPointer = 0;
        //System.out.println("\nMemory:");
        for(int i=0; i<memory.length; i++){
            //System.out.print(memory[i] + "; ");
            writeInOF();
            dataPointer++;
        }
    }

    /**
     * Reads the IF array and puts its current value in the current position of the memory,
     * then increases the IF pointer in one position
     */
    private void readIF(){
        memory[dataPointer] = ifArray[IFPointer];
        IFPointer++;
    }
}