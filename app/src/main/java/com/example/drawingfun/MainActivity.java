package com.example.drawingfun;

import java.io.FileNotFoundException;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * This is demo code to accompany the Mobiletuts+ tutorial series:
 * - Android SDK: Create a Drawing App
 * - extended for follow-up tutorials on using patterns and opacity
 * 
 * Sue Smith
 * August 2013 / September 2013
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	//custom drawing view
	private DrawingView drawView;

	//sizes
	private float smallBrush, mediumBrush, largeBrush;

	private ImageView paletteIcon, colorIcon;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//get drawing view
		drawView = (DrawingView)findViewById(R.id.drawing);

		//sizes from dimensions
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);

		//set initial size
		drawView.setBrushSize(mediumBrush);

		//set color filter

		// Menu
		SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

		//region NEW-IMPORT-SAVE
		//import photo button
		ImageView importIcon = new ImageView(this);
		importIcon.setImageDrawable(getDrawable(R.drawable.ic_insert_photo_white_24dp));
		SubActionButton importButton = itemBuilder.setContentView(importIcon)
				.setTheme(1)
				.build();
		importButton.setId(R.id.import_btn);
		importButton.setOnClickListener(this);

		//save button
		ImageView saveIcon = new ImageView(this);
		saveIcon.setImageDrawable(getDrawable(R.drawable.ic_save_white_24dp));
		SubActionButton saveButton= itemBuilder.setContentView(saveIcon)
				.setTheme(1)
				.build();
		saveButton.setId(R.id.save_btn);
		saveButton.setOnClickListener(this);

		//new button
		ImageView newIcon = new ImageView(this);
		newIcon.setImageDrawable(getDrawable(R.drawable.ic_delete_sweep_white_24dp) );
		SubActionButton newButton = itemBuilder.setContentView(newIcon)
				.setTheme(1)
				.build();
		newButton.setId(R.id.new_btn);
		newButton.setOnClickListener(this);
		//endregion

		//region LAYERS-PALETTE
		//layers button
		ImageView layersIcon = new ImageView(this);

		layersIcon.setImageDrawable(getDrawable(R.drawable.ic_layers_white_24dp));
		SubActionButton layersButton = itemBuilder.setContentView(layersIcon)
				.setTheme(1)
				.build();
		layersButton.setId(R.id.layers_btn);
		layersButton.setOnClickListener(this);

		//palette button
		paletteIcon = new ImageView(this);
		paletteIcon.setImageDrawable(getDrawable(R.drawable.ic_lens_black_24dp));
		paletteIcon.getDrawable().setColorFilter(0xff000000, PorterDuff.Mode.SRC_IN);
		SubActionButton paletteButton = itemBuilder.setContentView(paletteIcon)
				.setTheme(1)
				.build();
		paletteButton.setId(R.id.palette_btn);
		paletteButton.setOnClickListener(this);

		//endregion

		//region BRUSH-ERASE-TEXT
		//brush button
		ImageView brushIcon = new ImageView(this);
		brushIcon.setImageDrawable(getDrawable(R.drawable.ic_brush_white_24dp));
		SubActionButton brushButton = itemBuilder.setContentView(brushIcon)
				.setTheme(1)
				.build();
		brushButton.setId(R.id.draw_btn);
		brushButton.setOnClickListener(this);

		//eraser button
		ImageView eraserIcon = new ImageView(this);
		eraserIcon.setImageDrawable(getDrawable(R.drawable.eraser_variant));
		SubActionButton eraserButton = itemBuilder.setContentView(eraserIcon)
				.setTheme(1)
				.build();
		eraserButton.setId(R.id.erase_btn);
		eraserButton.setOnClickListener(this);

		//text button
		ImageView textIcon = new ImageView(this);
		textIcon.setImageDrawable(getDrawable(R.drawable.ic_text_fields_white_24dp));
		SubActionButton textButton = itemBuilder.setContentView(textIcon)
				.setTheme(1)
				.build();
		textButton.setId(R.id.text_btn);
		textButton.setOnClickListener(this);
		//endregion

		//region FILE MENU
		// Menu icon
		ImageView fileIcon = new ImageView(this); // Create an icon
		fileIcon.setImageDrawable(getDrawable(R.drawable.ic_insert_drive_file_white_24dp));

		FloatingActionButton fileActionButton = new FloatingActionButton.Builder(this)
				.setContentView(fileIcon)
				.setTheme(1)
				.setPosition(6)
				.build();

		// Custom menu
		FloatingActionMenu fileActionMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(newButton)
				.addSubActionView(importButton)
				.addSubActionView(saveButton)
				.setStartAngle(0)
				.setEndAngle(-90)
				// ...
				.attachTo(fileActionButton)
				.build();
		//endregion

		//region COLOR MENU
		// Menu icon
		colorIcon = new ImageView(this); // Create an icon
		colorIcon.setImageDrawable(getDrawable(R.drawable.ic_format_paint_white_24dp));

		FloatingActionButton colorActionButton = new FloatingActionButton.Builder(this)
				.setContentView(colorIcon)
				.setTheme(1)
				.setPosition(5)
				.build();

		// Custom menu
		FloatingActionMenu colorActionMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(paletteButton)
				.addSubActionView(layersButton)
				.setStartAngle(240)
				.setEndAngle(300)
				// ...
				.attachTo(colorActionButton)
				.build();
		//endregion

		//region EDIT MENU
		// Menu icon
		ImageView editIcon = new ImageView(this); // Create an icon
		editIcon.setImageDrawable(getDrawable(R.drawable.ic_mode_edit_white_24dp));

		FloatingActionButton editActionButton = new FloatingActionButton.Builder(this)
				.setContentView(editIcon)
				.setTheme(1)
				.setPosition(4)
				.build();

		// Custom menu
		FloatingActionMenu editActionMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(textButton)
				.addSubActionView(brushButton)
				.addSubActionView(eraserButton)
				.setStartAngle(270)
				.setEndAngle(180)
				// ...
				.attachTo(editActionButton)
				.build();
		//endregion

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//user clicked paint
	public void paintClicked(View view){
		//use chosen color

		//set erase false
		drawView.setErase(false);
		drawView.setPaintAlpha(100);
		drawView.setBrushSize(drawView.getLastBrushSize());

		ColorPickerDialogBuilder
				.with(this)
				.setTitle("Choose color")
				.initialColor(Color.GREEN)
				.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
				.density(12)
				.setOnColorSelectedListener(new OnColorSelectedListener() {
					@Override
					public void onColorSelected(int selectedColor) {
						//toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
					}
				})
				.setPositiveButton("ok", new ColorPickerClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
						Log.d("Color", Integer.toHexString(selectedColor));
						drawView.setColor("#" + Integer.toHexString(selectedColor));
						paletteIcon.getDrawable().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
						colorIcon.getDrawable().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
					}
				})
				.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.build()
				.show();
	}


	public void getPicture(View view)
	{
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK)
		{
			Uri targetUri = data.getData();
			Bitmap bitmap;
			try
			{
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
				drawView.setPhoto(bitmap);
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View view){
		//region notInteresting
		if(view.getId()==R.id.draw_btn){
			//draw button clicked
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			//listen for clicks on size buttons
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(false);
					drawView.setBrushSize(smallBrush);
					drawView.setLastBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(false);
					drawView.setBrushSize(mediumBrush);
					drawView.setLastBrushSize(mediumBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
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
		else if(view.getId()==R.id.erase_btn){
			//switch to erase - choose size
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Eraser size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			//size buttons
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(mediumBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(largeBrush);
					brushDialog.dismiss();
				}
			});
			brushDialog.show();
		}
		else if(view.getId()==R.id.new_btn){
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
		else if(view.getId()==R.id.save_btn){
			//save drawing
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					//save drawing
					drawView.setDrawingCacheEnabled(true);
					//attempt to save
					String imgSaved = MediaStore.Images.Media.insertImage(
							getContentResolver(), drawView.getDrawingCache(),
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
		else if(view.getId()==R.id.opacity_btn){
			//launch opacity chooser
			final Dialog seekDialog = new Dialog(this);
			seekDialog.setTitle("Opacity level:");
			seekDialog.setContentView(R.layout.opacity_chooser);
			//get ui elements
			final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
			final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
			//set max
			seekOpq.setMax(100);
			//show current level
			int currLevel = drawView.getPaintAlpha();
			seekTxt.setText(currLevel+"%");
			seekOpq.setProgress(currLevel);
			//update as user interacts
			seekOpq.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					seekTxt.setText(Integer.toString(progress)+"%");
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {}

			});
			//listen for clicks on ok
			Button opqBtn = (Button)seekDialog.findViewById(R.id.opq_ok);
			opqBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setPaintAlpha(seekOpq.getProgress());
					seekDialog.dismiss();
				}
			});
			//show dialog
			seekDialog.show();
		}
		//endregion

		else if (view.getId() == R.id.import_btn)
		{
			getPicture(view);
		}
		else if (view.getId() == R.id.palette_btn)
		{
			paintClicked(view);
		}

	}

}
