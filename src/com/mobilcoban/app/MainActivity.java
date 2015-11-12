package com.mobilcoban.app;



import android.annotation.SuppressLint;
import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
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
	private Intent iNuevoContrato; //Nuevos Contratos 
	
	
	
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      
      // Creo mis botones
      
      btnPend = (Button)findViewById(R.id.btnPend);
      btnReali = (Button)findViewById(R.id.btnReali);
      /*btnRecordar = (Button)findViewById(R.id.btnRecordar);*/
      
      // Imagen boton
      imgBtnImage =(ImageView)findViewById(R.id.imgSalir); 
      
      // Creo el Listener para los botones        
      btnPend.setOnClickListener(this);
      btnReali.setOnClickListener(this);
      /*btnRecordar.setOnClickListener(this);*/
     
      
      // Creo el Listener de la imagen
      imgBtnImage.setOnClickListener(this);
      
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
  		
  		iNuevoContrato = new Intent(MainActivity.this, NuevoCredActivity.class);
  		iNuevoContrato.putExtra("soli", "2");  		
  		startActivity(iNuevoContrato);
  		
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