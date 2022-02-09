/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclassmaker;

/**
 *
 * @author Khaled
 */
public class Field {


    private boolean get = false, set = false, constructor = false;
    private String name = "", type = "", className;
    private int pointerDeep;

    public String getType() {
        return type;
    }

    public Field(String line, String className) {
        this.className = className;
        boolean readType = true, readName = false, readPointers = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (readName) {
                if (c == ' ' && name.length() != 0) {
                    readName = false;
                } else {
                    name += c;
                }
            } else if (readPointers) {
                if (c != '*' && c != ' ') {
                    readPointers = false;
                    readName = true;
                    name += c;
                } else if (c == '*') {
                    pointerDeep++;
                }
            } else if (readType) {
                if (c != ' ' && c != '*') {
                    type += c;
                } else {
                    if (c == '*') {
                        pointerDeep++;
                    }
                    readType = false;
                    readPointers = true;
                }
            } else {
                if (c == 'g') {
                    get = true;
                }
                if (c == 's') {
                    set = true;
                }
                if (c == 'c') {
                    constructor = true;
                }
            }
        }
    }

    public boolean isConstructor() {
        return constructor;
    }

    public String getHeaderFunc() {
        String ret = "";
        if (get) {
            ret = "  " + type + PointerToS() + " get" + ((name.charAt(0) + "").toUpperCase() + name.substring(1)) + "();\n";
        }
        if (set) {
            ret += "  void set" + ((name.charAt(0) + "").toUpperCase() + name.substring(1)) + "(" + type + PointerToS() + ");\n";
        }
        return ret;
    }

    public String getHeaderVariables() {
        return "  " + type + PointerToS() + " " + name + ";\n";
    }

    public String getSourceMembers() {
        String ret = "";
        if (get) {
            ret = type + PointerToS() + " " + className + "::get" + ((name.charAt(0) + "").toUpperCase() + name.substring(1))
                    + "(){return this->" + name + ";}\n";
        }
        if (set) {
            ret += "void " + className + "::set" + ((name.charAt(0) + "").toUpperCase() + name.substring(1)) + "(" + type + PointerToS() + " " + name
                    + "){this->" + name + " = " + name + ";}\n";
        }
        return ret;
    }

    public String PointerToS() {
        String p = "";
        for (int i = 0; i < pointerDeep; i++) {
            p += "*";
        }
        return p;
    }

    public String getName() {
        return name;
    }

}
