package com.example.drawingfun;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by hugob on 23/11/2017.
 */

public class MenuClickListener implements View.OnClickListener, View.OnLongClickListener
{
    private Menu menu;
    private Actions actions;


    MenuClickListener(Menu menu1)
    {
        menu = menu1;
        actions = new Actions();
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.import_btn:
                break;

            case R.id.save_btn:
                menu.fileActionMenu.close(true);
                break;

            case R.id.new_btn:
                menu.fileActionMenu.close(true);
                break;

            case R.id.draw_btn:

                menu.colorActionMenu.close(true);
                break;

            case R.id.palette_btn:
                break;

            case R.id.share_btn:
                break;

            case R.id.erase_btn:
                menu.colorActionMenu.close(true);
                break;

            //case R.id.position_btn:
              //  break;

            case R.id.text_btn:
                break;
        }
    }

    @Override
    public boolean onLongClick(View view)
    {
        Actions.OpenCloseMenus(menu);
        return true;
    }
}
