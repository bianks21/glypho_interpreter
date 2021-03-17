import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Decoder {
    
    public List<Integer> decode(String str){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < str.length(); i+=4) {
            // retine codul pentru un caracter
            HashMap <Character, Integer> map = new HashMap<>();
            Integer currentIndex = 0;
            List <Integer> code = new ArrayList<>();
            for(int j = i; j < i+ 4; j++) {
                // daca nu l-am citit anterior, il adaug in hashmap
                if(!(map.containsKey(str.charAt(j)))) {
                    map.put(str.charAt(j), currentIndex);
                    code.add(currentIndex);
                    currentIndex++;
                } else {
                    // altfel, folosesc codul existent
                    code.add(map.get(str.charAt(j)));
                }
            }
            // transform codurile in baza 3 pentru a le
            // folosi mai usor
            Integer sum = 0;
            for(int k = 3; k >= 0; k--) {
                sum += code.get(k) * (int)Math.pow(3, 3-k);
            }
            list.add(sum);
        }

        // returneaza lista cu codurile instructiunilor
        return list;
    }
}