package com.landspecpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {

	private WebView webview;
	
	// For file uploading
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if (requestCode == FILECHOOSER_RESULTCODE){
			if (null == mUploadMessage) return;
			
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.main_webview);
		
		webview = (WebView)findViewById(R.id.webview);
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		final ProgressBar Pbar;
		Pbar = (ProgressBar) findViewById(R.id.progressbar);
		 
		final Activity activity = this;
		
		// Android 4.1+
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			webview.setWebChromeClient(new WebChromeClient() {
				
				public void onProgressChanged(WebView view, int progress) {
	               if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
	                   Pbar.setVisibility(ProgressBar.VISIBLE);
	                   //txtview.setVisibility(View.VISIBLE);
	               }
	               Pbar.setProgress(progress);
	               if(progress == 100) {
	                   Pbar.setVisibility(ProgressBar.GONE);
	                  //txtview.setVisibility(View.GONE);
	               }
				}
				
				// File Chooser code
				@SuppressWarnings("unused")
				public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType, String capture ){
					mUploadMessage = uploadMsg;
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.addCategory(Intent.CATEGORY_OPENABLE);
					i.setType("image/*");
					MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
				}
			});
		}
		
		// Android 3.0 - 4.0
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			webview.setWebChromeClient(new WebChromeClient() {
				
				public void onProgressChanged(WebView view, int progress) {
	               if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
	                   Pbar.setVisibility(ProgressBar.VISIBLE);
	                   //txtview.setVisibility(View.VISIBLE);
	               }
	               Pbar.setProgress(progress);
	               if(progress == 100) {
	                   Pbar.setVisibility(ProgressBar.GONE);
	                  //txtview.setVisibility(View.GONE);
	               }
				}
				
				// File Chooser code
				@SuppressWarnings("unused")
				public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType ){
					mUploadMessage = uploadMsg;
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.addCategory(Intent.CATEGORY_OPENABLE);
					i.setType("image/*");
					MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
				}
			});
		}
		
		// Android 2.x
		else{
			webview.setWebChromeClient(new WebChromeClient() {
			
				public void onProgressChanged(WebView view, int progress) {
	               if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
	                   Pbar.setVisibility(ProgressBar.VISIBLE);
	                   //txtview.setVisibility(View.VISIBLE);
	               }
	               Pbar.setProgress(progress);
	               if(progress == 100) {
	                   Pbar.setVisibility(ProgressBar.GONE);
	                  //txtview.setVisibility(View.GONE);
	               }
				}
				
				// File Chooser code
				@SuppressWarnings("unused")
				public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType, String capture ){
					mUploadMessage = uploadMsg;
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.addCategory(Intent.CATEGORY_OPENABLE);
					i.setType("image/*");
					MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
				}
				
			});
		}
		
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
		});		
		
		// TODO: Need to test the action bar loading and not loading thing on android 2.3 and up through 4.4
		webview.loadUrl("http://www.landspecpro.com");
		
		webview.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				ActivityCompat.invalidateOptionsMenu(activity);
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (webview.getUrl() == null){
			menu.clear();
			getMenuInflater().inflate(R.menu.not_logged_in, menu);
			return true;
		}
		else{
			if ( webview.getUrl().equalsIgnoreCase("http://www.landspecpro.com") ||
					webview.getUrl().equalsIgnoreCase("http://www.landspecpro.com/home") ||
					webview.getUrl().equalsIgnoreCase("www.landspecpro.com") ||
					webview.getUrl().equalsIgnoreCase("www.landspecpro.com/home") ||
					webview.getUrl().equalsIgnoreCase("http://www.landspecpro.com/login") ||
					webview.getUrl().equalsIgnoreCase("www.landspecpro.com/login") ){
				menu.clear();
				getMenuInflater().inflate(R.menu.not_logged_in, menu);
				return true;
			} else {
				menu.clear();
				getMenuInflater().inflate(R.menu.main, menu);
				return true;
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_menu_logout:
			webview.loadUrl("http://www.landspecpro.com/logout");
			return true;
		case R.id.id_menu_refresh:
			webview.reload();
			return true;
		case R.id.id_menu_login:
			webview.loadUrl("http://www.landspecpro.com/login");
			return true;
		case R.id.id_menu_register:
			webview.loadUrl("http://www.landspecpro.com/register");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
            case KeyEvent.KEYCODE_BACK:
                if(webview.canGoBack() == true){
                	webview.goBack();
                }else{
                	new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit!")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();    
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();  
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
