package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Objects;

public class State implements Serializable{
 
    private String abv;
    private String name;

    public State(String abv, String name) {
        this.abv = abv;
        this.name = name;
    }
    
    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.abv);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (!Objects.equals(this.abv, other.abv)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "State{" + "abv=" + abv + ", name=" + name + '}';
    }    
    
}