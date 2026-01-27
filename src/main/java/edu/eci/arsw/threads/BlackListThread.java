package edu.eci.arsw.threads;

import java.util.LinkedList;
import java.util.List;

import edu.eci.arsw.spamkeywordsdatasource.*;

/**
 * Thread class that searches for a host's IP in a segment of blacklists.
 * Each thread is responsible for searching a specific range of blacklist servers.
 */
public class BlackListThread extends Thread {
    
    private int initIndex;
    private int endIndex;
    private String ipaddress;
    private List<Integer> blackListOcurrences;
    private int ocurrencesCount;
    
    /**
     * Constructor for BlackListThread
     * @param initIndex Starting index of the blacklist segment to check
     * @param endIndex Ending index of the blacklist segment to check
     * @param ipaddress IP address to validate
     */
    public BlackListThread(int initIndex, int endIndex, String ipaddress) {
        this.initIndex = initIndex;
        this.endIndex = endIndex;
        this.ipaddress = ipaddress;
        this.blackListOcurrences = new LinkedList<>();
        this.ocurrencesCount = 0;
    }
    
    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds;
        skds = HostBlacklistsDataSourceFacade.getInstance();

        // Search in the assigned segment
        for (int i = initIndex; i < endIndex; i++) {
            if (skds.isInBlackListServer(i, ipaddress)) {
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }
    }
    
    /**
     * Get the number of occurrences found by this thread
     * @return Number of blacklists where the IP was found
     */
    public int getOcurrencesCount() {
        return ocurrencesCount;
    }
    
    /**
     * Get the list of blacklist indices where the IP was found
     * @return List of blacklist numbers
     */
    public List<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }
}
