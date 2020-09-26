//n sei como vcs querem fazer mas vou so criar uma classe de boas aqui para a minha parte
public class App {
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
}