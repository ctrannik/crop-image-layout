package me.littlecheesecake.cropimagelayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import me.littlecheesecake.croplayout.EditPhotoView;
import me.littlecheesecake.croplayout.EditableImage;
import me.littlecheesecake.croplayout.handler.OnBoxChangedListener;
import me.littlecheesecake.croplayout.model.ScalableBox;

public class MainActivity extends AppCompatActivity {

    private int x1 = 25;
    private int x2 = 640;
    private int y1 = 180;
    private int y2 = 880;
    private Bitmap bitmap;
    private ImageView result;
    private EditPhotoView imageView;
    private TextView boxText;
    private EditableImage image;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (ImageView) findViewById(R.id.result);
        imageView = (EditPhotoView) findViewById(R.id.editable_image);
        boxText = (TextView) findViewById(R.id.box_text);
        image = new EditableImage(this, R.drawable.photo2);
        bitmap = image.getOriginalImage();
        ScalableBox box = new ScalableBox(25,180,640,880);
        image.setBox(box);
        imageView.initView(this, image);

        boxText.setText("box: [" + 25 + "," + 180 +"],[" + 640 + "," + 880 + "]");
        imageView.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {
                boxText.setText("box: [" + x1 + "," + y1 +"],[" + x2 + "," + y2 + "]");
                MainActivity.this.x1 = x1;
                MainActivity.this.x2 = x2;
                MainActivity.this.y1 = y1;
                MainActivity.this.y2 = y2;
            }
        });

        Button button = (Button)findViewById(R.id.rotate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.rotateImageView();
                bitmap = image.getOriginalImage();
            }
        });

        Button buttonCut = (Button)findViewById(R.id.cat_button);
        buttonCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap cropBitmap = Bitmap.createBitmap(bitmap, x1, y1, x2 - x1, y2 - y1);
                result.setImageBitmap(cropBitmap);
            }
        });

    }

    public void makePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageView.changeImage(imageBitmap);
//            imageView.onSizeChanged(imageBitmap.getWidth(), imageBitmap.getHeight() ,bitmap.getWidth() ,bitmap.getHeight());
            result.setImageBitmap(imageBitmap);
            bitmap = imageBitmap;
        }
    }

}
