package TerminalPortManagementSystem.User;

import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.Utility.Log;
import TerminalPortManagementSystem.VehicleType;
import TerminalPortManagementSystem.Vehicles.Vehicle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Admin implements Serializable, User {
    private final String username = "admin";
    private final String password = "admin12345";
    private static final Admin instance = new Admin();

    public static Admin getInstance() {
        return instance;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getUserType() {
        return 1;
    }

    //create obj
    public String createManager(){
        String output = new String();
        return output;
    }
    public String createPort(){
        String output = new String();
        return output;
    }
    public String createVehicle(){
        String output = new String();
        return output;
    }
    public String createContainer(){
        String output = new String();
        return output;
    }

    //remove obj
    public String removeManager(){
        String output = new String();
        return output;
    }
    public String removePort(){
        String output = new String();
        return output;
    }
    public String removeVehicle(){
        String output = new String();
        return output;
    }
    public String removeContainer(){
        String output = new String();
        return output;
    }
    //transporting
    public String loadContainer(){
        String output = new String();
        return output;
    }
    public String unloadContainer(){
        String output = new String();
        return output;
    }
    public String refuelVehicle(){
        String output = new String();
        return output;
    }
    public String moveToPort(){
        String output = new String();
        return output;
    }
    //static
    public String totalFuelConsumedPerDay(){
        String output = new String();
        return output;
    }
    public String getTotalConsumedFuelByDay(){
        String output = new String();
        return output;
    }
    public String CalculateWeightOfEachTypeOfAll(){
        String output = new String();
        return output;
    }public String CalculateWeightOfEachTypeOfEachPort(){
        String output = new String();
        return output;
    }
    public String getNumberOfContainerOfEachType(){
        String output = new String();
        return output;
    }
    public ArrayList<Container> getListOfAllContainer(){
        //return all containers in both ship and port
        ArrayList<Container> listContainers = new ArrayList<>();
        return listContainers;
    }
    public ArrayList<Vehicle> getListOfVehicleByType(VehicleType vehicleType){
        //return list of vehicles by vehicle type
        ArrayList<Vehicle> listVehicle = new ArrayList<>();
        return listVehicle;
    }

    // extract log
    public List<Log> getTripsByDate(Date dateToQuery){

        java.util.List<Log> tripsOnDate = new ArrayList<>();
        return tripsOnDate;
    }
    public List<Log> getTripsBetweenDates(Date startDate, Date endDate){

        List<Log> tripsBetweenDates = new ArrayList<>();
        return tripsBetweenDates;
    }


}
