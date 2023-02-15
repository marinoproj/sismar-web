package br.com.marino.sismar.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Weather implements Serializable{

    private City city;
    private Date date;
    private double temperature;
    private String windDirection;
    private double windVelocity;
    private double humidity;
    private String condition;
    private double pressure;
    private String icon;
    private double sensation;

    public Weather(City city, Date date, double temperature, String windDirection, 
            double windVelocity, double humidity, String condition, 
            double pressure, String icon, double sensation) {
        this.city = city;
        this.date = date;
        this.temperature = temperature;
        this.windDirection = windDirection;
        this.windVelocity = windVelocity;
        this.humidity = humidity;
        this.condition = condition;
        this.pressure = pressure;
        this.icon = icon;
        this.sensation = sensation;
    }   

    public Weather() {
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public double getWindVelocity() {
        return windVelocity;
    }

    public void setWindVelocity(double windVelocity) {
        this.windVelocity = windVelocity;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getSensation() {
        return sensation;
    }

    public void setSensation(double sensation) {
        this.sensation = sensation;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.date);
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
        final Weather other = (Weather) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Weather{" + "date=" + date + ", temperature=" + temperature + ", windDirection=" + windDirection + ", windVelocity=" + windVelocity + ", humidity=" + humidity + ", condition=" + condition + ", pressure=" + pressure + ", icon=" + icon + ", sensation=" + sensation + '}';
    }
    
}