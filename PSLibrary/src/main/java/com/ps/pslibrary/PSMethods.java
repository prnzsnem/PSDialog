package com.ps.pslibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Sanam Mijar on 14,May,2021.
 */

public class PSMethods {
    private final Context context;
    static final float END_SCALE = 0.7f;
    private static final int IMG_WIDTH = 400;
    private static final int IMG_HEIGHT = 400;
    private Boolean IS_FAB_OPEN = false;
    private Boolean IS_NET_AVAIL = false;
    private final Animation FAB_OPEN;
    private final Animation FAB_CLOSE;
    private final Animation ROTATE_FORWARD;
    private final Animation ROTATE_BACKWARD;

    public PSMethods(Context context){
        this.context = context;
        FAB_OPEN = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        FAB_CLOSE = AnimationUtils.loadAnimation(context,R.anim.fab_close);
        ROTATE_FORWARD = AnimationUtils.loadAnimation(context,R.anim.fab_forward_rotate);
        ROTATE_BACKWARD = AnimationUtils.loadAnimation(context,R.anim.fab_backward_rotate); }

        public static String getAbsoluteUrl(Context context){
        PSSession session = new PSSession(context);
            String fileUrl = "";
            try {
                URL filePathUrl = new URL(session.getSystemSession(PSSession.KEY_SERVER_ADDRESS));
                fileUrl = filePathUrl.getProtocol() + "://" + filePathUrl.getHost() +":"+ filePathUrl.getPort();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return fileUrl;
        }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String generateRandomName() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return generateRandomString(10) + simpleDateFormat.format(new Date()) + (int) (Math.random() * 900 + 100);
    }

