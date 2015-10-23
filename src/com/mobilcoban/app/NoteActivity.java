package com.mobilcoban.app;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class NoteActivity extends Activity {
	
	
	GPSTracker gps;
	String sResultNote;
	
	String sAppId;
	String sCuotaId;
	String sAction;
	
	EditText NotaText;
	Button btnGrabar, btnCancelar;
	
	double latitudNote = 0;
	double longitudNote = 0;
	
	String sNoteResult;
	String sNoteMessage;
	
	private ProgressDialog DialogoGrabar;
	
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putString("_nota", NotaText.getText().toString());
		
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		NotaText.setText(savedInstanceState.getString("_nota"));
		
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		
		
		if(verificaConexion(this) == false) 
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
			
			
			
		}
		else
		{
			//  Creo el control de entrada de Texto
			gps = new GPSTracker(NoteActivity.this);
			
			NotaText = (EditText)findViewById(R.id.txtNotas);
			
			// Creo los botones de grabar y cancelar
			btnGrabar = (Button)findViewById(R.id.btnGrabarNota);
			btnCancelar = (Button)findViewById(R.id.btnCancelarNota);
			
			Intent iNotacion = getIntent();
			sAppId = iNotacion.getStringExtra("appID");
			sCuotaId = iNotacion.getStringExtra("cuoteId");
			sAction = iNotacion.getStringExtra("action");
			
			btnListener listener = new btnListener();
			btnGrabar.setOnClickListener(listener);
			btnCancelar.setOnClickListener(listener);
		
		}
	}
	
	
	private Handler NoteHnd = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(msg.what == 1)
			{
				DialogoGrabar.dismiss();
				
				try {
					JSONArray ArrayNote = new JSONArray(sResultNote);
					
					if(ArrayNote.length() == 0)
					{
						Toast.makeText(NoteActivity.this, "No se ha generado cobro para este cliente", Toast.LENGTH_SHORT).show();
                        finish();
					}else
					{
						//CobroDetalleItems itemsObten;
						
						for(int i =0; i<ArrayNote.length(); i++)
						{
							JSONObject Jo = ArrayNote.getJSONObject(i);
							//itemsObten = new CobroDetalleItems();
							sNoteResult = Jo.getString("RESULT").toString();
							sNoteMessage = Jo.getString("MESSAGE").toString();
														
						}
						//Toast.makeText(NoteActivity.this, sNoteResult, Toast.LENGTH_SHORT).show();
						NotaText.setText("");
						NotaText.requestFocus();
						
						/*Toast.makeText(NoteActivity.this, sResultNote + " Lo lamentamos existe un problema con la comicacion " +
								"\n entre el servidor y el App, los datos no se pudieron " +
								"uardar ne la base de datos",Toast.LENGTH_SHORT).show();*/
				
						
						Log.i("MENSAJE JSON", sNoteMessage);
				
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
								
			}
			
		}
		
	};
	
	
	class btnListener implements OnClickListener
	{

		@Override
		public void onClick(View v) 
		{
			
			
			int id = v.getId();
			
			
			if(id == R.id.btnGrabarNota){
				if(gps.canGetLocation())
				{
					latitudNote = gps.getLatitude();
					longitudNote = gps.getLongitude();
				}
				
				
					
					Log.i("DATOS NOTA ANTES DE INCERTAR", sAppId + " CUOTA ID: " + sCuotaId  + " ACCIÓN: " + 
							sCuotaId + " NOTA: " + NotaText.getText().toString());
				
					DialogoGrabar = new ProgressDialog(NoteActivity.this);
					DialogoGrabar.setMessage("Grabando Nota...");
					DialogoGrabar.setCancelable(false);
					DialogoGrabar.show();
				
					new Thread()
					{

						@Override
						public void run() 
						{
							super.run();
							
							sResultNote = NetworkWs.SetNote(sCuotaId, sAppId, sAction, NotaText.getText().toString(), String.valueOf(latitudNote), String.valueOf(longitudNote));
							NoteHnd.sendEmptyMessage(1);
							
						}}.start();
				
				/*if(sResultNote)
				{
				}else
				{						
					Toast.makeText(NoteActivity.this, "Datos no enviados no hay nota que registrar", Toast.LENGTH_LONG).show();
					NotaText.requestFocus();
				}*/
						//finish();
				
			}else if(id == R.id.btnCancelarNota){
				NotaText.setText("");	
			}	
			
		}
		
		
	}
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
