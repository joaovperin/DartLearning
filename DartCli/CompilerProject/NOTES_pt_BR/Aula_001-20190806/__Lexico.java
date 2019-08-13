import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

/**
 * Lexico - Classe para implementação de um analisador Léxico básico
 *
 * @author Ricardo Ferreira de Oliveira
 * @author turma de compiladores de 1/2019
 *
 */

public class Lexico {

    static final int T_AND = 1;
    static final int T_ASSERT = 2;
    static final int T_BREAK = 3;
    static final int T_CLASS = 4;
    static final int T_CONTINUE = 5;
    static final int T_DEF = 6;
    static final int T_DEL = 7;
    static final int T_ELIF = 8;
    static final int T_ELSE = 9;
    static final int T_EXCEPT = 10;
    static final int T_EXEC = 11;
    static final int T_FINALLY = 12;
    static final int T_FOR = 13;
    static final int T_FROM = 14;
    static final int T_GLOBAL = 15;
    static final int T_IF = 16;
    static final int T_IMPORT = 17;
    static final int T_IN = 18;
    static final int T_IS = 19;
    static final int T_LAMBDA = 20;
    static final int T_NOT = 21;
    static final int T_OR = 22;
    static final int T_PASS = 23;
    static final int T_PRINT = 24;
    static final int T_RAISE = 25;
    static final int T_RETURN = 26;
    static final int T_TRY = 27;
    static final int T_WHILE = 28;
    static final int T_YIELD = 29;
    static final int T_ID = 30;
    static final int T_NUMERO = 31;
    static final int T_ABRE_PARENTE = 32;
    static final int T_FECHA_PARENTE = 33;

    static final int T_SYMBOL_PLUS = 34;
    static final int T_SYMBOL_MINUS = 35;
    static final int T_SYMBOL_MULTIPLY = 36;
    static final int T_SYMBOL_DIVIDE = 37;
    static final int T_SYMBOL_TWO_POINTS = 38;

    static final int T_SYMBOL_EQUAL = 39;
    static final int T_SYMBOL_LT = 40;
    static final int T_SYMBOL_LTE = 41;
    static final int T_SYMBOL_GT = 42;
    static final int T_SYMBOL_GTE = 43;
    static final int T_SYMBOL_UNEQUAL = 44;

    static final int T_FIM_FONTE = 90;
    static final int T_ERRO_LEX = 98;
    static final int T_NULO = 99;

    static final int FIM_ARQUIVO = 26;

    static File arqFonte;
    static BufferedReader rdFonte;
    static File arqDestino;

    static char lookAhead;
    static int token;
    static String lexema;
    static int ponteiro;
    static String linhaFonte;
    static int linhaAtual;
    static int colunaAtual;
    static String mensagemDeErro;
    static StringBuilder tokensIdentificados = new StringBuilder();

