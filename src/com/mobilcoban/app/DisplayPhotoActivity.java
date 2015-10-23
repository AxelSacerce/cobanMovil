package com.mobilcoban.app;



import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobilcoban.data.NetworkWs;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint ("HandlerLeak") public class DisplayPhotoActivity extends Activity {

	private ProgressDialog DialogoCargar;
	String sResultImageB64; // Recibe la imagen en base64 tal y como viene en el JSON
	ImageView bImagenCliente; // se guardará la Imagen ya decodificada
	String sIntGetAppId;
	String sIntGetAccion;
	String sIntGetCuotaId;
	String sIntGetNombreC;
	TextView sNombreC;
	String sData;
	String sConvertImg;
	Button btnBack;
		
	
	public Handler hPhoto = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			if(msg.what==1)
			{
				DialogoCargar.dismiss();
				
				try
				{
					// Creo variable para contener el array de respuesta
                    JSONArray ArrayPhoto = new JSONArray(sResultImageB64);
                    if(ArrayPhoto.length() == 0)
                    {
                    	Toast.makeText(DisplayPhotoActivity.this, "No se ha cargado la imagen correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                    {
                    	
                    	for(int i =0; i<ArrayPhoto.length(); i++)
						{                
                            JSONObject Jo = ArrayPhoto.getJSONObject(i);
                            sData = Jo.getString("CONTRACT_IMAGE"); // objeto base64
                            
                         }  
                                                        
                    	bImagenCliente.setImageBitmap(ConverToImage(sData)); 
                    	sNombreC.setText(sIntGetNombreC);
                                              
                        Toast.makeText(getApplicationContext(), "¡Imagen cargada!... ", Toast.LENGTH_SHORT).show();
                                      	
                    }
                    
					
				}catch(JSONException e)
				{
					e.getStackTrace();
				}
				
				
			}			
		}
		
	};
	
	
	// funcion para decodificar imagen
	
	public Bitmap ConverToImage(String sConvertImg)
	{
		try
		{
			String imageDataBytes = sConvertImg.substring(sConvertImg.indexOf(",")+1);
			
			InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
			Bitmap bitmap = BitmapFactory.decodeStream(stream);
			//Toast.makeText(DisplayPhotoActivity.this, "Imagen Decodificada", Toast.LENGTH_SHORT).show();
			
			return bitmap;
			
		}catch(Exception e)
		{
			e.getStackTrace();
			Log.i("IMAGEN NO PROCESADA", e.toString());
			return null;
			
		}
		
	}
	
	
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_photo);
		
		// si es falsa la conectividad
		if (verificaConexion(this) == false) 
		{
		
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
			
			
			
		}else{
		// si es verdadera la conectividad
		Intent iPhoto = getIntent();
		sIntGetAppId = iPhoto.getStringExtra("appID");
        sIntGetAccion = iPhoto.getStringExtra("Action");
        sIntGetCuotaId = iPhoto.getStringExtra("cuota");
        sIntGetNombreC = iPhoto.getStringExtra("NombreC");
        
        sNombreC = (TextView)findViewById(R.id.lblNombreCPhoto);
        bImagenCliente = (ImageView)findViewById(R.id.imageCliente);
        btnBack = (Button)findViewById(R.id.btnBack);
        
        btnListener listener = new btnListener();
        btnBack.setOnClickListener(listener);
		
     // Creo dialogo cargando foto
        DialogoCargar = new ProgressDialog(DisplayPhotoActivity.this);
        DialogoCargar.setMessage("Cargando foto del cliente...");
        DialogoCargar.setCancelable(false);
        DialogoCargar.show();
        
        
        new Thread()
        {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                sResultImageB64 = NetworkWs.DetalleCobro(sIntGetCuotaId, sIntGetAppId, sIntGetAccion);
                hPhoto.sendEmptyMessage(1);
            }
                        
        }.start();
		}
	}

	
	class btnListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			
			int id = v.getId();
			
			if(id == R.id.btnBack){
				finish();
			}
			
						
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.mnDisplayfoto) 
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
