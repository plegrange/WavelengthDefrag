package Test;

public class Wave {

    private int Destination;        //Node ID
    private Path Route;                //Route
    private double Wavelength;        //Wavelength
    private double Pheromone;        //Pheromone Value
    private int Success;            //Number Success
    private int Failure;            //Number Failures

    public Wave() {

    }

    public Wave(Path r, double w, Double p) {
        Route = r;
        Wavelength = w;
        Pheromone = p;
        Success = 0;
        Failure = 0;
        Destination = r._Path.getLast()._ID;
    }

    public void Success() {
        Success++;
    }

    public void Failure() {
        Failure++;
    }

    public void Update(Double p) {
        Pheromone = p;
    }

    public double getWavelength() {
        return Wavelength;
    }

    public double getPheromone() {
        return Pheromone;
    }

    public int getSuccess() {
        return Success;
    }

    public int getFailure() {
        return Failure;
    }

    public Path getPath() {

        Path cloned;
        try {
            cloned = (Path) Route.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Route;
        }
        return cloned;
        //return Route;

    }

    public int getDestination() {
        return Destination;
    }


    //Modified clone() method in Employee class
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Path cloned = (Path) super.clone();
        return cloned;
    }


}