    public static void main(String s[]) throws java.io.IOException {
        try {
            abreArquivo();
            // abreDestino();
            linhaAtual = 0;
            colunaAtual = 0;
            ponteiro = 0;
            linhaFonte = "";
            token = T_NULO;
            mensagemDeErro = "";

            movelookAhead();

            while ((token != T_FIM_FONTE) && (token != T_ERRO_LEX)) {
                buscaProximoToken();
                mostraToken();
            }

            if (token == T_ERRO_LEX) {
                // JOptionPane.showMessageDialog(null, mensagemDeErro, "Erro Léxico!",
                // JOptionPane.ERROR_MESSAGE);
            } else {
                // JOptionPane.showMessageDialog(null, "Análise Léxica terminada sem erros
                // léxicos", "Análise Léxica terminada!", JOptionPane.INFORMATION_MESSAGE);
            }

            exibeTokens();

            gravaSaida(arqDestino);

            fechaFonte();

        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null, "Arquivo nao existe!", "FileNotFoundException!",
                    JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedEncodingException uee) {
            JOptionPane.showMessageDialog(null, "Erro desconhecido", "UnsupportedEncodingException!",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Erro de io: " + ioe.getMessage(), "IOException!",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            System.out.println("Execucao terminada!");
        }
    }

    static void fechaFonte() throws IOException {
        rdFonte.close();
    }

    static void movelookAhead() throws IOException {
        if ((ponteiro + 1) > linhaFonte.length()) {
            linhaAtual++;
            ponteiro = 0;
            if ((linhaFonte = rdFonte.readLine()) == null) {
                lookAhead = FIM_ARQUIVO;
            } else {
                StringBuffer sbLinhaFonte = new StringBuffer(linhaFonte);
                sbLinhaFonte.append('\13').append('\10');
                linhaFonte = sbLinhaFonte.toString();

                lookAhead = linhaFonte.charAt(ponteiro);
            }
        } else {
            lookAhead = linhaFonte.charAt(ponteiro);
        }

        ponteiro++;
        colunaAtual = ponteiro + 1;
    }

    static void buscaProximoToken() throws IOException {
        int i, j;

        StringBuffer sbLexema = new StringBuffer("");

        while ((lookAhead == 9) || (lookAhead == '\n') || (lookAhead == 8) || (lookAhead == 11) || (lookAhead == 12)
                || (lookAhead == '\r') || (lookAhead == 32)) {
            movelookAhead();
        }

        /*--------------------------------------------------------------*
         * Caso o primeiro caracter seja alfabetico, procuro capturar a *
         * sequencia de caracteres que se segue a ele e classifica-la   *
         *--------------------------------------------------------------*/
        if (((lookAhead >= 'A') && (lookAhead <= 'Z')) || ((lookAhead >= 'a') && (lookAhead <= 'z'))
                || (lookAhead == '+') || (lookAhead == '-') || (lookAhead == '*') || (lookAhead == '/')
                || (lookAhead == ':') || (lookAhead == '=') || (lookAhead == '!') || (lookAhead == '>')
                || (lookAhead == '<')) {

            sbLexema.append(lookAhead);
            movelookAhead();

            while (((lookAhead >= 'a') && (lookAhead <= 'z')) || ((lookAhead >= 'A') && (lookAhead <= 'Z'))
                    || ((lookAhead >= '0') && (lookAhead <= '9')) || (lookAhead == '_') || (lookAhead == '+')
                    || (lookAhead == '-') || (lookAhead == '*') || (lookAhead == '/') || (lookAhead == ':')
                    || (lookAhead == '=') || (lookAhead == '!') || (lookAhead == '>') || (lookAhead == '<')) {
                sbLexema.append(lookAhead);
                movelookAhead();
            }

            lexema = sbLexema.toString();

            /* Classifico o meu token como palavra reservada ou id */
            if (lexema.equals("and"))
                token = T_AND;
            else if (lexema.equals("assert"))
                token = T_ASSERT;
            else if (lexema.equals("break"))
                token = T_BREAK;
            else if (lexema.equals("class"))
                token = T_CLASS;
            else if (lexema.equals("continue"))
                token = T_CONTINUE;
            else if (lexema.equals("def"))
                token = T_DEF;
            else if (lexema.equals("del"))
                token = T_DEL;
            else if (lexema.equals("elif"))
                token = T_ELIF;
            else if (lexema.equals("else"))
                token = T_ELSE;
            else if (lexema.equals("except"))
                token = T_EXCEPT;
            else if (lexema.equals("exec"))
                token = T_EXEC;
            else if (lexema.equals("finally"))
                token = T_FINALLY;
            else if (lexema.equals("for"))
                token = T_FOR;
            else if (lexema.equals("from"))
                token = T_FROM;
            else if (lexema.equals("global"))
                token = T_GLOBAL;
            else if (lexema.equals("if"))
                token = T_IF;
            else if (lexema.equals("import"))
                token = T_IMPORT;
            else if (lexema.equals("in"))
                token = T_IN;
            else if (lexema.equals("is"))
                token = T_IS;
            else if (lexema.equals("lambda"))
                token = T_LAMBDA;
            else if (lexema.equals("not"))
                token = T_NOT;
            else if (lexema.equals("or"))
                token = T_OR;
            else if (lexema.equals("pass"))
                token = T_PASS;
            else if (lexema.equals("print"))
                token = T_PRINT;
            else if (lexema.equals("raise"))
                token = T_RAISE;
            else if (lexema.equals("return"))
                token = T_RETURN;
            else if (lexema.equals("try"))
                token = T_TRY;
            else if (lexema.equals("while"))
                token = T_WHILE;
            else if (lexema.equals("yield"))
                token = T_YIELD;
            else if (lexema.equals("+"))
                token = T_SYMBOL_PLUS;
            else if (lexema.equals("-"))
                token = T_SYMBOL_MINUS;
            else if (lexema.equals("*"))
                token = T_SYMBOL_MULTIPLY;
            else if (lexema.equals("/"))
                token = T_SYMBOL_DIVIDE;
            else if (lexema.equals(":"))
                token = T_SYMBOL_TWO_POINTS;

            else if (lexema.equals(">"))
                token = T_SYMBOL_GT;
            else if (lexema.equals(">="))
                token = T_SYMBOL_GTE;
            else if (lexema.equals("<"))
                token = T_SYMBOL_LT;
            else if (lexema.equals("<="))
                token = T_SYMBOL_LTE;
            else if (lexema.equals("=="))
                token = T_SYMBOL_EQUAL;
            else if (lexema.equals("!="))
                token = T_SYMBOL_UNEQUAL;
            else {
                token = T_ID;
            }
        } else if ((lookAhead >= '0') && (lookAhead <= '9')) {
            sbLexema.append(lookAhead);
            movelookAhead();
            while ((lookAhead >= '0') && (lookAhead <= '9')) {
                sbLexema.append(lookAhead);
                movelookAhead();
            }
            if (lookAhead == '.') {
                sbLexema.append(lookAhead);
                movelookAhead();
                while ((lookAhead >= '0') && (lookAhead <= '9')) {
                    sbLexema.append(lookAhead);
                    movelookAhead();
                }
            }
            token = T_NUMERO;
        } else if (lookAhead == '(') {
            sbLexema.append(lookAhead);
            movelookAhead();
            token = T_ABRE_PARENTE;
        } else if (lookAhead == ')') {
            sbLexema.append(lookAhead);
            movelookAhead();
            token = T_FECHA_PARENTE;
        } else if (lookAhead == FIM_ARQUIVO) {
            token = T_FIM_FONTE;
        } else {
            token = T_ERRO_LEX;
            mensagemDeErro = "Erro Léxico na linha: " + linhaAtual + "\nReconhecido ao atingir a coluna: " + colunaAtual
                    + "\nLinha do Erro: <" + linhaFonte + ">\nToken desconhecido: " + lookAhead;
            sbLexema.append(lookAhead);
        }

        lexema = sbLexema.toString();
    }

    static void mostraToken() {

        StringBuffer tokenLexema = new StringBuffer("");
        Pattern ptTokenFields = Pattern.compile("T_.+");

        Field[] fields = Lexico.class.getDeclaredFields();
        Map<String, Integer> staticFields = new ArrayList<>();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && ptTokenFields.matcher(fieldName).find()) {
                staticFields.put(fieldName, Integer.valueOf(field.getInt(Lexico.class)));
            }
        }

