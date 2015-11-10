package com.mobilcoban.app;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilcoban.data.NetworkWs;
import com.mobilcoban.data.itemCobrosBaseAdapter;
import com.mobilcoban.data.itemCobrosDetalle;
import com.zebra.printer.MobilePrinter;

@SuppressLint("HandlerLeak")public class ListadoCobrosActivity extends Activity{

	String AppID;
	String Accion;
	String Limite;
	String Filtro;
	String strResultado;
	private static final String TAG_DATA = "data";
	
	String TitleTxt;
	String TitleTxt2;
	String TitleTxt3;
	ArrayList<itemCobrosDetalle> lCobros = new ArrayList<itemCobrosDetalle>();
	ImageView btnSalir;
	ListView lvlistCobros;
	TextView Title;
	private ProgressDialog DialogoCargar;
	static String sID;
	static String sNombres;
	static String sTelefono;
	static String sDireccion;
	static String sCobroPeriodo;
	static String sMontoPrestamo;
	static String sCuotaValor;
	static String sCuotasNum;
	
	
	
	static MobilePrinter mMobilePrinter; // constante para manejo de imrpesora
	int posicion;
	

	//---------------------------------------------------- 
		// Printer Handler
		private final Handler mbHandler = new Handler() 
			{
				@SuppressWarnings("unchecked")
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) 
					{					
						
					case MobilePrinter.MESSAGE_DEVICE_SET:
						if (msg.obj == null) {
							Toast.makeText(getApplicationContext(), "No hay Impresora para conectarse...", Toast.LENGTH_SHORT).show();
						} else 
						{
							DialogosManager.showBluetoothDialog(ListadoCobrosActivity.this, (Set<BluetoothDevice>) msg.obj);
							Log.i("Conectado en", msg.obj.toString());
							Toast.makeText(getApplicationContext(), "Impresora conectada...", Toast.LENGTH_SHORT).show();				
						}
						break;
										
					}
				}
			};


	//---------------------------------------------------
	
	public Handler workHnd = new Handler()
	{
				
		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 1)
			{
				DialogoCargar.dismiss();
				//JSONArray ArrayCobros = null;
				//JSONObject jsCobros;
				
				try
				{
					// Si se recibe un JSON con arreglo el cual tiene nombre se recibe como objeto aplican las 
					// variables comentadas arriga  JSONArray, JSONObject
					//JSONObject jsCobros = new JSONObject(strResultado);
					//JSONArray  ArrayCobros = new JSONArray(strResultado);
					
					JSONArray ArrCobrosL  = null;
					JSONObject jsCobros;
					
					try
					{
						
						jsCobros = new JSONObject(strResultado);
						ArrCobrosL = jsCobros.getJSONArray(TAG_DATA);
						
						
					}catch(JSONException e)
					{
						e.printStackTrace();
					}
					
					
					
								
					if(ArrCobrosL == null)
					{
						
						Toast.makeText(ListadoCobrosActivity.this, "¡No hay Cobros disponibles para este día!", Toast.LENGTH_SHORT).show();
						finish();
						
					}else
					{
					
						itemCobrosDetalle itemDetails;
						for(int i = 0; i < ArrCobrosL.length(); i++)
						{
									JSONObject CobrosL = ArrCobrosL.getJSONObject(i);
									itemDetails = new itemCobrosDetalle();
									
									itemDetails.setid_Contrato(CobrosL.getString("id"));  
									itemDetails.setNombres(CobrosL.getString("nombres") + " " + CobrosL.getString("apellidos"));
									itemDetails.setTelefono(CobrosL.getString("telefonos"));
									itemDetails.setDireccion(CobrosL.getString("domicilio"));
									itemDetails.setPeriodo_Cobro(CobrosL.getString("periodo_cobro"));
									itemDetails.setMontoPago(CobrosL.getString("monto")) ;
									itemDetails.setValCuota(CobrosL.getString("valor_cuota"));
									itemDetails.setCuotasNum(CobrosL.getString("no_cuotas"));			
									
									
									
									
									
									
									
									lCobros.add(itemDetails);
									
									//Log.i("TEST DATA: ", "Numero cuotas:  " + sCuotasNum + "Valor Cuota " + sCuotaValor );
									//lCobros.add(arrayCobros.get(i).toString());
														
						}
						
						Toast.makeText(getApplicationContext(), "Lista cargada exitosamente...", Toast.LENGTH_SHORT).show();
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					//Log.i("MENSAJE EXEPCION AXEL", e.toString());
				}
								
				lvlistCobros.setAdapter(new itemCobrosBaseAdapter(ListadoCobrosActivity.this, lCobros));	
				registerForContextMenu(lvlistCobros);
			}
							
		}
		
	};	
	
	
	// verifica si existe conexion a internet
			 public static boolean verificaConexion(Context ctx) {
				    boolean bConectado = false;
				    ConnectivityManager connec = (ConnectivityManager) ctx
				            .getSystemService(Context.CONNECTIVITY_SERVICE);
				    // No sólo wifi, también GPRS
				    NetworkInfo[] redes = connec.getAllNetworkInfo();

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
		setContentView(R.layout.activity_listado_cobros);
		
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
			
			
			AlertDialog alert = builder.create();
			alert.show();
			
			
			
		}else
		{
		
		
				lvlistCobros = (ListView)findViewById(R.id.lvlistado);
				Title =(TextView)findViewById(R.id.lblTitle);
				
				TitleTxt="COBROS PENDIENTES";
				TitleTxt2="COBROS REALIZADOS";
				TitleTxt3 = "RECORDATORIOS";
				
				Intent iLista = getIntent();
				AppID = iLista.getStringExtra("Id");
				Filtro = iLista.getStringExtra("Filter");
				/*Accion = iLista.getStringExtra("Action");
				Limite = iLista.getStringExtra("Limit");
				*/
				
				// Creo mis objetos
				btnSalir = (ImageView)findViewById(R.id.imgInnerExit);
						
				// Escuchadores
				botonListener listener = new botonListener();
				btnSalir.setOnClickListener(listener);
				
				
				// Listener del clic en el item de la lista
				ItemListener ItemList = new ItemListener();		
				lvlistCobros.setOnItemClickListener(ItemList);
				
					
				// Creo dialogo cargar listado
				DialogoCargar = new ProgressDialog(ListadoCobrosActivity.this);
				DialogoCargar.setMessage("Cargando Listado...");
				DialogoCargar.setCancelable(false);
				DialogoCargar.show();
				
				// handler printer
				mMobilePrinter = new MobilePrinter(this, mbHandler, null); //instacion la clase MobilePriter
				
				if(Filtro.contains("1"))
				{
					Title.setText(TitleTxt);
				}else if(Filtro.contains("2"))
				{
					Title.setText(TitleTxt2);
				}else if(Filtro.contains("3"))
				{
					Title.setText(TitleTxt3);
				}
														
				new Thread()
				{
		
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						if(Filtro.contains("1"))
						{
						strResultado = NetworkWs.ListadoPendientes(2);
						
						workHnd.sendEmptyMessage(1);
						}else if(Filtro.contains("2"))
						{
							strResultado = NetworkWs.ListadoCobRealizados(AppID, Accion, Limite, Filtro);
							workHnd.sendEmptyMessage(1);
						}else if(Filtro.contains("3"))
						{
							strResultado = NetworkWs.recordatoriosList(AppID, Accion, Limite, Filtro);
							workHnd.sendEmptyMessage(1);
						}
						
					}
								
				}.start();
		}
	}
	
	
