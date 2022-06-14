import static dev.redio.pointer.Keywords.*;

import dev.redio.pointer.NativeArray;

public class Main {
    public static void main(String[] args) {
        NativeArray<Integer> intArray = newNA(int.class,20);
        for (int i = 0; i < 20; i++) 
            intArray.set(i, i);
        System.out.println(intArray);
    }
}