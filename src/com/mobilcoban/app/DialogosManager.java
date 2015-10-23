package com.mobilcoban.app;


import java.util.Set;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import com.zebra.printer.MobilePrinter;

public class DialogosManager {
	
	private static final String[] CODE_PAGE_ITEMS = {
        "Page 0 437 (USA, Standard Europe)",
        "Page 1 Katakana",
        "Page 2 850 (Multilingual)",
        "Page 3 860 (Portuguese)",
        "Page 4 863 (Canadian-French)",
        "Page 5 865 (Nordic)",
        "Page 16 1252 (Latin I)",
        "Page 17 866 (Cyrillic #2)",
        "Page 18 852 (Latin 2)",
        "Page 19 858 (Euro)",
        "Page 21 862 (Hebrew DOS code)",
        "Page 22 864 (Arabic)",
        "Page 23 Thai42",
        "Page 24 1253 (Greek)",
        "Page 25 1254 (Turkish)",
        "Page 26 1257 (Baltic)",
        "Page 27 Farsi",
        "Page 28 1251 (Cyrillic)",
        "Page 29 737 (Greek)",
        "Page 30 775 (Baltic)",
        "Page 31 Thai14",
        "Page 33 1255 (Hebrew New code)",
        "Page 34 Thai 11",
        "Page 35 Thai 18",
        "Page 36 855 (Cyrillic)",
        "Page 37 857 (Turkish)",
        "Page 38 928 (Greek)",
        "Page 39 Thai 16",
        "Page 40 1256 (Arabic)",
        "Page 41 1258 (Vietnam)",
        "Page 42 KHMER(Cambodia)",
        "Page 255 User set page"
    };

    private static final String[] PRINTER_ID_ITEMS = {
        "Firmware version",
        "Manufacturer",
        "Printer model",
        "Code page"
    };
    
    static void showBluetoothDialog(Context context, final Set<BluetoothDevice> pairedDevices) 
    {
        final String[] items = new String[pairedDevices.size()];
        int index = 0;
        
        for (BluetoothDevice device : pairedDevices) 
        {
            items[index++] = device.getAddress();
            
        }

        
        
        ListadoCobrosActivity.mMobilePrinter.connect(items[0]);        
              
        
        /*new AlertDialog.Builder(context).setTitle("Paired Bluetooth printers")
                .setItems(items, new DialogInterface.OnClickListener() {
                    
                	
                	public void onClick(DialogInterface dialog, int which) {
                        //ListadoCobrosActivity.mMobilePrinter.connect(items[which]);
                        ListadoCobrosActivity.mMobilePrinter.connect(items[which].toString());
                    }
                }).show();*/
        
    }


    static void showCodePageDialog(AlertDialog dialog, Context context, final Handler handler) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(context).setTitle("Code page")
                    .setItems(CODE_PAGE_ITEMS, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_437_USA);
                            	
                                break;
                            case 1:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_KATAKANA);
                            	
                                break;
                            case 2:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_850_MULTILINGUAL);
                            	
                                break;
                            case 3:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_860_PORTUGUESE);
                            	
                                break;
                            case 4:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_863_CANADIAN_FRENCH);
                            	
                                break;
                            case 5:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_865_NORDIC);
                            	
                                break;
                            case 6:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_1252_LATIN1);
                            	
                                break;
                            case 7:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_866_CYRILLIC2);
                            	
                                break;
                            case 8:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_852_LATIN2);
                            	
                                break;
                            case 9:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_858_EURO);
                            	
                                break;
                            case 10:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_862_HEBREW_DOS_CODE);
                            	
                                break;
                            case 11:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_864_ARABIC);
                            	
                                break;
                            case 12:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_THAI42);
                            	
                                break;
                            case 13:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_1253_GREEK);
                            	
                                break;
                            case 14:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_1254_TURKISH);
                            	
                                break;
                            case 15:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_1257_BALTIC);
                            	
                                break;
                            case 16:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_FARSI);
                                break;
                            case 17:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_1251_CYRILLIC);
                                break;
                            case 18:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_737_GREEK);
                                break;
                            case 19:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_775_BALTIC);
                                break;
                            case 20:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_437_THAI14);
                                break;
                            case 21:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_1255_HEBREW_NEW_CODE);
                                break;
                            case 22:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_THAI11);
                                break;
                            case 23:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_THAI18);
                                break;
                            case 24:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_855_CYRILLIC);
                                break;
                            case 25:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_857_TURKISH);
                                break;
                            case 26:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_928_GREEK);
                                break;
                            case 27:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_THAI16);
                                break;
                            case 28:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_1256_ARABIC);
                                break;
                            case 29:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_1258_VIETNAM);
                                break;
                            case 30:
                            	ListadoCobrosActivity.mMobilePrinter.setCodePage(MobilePrinter.CODE_PAGE_KHMER_CAMBODIA);
                                break;
                            }

                        }
                    }).create();
        }
        dialog.show();
    }

    static void showPrinterIdDialog(AlertDialog dialog, Context context) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(context).setTitle("Get printer ID")
                    .setItems(PRINTER_ID_ITEMS, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                            	ListadoCobrosActivity.mMobilePrinter.getPrinterId(MobilePrinter.PRINTER_ID_FIRMWARE_VERSION);
                            	
                                break;
                            case 1:
                            	ListadoCobrosActivity.mMobilePrinter.getPrinterId(MobilePrinter.PRINTER_ID_MANUFACTURER);
                            	
                                break;
                            case 2:
                            	ListadoCobrosActivity.mMobilePrinter.getPrinterId(MobilePrinter.PRINTER_ID_PRINTER_MODEL);
                            	
                                break;
                            case 3:
                            	ListadoCobrosActivity.mMobilePrinter.getPrinterId(MobilePrinter.PRINTER_ID_CODE_PAGE);
                            	
                                break;
                            }

                        }
                    }).create();

        }
        dialog.show();
    }

}
