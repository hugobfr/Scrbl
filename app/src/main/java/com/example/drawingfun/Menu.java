package com.example.drawingfun;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.media.Image;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by hugob on 23/11/2017.
 */

public class Menu
{

    private MenuClickListener menuClickListener;

    public ImageView importIcon, saveIcon, newIcon,
            brushIcon, paletteIcon, shareIcon,
            eraserIcon, positionIcon, textIcon,
            fileIcon, colorIcon, editIcon;

    public SubActionButton importButton, saveButton, newButton,
            brushButton, paletteButton, shareButton,
            eraserButton, positionButton, textButton;

    public FloatingActionButton colorActionButton, fileActionButton, editActionButton;

    public FloatingActionMenu colorActionMenu, fileActionMenu, editActionMenu;



    Menu(Activity activity)
    {
        menuClickListener = new MenuClickListener(this);
        //region SUPER MENU

        // Menu builder
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);

        //region MENUS BUTTONS
        //import photo button
        importIcon = new ImageView(activity.getApplicationContext());
        importIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_insert_photo_white_24dp));
        importButton = itemBuilder.setContentView(importIcon)
                .setTheme(1)
                .build();
        importButton.setId(R.id.import_btn);
        importButton.setOnClickListener(menuClickListener);

        //save button
        saveIcon = new ImageView(activity.getApplicationContext());
        saveIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_save_white_24dp));
        saveButton= itemBuilder.setContentView(saveIcon)
                .setTheme(1)
                .build();
        saveButton.setId(R.id.save_btn);
        saveButton.setOnClickListener(menuClickListener);

        //new button
        newIcon = new ImageView(activity.getApplicationContext());
        newIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_delete_sweep_white_24dp) );
        newButton = itemBuilder.setContentView(newIcon)
                .setTheme(1)
                .build();
        newButton.setId(R.id.new_btn);
        newButton.setOnClickListener(menuClickListener);

        //layers button
        positionIcon = new ImageView(activity.getApplicationContext());

        positionIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_gps_not_fixed_white_24dp));
        positionButton = itemBuilder.setContentView(positionIcon)
                .setTheme(1)
                .build();
        positionButton.setId(R.id.position_btn);
        positionButton.setOnClickListener(menuClickListener);

        //palette button
        paletteIcon = new ImageView(activity.getApplicationContext());
        paletteIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_lens_black_24dp));
        paletteIcon.getDrawable().setColorFilter(0xff000000, PorterDuff.Mode.SRC_IN);
        paletteButton = itemBuilder.setContentView(paletteIcon)
                .setTheme(1)
                .build();
        paletteButton.setId(R.id.palette_btn);
        paletteButton.setOnClickListener(menuClickListener);

        //share button
        shareIcon = new ImageView(activity.getApplicationContext());
        shareIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_share_white_24dp));
        shareButton = itemBuilder.setContentView(shareIcon)
                .setTheme(1)
                .build();
        shareButton.setId(R.id.share_btn);
        shareButton.setOnClickListener(menuClickListener);

        //brush button
        brushIcon = new ImageView(activity.getApplicationContext());
        brushIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_brush_white_24dp));
        brushButton = itemBuilder.setContentView(brushIcon)
                .setTheme(1)
                .build();
        brushButton.setId(R.id.draw_btn);
        brushButton.setOnClickListener(menuClickListener);

        //eraser button
        eraserIcon = new ImageView(activity.getApplicationContext());
        eraserIcon.setImageDrawable(activity.getDrawable(R.drawable.eraser_variant));
        eraserButton = itemBuilder.setContentView(eraserIcon)
                .setTheme(1)
                .build();
        eraserButton.setId(R.id.erase_btn);
        eraserButton.setOnClickListener(menuClickListener);

        //text button
        textIcon = new ImageView(activity.getApplicationContext());
        textIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_text_fields_white_24dp));
        textButton = itemBuilder.setContentView(textIcon)
                .setTheme(1)
                .build();
        textButton.setId(R.id.text_btn);
        textButton.setOnClickListener(menuClickListener);
        //endregion

        //region FILE MENU
        // Menu icon
        fileIcon = new ImageView(activity.getApplicationContext()); // Create an icon
        fileIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_insert_drive_file_white_24dp));

        fileActionButton = new FloatingActionButton.Builder(activity)
                .setContentView(fileIcon)
                .setTheme(1)
                .setPosition(6)
                .build();
        fileActionButton.setOnLongClickListener(menuClickListener);

        // Custom menu
         fileActionMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(newButton)
                .addSubActionView(shareButton)
                .addSubActionView(saveButton)
                .setStartAngle(0)
                .setEndAngle(-90)
                // ...
                .attachTo(fileActionButton)
                .build();
        //endregion

        //region COLOR MENU
        // Menu icon
        colorIcon = new ImageView(activity.getApplicationContext()); // Create an icon
        colorIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_format_paint_white_24dp));

        colorActionButton = new FloatingActionButton.Builder(activity)
                .setContentView(colorIcon)
                .setTheme(1)
                .setPosition(5)
                .build();
        colorActionButton.setOnLongClickListener(menuClickListener);

        // Custom menu
         colorActionMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(paletteButton)
                .addSubActionView(eraserButton)
                .addSubActionView(brushButton)
                .setStartAngle(240)
                .setEndAngle(300)
                // ...
                .attachTo(colorActionButton)
                .build();
        //endregion

        //region EDIT MENU
        // Menu icon
        editIcon = new ImageView(activity.getApplicationContext()); // Create an icon
        editIcon.setImageDrawable(activity.getDrawable(R.drawable.ic_mode_edit_white_24dp));

        editActionButton = new FloatingActionButton.Builder(activity)
                .setContentView(editIcon)
                .setTheme(1)
                .setPosition(4)
                .build();
        editActionButton.setOnLongClickListener(menuClickListener);

        // Custom menu
         editActionMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(textButton)
                .addSubActionView(positionButton)
                .addSubActionView(importButton)
                .setStartAngle(270)
                .setEndAngle(180)
                // ...
                .attachTo(editActionButton)
                .build();
        //endregion

        //endregion
    }
}
