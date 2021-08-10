package com.jvidal.worksmarter.PDFReport;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jvidal.worksmarter.PDFReport.activity.PDFCreatorActivity;
import com.jvidal.worksmarter.PDFReport.utils.PDFUtil;
import com.jvidal.worksmarter.PDFReport.views.PDFBody;
import com.jvidal.worksmarter.PDFReport.views.PDFFooterView;
import com.jvidal.worksmarter.PDFReport.views.PDFHeaderView;
import com.jvidal.worksmarter.PDFReport.views.PDFTableView;
import com.jvidal.worksmarter.PDFReport.views.basic.PDFHorizontalView;
import com.jvidal.worksmarter.PDFReport.views.basic.PDFImageView;
import com.jvidal.worksmarter.PDFReport.views.basic.PDFLineSeparatorView;
import com.jvidal.worksmarter.PDFReport.views.basic.PDFPageBreakView;
import com.jvidal.worksmarter.PDFReport.views.basic.PDFTextView;
import com.jvidal.worksmarter.R;

import java.io.File;

public class PdfCreatorExampleActivity extends PDFCreatorActivity {
    //Fisrt Page
    PDFTextView headingText;
    PDFTextView structureName;
    PDFImageView mainLogoBottom;

    //Second page
    PDFTextView secondPageInformationText;
    PDFImageView rightLogo;
    PDFImageView greenLineHorizontal;

    PDFTextView nameCompanyText;
    PDFTextView clientNameText;
    PDFTextView productNameText;
    PDFTextView totalStructurecountText;
    PDFImageView secondPageGreenline;
    PDFTextView nameText;
    PDFTextView positionNameText;

    //actual report start pages
    PDFTextView detailHeading;
    PDFImageView structureImage;

    PDFTextView spaceText;
    PDFTextView codeText;
    PDFTextView locationText;
    PDFTextView dimensionText;
    //page three
    PDFHorizontalView horizontalView;
    LinearLayout.LayoutParams topgreenLayout;

    LinearLayout.LayoutParams imageLayoutParam;


    //Footer Text
    PDFTextView footerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        createPDF("test", new PDFUtil.PDFUtilListener() {
            @Override
            public void pdfGenerationSuccess(File savedPDFFile) {
                Toast.makeText(PdfCreatorExampleActivity.this, "PDF Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pdfGenerationFailure(Exception exception) {
                Toast.makeText(PdfCreatorExampleActivity.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected PDFHeaderView getHeaderView(int pageIndex) {
        PDFHeaderView headerView = new PDFHeaderView(getApplicationContext());
        horizontalView = new PDFHorizontalView(getApplicationContext());
        rightLogo = new PDFImageView(getApplicationContext());


        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(
                60,
                60, 1);
        rightLogo.setImageScale(ImageView.ScaleType.FIT_CENTER);
        rightLogo.setImageResource(R.drawable.logo);
        imageLayoutParam.setMargins(320, 0, 0, 0);
        rightLogo.setLayout(imageLayoutParam);
        horizontalView.addView(rightLogo);
        headerView.addView(horizontalView);


        secondPageInformationText = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.H1);
        secondPageInformationText.setText("CRISTIAN CASTRO");
        secondPageInformationText.getView().setGravity(Gravity.LEFT);
        secondPageInformationText.setPadding(40, 0, 0, 10);
        headerView.addView(secondPageInformationText);


        topgreenLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10, 0);
        greenLineHorizontal = new PDFImageView(getApplicationContext());
        greenLineHorizontal.setLayout(topgreenLayout);
        greenLineHorizontal.setImageResource(R.drawable.leftdark);
        greenLineHorizontal.setLayout(topgreenLayout);
        headerView.addView(greenLineHorizontal);


        detailHeading = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.H3);
        detailHeading.setText(" Detalles del Espacio");
        detailHeading.getView().setGravity(Gravity.LEFT);
        detailHeading.setPadding(45, 5, 0, 0);
        headerView.addView(detailHeading);









/*

        PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H1);
        SpannableString word = new SpannableString("Reporte de Instalación de Publicidad");
        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pdfTextView.setText(word);
        pdfTextView.getView().setGravity(Gravity.CENTER);
        pdfTextView.getView().setHeight(100);
        pdfTextView.getView().setTypeface(pdfTextView.getView().getTypeface(), Typeface.BOLD);
        horizontalView.addView(pdfTextView);*/

       /* PDFImageView imageView = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(
                60,
                60, 0);
        imageView.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageLayoutParam.setMargins(0, 0, 10, 0);
        imageView.setLayout(imageLayoutParam);

        horizontalView.addView(imageView);*/
        PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        headerView.addView(lineSeparatorView1);
        // headerView.addView(horizontalView);


        return headerView;
    }

    @Override
    protected PDFBody getBodyViews() {
        PDFBody pdfBody = new PDFBody();


        headingText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H1);
        SpannableString word = new SpannableString("Reporte de Instalación de Publicidad");
        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        headingText.setText(word);
        headingText.getView().setGravity(Gravity.CENTER);
        headingText.getView().setHeight(200);
        headingText.getView().setTypeface(headingText.getView().getTypeface(), Typeface.BOLD);
        pdfBody.addView(headingText);


        structureName = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.H1);
        structureName.setText("CRISTIAN CASTRO");
        structureName.getView().setGravity(Gravity.CENTER);
        structureName.getView().setHeight(380);
        pdfBody.addView(structureName);


        mainLogoBottom = new PDFImageView(getApplicationContext());
        FrameLayout.LayoutParams childLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                100, Gravity.CENTER);
        mainLogoBottom.setLayout(childLayoutParams);

