package edu.emory.mathcs.nlp.test;

/**
 * Created by Song on 10/13/2015.
 */
import java.io.PrintStream;

public class Test {
    public static String[] arr = new String[]{"root", "ssh", "pragma", "memory", "gcc", "malloc", "compile", "packet", "reverse", "stack", "grep", "icectf", "system", "cache", "hack", "backdoor", "fail", "regex", "default", "class", "rsa", "foo", "engineer", "tarball", "void", "unix", "/dev/null"};

    public static void main(String[] arrstring) {
        block6 : {
            try {
                if (arrstring[0].charAt(0) == 'f' && arrstring[0].charAt(1) == 'l' && arrstring[0].charAt(2) == 'a' && arrstring[0].charAt(3) == 'g') {
                    if (Test.funky_func(arrstring[0])) {
                        if (Test.funkier_func(arrstring[0])) {
                            if (Test.funkiest_func(arrstring[0])) {
                                System.out.println("AAA");
                                System.out.println("Congratulations!");
                                System.out.println("Flag: " + arrstring[0]);
                                break block6;
                            }
                            throw new Exception();
                        }
                        throw new Exception();
                    }
                    throw new Exception();
                }
                throw new Exception();
            }
            catch (Exception var1_1) {
                System.out.println("PWNZ0R'D 1N TH3 PH4CE!!1");
            }
        }
    }

    public static boolean funky_func(String string) {
        for (int i = 11; i > 0; --i) {
            if (i % 8 != 0 || !string.split("_")[1].equals(arr[i])) continue;
            return true;
        }
        return false;
    }

    public static boolean funkier_func(String string) {
        for (int i = 5; i <= 22; ++i) {
            if (i % 2 != 0 || arr[i].charAt(0) != 'e') continue;
            return arr[i].equals(string.split("_")[2]);
        }
        return false;
    }

    public static boolean funkiest_func(String string) {
        return string.split("_")[3].equals(arr[11]);
    }
}