    public static String generateRandomString(int len){
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public void isInternetConnected(boolean isConnected){
        String message;
        int dialogType;
        if (!isConnected) {
            message = context.getString(R.string.offline);
            dialogType = PSDialog.ERROR;
        } else {
            message = context.getString(R.string.online);
            dialogType = PSDialog.SUCCESS;
        }

//        if (!isConnected){
            new PSDialog(context).showPSDialog(PSDialog.DIALOG_SNACK_BAR, dialogType, message, null);
//        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getThumbnail(Context context, String fileExtension){
        Drawable tnDrawable = context.getResources().getDrawable(R.drawable.ic_image);
        for (int i = 0; i<PSStrings.FILE_TYPE_ARR.length; i++){
            if (PSStrings.FILE_TYPE_ARR[i].equals(fileExtension)){
                tnDrawable = context.getResources().getDrawable(PSStrings.FILE_TYPE_THUMBNAIL[i]);
            }
        }
        return tnDrawable;
    }


    public void animateNavigationDrawer(DrawerLayout drawer, View container) {
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                container.setScaleX(offsetScale);
                container.setScaleY(offsetScale);
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = container.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                container.setTranslationX(xTranslation);
            }
        });
    }

    public void rotateView(View view, int degree, int duration){
        view.animate().rotationBy(Float.parseFloat(String.valueOf(degree))).setDuration(duration).setInterpolator(new LinearInterpolator()).start();
    }

    public static String truncateAfterWords(final String content, final int lastIndex) {
        String result;
        if (content.length() > lastIndex) {
            result = content.substring(0, lastIndex);
            if (content.charAt(lastIndex) != ' ') {
                result = result.substring(0, result.lastIndexOf(" ")) + "...";
            }
        }else{
            result = content;
        }
        return Html.fromHtml(result).toString();
    }

    private static String resizeBase64Image(String base64image) {
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);

        image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // To upload the image use 70 quality
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        byte[] b = outputStream.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    private static Bitmap convertString64ToImage(String b64) {
        byte[] decodedString = Base64.decode(b64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap convertStringToBitmap(String b64) {
        String finalImage;
        if (b64.contains(",")){
            finalImage = b64.substring(b64.indexOf(","));
        }else{
            finalImage = b64;
        }
        return convertString64ToImage(resizeBase64Image(finalImage));
    }

    public static Bitmap getBitmapFromUri(Context context, Uri imageUri) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    }

    public static String convertBitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static String ConvertToString(Context context, Uri uri){
        String base64 = "";
        String uriString = uri.toString();
        byte[] bytes;
        Log.d("data", "onActivityResult: uri"+uriString);
        try {
            InputStream in = context.getContentResolver().openInputStream(uri);
            bytes = getBytes(in);
            Log.d("data", "onActivityResult: bytes size="+bytes.length);
            Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes,Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes,Base64.DEFAULT);
            base64 = Base64.encodeToString(bytes,Base64.DEFAULT);

        } catch (Exception e) { e.printStackTrace(); }
        return base64;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    /**
     * colorStr
     * This method is to color the string before ofter the regex user mentioned.
     * Params:
     *  String inpStr: Input full string/Text.
     *  int psColor: Can use any color from 'Color' class or PSColor' class.
     *  int colorAfterOrBeforeRegex: Defines to color the string either before the regex or after. If value is 0 then before, if 1 then after.
     *  String regex: String or any regex that defines at what position to start color to/from.
     * **/
    public static Spannable colorStr(String inpStr, int psColor, int colorAfterOrBeforeRegex, String regex){
        Spannable spannable = new SpannableString(inpStr);
//        String splitStr = inpStr.split(regex)[colorAfterOrBeforeRegex];

        String splitStr = inpStr.substring(0, inpStr.indexOf(regex));
//        String splitStrAfter = inpStr.substring(inpStr.indexOf(regex) + regex.length());

        if (colorAfterOrBeforeRegex == PSRegex.BEFORE){
            spannable.setSpan(new ForegroundColorSpan(psColor), 0, splitStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (colorAfterOrBeforeRegex == PSRegex.AFTER){
            spannable.setSpan(new ForegroundColorSpan(psColor), splitStr.length() + 1, inpStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (colorAfterOrBeforeRegex == PSRegex.UP_TO){
            spannable.setSpan(new ForegroundColorSpan(psColor), 0, splitStr.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (colorAfterOrBeforeRegex == PSRegex.FROM){
            spannable.setSpan(new ForegroundColorSpan(psColor), splitStr.length(), inpStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannable.setSpan(new ForegroundColorSpan(psColor), colorAfterOrBeforeRegex, splitStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }


    /**
     * colorStr
     * This method is to color the whole string.
     * Params:
     *  String strToColor: Input full string/Text to color.
     *  int psColor: Can use any color from 'Color' class or PSColor' class.
     * **/
    public static Spannable colorStr(String strToColor, int psColor){
        Spannable spannable = new SpannableString(strToColor);
        spannable.setSpan(new ForegroundColorSpan(psColor), 0, strToColor.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }


    /**
     * capitalizeInitials
     * This method is to capitalize Initial letter of every words in string.
     * Params:
     *  String str: Input full string/Text.
     * **/
    public static String capitalizeInitials(String str){
        String returnString = str.toLowerCase();
        char[] charArray = returnString.toCharArray();
        boolean foundSpace = true;
        for(int i = 0; i < charArray.length; i++) {
            if(Character.isLetter(charArray[i])) {
                if(foundSpace) {
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            }
            else {
                foundSpace = true;
            }
        }
        returnString = String.valueOf(charArray);
        return returnString;
    }


    public String getFileNameByUri(Context context, Uri uri){
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;
        } else if (uri.getScheme().compareTo("file") == 0){
            try{
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();
            }catch (URISyntaxException ignored) {}
        } else {
            filepath = uri.getPath();
        }
        return filepath;
    }

    public static String getSelectedImage(Context context, Intent data, ImageView uploadedImageView){
        String imageText = "";
        try {
            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File destination = new File(Environment.getExternalStorageDirectory() + "/" +
                    context.getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream (destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException ignored) {}

            String imgPath = destination.getAbsolutePath();
            uploadedImageView.setImageBitmap(bitmap);
            imageText = "data:image/png;base64," + convertBitmapToString(bitmap);

        } catch (Exception ignored) {}
        return imageText;
    }

    public static String getSelectedImgFromGallery(Context context, Intent data, ImageView uploadedImageView){
        String imageText = "";
        try {
            Uri selectedImage = data.getData();
            Bitmap itemImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
            uploadedImageView.setImageBitmap(itemImage);
            imageText = "data:image/png;base64," + PSMethods.convertBitmapToString(itemImage);
        } catch (IOException ignored) {}
        return imageText;
    }

    public static Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String path = "";
        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public void animateFABIcon(FloatingActionButton fab_add_icon){
        if(IS_FAB_OPEN){
            fab_add_icon.startAnimation(ROTATE_BACKWARD);
            IS_FAB_OPEN = false;
        } else {
            fab_add_icon.startAnimation(ROTATE_FORWARD);
            IS_FAB_OPEN = true;
        }
    }

    public String getFileNameByUri(Uri uri){
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().compareTo("content") == 0)
        {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        }
        else
        if (uri.getScheme().compareTo("file") == 0)
        {
            try
            {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();
            }
            catch (URISyntaxException ignored) {}
        }
        else
        {
            filepath = uri.getPath();
        }
        return filepath;
    }


    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";
    public static void saveImage(Bitmap bmp) {
        FileOutputStream out = null;
        try {
            File folder = new File(FOLDER);
            if(!folder.exists())
                folder.mkdirs();
            File file = new File(folder, "PDF.png");
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            //todo with exception
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                //todo with exception
            }
        }
    }



    /**
     * Get pictures through uri and compress them
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //Image resolution is based on 480x800
        float hh = 800f;//The height is set as 800f here
        float ww = 480f;//Set the width here to 480f
        //Zoom ratio. Because it is a fixed scale, only one data of height or width is used for calculation
        int be = 1;//be=1 means no scaling
        if (originalWidth > originalHeight && originalWidth > ww) {//If the width is large, scale according to the fixed size of the width
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//If the height is high, scale according to the fixed size of the width
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //Proportional compression
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//Set scaling
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//Mass compression again
    }


    /**
     * Mass compression method
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Quality compression method, here 100 means no compression, store the compressed data in the BIOS
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //Cycle to determine if the compressed image is greater than 100kb, greater than continue compression
            baos.reset();//Reset the BIOS to clear it
            //First parameter: picture format, second parameter: picture quality, 100 is the highest, 0 is the worst, third parameter: save the compressed data stream
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//Here, the compression options are used to store the compressed data in the BIOS
            options -= 10;//10 less each time
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//Store the compressed data in ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//Generate image from ByteArrayInputStream data
        return bitmap;
    }

    public static boolean isNotNullOrEmpty(String str){
        return (str != null && !str.isEmpty());
    }

    public static String checkNullGetStr(String str){
        return isNotNullOrEmpty(str) ? str : PSStrings.STR_NA;
    }

    public static boolean isValMoreThanZero(String str){
        int count = Integer.parseInt(str);
        return count > 0;
    }

    public static String convertToDeleteLineFont(String string){
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            // \\u is just a code name, please add corresponding symbols according to specific needs
            unicode.append("\\u").append(Integer.toHexString(c)).append("\\u0966");
        }
        return unicodeToString(unicode.toString());
    }

    private static String unicodeToString(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }

    private static String stringToUnicode(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append("\\u").append(Integer.toHexString(c));
        }
        return unicode.toString();
    }
}