        mainLogoBottom.setImageResource(R.drawable.logo);
        pdfBody.addView(mainLogoBottom);


        //Page break
        //Second Page

        final PDFPageBreakView page2 = new PDFPageBreakView(getApplicationContext());
        pdfBody.addView(page2);

        horizontalView = new PDFHorizontalView(getApplicationContext());
        rightLogo = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(
                60,
                60, 1);
        rightLogo.setImageScale(ImageView.ScaleType.FIT_CENTER);
        rightLogo.setImageResource(R.drawable.logo);
        imageLayoutParam.setMargins(320, 0, 0, 0);
        rightLogo.setLayout(imageLayoutParam);
        horizontalView.addView(rightLogo);
        pdfBody.addView(horizontalView);


        secondPageInformationText = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.H1);
        secondPageInformationText.setText("Informaciones Generales");
        secondPageInformationText.getView().setGravity(Gravity.LEFT);
        secondPageInformationText.setPadding(40, 0, 0, 0);
        pdfBody.addView(secondPageInformationText);


        topgreenLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10, 0);
        greenLineHorizontal = new PDFImageView(getApplicationContext());
        greenLineHorizontal.setLayout(childLayoutParams);
        greenLineHorizontal.setImageResource(R.drawable.leftdark);
        greenLineHorizontal.setLayout(topgreenLayout);
        pdfBody.addView(greenLineHorizontal);

        nameCompanyText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        nameCompanyText.setPadding(40, 150, 0, 0);
        nameCompanyText.setText("Nombre Proyecto / Campaña           :            CRISTIAN CASTRO");
        pdfBody.addView(nameCompanyText);


        clientNameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        clientNameText.setPadding(40, 30, 0, 0);
        clientNameText.setText("Cliente / Empresa                                :             ED LIVE NETWORKS, SRL");
        pdfBody.addView(clientNameText);


        productNameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        productNameText.setPadding(40, 30, 0, 0);
        productNameText.setText("Producto                                               :              CRISTIAN CASTRO");
        pdfBody.addView(productNameText);


        totalStructurecountText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        totalStructurecountText.setPadding(40, 30, 0, 25);
        totalStructurecountText.setText("Cantidad de Espacios                         :              19");
        pdfBody.addView(totalStructurecountText);

        LinearLayout.LayoutParams space = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100, 0);
        PDFTextView textViewSpace = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        textViewSpace.setLayout(space);
        pdfBody.addView(textViewSpace);

        LinearLayout.LayoutParams bottomgreen = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5, 0);
        secondPageGreenline = new PDFImageView(getApplicationContext());
        secondPageGreenline.setLayout(childLayoutParams);
        secondPageGreenline.setImageResource(R.drawable.straightgreen);
        secondPageGreenline.setLayout(bottomgreen);
        secondPageGreenline.getView().setAdjustViewBounds(true);
        secondPageGreenline.setPadding(20, 0, 20, 0);
        pdfBody.addView(secondPageGreenline);


        nameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        nameText.setPadding(35, 0, 0, 0);
        nameText.setText("Elaborado por:                Juan Núñez                       Fecha:              2-ago-21                                Código:                FO-OPE-04");
        pdfBody.addView(nameText);


        positionNameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        positionNameText.setPadding(35, 5, 0, 0);
        positionNameText.setText("Posición:                            Contratista                         Supervisor Santo Domingo, R.D.             Edición No.:       1");
        pdfBody.addView(positionNameText);

        //Page three table

        final PDFPageBreakView page3 = new PDFPageBreakView(getApplicationContext());
        pdfBody.addView(page3);

        horizontalView = new PDFHorizontalView(getApplicationContext());
        rightLogo = new PDFImageView(getApplicationContext());
        imageLayoutParam = new LinearLayout.LayoutParams(
                60,
                60, 1);
        rightLogo.setImageScale(ImageView.ScaleType.FIT_CENTER);
        rightLogo.setImageResource(R.drawable.logo);
        imageLayoutParam.setMargins(320, 0, 0, 0);
        rightLogo.setLayout(imageLayoutParam);
        horizontalView.addView(rightLogo);
        pdfBody.addView(horizontalView);


        secondPageInformationText = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.H1);
        secondPageInformationText.setText("Informaciones Generales");
        secondPageInformationText.getView().setGravity(Gravity.LEFT);
        secondPageInformationText.setPadding(40, 0, 0, 0);
        pdfBody.addView(secondPageInformationText);


        topgreenLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10, 0);
        greenLineHorizontal = new PDFImageView(getApplicationContext());
        greenLineHorizontal.setLayout(childLayoutParams);
        greenLineHorizontal.setImageResource(R.drawable.leftdark);
        greenLineHorizontal.setLayout(topgreenLayout);
        pdfBody.addView(greenLineHorizontal);

        detailHeading = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.H3);
        detailHeading.setText("Detalles de Instalación – Resumen Distrito Nacional");
        detailHeading.getView().setGravity(Gravity.LEFT);
        detailHeading.setPadding(45, 10, 0, 10);
        pdfBody.addView(detailHeading);

        int[] widthPercent = {10, 15, 45, 10, 20}; // Sum should be equal to 100%
        String[] textInTable = {"Item", "Código", "Dirección", "Tamaño", "Diseño"};


        PDFTableView.PDFTableRowView columnHeader = new PDFTableView.PDFTableRowView(getApplicationContext());
        columnHeader.setBackgroundColor(getResources().getColor(R.color.green));
        for (String s : textInTable) {
            PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            if (s.equals("Item")) {
                pdfTextView.setText(s);
                pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.halfborder));
                pdfTextView.getView().setGravity(Gravity.CENTER);
            }
            if (s.equals("Código")) {
                pdfTextView.getView().setGravity(Gravity.CENTER);
                pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.halfborder));
                pdfTextView.setText(s);
            }
            if (s.equals("Dirección")) {
                pdfTextView.getView().setGravity(Gravity.CENTER);
                pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.halfborder));
                pdfTextView.setText(s);
            }
            if (s.equals("Tamaño")) {
                pdfTextView.getView().setBackground(getResources().getDrawable(R.drawable.halfborder));
                pdfTextView.setText(s);
                pdfTextView.getView().setGravity(Gravity.LEFT);

            }
            if (s.equals("Diseño")) {
                pdfTextView.getView().setGravity(Gravity.CENTER);
                pdfTextView.setText(s);
            }

            pdfTextView.setText(s);
            pdfTextView.setPadding(3,3,3,3);
            columnHeader.addTextRow(pdfTextView);
            columnHeader.getView().setBackground(getResources().getDrawable(R.drawable.boarder));
        }


        PDFTableView tableView = new PDFTableView(getApplicationContext(), columnHeader);

        for (int i = 0; i < 30; i++) {
            // Create 10 rows
            PDFTableView.PDFTableRowView tableRowView = new PDFTableView.PDFTableRowView(getApplicationContext());
            for (String s : textInTable) {
                PDFTextView pdfTextViewinner = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
                if (s.equals("Item")) {
                    pdfTextViewinner.setText("          " + i);
                }
                if (s.equals("Código")) {
                    pdfTextViewinner.setText("        2F-013-01 ");
                }
                if (s.equals("Dirección")) {
                    pdfTextViewinner.setText("   Av. Rómulo Betancourt esq. Calle");
                }
                if (s.equals("Tamaño")) {
                    pdfTextViewinner.setText("9.5' X 4'");
                }
                if (s.equals("Diseño")) {
                    pdfTextViewinner.setText(" CRISTIAN CASTRO");
                }

                tableRowView.addTextRow(pdfTextViewinner);
            }
            tableView.addRow(tableRowView);

        }
        tableView.setColumnWidth(widthPercent);
        pdfBody.addView(tableView);


        clientNameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        clientNameText.setPadding(20, 20, 0, 0);
        clientNameText.setText("                                                                                                        "+"Leyenda del Material:");
        pdfBody.addView(clientNameText);


        nameCompanyText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        nameCompanyText.setPadding(20, 5, 0, 0);
        nameCompanyText.setText("Código: FO-OPE-04                                                                  FF – Flex Face PX – Panaflex ");
        pdfBody.addView(nameCompanyText);


        clientNameText = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        clientNameText.setPadding(20, 0, 0, 0);
        clientNameText.setText("Edición No.: 01                                                                        VA – Vinyl Adhesivo ST – Styrene");
        pdfBody.addView(clientNameText);



















        final PDFPageBreakView page4 = new PDFPageBreakView(getApplicationContext());
        pdfBody.addView(page4);


        for (int i = 0; i <10 ; i++) {
            structureImage = new PDFImageView(getApplicationContext());RelativeLayout.LayoutParams childLayoutParamss = new RelativeLayout.LayoutParams(360, 350);
            childLayoutParamss.setMargins(100, 10, 0, 0);
            structureImage.setLayout(childLayoutParamss);
            structureImage.setImageResource(R.drawable.str);
            pdfBody.addView(structureImage);


            RelativeLayout.LayoutParams inftext = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            inftext.setMargins(40, 10, 0, 0);

            spaceText = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.P);
            spaceText.setText("Espacio:         MOBILIARIO URBANO");
            spaceText.getView().setGravity(Gravity.LEFT);
            spaceText.setPadding(0, 0, 0, 0);
            spaceText.setLayout(inftext);
            pdfBody.addView(spaceText);


            codeText = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.P);
            codeText.setText("Código:          2F-013-01");
            codeText.getView().setGravity(Gravity.LEFT);
            codeText.setPadding(0, 0, 0, 0);
            codeText.setLayout(inftext);
            pdfBody.addView(codeText);


            locationText = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.P);
            locationText.setText("Ubicación:       Av. 27 de Febrero / Av. W. Churchill - Boulevard  Frente  a Plaza Lama");
            locationText.getView().setGravity(Gravity.LEFT);
            locationText.setPadding(0, 0, 0, 0);
            locationText.setLayout(inftext);
            pdfBody.addView(locationText);


            dimensionText = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.P);
            dimensionText.setText("Tamaño:         9.5'X4'");
            dimensionText.getView().setGravity(Gravity.LEFT);
            dimensionText.setPadding(0, 0, 0, 0);
            dimensionText.setLayout(inftext);
            pdfBody.addView(dimensionText);
            final PDFPageBreakView page5 = new PDFPageBreakView(getApplicationContext());
            pdfBody.addView(page5);
        }






        return pdfBody;
    }

    @Override
    protected PDFFooterView getFooterView(int pageIndex) {
        PDFFooterView footerView = new PDFFooterView(getApplicationContext());
        footerText = new PDFTextView(this, PDFTextView.PDF_TEXT_SIZE.SMALL);
        footerText.setText("Código: FO-OPE-04\nEdición No.: 0");
        footerText.getView().setGravity(Gravity.LEFT);
        footerText.setPadding(40, 0, 0, 0);


        footerView.addView(footerText);

        return footerView;
    }

    @Nullable
    @Override
    protected PDFImageView getWatermarkView(int forPage) {
        PDFImageView pdfImageView = new PDFImageView(getApplicationContext());
        FrameLayout.LayoutParams childLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                200, Gravity.CENTER);
        pdfImageView.setLayout(childLayoutParams);

        pdfImageView.setImageResource(R.drawable.ic_pdf);
        pdfImageView.setImageScale(ImageView.ScaleType.FIT_CENTER);
        pdfImageView.getView().setAlpha(0.3F);

        return pdfImageView;
    }

    @Override
    protected void onNextClicked(final File savedPDFFile) {
        Uri pdfUri = Uri.fromFile(savedPDFFile);

        Intent intentPdfViewer = new Intent(PdfCreatorExampleActivity.this, PdfViewerExampleActivity.class);
        intentPdfViewer.putExtra(PdfViewerExampleActivity.PDF_FILE_URI, pdfUri);

        startActivity(intentPdfViewer);
    }
}
