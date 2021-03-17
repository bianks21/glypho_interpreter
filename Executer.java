import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Executer {

    // pentru input
    public BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    // stiva
    public List<BigInteger> stack = new LinkedList<>();
    // hashmap care retine indicele parantezei pereche
    public HashMap<Integer, Integer> braces = new HashMap<>();

    // decode pentru execute
    public Integer decode(List<BigInteger> l){
        HashMap <BigInteger, Integer> map = new HashMap<>();
        Integer currentIndex = 0;
        List <Integer> code = new ArrayList<>();
        for(int j = 0; j < 4; j++) {
            if(!(map.containsKey(l.get(j)))) {
                map.put(l.get(j), currentIndex);
                code.add(currentIndex);
                currentIndex++;
            } else {
                code.add(map.get(l.get(j)));
            }
        }
        Integer sum = 0;
        for(int k = 3; k >= 0; k--) {
            sum += code.get(k) * (int)Math.pow(3, 3-k);
        }

        return sum;
    }

    // ruleaza codul
    public void execute(List<Integer> code) {
        // stiva pentru paranteze
        Stack<Integer> s = new Stack<Integer>();
        // calculez parantezele pereche
        for (int i = 0; i < code.size(); i++) { 
            if(code.get(i) == 12) {
                s.push(i);
            } else if(code.get(i) ==  18) {
                if(s.isEmpty()) {
                    System.err.println("Error:" + i);
                    System.exit(-1);
                }
                Integer id = s.pop();
                // de la ] la [
                braces.put(i, id);
                // invers
                braces.put(id, i);
            }
        }
        // daca stiva nu e goala, nu e corecta parantezarea
        if(!s.isEmpty()) {
            System.err.println("Error:" + code.size());
            System.exit(-1);
        }

        for (int i = 0; i < code.size(); i++) {
            switch (code.get(i)) {
                case 0:
                   // NOP
                    break;
                case 1:
                    // INPUT
                    String name;
                    try {
                        name = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    try {
                        // citesc in baza corespunzatoare
                        BigInteger x = new BigInteger(name, Main.base);
                        stack.add(x);
                    } catch (Exception x) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    }
                    break;
                case 3: 
                   // ROT
                   if(stack.size() == 0) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        BigInteger peak = stack.get(stack.size() -1);
                        stack.remove(stack.size()-1);
                        stack.add(0, peak);
                    }
                    break;
                case 4:
                   // SWAP
                    if(stack.size() <  2) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        BigInteger last = stack.get(stack.size() - 1);
                        BigInteger beforeLast = stack.get(stack.size() - 2);
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        stack.add(last);
                        stack.add(beforeLast);
                    }
                    break;
                case 5:
                    /// PUSH
                    stack.add(BigInteger.valueOf(1));
                    break;
                case 9:
                    // RROT
                    if(stack.size() == 0) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        BigInteger base = stack.get(0);
                        stack.remove(0);
                        stack.add(base);
                    }
                    break;
                case 10:
                    // DUP
                    if(stack.size() == 0) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        stack.add(stack.get(stack.size() - 1));
                    }
                    break;
                case 11:
                    // ADD
                    if(stack.size() <  2) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        BigInteger last = stack.get(stack.size() - 1);
                        BigInteger beforeLast = stack.get(stack.size() - 2);
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        BigInteger result = last.add(beforeLast);
                        stack.add(result);
                    }
                    break;    
                case 12:
                    // L-BRACE
                    if(stack.size() == 0) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        // daca e cazul, sar la paranteza pereche
                        if(stack.get(stack.size() - 1).equals(BigInteger.ZERO)) {
                            i = braces.get(i);
                        }
                    }
                    break;
                case 13:
                    // OUTPUT
                    if(stack.size() == 0) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        // afisez in baza corespunzatoare si convertesc la uppercase
                        System.out.println(stack.get(stack.size()-1).toString(Main.base).toUpperCase());
                        stack.remove(stack.size() -1);
                    }
                    break;
                case 14:
                    // MULTIPLY
                    if(stack.size() <  2) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        BigInteger last = stack.get(stack.size() - 1);
                        BigInteger beforeLast = stack.get(stack.size() - 2);
                        stack.remove(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        BigInteger result = last.multiply(beforeLast);
                        stack.add(result);
                    }
                    break;            
                case 15:
                    //  EXECUTE
                    if(stack.size() < 4) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        List<BigInteger> l = new ArrayList<>();
                        // ia 4 elemente din stiva
                        for(int k = 0; k < 4; k++) {
                            BigInteger y = stack.get(stack.size() - 1);
                            stack.remove(stack.size() - 1);
                            l.add(y);
                        }
                        // decodeaza instructiunea
                        Integer instruction = decode(l);
                        execFunc(instruction, i);
                    }
                    break;
                case 16:
                    // NEGATE
                    if(stack.size() == 0) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        BigInteger a = stack.get(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        BigInteger result = a.negate();
                        stack.add(result);
                    }
                    break;
                case 17:
                    // POP
                    if(stack.size() == 0) {
                        System.err.println("Exception:" + i);
                        System.exit(-2);
                    } else {
                        stack.remove(stack.size() - 1);
                    }
                    break;         
                case 18:
                    // R-BRACE
                    // se intoarce la paranteza pereche
                    i = braces.get(i) - 1;
                    break;
            }
        }
    }

    // folosit pentru a executa instructiunile pentru EXECUTE
    public void execFunc(Integer code, Integer index) {
        switch (code) {
            case 0:
               // NOP
                break;
            case 1:
                // INPUT
                String name;
                try {
                    name = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                try {
                    BigInteger x = new BigInteger(name, Main.base);
                    stack.add(x);
                } catch (Exception x) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                }
                break;
            case 3: 
               // ROT
               if(stack.size() == 0) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    BigInteger peak = stack.get(stack.size() -1);
                    stack.remove(stack.size()-1);
                    stack.add(0, peak);
                }
                break;
            case 4:
               // SWAP
                if(stack.size() <  2) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    BigInteger last = stack.get(stack.size() - 1);
                    BigInteger beforeLast = stack.get(stack.size() - 2);
                    stack.remove(stack.size() - 1);
                    stack.remove(stack.size() - 1);
                    stack.add(last);
                    stack.add(beforeLast);
                }
                break;
            case 5:
                // PUSH
                stack.add(BigInteger.valueOf(1));
                break;
            case 9:
                // RROT
                if(stack.size() == 0) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    BigInteger base = stack.get(0);
                    stack.remove(0);
                    stack.add(base);
                }
                break;
            case 10:
                // DUP
                if(stack.size() == 0) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    stack.add(stack.get(stack.size() - 1));
                }
                break;
            case 11:
                // ADD
                if(stack.size() <  2) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    BigInteger last = stack.get(stack.size() - 1);
                    BigInteger beforeLast = stack.get(stack.size() - 2);
                    stack.remove(stack.size() - 1);
                    stack.remove(stack.size() - 1);
                    BigInteger result = last.add(beforeLast);
                    stack.add(result);
                }
                break;
            case 12:
                // L-BRACE
                System.err.println("Exception:" + index);
                System.exit(-2);
                break;
            case 13:
                // OUTPUT
                if(stack.size() == 0) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    System.out.println(stack.get(stack.size()-1).toString(Main.base).toUpperCase());
                    stack.remove(stack.size() -1);
                }
                break;
            case 14:
                // MULTIPLY
                if(stack.size() <  2) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    BigInteger last = stack.get(stack.size() - 1);
                    BigInteger beforeLast = stack.get(stack.size() - 2);
                    stack.remove(stack.size() - 1);
                    stack.remove(stack.size() - 1);
                    BigInteger result = last.multiply(beforeLast);
                    stack.add(result);
                }
                break;            
            case 15:
                // EXECUTE
                if(stack.size() < 4) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    List<BigInteger> l = new ArrayList<>();
                    for(int k = 0; k < 4; k++) {
                        BigInteger y = stack.get(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                        l.add(y);
                    }
                    Integer instruction = decode(l);
                    execFunc(instruction, index);
                }
                break;
            case 16:
                // NEGATE
                if(stack.size() == 0) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    BigInteger a = stack.get(stack.size() - 1);
                    stack.remove(stack.size() - 1);
                    BigInteger result = a.negate();
                    stack.add(result);
                }
                break;
            case 17:
                // POP
                if(stack.size() == 0) {
                    System.err.println("Exception:" + index);
                    System.exit(-2);
                } else {
                    stack.remove(stack.size() - 1);
                }
                break;
            case 18:
                // R-BRACE
                System.err.println("Exception:" + index);
                System.exit(-2);
        }
    }
}