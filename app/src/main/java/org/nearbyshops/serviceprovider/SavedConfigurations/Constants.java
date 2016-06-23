package org.nearbyshops.serviceprovider.SavedConfigurations;

/**
 * Created by sumeet on 23/6/16.
 */
public class Constants {



    public static String getServiceType(int type)
    {
        if(type == 1)
        {
            return "Nonprofit";

        }else if(type == 2)
        {
            return "Government";
        }else if(type == 3)
        {
            return "Commertial";
        }

        return "";
    }


    public static String getServiceLevel(int level)
    {
        if(level == 1)
        {
            return "City";

        }else if(level == 2)
        {
            return "State";
        }else if(level ==3)
        {
            return "Country";
        }else if(level ==4 )
        {
            return "Global";
        }

        return "";
    }

}
