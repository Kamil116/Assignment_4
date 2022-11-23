import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlackList {
    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Integer> array = new HashMap<>();
        array.put("Rook", 1);
        array.put("Knight", 2);
        System.out.println(array.values());
        Set<String> keys = array.keySet();
        for (String val : keys) {
            System.out.println(array.get(val));
        }
        // Hello myClass = new World();
        // myClass.func();
    }
}

abstract class Hello {
    void func() {
        if (this.getClass() == World.class) {
            System.out.println("Hello, world!");
        } else {
            System.out.println("Some problems");
        }
    }
}

class World extends Hello {
    int a;
}