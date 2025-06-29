package packtwo;

import packone.Modifier;

public class Program2 {
    public static void main(String[] args) {
        Modifier obj = new Modifier();
        System.out.println("Public: " + obj.pub);
        // System.out.println("Private: " + obj.priv); // Error
        // System.out.println("Protected: " + obj.prot); // Error
        // System.out.println("Default: " + obj.def); // Error
    }
}
