package com.mm.saiaumain.yummyrecipe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mm.saiaumain.yummyrecipe.DetailActivity;
import com.mm.saiaumain.yummyrecipe.R;
import com.mm.saiaumain.yummyrecipe.uicomponents.CardImage;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;
import com.mm.saiaumain.yummyrecipe.vo.RecipeItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 10/29/2017.
 */

public class YummyRecipeUtils {

    private static final String TAG = "Yummy-YummyRecipeUtils";
    private static final String[] IMG_SIZES = new String[]{"400x200","150x180"};

    public static View generateRecipeItem(Context context, LayoutInflater inflater, ViewGroup container,
                                          int layout, Recipe recipe){
        Drawable background = YummyRecipeUtils.getDrawableFromAsset(context, "img_150x180/" + recipe.getImageInfo().getUrl());
        int imageHeight = ((BitmapDrawable)background).getBitmap().getHeight();
        imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, imageHeight, context.getResources().getDisplayMetrics());
        int width = ((BitmapDrawable)background).getBitmap().getWidth();
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());

        View view = inflater.inflate(layout, container, false);
        CardView cardView = (CardView) view.findViewById(R.id.itemCard);

        ImageView cardImage = (ImageView) cardView.findViewById(R.id.itemImage);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cardImage.getLayoutParams();
        params.width = width;
        params.height = imageHeight;
        cardImage.setLayoutParams(params);
        Bitmap bitmap = ((BitmapDrawable)background).getBitmap();
        cardImage.setImageBitmap(bitmap);

        TextView cardName = (TextView) cardView.findViewById(R.id.itemName);
        String recipeName = recipe.getName();
        if(recipeName.contains("\n"))
            recipeName = recipeName.replace("\n", "");
        if(recipeName.length() > 21){
            recipeName = recipeName.substring(0, 19);
            recipeName = recipeName.concat("...");
        }
        cardName.setText(recipeName);

        CheckedTextView itemTime = (CheckedTextView) cardView.findViewById(R.id.itemTime);
        itemTime.setText(recipe.getTotalTime());
        cardView.setTag(recipe.getId());
        cardView.setBackgroundColor(Color.WHITE);

        return view;
    }

    public static CardView generateRecipeCardItem(Context context, LayoutInflater inflater, ViewGroup container,
                                                  int layout, Recipe recipe){
        CardView cardView = (CardView) inflater.inflate(layout, container, false);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParam.setMargins(30, 35, 30, 35);
        cardView.setLayoutParams(layoutParam);

        Drawable background = YummyRecipeUtils.getDrawableFromAsset(context, "img_150x180/" + recipe.getImageInfo().getUrl());
        int imageHeight = ((BitmapDrawable)background).getBitmap().getHeight();
        imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, imageHeight, context.getResources().getDisplayMetrics());
        int width = ((BitmapDrawable)background).getBitmap().getWidth();
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());

        CardImage image = (CardImage) cardView.findViewById(R.id.image);
        LinearLayout.LayoutParams cardImageParam = (LinearLayout.LayoutParams) image.getLayoutParams();
        cardImageParam.width = width;
        cardImageParam.height = imageHeight;
        image.setLayoutParams(cardImageParam);
        Bitmap bitmap = ((BitmapDrawable)background).getBitmap();
        image.setImageBitmap(bitmap);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView name = (TextView) cardView.findViewById(R.id.name);
        String recipeName = recipe.getName();
        if(recipeName.contains("\n"))
            recipeName = recipeName.replace("\n", "");
        if(recipeName.length() > 21){
            recipeName = recipeName.substring(0, 19);
            recipeName = recipeName.concat("...");
        }
        name.setText(recipeName);

        CheckedTextView time = (CheckedTextView) cardView.findViewById(R.id.time);
        time.setText(recipe.getTotalTime());
        cardView.setTag(recipe.getId());
        return cardView;
    }

    public static List<RecipeItem> getAssetsRecipeList(Context context){
        List<RecipeItem> recipeItemList = new ArrayList<RecipeItem>();
        BufferedReader reader = null;
        try{
            AssetManager manager = context.getResources().getAssets();
            reader = new BufferedReader(new InputStreamReader(manager.open("receipebook.json")));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine())  != null) {
                builder.append(line);
            }
            Log.e(TAG, "***** Json File Content >>>>> " + builder.toString());
            Type type = new TypeToken<List<RecipeItem>>(){}.getType();
            recipeItemList = new Gson().fromJson(builder.toString(), type);
            return recipeItemList;
        }catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    public static void setupHorizontalList(Context context, RecipeItem item, LinearLayout layout){
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        TextView label = new TextView(context);
        label.setTextAppearance(R.style.HomeGalleryLabel);
        label.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(150, 0, 200, 0);
        label.setLayoutParams(params);
        String labelText = item.getLabel().replace("@@", "\n");
        label.setText(labelText);
        layout.addView(label);
    }

    public static void doDetailData(Context context, Recipe item){
        Intent intent = new Intent(context, DetailActivity.class);
        String data = new Gson().toJson(item);
        intent.putExtra("detail", data);
        context.startActivity(intent);
    }

    public static Drawable getDrawableFromAsset(Context context, String location){
        try{
            InputStream input = context.getAssets().open(location);
            return Drawable.createFromStream(input, null);
        }catch(IOException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    public static boolean isEmptyOrNull(String val){
        return (null == val || val.isEmpty())? true : false;
    }

    public static boolean hasPermissionSelfCheck(Context context, String... permissionCode){
        if(permissionCode.length > 1){
            String result = "";
            for(String code : permissionCode){
                int permission = ContextCompat.checkSelfPermission(context, code);
                result = result.concat((permission == PackageManager.PERMISSION_GRANTED)? "Y" : "N");
            }
            Log.e(TAG, "Multiple Permissions Result >>>>> " + result);
            return result.contains("N")? false : true;
        }else{
            int permission = ContextCompat.checkSelfPermission(context, permissionCode[0]);
            boolean result = (permission == PackageManager.PERMISSION_GRANTED)? true : false;
            Log.e(TAG, "Single Permissions Result >>>>> " + result);
            return result;
        }
    }

    public static boolean requiredPermissionExplanation(Activity context, String... permissionCodes){
        String result = "";
        for(String permissionCode : permissionCodes){
            Log.e(TAG, "Permission Code >>>> " + permissionCode);
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permissionCode)){
                result = result.concat("Y");
            }else{
                result = result.concat("N");
            }
        }
        Log.e(TAG, "Permission Explanation Required????? " + result);
        return (result.contains("Y")? true : false);
    }

    public static String generateImageName(Context context){
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        timeStamp = context.getString(R.string.app_name) + timeStamp + ".png";
        Log.e(TAG, "File Name >>>>> " + timeStamp);
        return timeStamp;
    }

    public static boolean setUpDirectories(String rootFilePath){
        boolean flag = false;
        try{
            for(String subFolderPath : IMG_SIZES){
                subFolderPath = rootFilePath + File.separator + subFolderPath + File.separator;
                Log.e(TAG, "SubFolder >>>>> " + subFolderPath);
                File subFolder = new File(subFolderPath);
                if(!subFolder.exists()) {
                    subFolder.mkdirs();
                    Log.e(TAG, "Create Subfolder " + subFolderPath + " Done !!!!");
                }else{
                    Log.e(TAG, "Subfolder " + subFolderPath + " Already exists !!!!");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public static Bitmap createImageFile(Context context, String fileName, int photoWidth, int photoHeight, Bitmap bitmap){
        String rootFilePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() +
                File.separator + context.getString(R.string.app_name);
        Log.e(TAG, "Root File Path >>>> " + rootFilePath);
        Log.e(TAG, "Photo Width >>>> " + photoWidth);
        Log.e(TAG, "Photo Height >>>> " + photoHeight);
        Log.e(TAG, "Bitmap Width >>>> " + bitmap.getWidth());
        Log.e(TAG, "Bitmap Height >>>> " + bitmap.getHeight());
        File rootFile = new File(rootFilePath);

        if(!rootFile.exists())
            rootFile.mkdirs();
        setUpDirectories(rootFilePath);

        for(String dir : IMG_SIZES){
            String imageFilePath = rootFilePath + File.separator + dir + File.separator + fileName;
            File imageFile = new File(imageFilePath);

            try{
                //imageFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                bitmap = scaleImageSize(dir, imageFilePath, photoWidth, photoHeight);
                Log.e(TAG, "Image Path >>>> " + imageFilePath);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    public static Bitmap scaleImageSize(String subFolder, String imagePath, int photoW, int photoH){
        int desireWidth = Integer.parseInt(subFolder.split("x")[0]);
        int desireHeight = Integer.parseInt(subFolder.split("x")[1]);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);

        int scaleFactor = Math.min(photoW/desireWidth, photoH/desireHeight);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        return bitmap;
    }
}
