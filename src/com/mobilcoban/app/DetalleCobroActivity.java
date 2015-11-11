package com.mobilcoban.app;	


import java.io.File;

import java.text.DecimalFormat;
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

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


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
	String sAppId; //borrar
	String sAction; // borrar
	String sCuotaId;// borrar
	String sFiltro;// borrar
	
	String sContratoId;
	String sNombres;
	String sTelefono;
	String sDireccion;
	String sPeriodoC;
	String sValCuota;
	String sNumCuotas;
	String sMontoPrest;
	
	
	private ProgressDialog DialogoCargar;
	
	
	
		
	// Asigno variables a los textview para setearlos con los datos de la respuestas del JSON
	TextView sTContrato;
	TextView sTNombre;
	TextView sTTelefono;
	TextView sTDideccion;
	TextView sTMontoCuota;
	TextView sTCuotasNum;
	TextView sTMontoPrestamo;
	TextView sTPeriodoCobro;

	
	
	String sINombreC;
	String sIquoteId;
	String sICuotaDia;
	Button btnImprime, btnAviso;
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
	
			
	static String sCuotaIdCl;
	String sResultadoFoto;
   	String ruta_fotografia = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/contratos/";
   	String str_imagen_fotografia;
    static	String DateTimePhoto;
    static  String DateTimeSystem;
    boolean borrado = false;     
    static File mediaFile;   		
	static MobilePrinter mMobilePrinter;
	String sValCuotaFormat;
	String sValPrestamo;
	
	DecimalFormat formato = new DecimalFormat("##,###,###.##");
	
	// Take Photo
	
	public String fechaHoraActual(){
		   return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
		}
	
	public String fechaHoraActualEs(){
		   return new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
		}
	
	
	
	
	
	// Take  Photo
	
		
	public Handler HnCobro = new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			
			if ((msg.what == 2) || (msg.what == 3 ))
			{
				DialogoCargar.dismiss();
				

				JSONArray ArrayPrint  = null;
				JSONObject jsPago;
				
				try
				{
					// Creo variable para contener el array de respuesta Respuesta array cobro
					jsPago = new JSONObject(sResultPrint);
					ArrayPrint = jsPago.getJSONArray("flag");
					
					
					
					if(ArrayPrint.length() == 0)
					{
						Toast.makeText(DetalleCobroActivity.this, "No se ha generado cobro para este cliente", Toast.LENGTH_SHORT).show();
                        finish();
					}else if(ArrayPrint.equals("true"))
					{
						/*CobroDetalleItems itemsObten;
						
						for(int i =0; i<ArrayPrint.length(); i++)
						{
							JSONObject Jo = ArrayPrint.getJSONObject(i);
							//itemsObten = new CobroDetalleItems();
							sPrintRetorno = Jo.getString("RESULT").toString();
							sPrintMessage = Jo.getString("MESSAGE").toString();
														
						}			*/	
						
						Toast.makeText(DetalleCobroActivity.this, "Cobro / Aviso guardado con éxito...", Toast.LENGTH_SHORT).show();
						
						Log.i("PRINT", sPrintMessage);
						//Toast.makeText(DetalleCobroActivity.this, "¡cobro procesado!", Toast.LENGTH_SHORT).show();
						
						
					}
																
				}catch(JSONException e)
				{
					e.getStackTrace();
					Log.i("CAPTURA DE MENSAJE LOG", e.toString());
				}
				
			}			
						
			if(msg.what == 4)
			{
				DialogoCargar.dismiss();
				
				Toast.makeText(DetalleCobroActivity.this, "Fotogradía enviada correctamente", Toast.LENGTH_SHORT).show();
				
				
			}
						
		}
		
	};
	
	
		
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_cobro);
				
		
			gps = new GPSTracker(DetalleCobroActivity.this);
			
			sTContrato = (TextView)findViewById(R.id.lblTextContrato);
			sTNombre = (TextView)findViewById(R.id.lblTextNombre);
			sTTelefono = (TextView)findViewById(R.id.lblTextNegocio);
			sTDideccion = (TextView)findViewById(R.id.lblTextDireccion);
			sTMontoPrestamo = (TextView)findViewById(R.id.lblTextMontoAtraso);
			sTMontoCuota = (TextView)findViewById(R.id.lblTextCuotaDia);
			sTCuotasNum = (TextView)findViewById(R.id.lblTextSaldoDia);
			sTPeriodoCobro = (TextView)findViewById(R.id.lblTextSaldoTot);
			
			mapa = (ImageView)findViewById(R.id.imgMapa);
			
			Intent iCobrar = getIntent();
			sContratoId = iCobrar.getStringExtra("id");
			sNombres = iCobrar.getStringExtra("nomnbres");
			sTelefono =  iCobrar.getStringExtra("telefono");
			sDireccion= iCobrar.getStringExtra("direccion");
			sPeriodoC= iCobrar.getStringExtra("periodoCobro");
			sValCuota= iCobrar.getStringExtra("valorCuota");
			sNumCuotas = iCobrar.getStringExtra("NumCuotas");
			sMontoPrest = iCobrar.getStringExtra("montoPrest").toString();
			
			Double dMontoCuota = Double.parseDouble(sValCuota);
			Double dMontoPresta = Double.parseDouble(sMontoPrest);
			
			sValCuotaFormat = formato.format(dMontoCuota); 
			sValPrestamo = formato.format(dMontoPresta);
			   
			
			sTContrato.setText("Contrato No.: " +  sContratoId);
			sTNombre.setText("Nombre cliente:  " +  sNombres);
			sTTelefono.setText("Teléfono:  " + sTelefono);
			sTDideccion.setText("Dirección:  " + sDireccion);
			sTPeriodoCobro.setText("Frecuencia de cobro: "  + sPeriodoC);
			sTMontoCuota.setText("Monto a pagar hoy: Q " + sValCuotaFormat)  ;
			sTCuotasNum.setText("Número de cuotas: " +  sNumCuotas);
			sTMontoPrestamo.setText("Total del préstamo: Q " +  sValPrestamo);
			
			
			// Creo Los Botones
			
			btnImprime = (Button)findViewById(R.id.btnPrint);
			btnAviso   = (Button)findViewById(R.id.btnAviso); 	
			
			
			btnListener listener = new btnListener();		
			mapa.setOnClickListener(listener);
			
			// Listener de botones			
			btnImprime.setOnClickListener(listener);			
			btnAviso.setOnClickListener(listener);
	
	}
	
	
	class btnListener implements OnClickListener
	{
		// Creo el Intent
		
		private Intent iMapa;
		
		//private Intent iAviso;
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
		public void onClick(View v) 
		{		
				int id = v.getId();
				if (id == R.id.btnPrint) {
					
					
					DialogoCargar = new ProgressDialog(DetalleCobroActivity.this);
					DialogoCargar.setMessage("Espere un momento mientras se imprime el recibo...");
					DialogoCargar.setCancelable(false);
					DialogoCargar.show();
					new Thread()
					{
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							
							
							DateTimeSystem = fechaHoraActual();
							String DatePrint = fechaHoraActualEs();
							String userID = "2";
							String cuotasPaga = "2";
							String typeEvent = "no_recibo"; 
							
							
							int numero = (int) (Math.random() * 65) + 1;
							
							
							sResultPrint = NetworkWs.CobroPrint(sContratoId, userID, DateTimeSystem, cuotasPaga, String.valueOf(numero), typeEvent);
							
							Log.i("RESULTADO GRABA PAGO", sResultPrint);
							
							ListadoCobrosActivity.mMobilePrinter.printText("CREDI CHAPIN COBAN \n"+"RECIBO COBRO: "+ numero +
									"\nContrato:  " + sContratoId  + "\nNombres: " + sNombres + "\nMonto pagado:  " +
									sValCuotaFormat + "\nFrecuencia de Cobro: " + sPeriodoC + "\nFecha: "+ DatePrint +
									"\n\n\n\n\n\n\n\n", MobilePrinter.ALIGNMENT_LEFT,
								MobilePrinter.TEXT_ATTRIBUTE_FONT_A, MobilePrinter.TEXT_SIZE_HORIZONTAL1, true);
							
							
							HnCobro.sendEmptyMessage(2);
							
							
							finish();		
					}}.start();
					Toast.makeText(DetalleCobroActivity.this, "Cobro procesado correctamente...!! ",  Toast.LENGTH_SHORT).show();
					
					
				} else if (id == R.id.btnAviso) {
					
					
					DialogoCargar = new ProgressDialog(DetalleCobroActivity.this);
					DialogoCargar.setMessage("Realizando Aviso...");
					DialogoCargar.setCancelable(false);
					DialogoCargar.show();
					new Thread()
					{
						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							
							
							DateTimeSystem = fechaHoraActual();
							String DatePrint = fechaHoraActualEs();
							String userID = "2";
							String cuotasPaga = "2";
							String typeEvent = "no_aviso"; 
							
							int numero = (int) (Math.random() * 65) + 1;
							
							sResultPrint = NetworkWs.CobroPrint(sContratoId, userID, DateTimeSystem, cuotasPaga, String.valueOf(numero), typeEvent);
							 
							ListadoCobrosActivity.mMobilePrinter.printText("CREDI CHAPIN COBAN\n"+ "AVISO DE COBRO: "+ 
										numero + "\nContrato:  " + sContratoId  + "\nNombre: " + sNombres + 
										"\nMonto a Pagar:  " + sValCuotaFormat + "\nFrecuencia Cobro: " + sPeriodoC +
										"\nFecha: " + DatePrint  + 
										"\n \n Solicitamos realice su pago \n Gracias por preferirnos!!\n\n\n\n\n",
										MobilePrinter.ALIGNMENT_LEFT,
								MobilePrinter.TEXT_ATTRIBUTE_FONT_A, MobilePrinter.TEXT_SIZE_HORIZONTAL1, true);
							
							// ESCRIBO LOG DE LA INFORMACION QUE SE ENVÍA
							Log.i("DATOS TRANSFERIDOS", "Contrato: "+ sContratoId + " Usuario: " + userID );
							
							HnCobro.sendEmptyMessage(3);
							
							finish();	
					}}.start();
				}  else if (id == R.id.imgMapa) {
					try{
						
						if(gps.canGetLocation())
						{
				            latitud = gps.getLatitude();
				            longitud = gps.getLongitude();
						}
						
							iMapa = new Intent(DetalleCobroActivity.this, MapaMostrarActivity.class);
							iMapa.putExtra("Latitud", latitud);
							iMapa.putExtra("Longitud", longitud);
							iMapa.putExtra("NombreC", sNombres);
							startActivity(iMapa);
							Log.i("LATITUD DETALLE COBRO SALE", String.valueOf(latitud));
							Log.i("LONGITUD DETALLE COBRO SALE", String.valueOf(longitud));
							Log.i("NOMBRE CLIENTE SALE",sNombres);
														
					
					
					}catch(Exception e)
					{
						e.getStackTrace();
						Log.i("ERROR AL INICIAR", e.toString());
					}
									
				} 
					
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
