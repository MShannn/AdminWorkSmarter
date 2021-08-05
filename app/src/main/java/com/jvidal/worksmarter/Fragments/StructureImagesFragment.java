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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StructureImagesFragment extends Fragment {
    ArrayList<ImagesURLModel> imagesUrl = new ArrayList<>();
    GridView gridView;
    LinearLayoutManager mLayoutManager;
    StructureImagesAdapter structureImagesAdapter;
    RelativeLayout progress;
    ImageView img_noRecord;
    String structureTwoDigitCode;

    public StructureImagesFragment() {


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.structure_images_fragment, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        structureTwoDigitCode = getArguments().getString("towDigitCode");
        gridView = root.findViewById(R.id.gridview);

        progress = (RelativeLayout) root.findViewById(R.id.progress_view);
        img_noRecord = (ImageView) root.findViewById(R.id.img_norecord);
        img_noRecord.setVisibility(View.GONE);

        new TestAsync().execute();

        return root;
    }



    public static String replaceAtTheEnd(String input) {
        input = input.replaceAll("\\s+$", "");
        return input;
    }

    class TestAsync extends AsyncTask<Void, Integer, String> {
        String TAG = "SHAN";

        protected void onPreExecute() {
            Log.d("SHAN" , " onPreExecute");
            progress.setVisibility(View.VISIBLE);
            imagesUrl.clear();


        }

        protected String doInBackground(Void... arg0) {
            Backendless.Files.listing("/Work Photos", structureTwoDigitCode + "*.png", true,
                    new AsyncCallback<List<FileInfo>>() {
                        @Override
                        public void handleResponse(List<FileInfo> response) {
                            Log.d("SHAN", "File fetching count=" + response.size());
                            if(response.size()==0){
                                progress.setVisibility(View.GONE);
                                    img_noRecord.setVisibility(View.VISIBLE);
                            }
                            Iterator<FileInfo> filesIterator = response.iterator();
                            while (filesIterator.hasNext()) {
                                FileInfo file = filesIterator.next();
                                String URL = file.getURL();
                                String publicURL = file.getPublicUrl();
                                Date createdOn = new Date(file.getCreatedOn());
                                String fileName = file.getName();

                                ImagesURLModel imagesURLModel = new ImagesURLModel();
                                imagesURLModel.setName(fileName);
                                imagesURLModel.setUrls(publicURL);

                                String subString = fileName.substring(0, 9);
                                String segments[] = subString.split("-");


                                String one = segments[0] + "-" + replaceAtTheEnd(segments[1]);
                                if (structureTwoDigitCode.equals(one)) {
                                    imagesUrl.add(imagesURLModel);
                                    Log.d("SHAN", " model added");

                                } else {
                                    Log.d("SHAN", " not added=" + structureTwoDigitCode + "=" + one);
                                }
                                progress.setVisibility(View.GONE);

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

                                //imagesUrl.add(imagesURLModel);
                            }


                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.d("SHAN", "error files" + fault.getMessage());
                            progress.setVisibility(View.GONE);
                        }
                    });


            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {



            Log.d("SHAN" + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d("SHAN" , " onPostExecute");


        }
    }
}
