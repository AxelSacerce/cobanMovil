package com.mobilcoban.app;	

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import javax.security.auth.DestroyFailedException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mobilcoban.data.NetworkWs;
import com.zebra.printer.MobilePrinter;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

@SuppressLint("HandlerLeak") public class DetalleCobroActivity extends Activity 
{
	
	// Detalle Cobro ----------------------------------
	String sResultCobro;
	String sResultAviso;
	String sAppId;
	String sAction;
	String sCuotaId;
	String sFiltro;
	private ProgressDialog DialogoCargar;
	
	
		
	// Asigno variables a los textview para setarlos con los datos de la respuestas del JSON
	TextView sTContrato ;
	TextView sTNombre ;
	TextView sTNegocio;
	TextView sTDideccion ;
	TextView sTMontoBack ;
	TextView sTCuotaDia ;
	TextView sTSaldoDia ;
	TextView sTSaldoTot ;
	String sINombreC;
	String sIquoteId;
	String sICuotaDia;
	Button btnPhoto, btnImprimir, btnNotice, btnNote, btnTakePhoto, btnSendPhoto;
	ImageView mapa;
	
	// Fin Detalle Cobro -------------------------------------
	
	GPSTracker gps;
	String sResultPrint;
	String sPrintRetorno;
	String sPrintMessage;
	String sRetornoAviso;
	String sMessageAviso;
	String AccionP, AppIdP;
	double latitud = 0;
	double longitud = 0; 
	double longitudAviso = 0;
	double latitudAviso = 0;
	
	String jSonLat;
	String jSonLong;
	
	// Take and upload photo
	static final int REQUEST_IMAGE_CAPTURE = 1;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private Uri fileUri;	
	static String sCuotaIdCl;
	String sResultadoFoto;
   	String ruta_fotografia = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/contratos/";
   	String str_imagen_fotografia;
    static	String DateTimePhoto;
    boolean borrado = false; 
    
    static File mediaFile;
   		
	static MobilePrinter mMobilePrinter;
	
	// Take Photo
	
