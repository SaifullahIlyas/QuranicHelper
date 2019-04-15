package com.example.quranichelper;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.quranichelper.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class customSurahViewAdapter  extends BaseAdapter {
    MediaPlayer player ;
    Context context;
    ArrayList<datalistdetail> list;
    public  customSurahViewAdapter(Context context,ArrayList<datalistdetail> l)
    {
        this.context = context;
        list = l;
    }

public  void resetAdapter(ArrayList<datalistdetail> datalistdetails)
{
    this.list =  datalistdetails;
}
    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final datalistdetail detail=list.get(position);

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.customrow, parent,false);
            TextView tittle=convertView.findViewById(R.id.listtitle);
            ImageView imag1=convertView.findViewById(R.id.listmp3);
            ImageView imag2=convertView.findViewById(R.id.listplay);
            tittle.setText(detail.name);
            imag1.setImageResource(detail.mp3);
            imag2.setImageResource(detail.play);

            imag2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListView)parent).performItemClick(v,position,0);
                }
            });

            /*imag2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent intent = new Intent(context,Listen.class);
                    intent.putExtra("surahname",detail.getName().toString());
                context.startActivity(intent);

                }
            });*/


        return convertView;
    }




}
