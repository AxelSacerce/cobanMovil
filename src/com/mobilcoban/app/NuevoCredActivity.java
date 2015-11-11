package com.mobilcoban.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobilcoban.data.NetworkWs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NuevoCredActivity extends Activity implements OnClickListener{
	
	// botones variables
	Button btnEnviar, btnCancelar;
	
	//Edit texts
	EditText txtNombres, txtApellidos, txtDpi, txtDate, txtDirec, txtTel, txtMonto;
	
	// Strings para hacer el grabado de los datos
	String sExtSoliPor, sSoliEl;
	String sEnvMonto, sEnvNo_quote, sEnvPeriodo, sEnvSolicitaPor, sEnvNombres, sEnvApellidos, sEnvIDpi, sEnvFechaNac, sEnvDirecc, sEnvTelefonos;
	
	static String sEnvSolicitaEn;
	
	
	// String Resultado Web service
	String sResultNuevoContrato;
	
	
	// spinners variables 	
	Spinner spPeriodo, spCuotas;
	
	// Dialogo
	private ProgressDialog DialogoCargar;
	
	
	private String periodo[], cuotas[];
	
	public String fechaHoraActual(){
        return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
     }

	
	
	
	public Handler HnNuevoContrato = new Handler()
    {

        @Override
        public void handleMessage(Message msg) 
        {
            super.handleMessage(msg);
            
            if ((msg.what == 1))
            {
                DialogoCargar.dismiss();
                

                JSONArray ArrayPrint  = null;
                JSONObject jsPago;
                
                try
                {
                    // Creo variable para contener el array de respuesta Respuesta array cobro
                    jsPago = new JSONObject(sResultNuevoContrato);
                    ArrayPrint = jsPago.getJSONArray("flag");
                    
                    
                    
                    if(ArrayPrint.length() == 0)
                    {
                        Toast.makeText(NuevoCredActivity.this, "No se ha generado la nueva solicitud", Toast.LENGTH_SHORT).show();
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
                                                        
                        }           */  
                        
                        Toast.makeText(NuevoCredActivity.this, "Solicitud enviada..", Toast.LENGTH_SHORT).show();
                        
                        //Log.i("PRINT", sPrintMessage);
                        //Toast.makeText(DetalleCobroActivity.this, "¡cobro procesado!", Toast.LENGTH_SHORT).show();
                        
                        
                    }
                                                                
                }catch(JSONException e)
                {
                    e.getStackTrace();
                    Log.i("CAPTURA DE MENSAJE LOG", e.toString());
                }
                
            }           
                        
                                    
        }
        
    };
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_cred);
		
		
		// Se crea cada uno de los textos editables
		
		txtNombres 		= 	(EditText) findViewById(R.id.txtNombresCl);
		txtApellidos 	= 	(EditText) findViewById(R.id.txtApellidosC);
		txtDpi 			= 	(EditText) findViewById(R.id.txtIdentificacionCl);
		
		
		// Se crea el campo de texto editable para la fecha y el metodo que mostrará el calendario		
		txtDate 		=	(EditText) findViewById(R.id.txtFechaNacCl);
		txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					DateDialog dialogo = new DateDialog(v);
					
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					
					dialogo.show(ft,"DatePicker");
					
				}
				
				
			}
		});
		
		// se continua con la creacion de los textos
		txtDirec 		= 	(EditText) findViewById(R.id.txtDireccionCl);
		txtTel	 		= 	(EditText) findViewById(R.id.txtTelefonoCl);
		txtMonto		=	(EditText) findViewById(R.id.txtMontoPrestamoCl);
		
		
		// Creo los objetos buttons
		btnEnviar = (Button) findViewById(R.id.btnEnvSolCred);
		btnEnviar.setOnClickListener(this);
		
		btnCancelar = (Button) findViewById(R.id.btnCancelSolCred);
		btnCancelar.setOnClickListener(this);
		
		// creo data para el spinner Período
		
		periodo= new String[4];
		periodo[0]="diario";
		periodo[1]="semanal";
		periodo[2]="quincenal";
		periodo[3]="mensual";        
		
		spPeriodo = (Spinner) findViewById(R.id.spnPeriodoCobro);
		
		 // Adaptador para el spinner
		ArrayAdapter<String> adpPeriodo =new ArrayAdapter<String>
        (NuevoCredActivity.this,android.R.layout.simple_dropdown_item_1line, periodo);
		spPeriodo.setAdapter(adpPeriodo);

		
		cuotas= new String[8];
		cuotas[0]="24"; 
		cuotas[1]="30"; 
		cuotas[2]="60";
		cuotas[3]="90";
		cuotas[4]="120";
		cuotas[5]="150";
		cuotas[6]="180";
		cuotas[7]="210";
		
		spCuotas = (Spinner) findViewById(R.id.spnCuotasPrest);
		
		 // Adaptador para el spinner
		ArrayAdapter<String> adpCuotas =new ArrayAdapter<String>
        (NuevoCredActivity.this,android.R.layout.simple_dropdown_item_1line, cuotas);
		spCuotas.setAdapter(adpCuotas);	
		
		
		
		// Obtengo id de usuario
		
		Intent iContratoNuevo = getIntent();
		sExtSoliPor = iContratoNuevo.getStringExtra("soli");
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		int id = v.getId();
		
		if(id == R.id.btnEnvSolCred)
		{
			
			
            
            sEnvMonto 		=  txtMonto.getText().toString(); 
            sEnvNo_quote 	=  spCuotas.getSelectedItem().toString();
            sEnvPeriodo		=  spPeriodo.getSelectedItem().toString();
            sEnvSolicitaPor	=  sExtSoliPor.toString();
        	sEnvSolicitaEn	=  fechaHoraActual();
        	sEnvNombres		=  txtNombres.getText().toString();
        	sEnvApellidos	=  txtApellidos.getText().toString();
        	sEnvIDpi		=  txtDpi.getText().toString();
        	sEnvFechaNac	=  txtDate.getText().toString();
        	sEnvDirecc		=  txtDirec.getText().toString();
        	sEnvTelefonos	=  txtTel.getText().toString();

        	
        	
			new Thread()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					
					sResultNuevoContrato = NetworkWs.NewContract(sEnvMonto, sEnvNo_quote, sEnvPeriodo, sEnvSolicitaPor,	sEnvSolicitaEn, sEnvNombres, sEnvApellidos, sEnvIDpi, sEnvFechaNac, sEnvDirecc, sEnvTelefonos);
					
					
					
					
					
					
					
					Log.i("OBTEN NOMBRES Y APELLIDOS DEL CLIENTE: ", txtNombres.getText().toString() 
							+" "+txtApellidos.getText().toString());
					
					Log.i("OBTEN EL DPI", txtDpi.getText().toString());
					Log.i("OBTEN LA FECHA DE NACIMIENTO", txtDate.getText().toString());
					Log.i("OBTEN LA DIRECCION",  txtDirec.getText().toString());
					Log.i("OBTEN EL TELEFONO",   txtTel.getText().toString());
					Log.i("OBTEN EL MONTO",  txtMonto.getText().toString());
					Log.i("OBTEN LA DIRECCION",  txtDirec.getText().toString());
					Log.i("OBTEN EL PERIODO DE COBRO",  spPeriodo.getSelectedItem().toString());
					Log.i("OBTEN LAS CUOTAS", spCuotas.getSelectedItem().toString());
					
					
					HnNuevoContrato.sendEmptyMessage(1);
					Toast.makeText(getApplicationContext(), "Solicitud enviada" + " Fecha: "+ txtDate.getText().toString() , Toast.LENGTH_SHORT).show();
					finish();
					
				}}.start();
            
            
				
			
		}else if(id == R.id.btnCancelSolCred)
		{
			Toast.makeText(this, "Solicitud Cancelada", Toast.LENGTH_SHORT).show();
			finish();
		}
		
	}
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuevo_cred, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.mnSalirNCred) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}





	
}
