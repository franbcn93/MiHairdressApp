package com.example.myappfacebook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class HoraAdapter extends RecyclerView.Adapter<HoraAdapter.ViewHolder>{

    private ArrayList<Hora> data;
    ArrayList<String> horaC;
    final ArrayList<String> llista = new ArrayList<>();
    Bundle mBundle = new Bundle();
    String horaInici ="";
    Integer pos;

    public HoraAdapter(ArrayList<Hora> data) {
        this.data = data;
    }

    @Override
    /*public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musica, parent, false));
    }*/
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_musica, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    private ArrayList llistar(int pos){
        int count=9;
        int horesTreball =10;
        for (int i = 0; i <horesTreball; i++) {
            //L'horari es de 9 a 13 i de 15 a 19
            if(count==13){
                count = 15;
            }
            for (int j = 0; j < 6; j++) {
                llista.add(new String( count + ":" + j + "0"));
            }
            count++;
        }
        return llista;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        final Hora hora = data.get(position);

        holder.tvNombre.setText(hora.getNombre());
        holder.btncld.setText(hora.getClicked());//Linea afegida
        llistar(position);
        holder.btncld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Has escollit l'hora: " + llista.get(position),
                        Toast.LENGTH_LONG).show();
                Log.d("Resultat ",hora.getNombre());
                Intent intent = new Intent();
                horaInici=hora.getNombre();
                Log.d("Resultat2 ",horaInici);
                pos = position;
                Log.d("Posicio ",String.valueOf(pos));
                CalendariActivity cA = new CalendariActivity();
                cA.posicio(llista.get(position));
                //horaC.add(String.valueOf(data.get(position)));
                //holder.btncld.setBackgroundColor(Color.parseColor("#ffff0000"));
                //v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override //Metode afegit
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Implementat onclick
        Button btncld;
        TextView tvNombre;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombre = (TextView) itemView.findViewById(R.id.tv_nombre);
            btncld = (Button)itemView.findViewById(R.id.btncld);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }
}