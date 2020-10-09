import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;


public class App {

    private static String ofFile;
    private static String ifFile;
    private static String sourceFile;
    private static int dataPointer;
    private static int ProgramPointer;
    private static int[] memory;

    public static void main(String[] args) {
        memory = new int[10];
        dataPointer = 0;//que armazena um valor inteiro positivo indicando qual a posição de memória deve ser usada.
        ProgramPointer = 0;//que armazena um inteiro representando o índice do comando atual do programa.

        ofFile = "of.txt";
        ifFile = "if.txt";
        sourceFile = "source";

        String sourceProgram = readSource(sourceFile);
        int[] ifArrayFile = turnIfFileIntoArray(ifFile);
        // usem writeInOf para escrever no OF


        run();

        int[] arquivoIf = new int[10];
    }


    public static void run() {
        String programa = ">.>.>.>.";

        int linha = 0;

        for (int i = 0; i < programa.length(); i++) {
            if (programa.charAt(i) == '>') {
                if (dataPointer == 10) {
                    System.out.println("DataPointer cheio");

                } else {
                    dataPointer++;
                    //ProgramPointer++;
                    System.out.println("dataPointer = " + dataPointer + "\n" + "ProgramPointer = " + ProgramPointer + "\n");

                }

            }
            if (programa.charAt(i) == '<') {
                if (dataPointer == 0) {
                    System.out.println("Datapointer esta vazio");
                } else {
                    dataPointer--;
                    //ProgramPointer++;
                    System.out.println("dataPointer = " + dataPointer + "\n" + "ProgramPointer = " + ProgramPointer + "\n");
                }
            }
            if (programa.charAt(i) == '+') {
                memory[dataPointer]++;
                ProgramPointer++;
                System.out.println("Memory[dataPointer] = " + memory[dataPointer] + "\n" + "ProgramPointer = " + ProgramPointer + "\n");
            }
            if (programa.charAt(i) == '-') {
                memory[dataPointer]--;
                ProgramPointer++;
                System.out.println("Memory[dataPointer] = " + memory[dataPointer] + "\n" + "ProgramPointer = " + ProgramPointer + "\n");
            }
            if (programa.charAt(i) == ',') {//lê uma linha do arquivo IF e o armazena na posição apontada pelo ponteiro de dados

                memory[dataPointer] = 0;
                System.out.println("Leitura do IF, dataPointer está em: " + dataPointer + " e ira armazenar em memory[datapointer] que contem valor " + memory[dataPointer] + "\n");
                linha++;
            }
            if (programa.charAt(i) == '.') {//escreve no arquivo OF o byte apontado pelo ponteiro de dados.
                int dado = memory[dataPointer];
                writeInOF(dado);
                System.out.println("Escrevendo no OF byte apontado pelo DataPointer: " + dataPointer + "\n");
            }
            if (programa.charAt(i) == '$') {
                System.out.println("Fim do programa");
                break;
            }
            if (programa.charAt(i) == '[') {
                //Lucas sua parte vem aqui
            }
            if (programa.charAt(i) == ']') {
                //Lucas sua parte vem aqui
            }

        }
    }

    /**
     * Writes the current position of the memory int the OF file
     *
     * @throws IOException On file not found error
     */
    private static void writeInOF(int dado) {
        Path pathTexto = Paths.get(ofFile);

        try {
            PrintWriter writer = new PrintWriter(Files.newBufferedWriter(pathTexto.getFileName(), Charset.forName("utf8")));
            writer.println(12);
        } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }

    }

    /**
     * Reads the SOURCE file and turns it into a single String for easier manipulation
     *
     * @param source                    the SOURCE file
     * @exception NoSuchFileException   on file not found error
     * @exception IOException           on any other error
     * @return                          a String with the content of the SOURCE file
     */
    private static String readSource(String source) {
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
     * Reads the IF file and turns it into an array of int for easier manipulation
     *
     * @param ifFile                    the IF file
     * @exception NoSuchFileException   on file not found error
     * @exception NumberFormatException if found anything else other than integer values in the IF file
     * @exception IOException           on any other error
     * @return                          an array with the IF file values
     */
    private int [] turnIFFileIntoArray(String ifFile) {
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

                    // if the value in the IF file is a number it is entered as a single value in the array
                    if (isNumeric(line)) {
                        IFValue = Integer.parseInt(line);
                        IFArray[index] = IFValue;
                        index++;
                    }

                    // if the value is a String it is entered as a sequence of bytes
                    else {
                        for (int i=0; i<line.length(); i++) {
                            IFValue = (byte) line.charAt(i);
                            IFArray[index] = IFValue;
                            index++;
                        }
                    }

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


    /**
     * Checks if the line read in the IF file is a number
     *
     * @param line  the line in the IF file
     * @return      true if it is a int or false otherwise
     */
    private static boolean isNumeric(String line) {
        try {
            Integer.parseInt(line);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }



    /**
     * reads the IF file to check how many values it has
     *
     * @param ifFile the IF file
     * @return the number os values in the IF file
     * @throws IOException On file not found error
     */
    public static int getSizeForTheIfFileArray(String ifFile) {
        Path path1 = Paths.get(ifFile);
        int size = 0;

        try (BufferedReader reader = Files.newBufferedReader(path1.getFileName(), Charset.forName("utf8"))) {
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    size++;
                }
            }

            return size;

        } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }
        return 0;
    }

}