package Test;

public class Phero {

    private double Wavelength;    //Wavelength
    private double Pheromone;    //Pheromone Value


    public Phero() {

    }

    public Phero(double w, Double p) {
        Wavelength = w;
        Pheromone = p;
    }

    public void Update(Double d) {
        Pheromone = d;
    }

    public double getWavelength() {
        return Wavelength;
    }

    public double getPheromone() {
        return Pheromone;
    }

}
