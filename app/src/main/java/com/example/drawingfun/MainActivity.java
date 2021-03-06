package com.example.drawingfun;

import java.io.FileNotFoundException;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainActivity extends Activity implements OnClickListener
{

	//custom drawing view
	private DrawingView drawView;
	private Uri bitmapUri;

	private FrameLayout frameLayout;
	private ImageView imageView;
	private EditTextBackEvent editText;
	private ImageButton takePhotoButton;
	//sizes
	private float smallBrush, mediumBrush, largeBrush;


	// Menu
	private ImageView paletteIcon, colorIcon, fileIcon;
 	private FloatingActionButton fileActionButton, colorActionButton, editActionButton;
 	private FloatingActionMenu fileActionMenu, colorActionMenu, editActionMenu;

 	private static final int REQUEST_IMAGE_CAPTURE = 1;
 	private static final int REQUEST_IMAGE_FROM_GALLERY = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
		imageView = (ImageView) findViewById(R.id.photo_view);
		drawView = (DrawingView)findViewById(R.id.drawing);
		editText = (EditTextBackEvent) findViewById(R.id.edit_text);

		drawView.setEditText(editText);

		drawView.requestFocus();
		//sizes from dimensions
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);

		//set initial size
		drawView.setBrushSize(smallBrush);

		takePhotoButton = (ImageButton) findViewById(R.id.takephoto_btn);
		takePhotoButton.setOnClickListener(this);
		//region SUPER MENU

		// Menu builder
		SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

		//region MENUS BUTTONS
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

		//layers button
		ImageView cleanIcon = new ImageView(this);

		cleanIcon.setImageDrawable(getDrawable(R.drawable.ic_format_color_reset_white_24dp));
		SubActionButton cleanButton = itemBuilder.setContentView(cleanIcon)
				.setTheme(1)
				.build();
		cleanButton.setId(R.id.clean_btn);
		cleanButton.setOnClickListener(this);

		//palette button
		paletteIcon = new ImageView(this);
		paletteIcon.setImageDrawable(getDrawable(R.drawable.ic_lens_black_24dp));
		paletteIcon.getDrawable().setColorFilter(0xff000000, PorterDuff.Mode.SRC_IN);
		SubActionButton paletteButton = itemBuilder.setContentView(paletteIcon)
				.setTheme(1)
				.build();
		paletteButton.setId(R.id.palette_btn);
		paletteButton.setOnClickListener(this);

		//share button
		ImageView shareIcon = new ImageView(this);
		shareIcon.setImageDrawable(getDrawable(R.drawable.ic_share_white_24dp));
		SubActionButton shareButton = itemBuilder.setContentView(shareIcon)
				.setTheme(1)
				.build();
		shareButton.setId(R.id.share_btn);
		shareButton.setOnClickListener(this);

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
		fileIcon = new ImageView(this); // Create an icon
		fileIcon.setImageDrawable(getDrawable(R.drawable.ic_insert_drive_file_white_24dp));

		fileActionButton = new FloatingActionButton.Builder(this)
				.setContentView(fileIcon)
				.setTheme(1)
				.setPosition(6)
				.build();
		//fileActionButton.setOnLongClickListener(this);

		// Custom menu
		fileActionMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(newButton)
				.addSubActionView(shareButton)
				.addSubActionView(saveButton)
				.setStartAngle(0)
				.setEndAngle(-90)
				.setRadius(160)
				// ...
				.attachTo(fileActionButton)
				.build();
		//endregion

		//region COLOR MENU
		// Menu icon
		colorIcon = new ImageView(this); // Create an icon
		colorIcon.setImageDrawable(getDrawable(R.drawable.small));

		colorActionButton = new FloatingActionButton.Builder(this)
				.setContentView(colorIcon)
				.setTheme(1)
				.setPosition(5)
				.build();
		//colorActionButton.setId(R.id.color_menu_btn);
		//colorActionButton.setOnLongClickListener(this);


		// Custom menu
		colorActionMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(paletteButton)
				.addSubActionView(eraserButton)
				.addSubActionView(brushButton)
				.setStartAngle(225)
				.setEndAngle(315)
				.setRadius(160)
				// ...
				.attachTo(colorActionButton)
				.build();

		//endregion

		//region EDIT MENU
		// Menu icon
		ImageView editIcon = new ImageView(this); // Create an icon
		editIcon.setImageDrawable(getDrawable(R.drawable.ic_add_white_24dp));

		editActionButton = new FloatingActionButton.Builder(this)
				.setContentView(editIcon)
				.setTheme(1)
				.setPosition(4)
				.build();
		//editActionButton.setOnLongClickListener(this);

		// Custom menu
		editActionMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(textButton)
				.addSubActionView(cleanButton)
				.addSubActionView(importButton)
				.setStartAngle(270)
				.setEndAngle(180)
				.setRadius(160)
				// ...
				.attachTo(editActionButton)
				.build();

		//endregion

		//endregion
	}

	@Override
	public void onClick(View view){
		//region NOT INTERESTING
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
					drawView.setLastBrushSize(largeBrush);
					colorIcon.setImageDrawable(getDrawable(R.drawable.small));
					colorIcon.getDrawable().setColorFilter(drawView.getPaintColor(), PorterDuff.Mode.SRC_IN);
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
					drawView.setLastBrushSize(largeBrush);
					colorIcon.setImageDrawable(getDrawable(R.drawable.medium));
					colorIcon.getDrawable().setColorFilter(drawView.getPaintColor(), PorterDuff.Mode.SRC_IN);
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
					colorIcon.setImageDrawable(getDrawable(R.drawable.large));
					colorIcon.getDrawable().setColorFilter(drawView.getPaintColor(), PorterDuff.Mode.SRC_IN);
					brushDialog.dismiss();
				}
			});
			colorActionMenu.close(true);
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
					drawView.setLastBrushSize(largeBrush);
					drawView.setBrushSize(smallBrush);
					colorIcon.setImageDrawable(getDrawable(R.drawable.eraser_variant));
					colorIcon.getDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_IN);
					brushDialog.dismiss();
				}
			});
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(mediumBrush);
					colorIcon.setImageDrawable(getDrawable(R.drawable.eraser_variant));
					colorIcon.getDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_IN);
					brushDialog.dismiss();
				}
			});
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(largeBrush);
					colorIcon.setImageDrawable(getDrawable(R.drawable.eraser_variant));
					colorIcon.getDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_IN);
					brushDialog.dismiss();
				}
			});
			colorActionMenu.close(true);
			brushDialog.show();

		}
		else if(view.getId()==R.id.new_btn){
			//new button
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
			newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					imageView.setImageDrawable(null);
					drawView.startNew();
					editText.setText(null);

					dialog.dismiss();
				}
			});
			newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					dialog.cancel();
				}
			});
			fileActionMenu.close(true);
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
			fileActionMenu.close(true);
			saveDialog.show();
		}
		//endregion
		//region opacity
		/*
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
		*/
		//endregion

		else if (view.getId() == R.id.import_btn)
		{
			getPicture(view);
			editActionMenu.close(false);
		}
		else if (view.getId() == R.id.palette_btn)
		{
			paintClicked(view);
			colorActionMenu.close(true);
		}
		else if (view.getId() == R.id.share_btn)
		{
			//save drawing
			frameLayout.setDrawingCacheEnabled(true);
			Bitmap bitmap = frameLayout.getDrawingCache();
			String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),
					 bitmap,
					"Drawing",
					null);
			Uri bitmapUri = Uri.parse(bitmapPath);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
			intent.setType("image/*");
			startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
			frameLayout.destroyDrawingCache();
		}
		else if (view.getId() == R.id.text_btn)
		{
			Log.d("text_button", editText.toString());
			editText.setVisibility(View.VISIBLE);
			editText.setFocusable(true);
			editText.requestFocus();
			editText.setTextColor(drawView.getPaintColor());
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
		}
		else if (view.getId() == R.id.clean_btn)
		{
				drawView.startNew();
		}

		else if (view.getId() == R.id.takephoto_btn)
		{
			dispatchTakePictureIntent();
		}
		else if (view.getId() == R.id.file_menu_btn)
		{
			if (colorActionMenu.isOpen())
				colorActionButton.performClick();

			if (editActionMenu.isOpen())
				editActionButton.performClick();
		}

		else if (view.getId() == R.id.color_menu_btn)
		{

				fileActionButton.performClick();

			if (editActionMenu.isOpen())
				editActionButton.performClick();
		}

		else if (view.getId() == R.id.edit_menu_btn)
		{
			if (colorActionMenu.isOpen())
				colorActionButton.performClick();

			if (fileActionMenu.isOpen())
				fileActionButton.performClick();
		}
	}



	//user clicked paint
	public void paintClicked(View view)
	{
		//use chosen color

		//set erase false
		drawView.setErase(false);
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
						colorIcon.setImageDrawable(getDrawable(R.drawable.medium));
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
		startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_IMAGE_FROM_GALLERY &&  resultCode == RESULT_OK)
		{
			Uri targetUri = data.getData();
			Bitmap bitmap;
			try
			{
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
				if (bitmap.getWidth() > bitmap.getHeight())
				{
					Matrix matrix = new Matrix();
					matrix.postRotate(90);
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),true);
					bitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
				}
				imageView.setImageBitmap(bitmap);
				imageView.invalidate();

				drawView.setBackgroundColor(0x00000000);
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
		{
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			imageView.setImageBitmap(imageBitmap);
			imageView.invalidate();
			drawView.setBackgroundColor(0x00000000);
		}

	}

	public void closeOpenMenus()
	{
		if (colorActionButton.isEnabled())
		{
			colorActionMenu.close(true);
			colorActionButton.setEnabled(false);
			colorActionButton.setVisibility(View.GONE);
			editActionMenu.close(true);
			editActionButton.setEnabled(false);
			editActionButton.setVisibility(View.GONE);
			fileActionMenu.close(true);
			fileActionButton.setEnabled(false);
			fileActionButton.setVisibility(View.GONE);
			takePhotoButton.setVisibility(View.GONE);
		}
		else
		{
			colorActionButton.setEnabled(true);
			colorActionButton.setVisibility(View.VISIBLE);
			editActionButton.setEnabled(true);
			editActionButton.setVisibility(View.VISIBLE);
			fileActionButton.setEnabled(true);
			fileActionButton.setVisibility(View.VISIBLE);
			takePhotoButton.setVisibility(View.VISIBLE);
		}
	}
}
