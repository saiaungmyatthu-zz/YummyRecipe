package com.mm.saiaumain.yummyrecipe;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mm.saiaumain.yummyrecipe.adapters.ItemAdapter;
import com.mm.saiaumain.yummyrecipe.adapters.RecipePagerAdapter;
import com.mm.saiaumain.yummyrecipe.utils.UIComponentUtils;
import com.mm.saiaumain.yummyrecipe.utils.YummyRecipeUtils;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 11/14/2017.
 */

public class NewRecipeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{

    private ImageButton back;
    private Button prev, next;
    private TextView label, totalTime;
    private TextInputEditText recipeName, prepareTime, cookingTime;
    private LinearLayout indicatorLayout, total;
    private ViewPager pager;
    protected View view;
    private ImageView[] indicators;
    private ImageView photo;
    private int indicatorCount;
    private RecipePagerAdapter adapter;
    private View itemView;
    private NumberPicker servingCount;
    private RecyclerView ingredients, directions;
    private ImageButton addItem, editItem;
    private EditText dynamicText;

    private int[] labels = new int[]{R.string.title_step_1,R.string.title_step_2,R.string.title_step_3,
            R.string.title_step_4,R.string.title_step_5,R.string.title_step_6};
    private int[] layouts = new int[]{R.layout.input_1, R.layout.input_2, R.layout.input_3,
            R.layout.input_4, R.layout.input_5, R.layout.input_5};
    private Recipe recipe = new Recipe();
    private List<String> ingridentsList = new ArrayList<>();
    private List<String> directionsList = new ArrayList<>();
    private ItemAdapter ingredientAdapter, directionAdapter;
    private int ingredientEditIndex, directionEditIndex;

    private final String[] PHOTO_PERMISSION = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String TAG = "Yummy-NewRecipe";
    private static final int PHOTO_PERMISSION_CODE = 1000, PHOTO_CAMERA = 1001, PHOTO_GALLERY = 1002;

    private void initViews(){
        back = (ImageButton) findViewById(R.id.btnBack);
        prev = (Button) findViewById(R.id.btnPrev);
        next = (Button) findViewById(R.id.btnNxt);
        label = (TextView) findViewById(R.id.label);
        label.setText(getString(labels[0]));
        indicatorLayout = (LinearLayout) findViewById(R.id.indicator);

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new RecipePagerAdapter(this, layouts);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(this);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        back.setOnClickListener(this);
        displayIndicators();
    }

