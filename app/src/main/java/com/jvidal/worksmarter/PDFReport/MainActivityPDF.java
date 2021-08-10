package com.jvidal.worksmarter.PDFReport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PDFPrint;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


import com.jvidal.worksmarter.PDFReport.utils.FileManager;
import com.jvidal.worksmarter.PDFReport.utils.PDFUtil;
import com.jvidal.worksmarter.R;

import java.io.File;

public class MainActivityPDF extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_pdf);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        LinearLayout layoutPdfCreator = (LinearLayout) findViewById(R.id.layoutGeneratePdf);
        LinearLayout layoutHtmlPdfCreator = (LinearLayout) findViewById(R.id.layoutGenerateHtmlPdf);
        LinearLayout layoutEditHtmlPdf = (LinearLayout) findViewById(R.id.layoutEditHtmlPdf);
        startActivity(new Intent(MainActivityPDF.this, PdfCreatorExampleActivity.class));

        layoutPdfCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityPDF.this, PdfCreatorExampleActivity.class));
            }
        });

        layoutHtmlPdfCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager.getInstance().cleanTempFolder(getApplicationContext());
                // Create Temp File to save Pdf To
                final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
                // Generate Pdf From Html
                PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile,
                        "<!DOCTYPE html>\n" +

                                "<html>\n" +

                                "<body>\n" +

                                "\n" +

                                "<h1>My First Heading.</h1>\n" +

                                "<p>My first paragraph.</p>\n" +

                                "<p>!1 @2 #3 $4 %5 ^6 &7 *8 (9 )0</p>\n" +

                                "<p>@#&=*+-_.,:!?()/~'%</p>\n" +

                                "<a href='https://www.example.com'>This is a link</a>" +

                                "\n" +

                                "</body>\n" +

                                "</html>",


                        new PDFPrint.OnPDFPrintListener() {
                            @Override
                            public void onSuccess(File file) {
                                // Open Pdf Viewer
                                Uri pdfUri = Uri.fromFile(savedPDFFile);

                                Intent intentPdfViewer = new Intent(MainActivityPDF.this, PdfViewerExampleActivity.class);
                                intentPdfViewer.putExtra(PdfViewerExampleActivity.PDF_FILE_URI, pdfUri);

                                startActivity(intentPdfViewer);
                            }

                            @Override
                            public void onError(Exception exception) {
                                exception.printStackTrace();
                            }
                        });
            }
        });
       /* TableLayout table = new TableLayout(this);
        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.BLACK);
        tr.setPadding(0, 0, 0, 2); //Border between rows

        TableRow.LayoutParams llp = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 2, 0);//2px right-margin

//New Cell
        LinearLayout cell = new LinearLayout(this);
        cell.setBackgroundColor(Color.WHITE);
        cell.setLayoutParams(llp);//2px border on the right for the cell


        TextView tv = new TextView(this);
        tv.setText("Some Text");
        tv.setPadding(0, 0, 4, 3);

        cell.addView(tv);
        tr.addView(cell);
//add as many cells you want to a row, using the same approach

        table.addView(tr);*/



    }
}
