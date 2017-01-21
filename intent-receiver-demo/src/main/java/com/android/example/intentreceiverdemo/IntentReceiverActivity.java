package com.android.example.intentreceiverdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class IntentReceiverActivity extends AppCompatActivity {

    private static final String TAG = "IntentReceiverActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_receiver);

        TextView textView = (TextView) findViewById(R.id.textview_received_text);

        ShareCompat.IntentReader intentReader = ShareCompat.IntentReader.from(this);
        if (intentReader.isShareIntent()) {
            String type = intentReader.getType();
            if (type != null) {
                switch (type) {
                    case "text/plain":
                        CharSequence text = intentReader.getText();
                        textView.setText(text);
                        break;
                    case "text/html":
                        String[] emailTo = intentReader.getEmailTo();
                        String emailContent = intentReader.getHtmlText();
                        Spanned textContent = Html.fromHtml(emailContent);

                        if (emailTo != null) {
                            textView.append("To: ");
                            textView.append(emailTo[0]);
                        }
                        if (emailContent != null) {
                            textView.append(" Content: ");
                            textView.append(textContent);
                        }

                        break;
                    case "image/png":
                        Uri fileStream = intentReader.getStream();
                        Bitmap bitmap = null;
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(fileStream);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        ((ImageView) findViewById(R.id.imageview_received_image)).setImageBitmap(bitmap);
                        break;
                }
            }
        }
    }
}
