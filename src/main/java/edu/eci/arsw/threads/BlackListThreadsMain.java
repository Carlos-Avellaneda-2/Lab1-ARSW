/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

import java.util.List;
import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;

/**
 * Main class to test the parallel blacklist validation
 * @author hcadavid
 */
public class BlackListThreadsMain {
    
    public static void main(String[] args) {
        
        HostBlackListsValidator validator = new HostBlackListsValidator();

        int[] threadCounts = {1, 2, 4, 8};
        
        // Test 1: IP with many coincidences (should be found quickly)
        System.out.println("=== Testing IP with many coincidences ===");
        testIP("200.24.34.55", validator, threadCounts);
        
        System.out.println("\n=== Testing IP with scattered coincidences ===");
        testIP("202.24.34.55", validator, threadCounts);
        
        System.out.println("\n=== Testing IP with no coincidences ===");
        testIP("212.24.24.55", validator, threadCounts);
    }
    
    private static void testIP(String ipAddress, HostBlackListsValidator validator, int[] threadCounts) {
        for (int numThreads : threadCounts) {
            long startTime = System.currentTimeMillis();
            List<Integer> result = validator.checkHost(ipAddress, numThreads);
            long endTime = System.currentTimeMillis();
            
            System.out.println("Threads: " + numThreads + " | Found in blacklists: " + result + 
                             " | Time: " + (endTime - startTime) + "ms");
        }
    }
}
