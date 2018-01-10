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

            Picasso.with(context).load(url).placeholder(R.drawable.food_off_boemyo).into(imageView);

        }else{

            Picasso.with(context).load(R.drawable.food_off_boemyo).into(imageView);

        }

    }

}
