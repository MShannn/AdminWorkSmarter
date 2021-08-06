package com.jvidal.worksmarter.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.FileInfo;
import com.jvidal.worksmarter.Adapters.StructureImagesAdapter;
import com.jvidal.worksmarter.Models.ImagesURLModel;
import com.jvidal.worksmarter.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnomaliesImagesFragment extends Fragment {
    ArrayList<ImagesURLModel> imagesUrl = new ArrayList<>();
    GridView gridView;
    LinearLayoutManager mLayoutManager;
    StructureImagesAdapter structureImagesAdapter;
    RelativeLayout progress;
    String structureTwoDigitCode;

    ImageView img_noRecord;

    public AnomaliesImagesFragment() {
        //  structureTwoDigitCode = getArguments().getString("towDigitCode");

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.anomalies_images_fragment, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        structureTwoDigitCode = getArguments().getString("towDigitCode");
        gridView = root.findViewById(R.id.gridview);
        Log.d("SHAN", "In structure image" + structureTwoDigitCode);
        progress = (RelativeLayout) root.findViewById(R.id.progress_view);
        img_noRecord = (ImageView) root.findViewById(R.id.img_norecord);
        img_noRecord.setVisibility(View.GONE);
        //progress.setVisibility(View.VISIBLE);
        new TestAsync().execute();


        return root;
    }


    class TestAsync extends AsyncTask<Void, Integer, String> {


        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            imagesUrl.clear();
        }

        protected String doInBackground(Void... arg0) {

            Backendless.Files.listing("/Work Anomalies Photo", structureTwoDigitCode + "*.png", true,
                    new AsyncCallback<List<FileInfo>>() {
                        @Override
                        public void handleResponse(List<FileInfo> response) {
                            if (response.size() == 0) {
                                progress.setVisibility(View.GONE);
                                img_noRecord.setVisibility(View.VISIBLE);
                            }
                            Iterator<FileInfo> filesIterator = response.iterator();
                            Log.d("SHAN", " doInBackground= " + response.size());
                            while (filesIterator.hasNext()) {
                                FileInfo file = filesIterator.next();
                                String publicURL = file.getPublicUrl();
                                String fileName = file.getName();

                                ImagesURLModel imagesURLModel = new ImagesURLModel();
                                imagesURLModel.setName(fileName);
                                imagesURLModel.setUrls(publicURL);
                                String subString = fileName.substring(0, 9);
                                String segments[] = subString.split("-");

                                Log.d("SHAN", " subString= " + subString);
                                Log.d("SHAN", " sub 1= " + segments[1]);
                                Log.d("SHAN", " sub 0= " + segments[0]);
                                Log.d("SHAN", " structureTwoDigitCode=" + structureTwoDigitCode);

                                String one = segments[0] + "-" + replaceAtTheEnd(segments[1]);
                                if (structureTwoDigitCode.equals(one)) {
                                    imagesUrl.add(imagesURLModel);
                                }

                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    structureImagesAdapter = new StructureImagesAdapter(getActivity(), imagesUrl);
                                    gridView.setAdapter(structureImagesAdapter);
                                    progress.setVisibility(View.GONE);
                                    if (imagesUrl.size() < 1) {
                                        img_noRecord.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.d("SHAN", "error files" + fault.getMessage());
                            progress.setVisibility(View.GONE);
                        }
                    });
            return "You are at PostExecute";
        }


        protected void onPostExecute(String result) {


        }
    }

    public static String replaceAtTheEnd(String input) {
        input = input.replaceAll("\\s+$", "");
        return input;
    }


}
