package br.com.marino.sismar.entity;

import java.util.Date;
import java.util.List;

public class AisPlayBack {

    private Date date;
    private List<Ais> listPlayBack;

    public AisPlayBack(Date date, List<Ais> listPlayBack) {
        this.date = date;
        this.listPlayBack = listPlayBack;
    }

    public AisPlayBack() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Ais> getListPlayBack() {
        return listPlayBack;
    }

    public void setListPlayBack(List<Ais> listPlayBack) {
        this.listPlayBack = listPlayBack;
    }

}
