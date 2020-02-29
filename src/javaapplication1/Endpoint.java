/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.TreeSet;

/**
 *
 * @author Paris
 */
public class Endpoint {

    int latencytodc;

    Hashtable<CacheServer, EndpointLink> connectedservers = new Hashtable<>();
    ArrayList<EndpointLink> sortedlinks;

    TreeSet<Request> requests = new TreeSet<>((r1, r2) -> (-Double.compare(r1.weight, r2.weight)));
    Hashtable<Video, Request> videorequests = new Hashtable<>();

    public void preprocess() {

        sortedlinks = new ArrayList<>(connectedservers.values());
        Collections.shuffle(sortedlinks);
        sortedlinks.sort((l1, l2) -> (Integer.compare(l1.delay, l2.delay)));

    }

    public Request addRequest(Video vid, int count) {
        if (videorequests.get(vid) != null) {
            Request req = videorequests.get(vid);

            requests.remove(req);
            req.count += count;
            req.recalcweight(req.endp.latencytodc);
            requests.add(req);

            return req;
        } else {

            Request req = new Request(vid, this, count);

            requests.add(req);
            videorequests.put(vid, req);

            return req;
        }
    }

    public void updateRequest(Video vid, int newdelay) {
        Request req = videorequests.get(vid);
        requests.remove(req);
        Start.weightedrequests.remove(req);
        req.recalcweight(newdelay);
        Start.weightedrequests.add(req);
        requests.add(req);
    }

    public void connectTo(CacheServer s, int latency) {

        EndpointLink l = new EndpointLink(latency, s, this);
        connectedservers.put(s, l);
        s.connectedendpoints.add(l);
    }

    public int delayto(CacheServer s) {
        return connectedservers.get(s).delay;
    }

}
