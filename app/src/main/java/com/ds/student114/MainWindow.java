package com.ds.student114;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//Вызываем рисовалку для бокового меню
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

class MyWebViewClient3 extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("student-app") && !url.contains("upload")) {
            view.loadUrl(url);
            return false;
        } else {
            view.getContext().startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }
    }
}


public class MainWindow extends AppCompatActivity implements LocationListener {

    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    private final static int FILECHOOSER_RESULTCODE = 1;
    public String server = "http://student-app.ru/p/";
    ImageButton butnPhoto;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private Drawer result = null;
    private WebView mWebView;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_main);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        final ProgressBar Pbar;
        final FrameLayout Back;

        Back = (FrameLayout) findViewById(R.id.back);
        Pbar = (ProgressBar) findViewById(R.id.progressBar);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/ur.ttf");


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withTranslucentStatusBar(false)
                .withDrawerWidthDp(280)
                .withStatusBarColorRes(R.color.main_color)
                .withSliderBackgroundColorRes(R.color.main_color)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIcon(R.drawable.m_profile_s).withTypeface(typeface2).withName(R.string.side_nav_item_profile).withIdentifier(1),
                        new PrimaryDrawerItem().withIcon(R.drawable.m_friends_s).withTypeface(typeface2).withName(R.string.side_nav_item_friends).withIdentifier(2),
                        new PrimaryDrawerItem().withIcon(R.drawable.m_pm_s).withTypeface(typeface2).withName(R.string.side_nav_item_pm).withIdentifier(3),
                        new PrimaryDrawerItem().withIcon(R.drawable.m_chat_s).withTypeface(typeface2).withName(R.string.side_nav_item_chat).withIdentifier(4),
                        new PrimaryDrawerItem().withIcon(R.drawable.m_actions_s).withTypeface(typeface2).withName(R.string.side_nav_item_actions).withIdentifier(5),
                        new PrimaryDrawerItem().withIcon(R.drawable.m_help_s).withTypeface(typeface2).withName(R.string.side_nav_item_files).withIdentifier(6),
                        new PrimaryDrawerItem().withIcon(R.drawable.m_help_s).withTypeface(typeface2).withName(R.string.side_nav_item_map).withIdentifier(9),
                        new PrimaryDrawerItem().withIcon(R.drawable.m_options_s).withTypeface(typeface2).withName(R.string.side_nav_item_settings).withIdentifier(7),
                        new PrimaryDrawerItem().withIcon(R.drawable.m_search_s).withTypeface(typeface2).withName(R.string.side_nav_item_search).withIdentifier(8)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        String url = "_";
                        String title = "";

                        if (drawerItem != null) {
                            url = null;
                            final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ur.ttf");
                            final TextView header = (TextView) findViewById(R.id.toolbar_text);

                            final ImageButton head = (ImageButton) findViewById(R.id.head_logo);

                            if (drawerItem.getIdentifier() == 1) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.setWebViewClient(new MyWebViewClient3());
                                mWebView.loadUrl(server + "profile/index.php");
                                mWebView.setWebChromeClient(new WebChromeClient() {
                                    public static final String TAG = "";

                                    public void onProgressChanged(WebView view, int progress) {
                                        if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                                            Back.setVisibility(FrameLayout.VISIBLE);
                                            Pbar.setVisibility(ProgressBar.VISIBLE);
                                        }
                                        Pbar.setProgress(progress);
                                        if (progress == 100) {
                                            Pbar.setVisibility(ProgressBar.GONE);
                                            Back.setVisibility(FrameLayout.GONE);
                                        }
                                    }

                                    // page loading progress, gone when fully loaded

                                    // for Lollipop, all in one
                                    public boolean onShowFileChooser(
                                            WebView webView, ValueCallback<Uri[]> filePathCallback,
                                            FileChooserParams fileChooserParams) {
                                        if (mFilePathCallback != null) {
                                            mFilePathCallback.onReceiveValue(null);
                                        }
                                        mFilePathCallback = filePathCallback;

                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                                            // create the file where the photo should go
                                            File photoFile = null;
                                            try {
                                                photoFile = createImageFile();
                                                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                                            } catch (IOException ex) {
                                                // Error occurred while creating the File
                                                Log.e(TAG, "Unable to create Image File", ex);
                                            }

                                            // continue only if the file was successfully created
                                            if (photoFile != null) {
                                                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                        Uri.fromFile(photoFile));
                                            } else {
                                                takePictureIntent = null;
                                            }
                                        }

                                        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                        contentSelectionIntent.setType("image/*");

                                        Intent[] intentArray;
                                        if (takePictureIntent != null) {
                                            intentArray = new Intent[]{takePictureIntent};
                                        } else {
                                            intentArray = new Intent[0];
                                        }

                                        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                                        chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser));
                                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                                        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                                        return true;
                                    }

                                    // creating image files (Lollipop only)
                                    private File createImageFile() throws IOException {

                                        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                                        if (!imageStorageDir.exists()) {
                                            imageStorageDir.mkdirs();
                                        }

                                        // create an image file name
                                        imageStorageDir = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                        return imageStorageDir;
                                    }

                                    // openFileChooser for Android 3.0+
                                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                                        mUploadMessage = uploadMsg;

                                        try {
                                            File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                                            if (!imageStorageDir.exists()) {
                                                imageStorageDir.mkdirs();
                                            }

                                            File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                                            mCapturedImageURI = Uri.fromFile(file); // save to the private variable

                                            final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                                            // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                            i.addCategory(Intent.CATEGORY_OPENABLE);
                                            i.setType("image/*");

                                            Intent chooserIntent = Intent.createChooser(i, getString(R.string.image_chooser));
                                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                                            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                                        } catch (Exception e) {
                                            Toast.makeText(getBaseContext(), "Camera Exception:" + e, Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    // openFileChooser for Android < 3.0
                                    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                                        openFileChooser(uploadMsg, "");
                                    }

                                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                                        openFileChooser(uploadMsg, acceptType);
                                    }

                                });

                                header.setText("Мой профиль");
                                header.setTypeface(typeface);
                                head.setImageResource(R.drawable.m_profile_s);
                                butnPhoto = (ImageButton) findViewById(R.id.head_logo);

                                View.OnClickListener onPhotoClick = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mWebView = (WebView) findViewById(R.id.chat_webView5);
                                        mWebView.getSettings().setJavaScriptEnabled(true);
                                        mWebView.getSettings().setAllowFileAccess(true);
                                        mWebView.loadUrl(server + "profile/add-avatar.php");
                                        mWebView.setWebViewClient(new MyWebViewClient3());
                                        mWebView.setWebChromeClient(new WebChromeClient() {
                                            public static final String TAG = "";

                                            public void onProgressChanged(WebView view, int progress) {
                                                if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                                                    Back.setVisibility(FrameLayout.VISIBLE);
                                                    Pbar.setVisibility(ProgressBar.VISIBLE);
                                                }
                                                Pbar.setProgress(progress);
                                                if (progress == 100) {
                                                    Pbar.setVisibility(ProgressBar.GONE);
                                                    Back.setVisibility(FrameLayout.GONE);
                                                }
                                            }

                                            // page loading progress, gone when fully loaded

                                            // for Lollipop, all in one
                                            public boolean onShowFileChooser(
                                                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                                                    FileChooserParams fileChooserParams) {
                                                if (mFilePathCallback != null) {
                                                    mFilePathCallback.onReceiveValue(null);
                                                }
                                                mFilePathCallback = filePathCallback;

                                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                                                    // create the file where the photo should go
                                                    File photoFile = null;
                                                    try {
                                                        photoFile = createImageFile();
                                                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                                                    } catch (IOException ex) {
                                                        // Error occurred while creating the File
                                                        Log.e(TAG, "Unable to create Image File", ex);
                                                    }

                                                    // continue only if the file was successfully created
                                                    if (photoFile != null) {
                                                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                                Uri.fromFile(photoFile));
                                                    } else {
                                                        takePictureIntent = null;
                                                    }
                                                }

                                                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                                contentSelectionIntent.setType("image/*");

                                                Intent[] intentArray;
                                                if (takePictureIntent != null) {
                                                    intentArray = new Intent[]{takePictureIntent};
                                                } else {
                                                    intentArray = new Intent[0];
                                                }

                                                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                                                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                                                chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser));
                                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                                                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                                                return true;
                                            }

                                            // creating image files (Lollipop only)
                                            private File createImageFile() throws IOException {

                                                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                                                if (!imageStorageDir.exists()) {
                                                    imageStorageDir.mkdirs();
                                                }

                                                // create an image file name
                                                imageStorageDir = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                                return imageStorageDir;
                                            }

                                            // openFileChooser for Android 3.0+
                                            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                                                mUploadMessage = uploadMsg;

                                                try {
                                                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                                                    if (!imageStorageDir.exists()) {
                                                        imageStorageDir.mkdirs();
                                                    }

                                                    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                                                    mCapturedImageURI = Uri.fromFile(file); // save to the private variable

                                                    final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                                                    // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                                                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                                    i.addCategory(Intent.CATEGORY_OPENABLE);
                                                    i.setType("image/*");

                                                    Intent chooserIntent = Intent.createChooser(i, getString(R.string.image_chooser));
                                                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                                                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                                                } catch (Exception e) {
                                                    Toast.makeText(getBaseContext(), "Camera Exception:" + e, Toast.LENGTH_LONG).show();
                                                }

                                            }

                                            // openFileChooser for Android < 3.0
                                            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                                                openFileChooser(uploadMsg, "");
                                            }

                                            // openFileChooser for other Android versions
            /* may not work on KitKat due to lack of implementation of openFileChooser() or onShowFileChooser()
               https://code.google.com/p/android/issues/detail?id=62220
               however newer versions of KitKat fixed it on some devices */
                                            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                                                openFileChooser(uploadMsg, acceptType);
                                            }

                                        });

                                    }
                                };
                                butnPhoto.setOnClickListener(onPhotoClick);
                            } else if (drawerItem.getIdentifier() == 2) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.setWebViewClient(new MyWebViewClient3());
                                mWebView.loadUrl(server + "friends/index.php");
                                header.setText("Друзья");
                                header.setTypeface(typeface);
                                head.setImageResource(R.drawable.m_friends_s);

                            } else if (drawerItem.getIdentifier() == 3) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.loadUrl(server + "pm/index.php");
                                mWebView.setWebViewClient(new MyWebViewClient3() {
                                    public void onPageFinished(WebView view, String url) {
                                        String avatarSrc = mWebView.getTitle();
                                        Picasso.with(MainWindow.this).load(avatarSrc).resize(60, 60).centerCrop().into(head);
                                    }
                                });
                                header.setText("Личные сообщения");
                                header.setTypeface(typeface);
                                head.setImageResource(R.drawable.m_pm_s);

                            } else if (drawerItem.getIdentifier() == 4) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.setWebViewClient(new MyWebViewClient3());
                                mWebView.loadUrl("http://student-app.ru/cometchat/extensions/mobilewebapp/");
                                header.setText("Чат");
                                header.setTypeface(typeface);
                                head.setImageResource(R.drawable.m_chat_s);

                            } else if (drawerItem.getIdentifier() == 5) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.setWebViewClient(new MyWebViewClient3());
                                mWebView.loadUrl(server + "events/index.php");
                                header.setText("Акции");
                                header.setTypeface(typeface);
                                head.setImageResource(R.drawable.m_actions_s);

                            } else if (drawerItem.getIdentifier() == 6) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.getSettings().setAllowContentAccess(true);
                                mWebView.getSettings().setAllowFileAccess(true);
                                mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
                                mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                                mWebView.loadUrl(server + "works/index.php");
                                mWebView.setWebViewClient(new MyWebViewClient3());
                                mWebView.setWebChromeClient(new WebChromeClient() {
                                    public static final String TAG = "";

                                    public void onProgressChanged(WebView view, int progress) {
                                        if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                                            Back.setVisibility(FrameLayout.VISIBLE);
                                            Pbar.setVisibility(ProgressBar.VISIBLE);
                                        }
                                        Pbar.setProgress(progress);
                                        if (progress == 100) {
                                            Pbar.setVisibility(ProgressBar.GONE);
                                            Back.setVisibility(FrameLayout.GONE);
                                        }
                                    }

                                    // page loading progress, gone when fully loaded

                                    // for Lollipop, all in one
                                    public boolean onShowFileChooser(
                                            WebView webView, ValueCallback<Uri[]> filePathCallback,
                                            FileChooserParams fileChooserParams) {
                                        if (mFilePathCallback != null) {
                                            mFilePathCallback.onReceiveValue(null);
                                        }
                                        mFilePathCallback = filePathCallback;

                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                                            // create the file where the photo should go
                                            File photoFile = null;
                                            try {
                                                photoFile = createImageFile();
                                                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                                            } catch (IOException ex) {
                                                // Error occurred while creating the File
                                                Log.e(TAG, "Unable to create Image File", ex);
                                            }

                                            // continue only if the file was successfully created
                                            if (photoFile != null) {
                                                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                        Uri.fromFile(photoFile));
                                            } else {
                                                takePictureIntent = null;
                                            }
                                        }

                                        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                        contentSelectionIntent.setType("image/*");

                                        Intent[] intentArray;
                                        if (takePictureIntent != null) {
                                            intentArray = new Intent[]{takePictureIntent};
                                        } else {
                                            intentArray = new Intent[0];
                                        }

                                        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                                        chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser));
                                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                                        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                                        return true;
                                    }

                                    // creating image files (Lollipop only)
                                    private File createImageFile() throws IOException {

                                        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                                        if (!imageStorageDir.exists()) {
                                            imageStorageDir.mkdirs();
                                        }

                                        // create an image file name
                                        imageStorageDir = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                        return imageStorageDir;
                                    }

                                    // openFileChooser for Android 3.0+
                                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                                        mUploadMessage = uploadMsg;

                                        try {
                                            File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                                            if (!imageStorageDir.exists()) {
                                                imageStorageDir.mkdirs();
                                            }

                                            File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                                            mCapturedImageURI = Uri.fromFile(file); // save to the private variable

                                            final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                                            // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                            i.addCategory(Intent.CATEGORY_OPENABLE);
                                            i.setType("image/*");

                                            Intent chooserIntent = Intent.createChooser(i, getString(R.string.image_chooser));
                                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                                            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                                        } catch (Exception e) {
                                            Toast.makeText(getBaseContext(), "Camera Exception:" + e, Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    // openFileChooser for Android < 3.0
                                    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                                        openFileChooser(uploadMsg, "");
                                    }

                                    // openFileChooser for other Android versions
            /* may not work on KitKat due to lack of implementation of openFileChooser() or onShowFileChooser()
               https://code.google.com/p/android/issues/detail?id=62220
               however newer versions of KitKat fixed it on some devices */
                                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                                        openFileChooser(uploadMsg, acceptType);
                                    }
                                });
                                mWebView.setDownloadListener(new DownloadListener() {
                                    public void onDownloadStart(String url, String userAgent,
                                                                String contentDisposition, String mimetype,
                                                                long contentLength) {
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                    }
                                });

                                header.setText("HELPer");
                                header.setTypeface(typeface);
                                head.setImageResource(R.drawable.m_help_s);

                            } else if (drawerItem.getIdentifier() == 7) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.loadUrl(server + "settings/index.php");
                                header.setText("Настройки");
                                header.setTypeface(typeface);
                                head.setImageResource(R.drawable.m_options_s);
                            } else if (drawerItem.getIdentifier() == 8) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.setWebViewClient(new MyWebViewClient3());
                                mWebView.loadUrl(server + "search/search-page.php");
                                header.setText("Поиск");
                                header.setTypeface(typeface);
                                head.setImageResource(R.drawable.m_search_s);
                            } else if (drawerItem.getIdentifier() == 9) {
                                mWebView = (WebView) findViewById(R.id.chat_webView5);
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.getSettings().setAllowContentAccess(true);
                                mWebView.getSettings().setAllowFileAccess(true);
                                mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
                                mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                                mWebView.loadUrl("http://student-app.ru/map.php");
                                mWebView.setWebViewClient(new MyWebViewClient3());
                                mWebView.setWebChromeClient(new WebChromeClient() {
                                    public void onProgressChanged(WebView view, int progress) {
                                        if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                                            Back.setVisibility(FrameLayout.VISIBLE);
                                            Pbar.setVisibility(ProgressBar.VISIBLE);
                                        }
                                        Pbar.setProgress(progress);
                                        if (progress == 100) {
                                            Pbar.setVisibility(ProgressBar.GONE);
                                            Back.setVisibility(FrameLayout.GONE);
                                        }
                                    }
                                });
                            }
                            if (url != null) {
                            }
                        }
                        return false;
                    }

                })

                .build();
        toolbar.setNavigationIcon(R.drawable.menu_icon_s);
        result.keyboardSupportEnabled(this, true);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ur.ttf");

        TextView header = (TextView) findViewById(R.id.toolbar_text);
        header.setText(getTitle());
        header.setTypeface(typeface);


        mWebView = (WebView) findViewById(R.id.chat_webView5);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient3());
        mWebView.loadUrl(server + "auth/index.php");
        mWebView.setWebChromeClient(new WebChromeClient() {
            public static final String TAG = "";

            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                    Back.setVisibility(FrameLayout.VISIBLE);
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                }
                Pbar.setProgress(progress);
                if (progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                    Back.setVisibility(FrameLayout.GONE);
                }
            }

            // page loading progress, gone when fully loaded

            // for Lollipop, all in one
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    // create the file where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // continue only if the file was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                return true;
            }

            // creating image files (Lollipop only)
            private File createImageFile() throws IOException {

                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs();
                }

                // create an image file name
                imageStorageDir = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                return imageStorageDir;
            }

            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;

                try {
                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }

                    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    mCapturedImageURI = Uri.fromFile(file); // save to the private variable

                    final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(i, getString(R.string.image_chooser));
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Camera Exception:" + e, Toast.LENGTH_LONG).show();
                }

            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

        });
        header.setText("Мой профиль");

        butnPhoto = (ImageButton) findViewById(R.id.head_logo);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // code for all versions except of Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e, Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }

        } // end of code for all versions except of Lollipop

        // start of code for Lollipop only
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != FILECHOOSER_RESULTCODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null || data.getData() == null) {
                    // if there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } // end of code for Lollipop only
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location){
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());

        URL urld;

        try{
            String strat = "http://student-app.ru/map-res.php?lat="+latitude+"&lng="+longitude;
            urld = new URL(strat);
            HttpURLConnection conn = (HttpURLConnection) urld.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i("Geo_Location", strat);
                }else{
                Log.i("Geo_Location", "FALSE");
            }
            //Log.i("Geo_Location", strat);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}