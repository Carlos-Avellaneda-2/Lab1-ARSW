package edu.eci.arsw.threads;

import java.util.List;

import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator; 

public class BlackListThread {

    public static void main(String[] args) {

        HostBlackListsValidator validator = new HostBlackListsValidator();


        System.out.println("=== Probando IP con muchas coincidencias ===");
        List<Integer> result1 = validator.checkHost("200.24.34.55");
        System.out.println("Blacklists encontradas: " + result1);
        System.out.println();

        System.out.println("=== Probando IP con coincidencias dispersas ===");
        List<Integer> result2 = validator.checkHost("202.24.34.55");
        System.out.println("Blacklists encontradas: " + result2);
        System.out.println();

        System.out.println("=== Probando IP sin coincidencias ===");
        List<Integer> result3 = validator.checkHost("212.24.24.55");
        System.out.println("Blacklists encontradas: " + result3);
    }
}
