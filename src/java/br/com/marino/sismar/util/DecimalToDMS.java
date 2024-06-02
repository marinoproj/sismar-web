package br.com.marino.sismar.util;

public class DecimalToDMS {

    public static void main(String[] args) {
        double latitude = -69.2873; // exemplo de latitude decimal
        double longitude = 8.0859; // exemplo de longitude decimal
        
        String dmsLatitude = convertToDMS(latitude, true);
        String dmsLongitude = convertToDMS(longitude, false);
        
        System.out.println("Latitude em DMS: " + dmsLatitude);
        System.out.println("Longitude em DMS: " + dmsLongitude);
    }

    public static String convertToDMS(double decimal, boolean isLatitude) {
        String direction;
        if (isLatitude) {
            direction = decimal >= 0 ? "N" : "S";
        } else {
            direction = decimal >= 0 ? "E" : "W";
        }

        decimal = Math.abs(decimal);
        int degrees = (int) decimal;
        double fractionalPart = decimal - degrees;
        int minutes = (int) (fractionalPart * 60);
        double seconds = (fractionalPart * 60 - minutes) * 60;

        return String.format("%s%d°%02d'%06.3f", direction, degrees, minutes, seconds);
        //return String.format("%d°%02d'%06.3f\"%s", degrees, minutes, seconds, direction);
    }
}