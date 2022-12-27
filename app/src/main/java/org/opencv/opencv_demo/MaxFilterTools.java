package org.opencv.opencv_demo;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

public class MaxFilterTools {

    public static void doMaxFilterUseOpenCVWindow(int rows,int cols,int cvType,
                              byte[][] inputArray,
                              Mat kernel,
                              byte[][] outputArray) {

        Mat src = new Mat(rows, cols, cvType);

        for(int i =0;i<rows;i++){
            src.put(i, 0, inputArray[i]);
        }
        printMat("input array",src,1);

        Mat dst = new Mat(rows, cols, cvType);

        Imgproc.dilate(src, dst, kernel);

        for(int i =0;i<rows;i++){
            dst.get(i,0,outputArray[i]);
            printMat("output array",src,1);
        }
    }

    public static void doMaxFilterUseCustomWindow(int rows,int cols,int cvType,
                                   byte[][] inputArray,
                                   int windowSize,
                                   byte[][] outputArray) {

        Mat src = new Mat(rows, cols, cvType);

        for(int i =0;i<rows;i++){
            src.put(i, 0, inputArray[i]);
        }
        printMat("input array",src,1);

        Mat dst = new Mat(rows, cols, cvType);

        byte[][] kernelArray = generateWindowMatrix(windowSize);

        Mat diamondKernel = new Mat(kernelArray.length, kernelArray[0].length, cvType);

        int m = kernelArray.length;
        int n = kernelArray[0].length;
        for (int i = 0; i < m; i++) {
            for (int i1 = 0; i1 < n; i1++) {
                diamondKernel.put(i,i1,kernelArray[i][i1]);
            }
        }
        Imgproc.dilate(src, dst, diamondKernel);

        for(int i =0;i<rows;i++){
            dst.get(i,0,outputArray[i]);
        }
        printMat("output array",dst,1);
    }


    public static byte[][] generateWindowMatrix(int length){
        byte[][] gererateArray = new byte[length][length];

        int middle = (length -1)/2 ;
        int rowCursor = 0;

        boolean reduceFlag = false;
        for(int i = 0;i<length;i++){
            for(int j = 0;j<length;j++){
                int indexStart = middle - rowCursor;
                int indexEnd = middle + rowCursor;
                if(j<indexStart|| j> indexEnd){
                    gererateArray[i][j] = 0;
                }else{
                    gererateArray[i][j] = 1;
                }
            }
            if(rowCursor == middle){
                reduceFlag = true;
            }
            if(reduceFlag){
                rowCursor--;
            }else{
                rowCursor++;
            }
        }
        return gererateArray;
    }


    public static void printMat(String tip,Mat src, int channelCount){
        System.out.println("******"+ tip + "******");
        byte [] b= new byte[channelCount];
        for (int i=0;i<src.rows();i++){//遍历行、列
            for (int j=0;j<src.cols();j++){
                src.get(i,j,b);//获取每个运算，并存储在b数组中
                System.out.print(Arrays.toString(b));
            }
            System.out.println();
        }
        System.out.println("******"+ tip + "******");
    }
}