        if (staticFields.containsValue(token)) {
            //tokenLexema.append(staticFields.get(tok));
        }

        switch (token) {
        case T_AND:
            tokenLexema.append("T_AND");
            break;
        case T_ASSERT:
            tokenLexema.append("T_ASSERT");
            break;
        case T_BREAK:
            tokenLexema.append("T_BREAK");
            break;
        case T_CLASS:
            tokenLexema.append("T_CLASS");
            break;
        case T_CONTINUE:
            tokenLexema.append("T_CONTINUE");
            break;
        case T_DEF:
            tokenLexema.append("T_DEF");
            break;
        case T_DEL:
            tokenLexema.append("T_DEL");
            break;
        case T_ELIF:
            tokenLexema.append("T_ELIF");
            break;
        case T_ELSE:
            tokenLexema.append("T_ELSE");
            break;
        case T_EXCEPT:
            tokenLexema.append("T_EXCEPT");
            break;
        case T_EXEC:
            tokenLexema.append("T_EXEC");
            break;
        case T_FINALLY:
            tokenLexema.append("T_FINALLY");
            break;
        case T_FOR:
            tokenLexema.append("T_FOR");
            break;
        case T_FROM:
            tokenLexema.append("T_FROM");
            break;
        case T_GLOBAL:
            tokenLexema.append("T_GLOBAL");
            break;
        case T_IF:
            tokenLexema.append("T_IF");
            break;
        case T_IMPORT:
            tokenLexema.append("T_IMPORT");
            break;
        case T_IN:
            tokenLexema.append("T_IN");
            break;
        case T_IS:
            tokenLexema.append("T_IS");
            break;
        case T_LAMBDA:
            tokenLexema.append("T_LAMBDA");
            break;
        case T_NOT:
            tokenLexema.append("T_NOT");
            break;
        case T_OR:
            tokenLexema.append("T_OR");
            break;
        case T_PASS:
            tokenLexema.append("T_PASS");
            break;
        case T_PRINT:
            tokenLexema.append("T_PRINT");
            break;
        case T_RAISE:
            tokenLexema.append("T_RAISE");
            break;
        case T_RETURN:
            tokenLexema.append("T_RETURN");
            break;
        case T_TRY:
            tokenLexema.append("T_TRY");
            break;
        case T_WHILE:
            tokenLexema.append("T_WHILE");
            break;
        case T_YIELD:
            tokenLexema.append("T_YIELD");
            break;
        case T_ID:
            tokenLexema.append("T_ID");
            break;
        case T_ABRE_PARENTE:
            tokenLexema.append("T_ABRE_PARENTE");
            break;
        case T_FECHA_PARENTE:
            tokenLexema.append("T_FECHA_PARENTE");
            break;
        case T_FIM_FONTE:
            tokenLexema.append("T_FIM_FONTE");
            break;
        case T_ERRO_LEX:
            tokenLexema.append("T_ERRO_LEX");
            break;
        case T_NULO:
            tokenLexema.append("T_NULO");
            break;
        case T_NUMERO:
            tokenLexema.append("T_NUMERO");
            break;
        case T_SYMBOL_PLUS:
            tokenLexema.append("T_SYMBOL_PLUS");
            break;
        case T_SYMBOL_MINUS:
            tokenLexema.append("T_SYMBOL_MINUS");
            break;
        case T_SYMBOL_MULTIPLY:
            tokenLexema.append("T_SYMBOL_MULTIPLY");
            break;
        case T_SYMBOL_DIVIDE:
            tokenLexema.append("T_SYMBOL_DIVIDE");
            break;
        case T_SYMBOL_TWO_POINTS:
            tokenLexema.append("T_SYMBOL_TWO_POINTS");
            break;
        case T_SYMBOL_UNEQUAL:
            tokenLexema.append("T_SYMBOL_UNEQUAL");
            break;
        case T_SYMBOL_EQUAL:
            tokenLexema.append("T_SYMBOL_EQUAL");
            break;
        case T_SYMBOL_GT:
            tokenLexema.append("T_SYMBOL_GT");
            break;
        case T_SYMBOL_GTE:
            tokenLexema.append("T_SYMBOL_GTE");
            break;
        case T_SYMBOL_LT:
            tokenLexema.append("T_SYMBOL_LT");
            break;
        case T_SYMBOL_LTE:
            tokenLexema.append("T_SYMBOL_LTE");
            break;
        default:
            tokenLexema.append("N/A");
            break;
        }
        System.out.println(tokenLexema.toString() + " ( " + lexema + " )");
        acumulaToken(tokenLexema.toString() + " ( " + lexema + " )");
        tokenLexema.append(lexema);
    }

    private static void abreArquivo() {

        final String inputDirectory = "C:/fontes/dart/DartLearning/DartCli/CompilerProject/NOTES_pt_BR/Aula_001-20190806";
        // final String inputDirectory =
        // "/home/joaovperin/Projetos/Dart/DartLearning/DartCli/CompilerProject/NOTES_pt_BR/Aula_001-20190806";
        // JFileChooser fileChooser = new JFileChooser();
        // fileChooser.setCurrentDirectory(new File(inputDirectory));

        // fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // FiltroMe filtro = new FiltroMe();

        // fileChooser.addChoosableFileFilter(filtro);
        // int result = fileChooser.showOpenDialog(null);

        // if (result == JFileChooser.CANCEL_OPTION) {
        // return;
        // }

        // arqFonte = fileChooser.getSelectedFile();
        arqFonte = new File(inputDirectory + "/input.py");
        abreFonte(arqFonte);

    }

    private static boolean abreFonte(File fileName) {

        if (arqFonte == null || fileName.getName().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Nome de Arquivo Inválido", "Nome de Arquivo Inválido",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            linhaAtual = 1;
            try {
                FileReader fr = new FileReader(arqFonte);
                rdFonte = new BufferedReader(fr);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
    }

    private static void abreDestino() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(
                "/home/joaovperin/Projetos/Dart/DartLearning/DartCli/CompilerProject/NOTES_pt_BR/Aula_001-20190806"));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FiltroMe filtro = new FiltroMe();

        fileChooser.addChoosableFileFilter(filtro);
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        arqDestino = fileChooser.getSelectedFile();
    }

    private static boolean gravaSaida(File fileName) {

        if (arqDestino == null || fileName.getName().trim().equals("")) {
            // JOptionPane.showMessageDialog(null, "Nome de Arquivo Inválido", "Nome de
            // Arquivo Inválido",JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            FileWriter fw;
            try {
                System.out.println(arqDestino.toString());
                System.out.println(tokensIdentificados.toString());
                fw = new FileWriter(arqDestino);
                BufferedWriter bfw = new BufferedWriter(fw);
                bfw.write(tokensIdentificados.toString());
                bfw.close();
                JOptionPane.showMessageDialog(null, "Arquivo Salvo: " + arqDestino, "Salvando Arquivo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Entrada/Saída", JOptionPane.ERROR_MESSAGE);
            }
            return true;
        }
    }

    public static void exibeTokens() {

        JTextArea texto = new JTextArea();
        texto.append(tokensIdentificados.toString());
        // JOptionPane.showMessageDialog(null, texto, "Tokens Identificados
        // (token/lexema)", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void acumulaToken(String tokenIdentificado) {

        tokensIdentificados.append(tokenIdentificado);
        tokensIdentificados.append("\n");

    }

}

/**
 * Classe Interna para criação de filtro de seleção
 */
class FiltroMe extends FileFilter {

    public boolean accept(File arg0) {
        if (arg0 != null) {
            if (arg0.isDirectory()) {
                return true;
            }
            if (getExtensao(arg0) != null) {
                if (getExtensao(arg0).equalsIgnoreCase("py")) {
                    return true;
                }
            }
            ;
        }
        return false;
    }

    /**
     * Retorna quais extensões poderão ser escolhidas
     */
    public String getDescription() {
        return "*.py";
    }

    /**
     * Retorna a parte com a extensão de um arquivo
     */
    public String getExtensao(File arq) {
        if (arq != null) {
            String filename = arq.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
            ;
        }
        return null;
    }
}
