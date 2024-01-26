package br.com.marino.sismar.entity;

import br.com.marino.sismar.util.Util;
import java.util.Date;

public class VesselDockedStepTimeline {

    private String start;
    private String end;
    private String duration;

    public VesselDockedStepTimeline(String start, String end, String duration) {
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    public VesselDockedStepTimeline() {
    }
    
    public static VesselDockedStepTimeline build(Date start, Date end){        
        VesselDockedStepTimeline obj = new VesselDockedStepTimeline();
        
        obj.setStart(Util.getStringDateLastUpdateDash(start));
        
        if (end != null){            
            obj.setEnd(Util.getStringDateLastUpdateDash(end));
            obj.setDuration(Util.getTimeDuration(start, end));
            
        } else {            
            obj.setEnd("Em andamento");
            obj.setDuration(Util.getTimeDuration(start, new Date()));            
        }
        
        return obj;        
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }   
    
    public boolean inProgress(){
        return end.equalsIgnoreCase("Em andamento");
    }
    
    public boolean isFinished(){
        return !inProgress();
    }
    
}