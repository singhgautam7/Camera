package com.bat.iron.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button b;
    ImageView i1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b=(Button)findViewById(R.id.button);
        i1=(ImageView)findViewById(R.id.imageView);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //this action is for camera
                startActivityForResult(i,23); //here second parameter can be any number, which is called request code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle b=data.getExtras();  //everything in intent is stored in the form of bundle
        Bitmap bitmap=(Bitmap)b.get("data");  //'data' here is the data that is to be sent to intent which is form bitmap. here Bitmap is image format
        //i1.setImageBitmap(bitmap);
        //this image won't be saved after when the app is closed. to save the image permanently in a specified location, we us the following code:-

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root+ "/cameratest/firstattempt");
        myDir.mkdirs();

        SimpleDateFormat sd=new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String fname= sd.format(new Date())+".jpg";
        File file=new File(myDir, fname);
        if(file.exists()) file.delete();
        try {
            FileOutputStream out=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);

        i1.setImageBitmap(bitmap);
        getApplicationContext().sendBroadcast(mediaScanIntent);
    }
}
