package com.mobilcoban.fragment;
		

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobilcoban.app.R;

public class  PlacesFragment extends SupportMapFragment 
{
	private GoogleMap map;
	private Bundle saveInstanceState;
	
	public static String NomClien;
	public static LatLng GUATEMALA; 
	
	//static final LatLng PLACESNEW = new LatLng(GUATEMALA);
	
	public LatLng getLocation() {
        return GUATEMALA;
    }

    @SuppressWarnings("static-access")
	public void setLocation(double Latit, double longit) {
        this.GUATEMALA = new LatLng(Latit,longit);
        
        
    }
    
    public String get_NombreC()
    {
    	return NomClien;
    }
    
    @SuppressWarnings("static-access")
	public void set_NombreC(String data)
    {
    	this.NomClien = data;
    }

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.saveInstanceState = savedInstanceState;
	}

	@Override
	public void onResume() {
		
		super.onResume();
		
		setupMap();
		
	}

	private void setupMap() {
		if(map == null)
		{
			map = getMap();
			
			if(map != null)
			{
				if(saveInstanceState == null)
				{
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(GUATEMALA, 18));
					map.addMarker(new MarkerOptions()
						.title("MAPA - COMO APP")
						.snippet("Cliente: " + NomClien )
						.anchor(0.0f, 1.0f)
						.position(GUATEMALA)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmap)));
				}
				map.getUiSettings().setZoomControlsEnabled(true);
				
			}
		}
		
		
	}
	
	
	
}
