package br.com.marino.sismar.entity;

public class VesselDockedNow {

    private boolean hasShip;
    
    private String name;
    private String imo;
    private String type;    
    private String image;

    private String expectedEndDate;
    private long progress;
    private String timeExceeded;
    
    private VesselDockedStepTimeline anchorage;
    private VesselDockedStepTimeline navigation;
    private VesselDockedStepTimeline berth;

    public VesselDockedNow(boolean hasShip, String name, String imo, String type, String image, String expectedEndDate, long progress, VesselDockedStepTimeline anchorage, VesselDockedStepTimeline navigation, VesselDockedStepTimeline berth) {
        this.hasShip = hasShip;
        this.name = name;
        this.imo = imo;
        this.type = type;
        this.image = image;
        this.expectedEndDate = expectedEndDate;
        this.progress = progress;
        this.anchorage = anchorage;
        this.navigation = navigation;
        this.berth = berth;
    }

    public VesselDockedNow() {
    }

    public boolean isHasShip() {
        return hasShip;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImo() {
        return imo;
    }

    public void setImo(String imo) {
        this.imo = imo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(String expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public VesselDockedStepTimeline getAnchorage() {
        return anchorage;
    }

    public void setAnchorage(VesselDockedStepTimeline anchorage) {
        this.anchorage = anchorage;
    }

    public VesselDockedStepTimeline getNavigation() {
        return navigation;
    }

    public void setNavigation(VesselDockedStepTimeline navigation) {
        this.navigation = navigation;
    }

    public VesselDockedStepTimeline getBerth() {
        return berth;
    }

    public void setBerth(VesselDockedStepTimeline berth) {
        this.berth = berth;
    }       

    public String getTimeExceeded() {
        return timeExceeded;
    }

    public void setTimeExceeded(String timeExceeded) {
        this.timeExceeded = timeExceeded;
    }
    
    public boolean isExceededTime(){
        return progress >= 100;
    }
    
    public boolean isOnTime(){
        return !isExceededTime();
    }
    
    public String getClassProgress(){
        if (isExceededTime()){
            return "step-progress-bar-exceeded-limit";
        }
        return "step-progress-bar-on-time";
    }
    
}
