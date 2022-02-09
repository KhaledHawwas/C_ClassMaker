package cclassmaker;

import java.util.ArrayList;

public class Cclass {

    private boolean destructor, emptyCon;
    private String name;
    private ArrayList<Field> members = new ArrayList<>();

    public Cclass(String name, String text, boolean emptyCon, boolean destructor) {
        this.emptyCon = emptyCon;
        this.destructor = destructor;
        this.name = name;
        String line = "";
        for (int i = 0; i < text.length(); i++) {
            if (i >= text.length() - 1) {
                line += text.charAt(i);
            }
            if (text.charAt(i) == '\n' || i >= text.length() - 1) {
                members.add(new Field(line, name));
                line = "";
            } else {
                line += text.charAt(i);
            }
        }
    }

    public static String getHeaderFile(String className, String fields, boolean destructor, boolean emptyCon) {
        String ret = "#porgma once\n\nclass " + className + "{\n public:\n", line = "", variable = "", function = "", con = "";
        Cclass cClass = new Cclass(className, fields, emptyCon, destructor);
        for (Field d : cClass.getMembers()) {
            function += d.getHeaderFunc();
            variable += d.getHeaderVariables();
            if (d.isConstructor()) {
                con += (con.isBlank() ? "" : ",") + d.getType() + d.PointerToS();
            }
        }
        if (!con.isBlank()) {
            ret += "  " + className + "(" + con + ");\n";
        }
        if (emptyCon) {
            ret += "  " + className + "();\n";
        }
        if (destructor) {
            ret += "  virtual ~" + className + "();\n";
        }
        ret += function;
        ret += " private:\n";
        ret += variable;
        ret += "\n};";
        return ret;
    }

    public static String getSourceFile(String className, String fields, boolean destructor, boolean emptyCon) {
        String ret = "#include <" + className + ".h>\n\n", conParameter = "",composition="";
        Cclass cClass = new Cclass(className, fields, emptyCon, destructor);
        for (Field d : cClass.getMembers()) {
            ret += d.getSourceMembers();
            if (d.isConstructor()) {
                conParameter += (conParameter.isBlank() ? "" : ",") + d.getType() + d.PointerToS()+" new"+d.getName();
                composition += (composition.isBlank() ? ":" : ",") +d.getName()+"(new"+d.getName()+")";
            }
        }
        if (!conParameter.isBlank()) {
            ret += className+"::" + className + "(" + conParameter + ")"+composition+"{\n}\n";
        }
        if (emptyCon) {
            ret += className+"::" + className + "(){\n}\n";
        }
        if (destructor) {
            ret += "~" + className +"::"+className+ "(){\n}\n";
        }
        return ret;
    }

    public boolean isDestructor() {
        return destructor;
    }

    public boolean isEmptyCon() {
        return emptyCon;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Field> getMembers() {
        return members;
    }
}
