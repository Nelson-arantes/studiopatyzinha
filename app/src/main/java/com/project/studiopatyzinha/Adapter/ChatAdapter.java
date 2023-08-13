package com.project.studiopatyzinha.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.Chat_activity;
import com.project.studiopatyzinha.Login;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.MessageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter {
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    String recId;
    String chatSender;
    String meuID;
    DatabaseReference reference;
    Context context;
    ArrayList<MessageModel> messageModels;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.context = context;
        this.messageModels = messageModels;
        this.recId = recId;
        reference = FirebaseDatabase.getInstance().getReference();
        this.meuID = Login.pessoa.getId();

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType != SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciver, parent, false);
            return new RecieverViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getuId().equals(meuID)) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);
        String idpessoalocal = Login.pessoa.getId() + "";
        chatSender = recId + idpessoalocal;

        int status = 0;
        if (messageModel.getuId().equals(idpessoalocal)) {
            if (messageModel.getStatus().contains("Enviada")) {
                status = R.drawable.tic_enviada;
            } else {
                if (messageModel.getStatus().contains("Visualizada")) {
                    status = R.drawable.tic_visualisado;
                } else {
                    status = R.drawable.tic_recebida;
                }
            }
        }

        if (holder.getClass() == RecieverViewHolder.class) {
            ((RecieverViewHolder) holder).receiverMsg.setText(messageModel.getMessage());
            if (messageModel.getFt() != null) {
                ((RecieverViewHolder) holder).ftmessagereceiver.setVisibility(View.VISIBLE);
                Bitmap a = StringToBitMap(messageModel.getFt());
                ((RecieverViewHolder) holder).ftmessagereceiver.setImageBitmap(a);
                ((RecieverViewHolder) holder).ftmessagereceiver.setOnClickListener(v -> {
                    View rootViewa = ((Activity) context).getWindow().getDecorView();
                    if (context != null) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(rootViewa.getWindowToken(), 0);
                    }
                    Chat_activity.foto_grande.setVisibility(View.VISIBLE);
                    Chat_activity.takeapicture.setVisibility(View.GONE);//tirar
                    Chat_activity.linear.setVisibility(View.GONE);//tirar
                    Chat_activity.imgcls_img_Chat_Main.setVisibility(View.VISIBLE);
                    Chat_activity.imgcls_img_Chat_Main.setOnClickListener(cv -> {
                        Chat_activity.takeapicture.setVisibility(View.VISIBLE);
                        Chat_activity.linear.setVisibility(View.VISIBLE);
                    });
                    Chat_activity.foto_grande.setImageBitmap(StringToBitMap(messageModel.getFt()));
                });
            } else {
                ((RecieverViewHolder) holder).ftmessagereceiver.setVisibility(View.GONE);
            }
            Date date = new Date(messageModel.getTimesStamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" HH:mm");
            String hour = simpleDateFormat.format(date);
            ((RecieverViewHolder) holder).receiverTime.setText(hour);
            reference.child("chats").child(messageModel.getuId() + idpessoalocal).child(messageModel.getTimesStamp() + messageModel.getuId()).addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    MessageModel message = snapshot.getValue(MessageModel.class);
                    try {
                        if (!message.getuId().equals(idpessoalocal)) {
                            System.out.println("status -> "+message.getStatus());
                            if (!message.getStatus().contains("Visualizada")) {
                                reference.child("chats").child(messageModel.getuId() + idpessoalocal).child(messageModel.getTimesStamp() + messageModel.getuId()).child("status").setValue("Visualizada");
                            }

                        }
                    } catch (NullPointerException e) {

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMessage());
            if (messageModel.getFt() != null) {
                ((SenderViewHolder) holder).ftmessagesender.setVisibility(View.VISIBLE);
                Bitmap a = StringToBitMap(messageModel.getFt());
                ((SenderViewHolder) holder).ftmessagesender.setImageBitmap(a);
                ((SenderViewHolder) holder).ftmessagesender.setOnClickListener(v -> {
                    View rootView = ((Activity) context).getWindow().getDecorView();
                    if (context != null) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                    }
                    Chat_activity.foto_grande.setVisibility(View.VISIBLE);
                    Chat_activity.takeapicture.setVisibility(View.GONE);//tirar
                    Chat_activity.linear.setVisibility(View.GONE);//tirar
                    Chat_activity.imgcls_img_Chat_Main.setVisibility(View.VISIBLE);
                    Chat_activity.imgcls_img_Chat_Main.setOnClickListener(cv -> {
                        Chat_activity.takeapicture.setVisibility(View.VISIBLE);
                        Chat_activity.linear.setVisibility(View.VISIBLE);
                    });
                    Chat_activity.foto_grande.setImageBitmap(StringToBitMap(messageModel.getFt()));
                });
            } else {
                ((SenderViewHolder) holder).ftmessagesender.setVisibility(View.GONE);
            }
            ((SenderViewHolder) holder).img_status_message.setImageResource(status);
            Date date = new Date(messageModel.getTimesStamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String hour = simpleDateFormat.format(date);
            ((SenderViewHolder) holder).senderTime.setText(hour);
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }


    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg, receiverTime;
        ImageView ftmessagereceiver;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.reciverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            ftmessagereceiver = itemView.findViewById(R.id.ftmessagereceiver);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime;
        ImageView img_status_message, ftmessagesender;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderText);
            img_status_message = itemView.findViewById(R.id.img_status_message);
            senderTime = itemView.findViewById(R.id.senderTime);
            ftmessagesender = itemView.findViewById(R.id.ftmessagesender);
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
