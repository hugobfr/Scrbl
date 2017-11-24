package com.example.drawingfun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by hugob on 23/11/2017.
 */

public class Actions extends Activity
{
    private DrawingView drawView;
    private FrameLayout frameLayout;
    private ImageView imageView;
    private EditTextBackEvent editText;
    private float smallBrush, mediumBrush, largeBrush;


    Actions()
    {
        drawView = findViewById(R.id.drawing);
        frameLayout= findViewById(R.id.frame_layout);
        imageView = findViewById(R.id.photo_view);
        editText = findViewById(R.id.edit_text);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
    }

    public static void OpenCloseMenus(Menu menu)
    {
        if (menu.colorActionButton.isEnabled())
        {
            menu.colorActionMenu.close(true);
            menu.colorActionButton.setEnabled(false);
            menu.colorActionButton.setVisibility(View.GONE);
            menu.editActionMenu.close(true);
            menu.editActionButton.setEnabled(false);
            menu.editActionButton.setVisibility(View.GONE);
        }
        else
        {
            menu.colorActionButton.setEnabled(true);
            menu.colorActionButton.setVisibility(View.VISIBLE);
            menu.editActionButton.setEnabled(true);
            menu.editActionButton.setVisibility(View.VISIBLE);
        }
    }

    public void Draw()
    {
        //draw button clicked
        final Dialog brushDialog = new android.app.Dialog(getApplicationContext());
        brushDialog.setTitle("Brush size:");
        brushDialog.setContentView(R.layout.brush_chooser);
        //listen for clicks on size buttons
        ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(false);
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(false);
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });
        //show and wait for user interaction
        brushDialog.show();
    }

    public void Erase()
    {
        //switch to erase - choose size
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Eraser size:");
        brushDialog.setContentView(R.layout.brush_chooser);
        //size buttons
        ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }

    public void New()
    {
        //new button
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawView.startNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    public void Save()
    {
        //save drawing
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //save drawing
                frameLayout.setDrawingCacheEnabled(true);
                //attempt to save
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), frameLayout.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");
                //feedback
                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                drawView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    public void Import()
    {

    }

    public void Palette()
    {

    }

    public void Share()
    {

    }

    public void Text()
    {

    }

    public void Position()
    {

    }
}
