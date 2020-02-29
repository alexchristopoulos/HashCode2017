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
public class Request {

    Video video;
    Endpoint endp;
    int count;
    double weight;

    public Request(Video video, Endpoint endp, int count) {
        this.endp = endp;
        this.count = count;
        this.video = video;

        weight = calcweight(endp.latencytodc);
    }

    public void recalcweight(int newlatency) {
        weight = calcweight(newlatency);
    }

    final public double calcweight(int delay) {
        if (video.sizeMb > CacheServer.sizeMB) {
            return -1;
        }

        return (double) (CacheServer.sizeMB / (2.0 * video.sizeMb + CacheServer.sizeMB / 10)) * count * (delay);
    }

}
