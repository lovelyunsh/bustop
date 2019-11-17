package org.soonytown.bustop_user;


public class NearBusInfo {

    private String busStopId;
    private String busStopName;
    private String nextBusStop;


    public NearBusInfo(String busStopId, String busStopName, String nextBusStop) {
        this.busStopId = busStopId;
        this.busStopName = busStopName;
        this.nextBusStop = nextBusStop;
    }

    public String getBusStopId() {
        return this.busStopId;
    }
    public void setBusStopId(String busStopId) {
        this.busStopId = busStopId;
    }

    public String getBusStopName() {
        return this.busStopName;
    }
    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public String getNextBusStop() {
        return this.nextBusStop;
    }
    public void setNextBusStop(String nextBusStop) {
        this.nextBusStop = nextBusStop;
    }

}
