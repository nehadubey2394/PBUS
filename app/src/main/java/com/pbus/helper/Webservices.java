package com.pbus.helper;

/**
 * Created by mindiii on 5/4/18.
 */

public class Webservices {

    private static final String BASE_URL="http://dev.mindiii.com/pbus/";
    public static final String LOGIN= BASE_URL+"service/login";
    public static final String FORGOT = BASE_URL + "service/forgotPassword";
    public static final String ROUTE_LIST = BASE_URL + "service/stations/getStation";
    public static final String BUS_LIST = BASE_URL + "service/buses/getBuses";
    public static final String BUS_DETAIL = BASE_URL + "service/buses/getBusDetails";
}
