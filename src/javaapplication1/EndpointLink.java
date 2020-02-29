/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author Paris
 */
public class EndpointLink {

    int delay;
    CacheServer server;
    Endpoint endpoint;

    public EndpointLink(int delay, CacheServer server, Endpoint endp) {
        this.delay = delay;
        this.server = server;
        this.endpoint = endp;
    }

}
