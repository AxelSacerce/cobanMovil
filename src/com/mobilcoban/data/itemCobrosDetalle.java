package com.mobilcoban.data;

public class itemCobrosDetalle {
	/*String sOrden;*/
	String sID_Contrato;
    String sNombres;   
    String sTelefono;
    String sDireccion;
    String sPeriodoCobro;
    String sMontoPago;
    String sValorCuota;
    String sCuotasNum;
    
    
    
    //int iPosicion;
    
    
    
    // Getters    
   /* public String get_orden()
    {
    	return sOrden;
    }*/
    
    // 	Adicionales
    
    public String get_periodoCobro()
    {
    	
    	return sPeriodoCobro;
    }
    
    public String get_MontoPago()
    {
    		return sMontoPago;
    	
    }
    
    public String get_ValCuota()
    {
    	
    		return sValorCuota;
    }
    
    public String get_CuotasNum()
    {
    	
    		return sCuotasNum;
    }

    // fin adicionales
    
    public String getid_Contrato()
    {
        return sID_Contrato;
    }
     
    
    public String getNombres()
    {
        return sNombres;
    } 
    
    public String getTelefono()
    {
        return sTelefono;
    }
    
    public String getDireccion()
    {
        return sDireccion;
    }
    
    /*public int getNumber()
    {
    	return iPosicion;
    }*/
    
    
    
    
    // Setters    
    
    /*public void set_Orden(String data)
    {
    	this.sOrden = data;
    }*/
    
    // Adicionales
    
    public void setPeriodo_Cobro(String data)
    {
    	this.sPeriodoCobro = data;
    	
    }
    
    public void setMontoPago(String data)
    {
    		this.sMontoPago = data;
    	
    }
    
    public void setValCuota(String data)
    {
    		this.sValorCuota = data;
    	
    }
    
    public void setCuotasNum(String data)
    {
    		this.sCuotasNum = data;
    	
    }

    
    // Fin Adicionales
    
    public void setid_Contrato(String data)
    {
        this.sID_Contrato = data;
    }
       
    public void setNombres(String data)
    {
    	this.sNombres = data;
    }
    
    public void setTelefono(String data)
    {
    	this.sTelefono = data;
    }   
        
    public void setDireccion(String data)
    {
        this.sDireccion = data;
    }
    
    /*public void setNumber(int data)
    {
    	this.iPosicion = data;
    }*/
    
   
   
}
