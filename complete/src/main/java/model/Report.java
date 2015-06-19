package model;

/**
 * Created by Ashkan on 6/18/2015.
 */
public class Report {

    public String terrestrial_date;
    public int sol;
    public double ls;
    public double min_temp;
    public double min_temp_fahrenheit;
    public double  max_temp;
    public double max_temp_fahrenheit;
    public double pressure;
    public String pressure_string;
    public double abs_humidity;
    public double wind_speed;
    public String wind_direction;
    public String atmo_opacity;
    public String season;
    public String sunrise;
    public String sunset;

    @Override
    public String toString() {
        return "terrestrial_date:" + terrestrial_date + "\n" +
                "sol:" + sol + "\n" +
                "ls:" + ls + "\n";
    }
}
