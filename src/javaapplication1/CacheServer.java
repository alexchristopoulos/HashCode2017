/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 *
 * @author Paris
 */
public class CacheServer {

    public static int idcount = 0;

    int id;
    static int sizeMB;
    int inuseMB = 0;

    ArrayList<EndpointLink> connectedendpoints = new ArrayList<>();

    Hashtable<Integer, Video> cachedvideos = new Hashtable<>();

    public CacheServer() {
        id = idcount;
        idcount++;
    }

    public void cachevideo(Video v) throws OutOfMemoryException {

        if (inuseMB + v.sizeMb > sizeMB) {
            throw new OutOfMemoryException();
        }
        cachedvideos.put(v.id, v);
        inuseMB += v.sizeMb;

        for (EndpointLink l : connectedendpoints) {
            Endpoint endp = l.endpoint;
            Request r = endp.videorequests.get(v);
            if (r != null) {
                if (r.calcweight(endp.delayto(this)) < r.weight) {
                    endp.updateRequest(v, endp.delayto(this));
                }
            }
        }

    }

}
