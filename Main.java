import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
 
class Main
{
    public static Integer base = 10;
 
    public static String readFile(String p)
    {   // citeste tot fisierul
        byte[] str;
        try {
            str = Files.readAllBytes(Paths.get(p));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new String(str, StandardCharsets.UTF_8);
    }
 
    public static void main(String[] args) {

        String filePath = args[0]; // fisierul

        // daca baza e diferita de 10
        if(args.length == 2) {
            base = Integer.parseInt(args[1]);
        }
 
        String strRead = null;

        strRead = readFile(filePath);

        // verificare daca simbolurile sunt cu valori ASCII
        // intre 33 si 126
        String input = "";
        StringBuffer sb =  new StringBuffer();
        for(char c : strRead.toCharArray()) {
            if(c >= 33 && c <= 126) {
                sb.append(c);
            }
        }
        
        input = sb.toString();

        // verificare daca lungimea codului e multiplu de 4
        if(input.length() % 4 != 0) {
            System.err.println("Error:" + input.length() / 4);
            System.exit(-1);
        }

        // decodez instructiunile din fisierele gly
        Decoder decoder = new Decoder();

        // lista cu instructiuni ce urmeaza sa fie executate
        List<Integer> operations = decoder.decode(input);
        Executer executer = new Executer();
        // execut instructiunile 
        executer.execute(operations);
        
        System.exit(0);
    }
}