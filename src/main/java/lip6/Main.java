package lip6;

import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;

public class Main {
    public static void main(String[] args) {

        SpoonAPI spoon = new Launcher();
        spoon.addInputResource("D:\\repos\\EssaiSpoon\\src\\main\\java\\lip6\\Main.java");
        CtModel model = spoon.buildModel();

    }
}

