package com.project.studiopatyzinha;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.Adapter.ChatAdapter;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.MessageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Chat_activity extends AppCompatActivity {
    TextView userName_person_chat;
    public static Accountpattern pessoa;
    ImageView send, profile_image;
    public static ImageView takeapicture, foto_grande, imgcls_img_Chat_Main;
    EditText enterMessage;
    ChatAdapter chatAdapter;
    RecyclerView chatRecyclerView;
    MessageModel model;
    DatabaseReference reference;
    String senderRoom;
    ActivityResultLauncher<String> mGetContent;
    String receiveRoom;
    int position;
    String stringbitmap;
    public static LinearLayout linear;
    public static String receiveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                Intent intent = new Intent(Chat_activity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });
        new Thread(() -> {
            receiveId = getIntent().getStringExtra("userId");
            pessoa = Login.pessoa;
            final String senderId = pessoa.getId();
            senderRoom = senderId + receiveId;
            receiveRoom = receiveId + senderId;
            chatRecyclerView = findViewById(R.id.chatRecyclerView);
            reference = FirebaseDatabase.getInstance().getReference();
            final ArrayList<MessageModel> messageModels = new ArrayList<>();
            chatAdapter = new ChatAdapter(messageModels, this, receiveId);
            chatRecyclerView.setAdapter(chatAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            chatRecyclerView.setLayoutManager(llm);
            reference.child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messageModels.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        model = snapshot1.getValue(MessageModel.class);
                        model.setMessageId(model.getTimesStamp() + model.getuId());
                        messageModels.add(model);
                        chatAdapter.notifyDataSetChanged();
                    }

                    position = chatAdapter.getItemCount() - 1;
                    chatRecyclerView.getLayoutManager().scrollToPosition(position);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            foto_grande = findViewById(R.id.foto_grande);
            linear = findViewById(R.id.linear);


            findViewById(R.id.imgvoltar_Chat_Main).setOnClickListener(v -> {
                receiveId = null;
                finish();
            });
            userName_person_chat = findViewById(R.id.userName_person_chat);
            takeapicture = findViewById(R.id.takeapicture);
            imgcls_img_Chat_Main = findViewById(R.id.imgcls_img_Chat_Main);

            takeapicture.setOnClickListener(v -> {
                mGetContent.launch("image/*");
            });

            imgcls_img_Chat_Main.setOnClickListener(v -> {
                takeapicture.setVisibility(View.VISIBLE);
                foto_grande.setVisibility(View.GONE);
                imgcls_img_Chat_Main.setVisibility(View.GONE);
                stringbitmap = null;
            });
            enterMessage = findViewById(R.id.enterMessage);


            send = findViewById(R.id.send);
            profile_image = findViewById(R.id.profile_image);
            String userName = getIntent().getStringExtra("userName");
            String profilePic = getIntent().getStringExtra("profilePic");


            send.setOnClickListener(v -> {
                if (takeapicture.getVisibility() == View.GONE) {
                    takeapicture.setVisibility(View.VISIBLE);
                    foto_grande.setVisibility(View.GONE);
                    imgcls_img_Chat_Main.setVisibility(View.GONE);
                }
                String message = enterMessage.getText().toString();
                if (!message.isEmpty() || stringbitmap != null) {
                    final MessageModel model = new MessageModel(senderId, message.trim());
                    model.setTimesStamp(new Date().getTime());
                    model.setStatus("Enviada NN");

                    if (stringbitmap != null) {
                        model.setFt(stringbitmap);
                        stringbitmap = null;
                    } else {
                        model.setFt(null);
                    }

                    enterMessage.setText("");
                    reference.child("chats")
                            .child(senderRoom).child(model.getTimesStamp() + model.getuId())
                            .setValue(model).addOnSuccessListener(command -> {
                                model.setStatus(null);
                                reference.child("chats").child(receiveRoom).child(model.getTimesStamp() + model.getuId()).setValue(model).addOnSuccessListener(command1 -> {
                                    model.setFt(null);
                                });
                            });
                }
            });
            runOnUiThread(() -> {
                userName_person_chat.setText(userName);
                Glide.with(getBaseContext()).load(Login.pessoa.getImgUri()).into(profile_image);
                Glide.with(getBaseContext()).load(profilePic).into(profile_image);
                position = chatAdapter.getItemCount() - 1;
                chatRecyclerView.getLayoutManager().scrollToPosition(position);
            });
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        pessoa = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            Uri resultUri = null;
            if (result != null) {
                resultUri = Uri.parse(result);
            }
            foto_grande.setImageURI(resultUri);
            try {
                stringbitmap = BitMapToString(MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri));
            } catch (IOException e) {
                return;
            }
            takeapicture.setVisibility(View.GONE);
            foto_grande.setVisibility(View.VISIBLE);
            imgcls_img_Chat_Main.setVisibility(View.VISIBLE);
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}