	public String fechaHoraActual(){
		   return new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
		}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
        case REQUEST_IMAGE_CAPTURE:
        	if ( resultCode == RESULT_OK)
        	{
        		Toast.makeText(DetalleCobroActivity.this, "Fotografia grabada correctamente", Toast.LENGTH_SHORT).show();
        	}
        	break;
        }
 
    }  
	
	
	private static File getOutputMediaFile(int type)
	{
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "contratos");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("contratos", "fracaso al crear directorio");
	            return null;
	        }
	    }

	    
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + DateTimePhoto + "_"+ sCuotaIdCl  + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	
	// Take  Photo
	
		
	public Handler HnCobro = new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			if(msg.what == 1)
			{
				DialogoCargar.dismiss();
				
				try
				{
					// Creo variable para contener el array de respuesta
					JSONArray ArrayCobrar = new JSONArray(sResultCobro);
					
					if(ArrayCobrar.length() == 0)
					{
						Toast.makeText(DetalleCobroActivity.this, "No se ha generado cobro para este cliente", Toast.LENGTH_SHORT).show();
                        finish();
					}else
					{
						//CobroDetalleItems itemsObten;
						
						for(int i =0; i<ArrayCobrar.length(); i++)
						{
							JSONObject Jo = ArrayCobrar.getJSONObject(i);
							//itemsObten = new CobroDetalleItems();
							sTContrato.setText("Contrato: "+Jo.getString("ID_CONTRATO"));
								sIquoteId = Jo.getString("ID_CONTRATO").toString();
							sTNombre.setText("Nombre: "+Jo.getString("NOMBRE"));
								sINombreC = Jo.getString("NOMBRE").toString();
							sTNegocio.setText("Negocio: "+Jo.getString("NOMBRE_NEGOCIO"));
							sTDideccion.setText("Direcci�n: "+Jo.getString("DIRECCION"));
							sTMontoBack.setText("Monto atrasado: Q."+Jo.getString("MONTO_ATRASADO"));
							sTCuotaDia.setText("Cuota del D�a: Q."+Jo.getString("CUOTA_HOY"));
								sICuotaDia= Jo.getString("CUOTA_HOY").toString();
							sTSaldoDia.setText("Saldo para hoy: "+Jo.getString("SALDO_PARA_HOY"));
							sTSaldoTot.setText("Saldo Total: "+Jo.getString("SALDO_TOTAL"));
															
							 if((Jo.getString("ULTIMA_LATITUD").toString()=="") || (Jo.getString("ULTIMA_LONGITUD").toString()==""))
							 {
								 jSonLat = "";
								 jSonLong = "";
								 
								 Log.i("RECEPCION DATOS WEB SERVICE", "LATITUD " + String.valueOf(jSonLat) + " Valor Vac�o" + " LONGITUD " + String.valueOf(jSonLong) + " Valor Vac�o");
							 }else
							 {
								 jSonLat=Jo.getString("ULTIMA_LATITUD").toString();
								 jSonLong = Jo.getString("ULTIMA_LONGITUD").toString();
								 Log.i("RECEPCION DATOS WEB SERVICE", "LATITUD " + String.valueOf(jSonLat) + " Valor con datos" + " LONGITUD " + String.valueOf(jSonLong) + " Valor con datos");
							 }
								
						}				
						
						
						Toast.makeText(getApplicationContext(), "�Datos cargados!...", Toast.LENGTH_SHORT).show();
						
					}
																
				}catch(JSONException e)
				{
					e.getStackTrace();
					Log.i("CAPTURA DE MENSAJE LOG", e.toString());
				}
				
			}
			if (msg.what == 2)
			{
				DialogoCargar.dismiss();
				
				
				try
				{
					// Creo variable para contener el array de respuesta
					JSONArray ArrayPrint = new JSONArray(sResultPrint);
					
					if(ArrayPrint.length() == 0)
					{
						Toast.makeText(DetalleCobroActivity.this, "No se ha generado cobro para este cliente", Toast.LENGTH_SHORT).show();
                        finish();
					}else
					{
						//CobroDetalleItems itemsObten;
						
						for(int i =0; i<ArrayPrint.length(); i++)
						{
							JSONObject Jo = ArrayPrint.getJSONObject(i);
							//itemsObten = new CobroDetalleItems();
							sPrintRetorno = Jo.getString("RESULT").toString();
							sPrintMessage = Jo.getString("MESSAGE").toString();
														
						}				
						
						Log.i("PRINT", sPrintMessage);
						//Toast.makeText(DetalleCobroActivity.this, "�cobro procesado!", Toast.LENGTH_SHORT).show();
						
						
					}
																
				}catch(JSONException e)
				{
					e.getStackTrace();
					Log.i("CAPTURA DE MENSAJE LOG", e.toString());
				}
				
			}			
			
			if(msg.what == 3)
			{
				DialogoCargar.dismiss();
				try
				{
					
					// Creo variable para contener el array de respuesta
					JSONArray ArrayAviso = new JSONArray(sResultAviso);
					
					if(ArrayAviso.length() == 0)
					{
						Toast.makeText(DetalleCobroActivity.this, "No se ha generado cobro para este cliente", Toast.LENGTH_SHORT).show();
                        finish();
					}else
					{
						
							for(int i =0; i<ArrayAviso.length(); i++)
							{
								JSONObject Jo = ArrayAviso.getJSONObject(i);
								//itemsObten = new CobroDetalleItems();
								sRetornoAviso = Jo.getString("RESULT").toString();
								sMessageAviso = Jo.getString("MESSAGE").toString();
															
							}				
							
							Log.i("AVISO", sMessageAviso);
							//Toast.makeText(DetalleCobroActivity.this, "�cobro procesado!", Toast.LENGTH_SHORT).show();
							
						
					}
					
				}catch(JSONException d)
				{
					d.getStackTrace();
					Log.i("CAPTURA DE MENSAJE LOG", d.toString());
				}
				
			}
			
			if(msg.what == 4)
			{
				DialogoCargar.dismiss();
				
				Toast.makeText(DetalleCobroActivity.this, "Fotograd�a enviada correctamente", Toast.LENGTH_SHORT).show();
				
				
			}
						
		}
		
	};
	
	
	
	// verifica si existe conexion a internet
		 public static boolean verificaConexion(Context ctx) {
			    boolean bConectado = false;
			    ConnectivityManager connec = (ConnectivityManager) ctx
			            .getSystemService(Context.CONNECTIVITY_SERVICE);
			    // No s�lo wifi, tambi�n GPRS
			    NetworkInfo[] redes = connec.getAllNetworkInfo();
			    // este bucle deber�a no ser tan �apa
			    for (int i = 0; i < redes.length; i++) {
			        // �Tenemos conexi�n? ponemos a true
			        if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
			            bConectado = true;
			        }
			    }
			    return bConectado;
			}
	
	
		
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_cobro);
				
		if(verificaConexion(this) == false) {
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
			gps = new GPSTracker(DetalleCobroActivity.this);
			
			sTContrato = (TextView)findViewById(R.id.lblTextContrato);
			sTNombre = (TextView)findViewById(R.id.lblTextNombre);
			sTNegocio = (TextView)findViewById(R.id.lblTextNegocio);
			sTDideccion = (TextView)findViewById(R.id.lblTextDireccion);
			sTMontoBack = (TextView)findViewById(R.id.lblTextMontoAtraso);
			sTCuotaDia = (TextView)findViewById(R.id.lblTextCuotaDia);
			sTSaldoDia = (TextView)findViewById(R.id.lblTextSaldoDia);
			sTSaldoTot = (TextView)findViewById(R.id.lblTextSaldoTot);
			
			mapa = (ImageView)findViewById(R.id.imgMapa);
			
			Intent iCobrar = getIntent();
			sAppId = iCobrar.getStringExtra("Id");
			sAction = iCobrar.getStringExtra("Action");
			sCuotaId =  iCobrar.getStringExtra("CuotaId");
			sFiltro = iCobrar.getStringExtra("Filter");
			
			
			// Creo Los Botones
			btnPhoto = (Button)findViewById(R.id.btnFoto);
			btnImprimir = (Button)findViewById(R.id.btnPrint);
			btnNotice = (Button)findViewById(R.id.btnAviso);
			btnNote = (Button)findViewById(R.id.btnNota);
			btnTakePhoto = (Button)findViewById(R.id.btnTakePhoto);
			btnSendPhoto = (Button)findViewById(R.id.btnSendPhoto);
			
			btnListener listener = new btnListener();
			
			mapa.setOnClickListener(listener);
			btnTakePhoto.setOnClickListener(listener);
			btnSendPhoto.setOnClickListener(listener);
			
			
			// Creo dialogo cargar listado
					DialogoCargar = new ProgressDialog(DetalleCobroActivity.this);
					DialogoCargar.setMessage("Cargando Datos...");
					DialogoCargar.setCancelable(false);
					DialogoCargar.show();
			
				if(sFiltro.contains("3"))
				{
					
					// Listener de botones
					btnPhoto.setOnClickListener(listener);
					btnImprimir.setOnClickListener(listener);
					btnNotice.setOnClickListener(listener);
					
					btnNote.setOnClickListener(listener);
					btnSendPhoto.setEnabled(false);
					btnSendPhoto.setBackground(getResources().getDrawable(R.drawable.btnfondogrey));
					
				}else if(sFiltro.contains("2"))
				{
									
					/*btnNotice.setEnabled(false);
					btnNotice.setBackground(getResources().getDrawable(R.drawable.btnfondogrey));*/
					
					btnNotice.setOnClickListener(listener);
					btnImprimir.setEnabled(false);
					btnImprimir.setBackground(getResources().getDrawable(R.drawable.btnfondogrey));
					
					
					// Listener de botones
					btnPhoto.setOnClickListener(listener);
					btnNote.setOnClickListener(listener);
					btnSendPhoto.setEnabled(false);
					btnSendPhoto.setBackground(getResources().getDrawable(R.drawable.btnfondogrey));
					
					
								
				}else if(sFiltro.contains("1"))
				{
					// Listener de botones
					btnPhoto.setOnClickListener(listener);
					btnImprimir.setOnClickListener(listener);
					
					btnNotice.setOnClickListener(listener);
					btnNote.setOnClickListener(listener);
					btnSendPhoto.setEnabled(false);
					btnSendPhoto.setBackground(getResources().getDrawable(R.drawable.btnfondogrey));
				}
					
					
					
			new Thread()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					
					sResultCobro = NetworkWs.DetalleCobro( sCuotaId, sAppId, sAction);
					HnCobro.sendEmptyMessage(1);
				}
				
				
			}.start();

			
		}
	
	}
	
	
	class btnListener implements OnClickListener
	{
		// Creo el Intent
		private Intent iFoto;
		private Intent iMapa;
		private Intent iNote;
		//private Intent iAviso;
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
		public void onClick(View v) 
		{		
				int id = v.getId();
				if (id == R.id.btnFoto) {
					iFoto = new Intent(DetalleCobroActivity.this, DisplayPhotoActivity.class);
					iFoto.putExtra("appID", "30");
					iFoto.putExtra("Action", "3");
					iFoto.putExtra("cuota", sCuotaId);
					iFoto.putExtra("NombreC", sINombreC);
					startActivity(iFoto);
				} else if (id == R.id.btnPrint) {
					if(gps.canGetLocation())
					{
			            latitud = gps.getLatitude();
			            longitud = gps.getLongitude();
					}
					AccionP = "4";
					AppIdP = "30";
					//DialogoCargar = new ProgressDialog(DetalleCobroActivity.this);
					DialogoCargar.setMessage("Realizando cobro...");
					DialogoCargar.setCancelable(false);
					DialogoCargar.show();
					new Thread()
					{
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							
							sResultPrint = NetworkWs.CobroPrint(sCuotaId, AppIdP, AccionP, sICuotaDia, String.valueOf(latitud), String.valueOf(longitud));
							
							ListadoCobrosActivity.mMobilePrinter.printText("RECIBO COBRO\nContrato:  " + sIquoteId  + "\nNombre: " + sINombreC + 
									"\nMonto pagado:  " + sICuotaDia + "\n\n\n\n\n\n\n", MobilePrinter.ALIGNMENT_LEFT,
								MobilePrinter.TEXT_ATTRIBUTE_FONT_A, MobilePrinter.TEXT_SIZE_HORIZONTAL1, true);
							
							
							HnCobro.sendEmptyMessage(2);
							
					}}.start();
					Toast.makeText(DetalleCobroActivity.this, "Cobro procesado correctamente...!! ",  Toast.LENGTH_SHORT).show();
					//Toast.makeText(DetalleCobroActivity.this, "Latitud: " + String.valueOf(latitud) + ","+ "Longitud: " + String.valueOf(longitud) + "\n", Toast.LENGTH_LONG).show();
					//Toast.makeText(DetalleCobroActivity.this, "�Cobro Procesado exitosamente!", Toast.LENGTH_SHORT).show();
				} else if (id == R.id.btnAviso) {
					if(gps.canGetLocation())
					{
						latitudAviso = gps.getLatitude();
			            longitudAviso = gps.getLongitude();
					}
					AccionP= "5";
					AppIdP = "30";
					//DialogoCargar = new ProgressDialog(DetalleCobroActivity.this);
					DialogoCargar.setMessage("Realizando Aviso...");
					DialogoCargar.setCancelable(false);
					DialogoCargar.show();
					new Thread()
					{
						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							
							sResultAviso = NetworkWs.avisoD(sCuotaId, AppIdP, AccionP, String.valueOf(latitudAviso), String.valueOf(longitudAviso));
							 
							ListadoCobrosActivity.mMobilePrinter.printText("AVISO DE COBRO\nContrato:  " + sIquoteId  + "\nNombre: " + sINombreC + 
									"\nMonto a Pagar:  " + sICuotaDia + "\n \n Solicitamos realice su pago \n Gracias por preferirnos!\n\n\n\n\n", MobilePrinter.ALIGNMENT_LEFT,
								MobilePrinter.TEXT_ATTRIBUTE_FONT_A, MobilePrinter.TEXT_SIZE_HORIZONTAL1, true);
							
							// ESCRIBO LOG DE LA INFORMACION QUE SE ENV�A
							Log.i("DATOS TRANSFERIDOS", "quoteID: "+ sCuotaId + " Apid: " + AppIdP + "Acci�n: " + AccionP
								 	+ "Latitud: "  + String.valueOf(latitudAviso) + "Longitud: " + String.valueOf(longitudAviso));
							
							HnCobro.sendEmptyMessage(3);
					}}.start();
				} else if (id == R.id.btnNota) {
					AppIdP = "30";
					AccionP = "6";
					iNote = new Intent(DetalleCobroActivity.this, NoteActivity.class);
					iNote.putExtra("appID", AppIdP);
					iNote.putExtra("action", AccionP);
					iNote.putExtra("cuoteId", sCuotaId);
					startActivity(iNote);
					Log.i("DATOS NOTA", "Cuota ID: " + sCuotaId + " AppID " + AppIdP + AccionP);
				} else if (id == R.id.imgMapa) {
					try{
						
						if( jSonLat.equals("") || jSonLong.equals(""))
						{
							//finish();
							Toast.makeText(DetalleCobroActivity.this, "No hay ubicaci�n designada para �ste cliente", Toast.LENGTH_SHORT).show();
							
						}else
						{
							iMapa = new Intent(DetalleCobroActivity.this, MapaMostrarActivity.class);
							iMapa.putExtra("Latitud", String.valueOf(jSonLat));
							iMapa.putExtra("Longitud", String.valueOf(jSonLong));
							iMapa.putExtra("NombreC", sINombreC);
							startActivity(iMapa);
							Log.i("LATITUD DETALLE COBRO SALE", String.valueOf(jSonLat));
							Log.i("LONGITUD DETALLE COBRO SALE",String.valueOf(jSonLong));
							Log.i("NOMBRE CLIENTE SALE",sINombreC);
														
						}
					
					}catch(Exception e)
					{
						e.getStackTrace();
						Log.i("ERROR AL INICIAR", e.toString());
					}
				} else if (id == R.id.btnTakePhoto) {
					//borraCarpeta();
					sCuotaIdCl = sIquoteId;
					DateTimePhoto = fechaHoraActual();
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
					if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
					    
					}
					btnSendPhoto.setEnabled(true);
					btnSendPhoto.setBackground(getResources().getDrawable(R.drawable.btnfondogreendark));
					btnTakePhoto.setEnabled(false);
					btnTakePhoto.setBackground(getResources().getDrawable(R.drawable.btnfondogrey));
				} else if (id == R.id.btnSendPhoto) {
					DialogoCargar = new ProgressDialog(DetalleCobroActivity.this);
					DialogoCargar.setMessage("Cargando Fotografia");
					DialogoCargar.setCancelable(false);
					DialogoCargar.show();
					new Thread() 
					{
						@Override
						public void run() {
							super.run();
							AppIdP = "30";
							AccionP = "7";
					        Bitmap bitmapOrg = BitmapFactory.decodeFile(ruta_fotografia + DateTimePhoto +"_" + sCuotaIdCl  + ".jpg");
					        int width = bitmapOrg.getWidth();
					        int height = bitmapOrg.getHeight();
					        
					        ExifInterface exif;
					        float scaleWidth = 0;
					        float scaleHeight = 0;
					        String orientString = "";
							try {
								exif = new ExifInterface(ruta_fotografia + DateTimePhoto +"_" + sCuotaIdCl  + ".jpg");
				    	        orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
					    	    scaleWidth = ((float) 800) / width;
					    	    scaleHeight = ((float) 600) / height;
					    	    Log.i ("Orientacion", orientString);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}        
					        			        
					        Matrix matrix = new Matrix();

					        matrix.postScale(scaleWidth, scaleHeight);
					        if ( orientString.equals("6") )
					        {
					        	Log.i ("Si rote imagen","si");
					        	matrix.postRotate(90);
					        }
					        
					        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, false);
					        			    	        			    	        
					        ByteArrayOutputStream bao = new ByteArrayOutputStream();
					        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, bao);
					        
					        byte [] ba = bao.toByteArray();
					        str_imagen_fotografia = Base64.encodeToString(ba,Base64.DEFAULT);					
					        sResultadoFoto = NetworkWs.CargarFoto(sCuotaIdCl, AppIdP, AccionP, "data:image/JPEG;base64,"+str_imagen_fotografia, DateTimePhoto +"_" + sCuotaIdCl  + ".jpg");
									//(str_imagen_fotografia, "centro_" + id_centro );
					        HnCobro.sendEmptyMessage(4);
							Log.i("RESULTADO UPLOAD IMG", sResultadoFoto );
							borraCarpeta();
							
						}
						
					}.start();
					btnTakePhoto.setEnabled(true);
					btnTakePhoto.setBackground(getResources().getDrawable(R.drawable.btnfondocorinto));
					btnSendPhoto.setEnabled(false);
					btnSendPhoto.setBackground(getResources().getDrawable(R.drawable.btnfondogrey));
						//borraCarpeta();
				}
					
			}
						
		}
	
	public void borraCarpeta(){
			
		File carpeta = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "contratos");
			
			File lista[];
			int num = 0;
			try{
	
				lista=carpeta.listFiles();
				num=lista.length;
				
				for(int i = 0; i < num; i++){ 
				lista[i].delete();
		
				}
				
				if(carpeta.delete()){
					borrado=true;
					Toast.makeText(DetalleCobroActivity.this, "Se ha borrado la carpeta y las im�gnes contenidas en ella", Toast.LENGTH_SHORT).show();
				}else
				{
					borrado=false;
					Toast.makeText(DetalleCobroActivity.this, "No se han podido borrar los datos ni la carpeta", Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				System.out.println("Error al borrar fichero: " + e.getMessage());
			}
		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_cobro, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.mnLista) 
		{
			
			System.exit(0);
			Toast.makeText(DetalleCobroActivity.this, "Impresora desconectada...", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
