package pbain.bu.edu.ec327project;

        import android.Manifest;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;

/**
 * GPS processing adapted from ToStand Tutorials
 */

//CALLS LOCATIONMANAGER GPS TO OBTAIN CURRENT GEOLOCATION
public class GPS
{
    Context m_context;
    Location m_location;
    LocationManager m_locationManager;
    String m_provider = LocationManager.GPS_PROVIDER;

    GPS(Context context)
    {
        m_context = context;
        m_locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //CHECK PERMISSIONS
        if((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED))
        {
            //QUIT
            return;
        }

        m_locationManager.requestLocationUpdates(m_provider, 60 * 10, 50, new LocationListener()
        {
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            public void onProviderEnabled(String s) {}
            public void onProviderDisabled(String s) {}

            public void onLocationChanged(Location location)
            {
                m_location = location;
            }
        });
    }

    //RETURN INITIAL LOCATION
    Location getLocation()
    {
        if(m_location == null)
            if((ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) ||
                    (ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED))
                m_location = m_locationManager.getLastKnownLocation(m_provider);

        if(m_location == null)
        {
            m_location = new Location(m_provider);
            m_location.setLatitude(0);
            m_location.setLongitude(0);
            m_location.setAltitude(0);
        }

        return m_location;
    }
}