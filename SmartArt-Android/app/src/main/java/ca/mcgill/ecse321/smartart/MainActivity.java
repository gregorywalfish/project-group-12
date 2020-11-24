package ca.mcgill.ecse321.smartart;

import android.os.Bundle;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.JsonStreamerEntity;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class MainActivity extends AppCompatActivity {
    private String error = null;
    private EditText localpostingID;
    private String title = "";
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        localpostingID = (EditText) findViewById(R.id.posting_id);          //goood stuff
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        refreshErrorMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewPostings(View v) {
        error = "";
       // final TextView tv = (TextView) findViewById(R.id.error);
        final TextView displayPostings = (TextView) findViewById(R.id.textViewPostings);
       // Intent intent = getIntent();
      //  token = intent.getStringExtra("token");

        displayPostings.setText("response");
        HttpUtils.get("postings", new RequestParams(), new JsonHttpResponseHandler() {
            private JSONArray response;
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                displayPostings.setText("");

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonobject = response.getJSONObject(i);
                     //   String artistName = jsonobject.getString("artistName");
                     //   String artistEmail = jsonobject.getString("artistEmail");
                       // String artist = jsonobject.getString("artist");
                        String title = jsonobject.getString("title");
                        String description = jsonobject.getString("description");
                    //    displayPostings.append(artistName + "   ");
                        displayPostings.append(title + "   ");
                        displayPostings.append(" Description:   " + description + "\n");



                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }



                }



            }



            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }


        });
// ...

    }

    public void getPostingName(View v) {
        error = "";
        String postingID = localpostingID.getText().toString();
     //   final EditText tv = (EditText) findViewById(R.id.posting_id);
        final TextView postingName = (TextView) findViewById(R.id.postID);
       // String number = "1126284095"; 1619110225
        RequestParams rp = new RequestParams();
    //    rp.add("postingID", postingID);
        HttpUtils.get("postings/"+ postingID,rp, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                postingName.setText("");
                try {
                    System.out.println(title);
                    title= response.getString("title");
                    postingName.append(" name is:   "+title);
             //       System.out.println(title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
    }
    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }
}