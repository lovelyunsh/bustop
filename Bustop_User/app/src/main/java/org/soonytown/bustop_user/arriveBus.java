package org.soonytown.bustop_user;

public class arriveBus {
    private String BusNum;
    private String arriveTime;
    private String currentLoc;

    public arriveBus() {
    }

    public arriveBus(String busNum, String arriveTime, String currentLoc) {
        BusNum = busNum;
        this.arriveTime = arriveTime;
        this.currentLoc = currentLoc;
    }

    public String getBusNum() {
        return BusNum;
    }

    public void setBusNum(String busNum) {
        BusNum = busNum;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getCurrentLoc() {
        return currentLoc;
    }

    public void setCurrentLoc(String currentLoc) {
        this.currentLoc = currentLoc;
    }
}

//
//public class arriveBus {
//    private String BusStop_ID;
//    private String Bus_ID;
//    private String user_ID;
//
//    public arriveBus() {
//    }
//
//    public arriveBus(String busStop_ID, String bus_ID, String user_ID) {
//        BusStop_ID = busStop_ID;
//        Bus_ID = bus_ID;
//        this.user_ID = user_ID;
//    }
//
//    public String getBusStop_ID() {
//        return BusStop_ID;
//    }
//
//    public void setBusStop_ID(String busStop_ID) {
//        BusStop_ID = busStop_ID;
//    }
//
//    public String getBus_ID() {
//        return Bus_ID;
//    }
//
//    public void setBus_ID(String bus_ID) {
//        Bus_ID = bus_ID;
//    }
//
//    public String getUser_ID() {
//        return user_ID;
//    }
//
//    public void setUser_ID(String user_ID) {
//        this.user_ID = user_ID;
//    }
//}
