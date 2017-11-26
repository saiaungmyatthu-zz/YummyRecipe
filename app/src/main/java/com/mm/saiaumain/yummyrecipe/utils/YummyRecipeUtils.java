package com.mm.saiaumain.yummyrecipe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 10/29/2017.
 */

public class YummyRecipeUtils {

    private static final String TAG = "Yummy-YummyRecipeUtils";

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

    public static File generateImageName(Context context) throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Yummy");
        if(!storageDir.exists())
            storageDir.mkdirs();
        String fileName = context.getString(R.string.app_name) + "_" + timeStamp;
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        return image;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int targetWidth, int targetHeight){
       int width = bitmap.getWidth(), height = bitmap.getHeight();
       float scaleWidth = ((float) targetWidth) / width;
       float scaleHeight = ((float) targetHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return resizedBitmap;
    }
}
