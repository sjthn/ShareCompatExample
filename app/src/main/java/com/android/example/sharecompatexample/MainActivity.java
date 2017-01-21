package com.android.example.sharecompatexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.edittext_share_text);

        findViewById(R.id.button_share_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    shareText(text);
                } else {
                    Toast.makeText(MainActivity.this, "Content is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.button_share_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareEmail();
            }
        });

        findViewById(R.id.button_share_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageChooser();
            }
        });

    }

    private void shareText(String text) {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this);
        Intent intent = intentBuilder
                .setType("text/plain")
                .setText(text)
                .createChooserIntent();

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No client support this content", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareEmail() {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this);
        Intent intent = intentBuilder
                .setType("text/html")
                .setHtmlText("<p>This is a paragraph.</p>")
                .setChooserTitle("Choose email client")
                .createChooserIntent();

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No client support this content", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareFile(Uri imageUri) {
        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setType("image/png")
                .setStream(imageUri)
                .setChooserTitle("Choose image client")
                .getIntent();

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No client support this content", Toast.LENGTH_SHORT).show();
        }

    }

    private void startImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/png");
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri imageUri = data.getData();
                    shareFile(imageUri);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
