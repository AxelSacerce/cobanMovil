package com.mobilcoban.app;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
//import android.widget.Toast;



public class MainActivity extends Activity implements OnClickListener{
	
	Button btnPend, btnReali,btnRecordar;
	ImageView imgBtnImage;
	private Intent iListadosCobros;
	private Intent iListadoCobReali; //Listado Cobros Realizados en el día 
	private Intent iListadoRecord;   // Listado Recordatorios
	//private Intent iListodoRecordar; // Listado de Clientes que se les dejó recordatorios
	
		//----------------------------------------------------
	
	
	
	
	// verifica si existe conexion a internet
	 public static boolean verificaConexion(Context ctx) {
		    boolean bConectado = false;
		    ConnectivityManager connec = (ConnectivityManager) ctx
		            .getSystemService(Context.CONNECTIVITY_SERVICE);
		    // No sólo wifi, también GPRS
		    NetworkInfo[] redes = connec.getAllNetworkInfo();
		    // este bucle debería no ser tan ñapa
		    for (int i = 0; i < redes.length; i++) {
		        // ¿Tenemos conexión? ponemos a true
		        if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
		            bConectado = true;
		        }
		    }
		    return bConectado;
		}
	
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      if (verificaConexion(this) == false) {
      	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.lblAccion).toString());
			builder.setMessage(getResources().getString(R.string.lblSeguroExit).toString());
			builder.setPositiveButton(getResources().getString(R.string.lblSi).toString(), new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog, int which) {
			    	
			    	System.exit(0);
			        
			    }
			
			});
			
			/*builder.setNegativeButton(getResources().getString(R.string.lblNo).toString(), new DialogInterface.OnClickListener() {

			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	
			        dialog.cancel();
			    }
			});*/			


			AlertDialog alert = builder.create();
			alert.show();
      }else
      {
      // Creo mis botones
      
      btnPend = (Button)findViewById(R.id.btnPend);
      btnReali = (Button)findViewById(R.id.btnReali);
      btnRecordar = (Button)findViewById(R.id.btnRecordar);
      
      // Imagen boton
      imgBtnImage =(ImageView)findViewById(R.id.imgSalir); 
      
      // Creo el Listener para los botones        
      btnPend.setOnClickListener(this);
      btnReali.setOnClickListener(this);
      btnRecordar.setOnClickListener(this);
     
      
      // Creo el Listener de la imagen
      imgBtnImage.setOnClickListener(this);
      
      }     
  }
   
  @SuppressLint("SimpleDateFormat") 
  @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
  	
  	    	
  	int id = v.getId();
  	if(id == R.id.imgSalir){
  		System.exit(0);
  	}else if(id == R.id.btnPend){
  			
  		iListadosCobros = new Intent(MainActivity.this, ListadoCobrosActivity.class);
  		iListadosCobros.putExtra("Id", "2");
  		iListadosCobros.putExtra("Filter", "1");
  		/*iListadosCobros.putExtra("Action", "1");
  		iListadosCobros.putExtra("Limit", "300");
  		*/
  		startActivity(iListadosCobros);
  			
  	}else if(id == R.id.btnReali){
  		
  		iListadoCobReali = new Intent(MainActivity.this,ListadoCobrosActivity.class);
  		iListadoCobReali.putExtra("Id", "30");
  		iListadoCobReali.putExtra("Action", "1");
  		iListadoCobReali.putExtra("Limit", "300");
  		iListadoCobReali.putExtra("Filter", "2");
  		startActivity(iListadoCobReali);
  		
  	}else if(id == R.id.btnRecordar){
  		/* String Time;
    		 Calendar cal = Calendar.getInstance(); 

    		 int Day = cal.get(Calendar.DAY_OF_MONTH);
    		 int Month = cal.get(Calendar.MONTH);
    		 int Year = cal.get(Calendar.YEAR);
    		 int HH = cal.get(Calendar.HOUR_OF_DAY);
    		 int MM = cal.get(Calendar.MINUTE);
    		 int SS = cal.get(Calendar.SECOND);
    		
    		 Time = Year + "-" + Month +"-"+ Day + " " + HH + ":" + MM +":"+ SS;
    		  
    		  
    		Toast.makeText(getApplicationContext(),Time, Toast.LENGTH_LONG).show();*/
     		
     		iListadoRecord = new Intent(MainActivity.this,ListadoCobrosActivity.class);
     		iListadoRecord.putExtra("Id", "30");
     		iListadoRecord.putExtra("Action", "1");
     		iListadoRecord.putExtra("Limit", "10");
     		iListadoRecord.putExtra("Filter", "3");
     		//iListadoRecord.putExtra("Xnow", Time);
     		startActivity(iListadoRecord);
  	}
  }
  	
  
  
  //--------------------------------------------------------
  // REFERENCE Opciones del menu bar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
  }  
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();
      if (id == R.id.mnSalir) 
      {
      	        	
			System.exit(0);
			//Toast.makeText(getApplicationContext(), "Hasta luego...", Toast.LENGTH_SHORT).show();
			        	
      	return true;
      }
      return super.onOptionsItemSelected(item);
  }

	
}