    private void displayIndicators(){
        indicatorCount = adapter.getCount();
        indicators = new ImageView[indicatorCount];
        for (int i = 0; i < indicatorCount; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_item));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 0, 20, 0);
            indicatorLayout.addView(indicators[i], params);
        }
        indicators[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_item));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recipe_screen);
        initViews();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        label.setText(getString(labels[position]));
        Drawable drawable;
        for (int i = 0; i < indicatorCount; i++) {
            if(i < position || i == position)
                drawable = getResources().getDrawable(R.drawable.selected_item);
            else
                drawable = getResources().getDrawable(R.drawable.unselected_item);
            indicators[i].setImageDrawable(drawable);
        }
        indicators[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_item));
        prev.setVisibility(position == 0? View.INVISIBLE : View.VISIBLE);
        next.setText(position == indicators.length - 1? R.string.lbl_subm : R.string.lbl_next);

        itemView = pager.findViewWithTag("item_" + position);
        switch (position){
            case 0:
                addRecipeName(itemView);
                break;
            case 1:
                addImage(itemView);
                break;
            case 2:
                addTimeSetup(itemView);
                break;
            case 3:
                addServingCount(itemView);
                break;
            case 4:
                addDynamicItems(itemView, false);
                break;
            case 5:
                addDynamicItems(itemView, true);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PHOTO_PERMISSION_CODE:
                boolean isAllGranted = false;
                String permissionName = null;
                for(int i = 0; i < grantResults.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        isAllGranted = true;
                    }else{
                        isAllGranted = false;
                        break;
                    }
                }
                Log.e(TAG, "isAllGranted Inside onRequestPermissionsResult >>>> " + isAllGranted);
                if(isAllGranted){
                    doPhotoAction();
                }else{
                    Toast.makeText(getBaseContext(),"Unable to get Permission for " + permissionName, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNxt:
                pager.setCurrentItem((pager.getCurrentItem() < indicatorCount)
                        ? pager.getCurrentItem() + 1 : 0);
                if(next.getText().toString().equalsIgnoreCase("Submit")){
                    // do validation & save
                }
                break;
            case R.id.btnPrev:
                pager.setCurrentItem(pager.getCurrentItem() - 1);
                break;
            case R.id.btnBack:
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                break;
        }
    }

    private void addRecipeName(View view){
        recipeName = (TextInputEditText) view.findViewById(R.id.recipeName);
        String name = recipeName.getText().toString().trim();
        if(YummyRecipeUtils.isEmptyOrNull(name)){
            recipe.setName(name);
        }
    }

    private void addImage(View view){
        photo = (ImageView) view.findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!YummyRecipeUtils.hasPermissionSelfCheck(NewRecipeActivity.this, PHOTO_PERMISSION)){
                    Log.e(TAG, "App neet to grant Camera & Gallery permissions.");
                    if(YummyRecipeUtils.requiredPermissionExplanation(NewRecipeActivity.this, PHOTO_PERMISSION)){
                        UIComponentUtils.showPermissionPopup(NewRecipeActivity.this, getString(R.string.photo_permission_title),
                                getString(R.string.photo_permission_message), getDrawable(R.mipmap.ic_permission), PHOTO_PERMISSION_CODE, PHOTO_PERMISSION);
                    }
                }else{
                    Log.e(TAG, "App already have permission for Camera & Gallery.");
                    doPhotoAction();
                }
            }
        });
    }

    private void addTimeSetup(View view){
        prepareTime = (TextInputEditText) view.findViewById(R.id.pTime);
        prepareTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTimerPopup(getString(R.string.add_lbl_prepare_time), prepareTime);
            }
        });
        cookingTime = (TextInputEditText) view.findViewById(R.id.cTime);
        cookingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTimerPopup(getString(R.string.add_lbl_cooking_time), cookingTime);
            }
        });
        total = (LinearLayout) view.findViewById(R.id.total);
        totalTime = (TextView) view.findViewById(R.id.totalTime);
    }

    private void addServingCount(View view){
        servingCount = (NumberPicker) view.findViewById(R.id.servingCount);
        servingCount.setMaxValue(50);
        servingCount.setMinValue(1);
    }

    private void addDynamicItems(View view, final boolean isDirection){
        dynamicText = (EditText) view.findViewById(R.id.itemText);
        dynamicText.setHint(isDirection? getString(R.string.lbl_add_new_direction) : getString(R.string.lbl_add_new_ingredient));
        LinearLayoutManager layoutManager = new LinearLayoutManager(NewRecipeActivity.this,
                LinearLayoutManager.VERTICAL, false);

        if(!isDirection){
            ingredientAdapter = new ItemAdapter(NewRecipeActivity.this, ingridentsList, R.layout.dynamic_item,
                    new ItemAdapter.OnItemClickAction() {
                @Override
                public void onItemClickAction(String item, int position) {
                    if(!YummyRecipeUtils.isEmptyOrNull(item)) {
                        addItem.setVisibility(View.GONE);
                        editItem.setVisibility(View.VISIBLE);
                        dynamicText.setText(item.trim());
                        ingredientEditIndex = position;
                    }
                }
            });
            ingredients = (RecyclerView) view.findViewById(R.id.recycler);
            ingredients.setLayoutManager(layoutManager);
            ingredients.setItemAnimator(new DefaultItemAnimator());
            ingredients.setAdapter(ingredientAdapter);
        }else{
            directionAdapter = new ItemAdapter(NewRecipeActivity.this, directionsList, R.layout.dynamic_item,
                    new ItemAdapter.OnItemClickAction() {
                @Override
                public void onItemClickAction(String item, int position) {
                    if(!YummyRecipeUtils.isEmptyOrNull(item)) {
                        dynamicText.setText(item.trim());
                        directionEditIndex = position;
                    }
                }
            });
            directions = (RecyclerView) view.findViewById(R.id.recycler);
            directions.setLayoutManager(layoutManager);
            directions.setItemAnimator(new DefaultItemAnimator());
            directions.setAdapter(directionAdapter);
        }

        editItem = (ImageButton) view.findViewById(R.id.editItem);
        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemText = dynamicText.getText().toString().trim();
                String currentString = isDirection? directionsList.get(directionEditIndex) : ingridentsList.get(ingredientEditIndex);
                if(!YummyRecipeUtils.isEmptyOrNull(itemText) && !YummyRecipeUtils.isEmptyOrNull(currentString)){
                    UIComponentUtils.playSound(NewRecipeActivity.this, R.raw.item_add);
                    if(isDirection){
                        directionsList.remove(directionEditIndex);
                        directionsList.add(directionEditIndex, currentString);
                        directionAdapter.notifyItemChanged(directionEditIndex);
                    }else{
                        ingridentsList.remove(ingredientEditIndex);
                        ingridentsList.add(ingredientEditIndex, itemText);
                        ingredientAdapter.notifyItemChanged(ingredientEditIndex);
                    }
                    editItem.setVisibility(View.GONE);
                    addItem.setVisibility(View.VISIBLE);
                }
                dynamicText.setText("");
            }
        });

        addItem = (ImageButton) view.findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemText = dynamicText.getText().toString().trim();
                if(!YummyRecipeUtils.isEmptyOrNull(itemText)){
                    UIComponentUtils.playSound(NewRecipeActivity.this, R.raw.item_add);
                    if(isDirection){
                        directionsList.add(directionsList.size(), itemText);
                        directionAdapter.notifyItemInserted(directionsList.size());
                    }else{
                        ingridentsList.add(ingridentsList.size(), itemText);
                        ingredientAdapter.notifyItemInserted(ingridentsList.size());
                    }
                }
                dynamicText.setText("");
            }
        });
    }

    private void doPhotoAction(){
        final Dialog dialog = UIComponentUtils.getCustomDialog(NewRecipeActivity.this, R.layout.photo_chooser, getString(R.string.choose_take_photo), true);
        dialog.show();

        CheckedTextView camera = (CheckedTextView) dialog.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PHOTO_CAMERA);
            }
        });

        CheckedTextView gallery = (CheckedTextView) dialog.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PHOTO_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "Activity Result >>>>>> Request Code: " + requestCode + " Result Code: " + resultCode);
        if(requestCode == PHOTO_CAMERA && resultCode == RESULT_OK && null != data){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String fileName = YummyRecipeUtils.generateImageName(NewRecipeActivity.this);

            LinearLayout.LayoutParams photoParam = (LinearLayout.LayoutParams) photo.getLayoutParams();
            Bitmap scaledBitmap = YummyRecipeUtils.createImageFile(NewRecipeActivity.this, fileName,
                    400, 200, bitmap);

            if(null != scaledBitmap)
                photo.setImageBitmap(scaledBitmap);
            else
                photo.setImageBitmap(bitmap);
        }else if(requestCode == PHOTO_GALLERY && resultCode == RESULT_OK && null != data){
            try{
                Uri uri = data.getData();
                String[] file = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, file, null, null, null);
                cursor.moveToFirst();
                int fileIndex = cursor.getColumnIndex(file[0]);
                String imageDecode = cursor.getString(fileIndex);
                photo.setImageBitmap(BitmapFactory.decodeFile(imageDecode));
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Please Try Again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayTimerPopup(String title, final TextInputEditText editText){
        TimePickerPopup popup = new TimePickerPopup().createInstance(title, new TimePickerPopup.Callback() {
            @Override
            public void onTimeSelectResult(String value) {
                Log.e(TAG, "Selected value from callback is >>>>>> " + value);
                editText.setText(value);
            }
        });
        popup.setCancelable(false);
        popup.show(getFragmentManager(), "dialog");
    }
}
