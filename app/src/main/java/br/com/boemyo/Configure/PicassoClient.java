package br.com.boemyo.Configure;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 05/11/2017.
 */

public class PicassoClient {

    public static void downloadImage(Context context, String url, ImageView imageView){

        if(url != null ){
            Picasso.get().load(url).into(imageView);
            //Picasso.with(context).load(url).into(imageView);

        }else{

            Picasso.get().load(R.drawable.food_off_boemyo).placeholder(R.color.colorSecondaryText).into(imageView);

        }

    }

}
