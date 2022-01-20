package com.example.simplecamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView myImage;
    private Button button, save_btn;
    public Bitmap bitmap;
    BitmapDrawable drawable;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            bitmap = (Bitmap)data.getExtras().get("data");
            myImage.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImage = (ImageView)findViewById(R.id.myimage);
        button = (Button)findViewById(R.id.button);
        save_btn = (Button)findViewById(R.id.save_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();

            }

        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawable = (BitmapDrawable) myImage.getDrawable();
                bitmap = drawable.getBitmap();
                FileOutputStream outputStream = null;


                File sdCard = Environment.getExternalStorageDirectory();
                File directory = new File(sdCard.getAbsolutePath());
                directory.mkdir();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(directory,fileName);

                /***
                 * Search photo in the Logcat to see the saved image name
                 */
                Log.i("photo", fileName);

                Toast.makeText(MainActivity.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
                try {
                    outputStream = new FileOutputStream(outFile);

                    /***
                     * Compressing the image quality to 50
                     */
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);

    }

}