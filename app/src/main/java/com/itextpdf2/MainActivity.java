package com.itextpdf2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private Button btnPdf;
    //
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private ParagraphBorder paragraphBorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        btnPdf = findViewById(R.id.btn_pdf);
        btnPdf.setOnClickListener(onClickPdf);
        //
        checkFileWritePermission();
    }

    View.OnClickListener onClickPdf = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {

            try {
                genPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    };

    //
    private void genPdf() throws FileNotFoundException, DocumentException {
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pfdItext/sample.pdf";
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pfdItext";
        //
        File folder= new File(Environment.getExternalStorageState());
        if(!folder.exists()){
            folder.mkdirs();
        }

        pdfFile = new File("/sdcard/sample2.pdf");
        //Create doc.
        //document = new Document();
        document = new Document(new Rectangle(216, 500), 10, 10, 10, 0);
        pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFile));
        paragraphBorder = new ParagraphBorder();
        pdfWriter.setPageEvent(paragraphBorder);
        //Location to save write..
        //open to write
        document.open();
        //document.setPageSize(PageSize.A4);
        //border = new ParagraphBorder();
        //PdfWriter.setPageEvent(border);
        document.addCreationDate();
        //document.add(new Paragraph("Open Code"+"\n"+"Gfe Luis Meneses"));
        document.addAuthor("Open Code");
        document.addCreator("Gfe Luis Meneses");
        //
        titulo_documento("111223323",0, "ssdasd");
        fecha_documento("9088798");
        datos_emisor("11111111","ssddsda","AADS","ASDA","SDADSA","asdd");
        //
        document.close();
        viewPdf(MainActivity.this);
    }

    public void titulo_documento(String rut, int tipodoc, String correlativo){
        String nombredoc="";
        switch (tipodoc){
            case 52:
                nombredoc="GUÍA DE DESPACHO ELECTRÓNICA";
                break;
            case 39:
                nombredoc="BOLETA ELECTRÓNICA";
                break;

            case 33:
                nombredoc="FACTURA ELECTRÓNICA";
                break;

            case 0:
                nombredoc="NOTA DE VENTA";
                break;
        }

        //
        try {
            paragraph= new Paragraph();
            PdfPCell pdfPCell;
            //addChildP(new Paragraph(rut,fontEncabezado));
            //addChildP(new Paragraph(nombredoc,fontEncabezado));
            //addChildP(new Paragraph("N° "+correlativo,fontEncabezado));
            paragraphBorder.setActive(true);
            Paragraph preface;

            preface = new Paragraph(rut+"\n"+nombredoc+"\n"+"N° "+correlativo);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.setSpacingAfter(2);
            document.add(preface);

            paragraphBorder.setActive(false);

            /*
            if(tipodoc!=0){

                preface = new Paragraph(new Paragraph("S.I.I. CONCEPCION",null));
                preface.setAlignment(Element.ALIGN_CENTER);
                preface.setSpacingBefore(5);

                document.add(preface);
            }
            */

        } catch (Exception  e) {
            Log.e("titulo_documento",e.toString());
        }


    }

    public void fecha_documento(String fecha){

        try{
            Paragraph preface = new Paragraph(fecha);
            preface.setSpacingAfter(10);
            document.add(preface);
        }catch (Exception e){
            Log.e("fecha_documento",e.toString());
        }


    }

    public void datos_emisor(String rut, String nombre, String giro, String direccion ,String email, String telefono){

        try{
            Paragraph preface = new Paragraph("RAZON SOCIAL DEL EMISOR");
            preface.setIndentationLeft(6);
            paragraphBorder.setActive(true);
            document.add(preface);
            paragraphBorder.setActive(false);

            preface = new Paragraph(new Paragraph("AAA"));
            preface.setSpacingBefore(5);
            document.add(preface);
            preface = new Paragraph(new Paragraph(nombre));
            document.add(preface);
            preface = new Paragraph(new Paragraph(giro));
            document.add(preface);
            preface = new Paragraph(new Paragraph(direccion));
            document.add(preface);
            preface = new Paragraph(new Paragraph(email));
            document.add(preface);
            preface = new Paragraph(new Paragraph(telefono));
            preface.setSpacingAfter(5);
            document.add(preface);
        }catch (Exception e){
            Log.e("datos_emisor",e.toString());
        }

    }

    //chequea permisos (ESCRITURA EXTERNA)..
    private void checkFileWritePermission(){
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //al crear levanta el PDF ..
    public void viewPdf(Activity activity){
        /*Intent intent = new Intent(context, ViewPDFActivity.class);
        intent.putExtra("path","/sdcard/midocumento.pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/

        try {

            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(Uri.fromFile(pdfFile),"application/pdf");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            pdfViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pdfViewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //Toast.makeText(this, "Guia generada correctamente", Toast.LENGTH_LONG).show();
            Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
            try {
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
