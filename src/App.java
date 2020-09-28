import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//n sei como vcs querem fazer mas vou so criar uma classe de boas aqui para a minha parte
public class App {

    private String ofFile;
    private String ifFile;
    private String sourceFile;

    public static void main(String[] args) {
        int[] memoria = new int[1000];
        int pontDado = 0;//que armazena um valor inteiro positivo indicando qual a posição de memória deve ser usada.
        int pontProg = 0;//que armazena um inteiro representando o índice do comando atual do programa.
        for (int i = 0; i < memoria.length; i++) {
            memoria[i] = 0;
        }


    }


    public void run() {
        while (true) {
            switch ((/*algo aqui*/)) {
                case ">"://incrementa o ponteiro de dados para a próxima posição (uma unidade à direita).
                    pontDado++;
                case "<"://decrementa o ponteiro de dados para a posição anterior (uma unidade à esquerda).
                    pontDado--;
                case "+"://incrementa em uma unidade a posição apontada pelo ponteiro de dados.

                case "-"://decrementa em uma unidade a posição apontada pelo ponteiro de dados.

                case "["://se a posição apontada pelo ponteiro de dados é 0, então desloque o ponteiro de programa para o próximo comando em sequência ao ] correspondente. Caso contrário, avance o ponteiro de programa.

                case "]"://se a posição apontada pelo ponteiro de dados é diferente de 0, então retroceda o ponteiro de programa para o [ correspondente.

                case ","://lê uma entrada do arquivo IF e o armazena na posição apontada pelo ponteiro de dados

                case ".":// escreve no arquivo OF o byte apontado pelo ponteiro de dados.

                case "$"://  termina o programa e imprime o conteúdo da memória no arquivo OF.
                    break;


            }
        }

    }

    /**
     * Writes the current position of the memory int the OF file
     *
     * @exception IOException     On file not found error
     */
    private void writeInOF(){
        Path pathTexto = Paths.get(ofFile);

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(pathTexto.getFileName(), Charset.forName("utf8")))) {
            writer.println(memory[dataPointer]);
        } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }

    }

    /**
     * Reads the SOURCE file and turns it into a single String for easier manuipulation
     *
     * @param source                the SOURCE file
     * @exception IOException       On file not found error
     * @return                      a String with the content of the SOURCE file
     */
    public String readSource(String source) {
        Path path1 = Paths.get(source);
        String sourceStringyfied ="";

        try (BufferedReader reader = Files.newBufferedReader(path1.getFileName(), Charset.forName("utf8"))) {
            String line = null;

            while ((line = reader.readLine()) != null) {

                if(!line.isEmpty()){
                    line = line.trim();
                    sourceStringyfied = sourceStringyfied + line;
                }
            }

            return sourceStringyfied;

        } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }
        return null;
    }

    /**
     * Reads the file IF and turns it into an array of int for easier manipulation
     *
     * @param ifFile                the IF file
     * @exception IOException       on file not found error
     * @return                      an array with the IF file values
     */
    public int [] turnIfFileIntoArray (String ifFile) {
        Path path1 = Paths.get(ifFile);
        int [] ifArray;

        try (BufferedReader reader = Files.newBufferedReader(path1.getFileName(), Charset.forName("utf8"))) {
            String line = null;

            // checks the size of the future array for the IF file
            int size = getSizeForTheIfFileArray(ifFile);

            ifArray = new int[size];

            int index=0;
            int IFvalue;

            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()){
                    line = line.trim();
                    IFvalue = Integer.parseInt(line);
                    ifArray[index]=IFvalue;
                    index++;
                }
            }

            return ifArray;

        } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }
        return null;
    }

    /**
     * reads the IF file to check how many values it has
     *
     * @param ifFile                the IF file
     * @exception IOException       On file not found error
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
            System.err.format("Erro de E/S: %s%n", x);
        }
        return 0;
    }

}