class ItemListener implements OnItemClickListener
{

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
						
		Object o = lvlistCobros.getItemAtPosition(position);
		
		itemCobrosDetalle obj_itemDetails = (itemCobrosDetalle)o;
		sID = obj_itemDetails.getid_Contrato();
		sNombres = obj_itemDetails.getNombres();
		sTelefono= obj_itemDetails.getTelefono();
		sDireccion=obj_itemDetails.getDireccion();	
		sCobroPeriodo= obj_itemDetails.get_periodoCobro();
		sMontoPrestamo = obj_itemDetails.get_MontoPago();
		sCuotaValor = obj_itemDetails.get_ValCuota();
		sCuotasNum = obj_itemDetails.get_CuotasNum();
		
		
		System.out.println(position + "-----texto");
		
		Intent iCobros;	
		iCobros = new Intent(ListadoCobrosActivity.this, DetalleCobroActivity.class);
		iCobros.putExtra("id", sID);
		iCobros.putExtra("nomnbres", sNombres);
		iCobros.putExtra("telefono", sTelefono);
		iCobros.putExtra("direccion", sDireccion);
		iCobros.putExtra("periodoCobro", sCobroPeriodo );
		iCobros.putExtra("montoPrest", sMontoPrestamo);
		iCobros.putExtra("valorCuota", sCuotaValor);
		iCobros.putExtra("NumCuotas", sCuotasNum);
		
		startActivity(iCobros);
		
		
		
		
		Log.i("TAG_DATOS", "Monto Prestamo " +  sMontoPrestamo.toString());
		//Log.i("SELECT DE LA LISTA ", sID.toString()+" " + sNombres.toString() );
		mMobilePrinter.findBluetoothPrinters();   //Busca conexion a impresora
		
	}

}
	
class botonListener implements OnClickListener
{	
	
	@Override
	public void onClick(View v) 
	{
		try
		{
			
			int id = v.getId();
			if(id == R.id.imgInnerExit){
				System.exit(0);
			}
			
			
		}catch (Exception e)
		{
			e.getStackTrace();
			Log.i("SALIDA BOTON IMAGEN", e.toString());
		}
	
	}
}
	
	// --------------------------------------------
	// REFERENCE Opciones de menú bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listado_cobros, menu);
		
		/*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchMenuItem = menu.findItem(R.id.action_search);		
		searchView = (SearchView) searchMenuItem.getActionView();
		
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));		
		searchView.setSubmitButtonEnabled(true);
		
		searchView.setOnQueryTextListener(this);*/		
		return true;
		
		
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Regresa al menú principal del App
		int id = item.getItemId();
		try
		{
			if (id == R.id.mnPrincipal) 
			{
				
				System.exit(0);
				
				return true;
			}
		
			
		}catch (Exception e)
		{
			e.getStackTrace();
			Log.i("SALIDA MENSAJE MENU", e.toString());
		}
		 
		return super.onOptionsItemSelected(item);		
	}
	
	
}
