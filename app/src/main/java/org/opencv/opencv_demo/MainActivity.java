package org.opencv.opencv_demo;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.opencv_demo.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView imageViewTest;
    boolean isInitOK = false;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    isInitOK = true;
                }
                break;
                default: {
                    Log.i("OpenCV", "OpenCV loaded failed");
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.img);
        imageViewTest = findViewById(R.id.img_grey);

        Button button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitOK) {
                    Togray();

                    byte[][] inputArray = new byte[][]{
                            {1,0,3,4,5,7,0,1},
                            {1,0,0,0,0,0,0,0},
                            {1,0,0,0,0,0,0,5},
                            {1,0,3,0,5,2,1,3}};

                    // result array:
                    // 3 4 5 7 7 7 7 7
                    // 1 3 4 5 7 7 7 5
                    // 1 3 3 5 5 7 5 5
                    // 3 3 5 5 5 5 5 5
                    testMaxFilter(inputArray,5);

                    byte[][] inputArray2 = new byte[][]{
                            {1,0,3,4,5,7,0,1},
                            {1,0,3,4,5,2,1,3},
                            {1,0,3,4,5,0,0,5},
                            {1,0,3,4,5,2,1,3}};

                    // result array:
                    // 1 3 4 5 7 7 7 3
                    // 1 3 4 5 5 7 3 5
                    // 1 3 4 5 5 5 5 5
                    // 1 3 4 5 5 5 3 5
                    testMaxFilter(inputArray2,3);

                } else {
                    Toast.makeText(MainActivity.this, "还没有初始化好OpenCV，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button button2 = findViewById(R.id.btn2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToErode();
            }
        });

        Button button3 = findViewById(R.id.btn3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todilate();
            }
        });
    }


    int bitmapId = R.drawable.lena;

    public void testMaxFilter(byte[][] inputArray,int windowSize){

        byte[][] outputArray = new byte[inputArray.length][inputArray[0].length];

        MaxFilterTools.doMaxFilterUseCustomWindow(4,8,CvType.CV_8UC1,
                inputArray,windowSize,outputArray);

    }

    private void Togray() {
        //灰度处理
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(dst, bitmap);
        imageViewTest.setImageBitmap(bitmap);
        src.release();
        dst.release();
    }


    private void ToErode() {
        //腐蚀
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
        Mat src = new Mat();
        Mat dst = new Mat();
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10));
        ;

        Utils.bitmapToMat(bitmap, src);
        //    src：源图像
        //    dst：输出图像
        //    element：这是我们将用来执行操作的内核。如果我们不指定，默认是一个简单的3x3矩阵。否则，我们可以指定它的形状。为此，我们需要使用函数cv :: getStructuringElement：

        Imgproc.erode(src, dst, element);
        Utils.matToBitmap(dst, bitmap);

        imageViewTest.setImageBitmap(bitmap);
        src.release();
        dst.release();
    }

    private void Todilate() {
        //膨胀
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
        //    src：源图像
        //    dst：输出图像
        //    element：这是我们将用来执行操作的内核。如果我们不指定，默认是一个简单的3x3矩阵。否则，我们可以指定它的形状。为此，我们需要使用函数cv :: getStructuringElement：
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10));
        ;
        Mat src = new Mat();
        Mat dst = new Mat();

        Utils.bitmapToMat(bitmap, src);
        Imgproc.dilate(src, dst, element);
        Utils.matToBitmap(dst, bitmap);

        imageViewTest.setImageBitmap(bitmap);

        src.release();
        dst.release();
    }


}