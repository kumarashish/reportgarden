package reportgarden.view;

import android.content.Intent;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import reportgarden.toppackapp.R;

public class Splash extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!


    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */

        //Customize Circular Reveal
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        configSplash.setBackgroundColor(R.color.colorAccent); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.logo_new); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
        //configSplash.setPathSplash(SyncStateContract.Constants.DROID_LOGO); //set path String
        configSplash.setOriginalHeight(200); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(200); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.colorAccent); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("developed by ashish kumar");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(10f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {

        //transit to another activity here
        //or do whatever you want
        startActivity(new Intent(Splash.this, Login.class));
        finish();

    }
}
