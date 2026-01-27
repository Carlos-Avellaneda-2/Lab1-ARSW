/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import edu.eci.arsw.threads.BlackListThread;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is parallelized across N threads for improved performance.
     * @param ipaddress suspicious host's IP address.
     * @param numThreads number of threads to use for parallel search.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int numThreads) {
        
        LinkedList<Integer> blackListOcurrences = new LinkedList<>();
        
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        
        int registeredServersCount = skds.getRegisteredServersCount();
        
        // Create an array to hold the threads
        BlackListThread[] threads = new BlackListThread[numThreads];
        
        // Calculate the size of each segment
        int segmentSize = registeredServersCount / numThreads;
        
        // Create and start all threads
        for (int i = 0; i < numThreads; i++) {
            int initIndex = i * segmentSize;
            int endIndex;
            
            // Handle the last thread (in case registeredServersCount is not divisible by numThreads)
            if (i == numThreads - 1) {
                endIndex = registeredServersCount;
            } else {
                endIndex = (i + 1) * segmentSize;
            }
            
            threads[i] = new BlackListThread(initIndex, endIndex, ipaddress);
            threads[i].start();
        }
        
        // Wait for all threads to complete
        int totalOcurrences = 0;
        try {
            for (int i = 0; i < numThreads; i++) {
                threads[i].join();
                
                // Collect results from each thread
                blackListOcurrences.addAll(threads[i].getBlackListOcurrences());
                totalOcurrences += threads[i].getOcurrencesCount();
            }
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, "Thread interrupted while checking blacklists", e);
        }
        
        // Determine if host is trustworthy based on total occurrences
        if (totalOcurrences >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }
        
        // Log the number of blacklists checked
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{registeredServersCount, registeredServersCount});
        
        return blackListOcurrences;
    }
    
    /**
     * Check the given host's IP address in all the available black lists.
     * This method uses a default number of threads based on available processors.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress) {
        return checkHost(ipaddress, Runtime.getRuntime().availableProcessors());
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
