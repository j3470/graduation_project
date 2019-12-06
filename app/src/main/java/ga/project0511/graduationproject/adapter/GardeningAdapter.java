package ga.project0511.graduationproject.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ga.project0511.graduationproject.R;
import ga.project0511.graduationproject.Retrofit.IMyService;
import ga.project0511.graduationproject.Retrofit.RetrofitClient;
import ga.project0511.graduationproject.datatype.Gardening;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class GardeningAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Gardening> sample;

    public static CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static Retrofit retrofitClient = RetrofitClient.getInstance();
    public static IMyService iMyService = retrofitClient.create(IMyService.class);

    TextView name_activity;
    TextView date_created;
    TextView id_manager;
    ImageView repImg;

    private String imgPath;

    public GardeningAdapter(Context context, ArrayList<Gardening> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
/*
        // download Activity Rep image from server
        for(int i=0; i<sample.size(); i++) {
            downImgFile(sample.get(i).getImgPath_server(), sample.get(i).getActivityName());
            sample.get(i).setImgPath_local(imgPath);
            }
 */
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Gardening getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        // View init
        View view = mLayoutInflater.inflate(R.layout.activity_listing_plants_listview, null);

        name_activity = view.findViewById(R.id.listing_plants_listView_activityName);
        date_created = view.findViewById(R.id.listing_plants_listView_createdAt);
        id_manager = view.findViewById(R.id.listing_plants_listView_managerID);
        repImg = view.findViewById(R.id.listing_plants_listView_image);

        // insert value into TextView
        name_activity.setText("활동명: "+sample.get(position).getActivityName());
        date_created.setText("시작 일시: "+sample.get(position).getCreatedAt());
        id_manager.setText("팀장: "+sample.get(position).getManagerID());
/*
        // insert image which is downloaded from server into ImageView
        isDownloaded = false;
        isSet = false;
        downImgFile(sample.get(position).getImgPath_server(), sample.get(position).getActivityName());

        // insert image into ImageView
        String imgPath = sample.get(position).getImgPath_local();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap originalBm = BitmapFactory.decodeFile(imgPath, options);
        Bitmap resizeBm = Bitmap.createScaledBitmap(originalBm, 80, 80, true);

        repImg.setImageBitmap(resizeBm);
*/

        return view;
    }

    private void downImgFile(String filePath, final String activityName) {
        // download image from server
        compositeDisposable.add(iMyService.downloadImage(filePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        writeResponseBodyToDisk(responseBody, activityName);
                    }
                }));
    }

    private boolean writeResponseBodyToDisk(final ResponseBody body, final String activityName) {

        try {
            // todo change the file location/name
            File storageDir = new File(Environment.getExternalStorageDirectory() +
                    "/activityDownloadImage");
            if(!storageDir.exists()) storageDir.mkdirs();

            String imageFileName = "repImg_"+activityName+"_";
            File imgFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            imgPath = imgFile.getAbsolutePath();

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(imgFile);

                while(true) {
                    int read = inputStream.read(fileReader);

                    if ( read == -1 ) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if(inputStream != null) {
                    inputStream.close();
                }

                if(outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
