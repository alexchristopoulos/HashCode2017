/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

/**
 *
 * @author Petros
 */
public class Start {

    /**
     * @param args the command line arguments
     */
    static CacheServer[] cacheservers;
    static Video[] videos;
    static Endpoint[] endpoints;
    static TreeSet<Request> weightedrequests = new TreeSet<>((r1, r2) -> (-Double.compare(r1.weight, r2.weight)));

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String filename = "videos_worth_spreading";
        //String filename = "kittens";
        //String filename = "me_at_the_zoo";
        //String filename = "trending_today";
        File f = new File(filename + ".in");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String first = br.readLine();

        String[] firstsplit = first.split(" ");
        int videonum = Integer.parseInt(firstsplit[0]);

        int endpointnum = Integer.parseInt(firstsplit[1]);

        int requestnum = Integer.parseInt(firstsplit[2]);

        int cachenum = Integer.parseInt(firstsplit[3]);

        int cacheserversize = Integer.parseInt(firstsplit[4]);

        cacheservers = new CacheServer[cachenum];
        for (int i = 0; i < cachenum; i++) {
            cacheservers[i] = new CacheServer();
        }

        CacheServer.sizeMB = cacheserversize;

        videos = new Video[videonum];
        String videosizes = br.readLine();
        String[] videosizessplit = videosizes.split(" ");
        for (int i = 0; i < videonum; i++) {
            videos[i] = new Video(Integer.parseInt(videosizessplit[i]));
        }

        endpoints = new Endpoint[endpointnum];
        for (int i = 0; i < endpointnum; i++) {
            String[] splitendpoint = br.readLine().split(" ");
            endpoints[i] = new Endpoint();
            endpoints[i].latencytodc = Integer.parseInt(splitendpoint[0]);

            for (int k = 0; k < Integer.parseInt(splitendpoint[1]); k++) {
                String[] splitendpointconnection = br.readLine().split(" ");
                endpoints[i].connectTo(cacheservers[Integer.parseInt(splitendpointconnection[0])],
                        Integer.parseInt(splitendpointconnection[1]));
            }
        }

        for (int i = 0; i < requestnum; i++) {
            String[] splitedrequest = br.readLine().split(" ");

            Video vid = videos[Integer.parseInt(splitedrequest[0])];

            Request req = endpoints[Integer.parseInt(splitedrequest[1])].addRequest(vid, Integer.parseInt(splitedrequest[2]));
            weightedrequests.add(req);

        }

        for (Endpoint endp : endpoints) {
            endp.preprocess();
        }

        while (weightedrequests.size() > 0) {
            Request req = weightedrequests.pollFirst();
            for (EndpointLink link : req.endp.sortedlinks) {
                try {
                    link.server.cachevideo(req.video);
                    break;
                } catch (OutOfMemoryException ex) {
                }
            }
        }

        File out = new File(filename + ".out");
        BufferedWriter bw = new BufferedWriter(new FileWriter(out));

        int usecount = 0;
        for (CacheServer c : cacheservers) {

            if (c.inuseMB > 0) {
                usecount++;
            }

        }

        bw.write(Integer.toString(usecount));
        bw.newLine();
        for (CacheServer c : cacheservers) {

            if (c.inuseMB <= 0) {
                continue;
            }

            String s = "";
            s += c.id;
            for (Video v : c.cachedvideos.values()) {
                s += " " + v.id;
            }
            bw.write(s);
            bw.newLine();

        }
        bw.flush();

        System.out.println("yay");

    }

}
