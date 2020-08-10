package com.example.myappfacebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class HoraAdapter_Admin extends RecyclerView.Adapter<HoraAdapter_Admin.ViewHolder>{

    private static ClickListener clickListener;
    private ArrayList<Hora> data;
    private ArrayList<String> llista = new ArrayList<>();
    private Hora hora;
    private Context mContext;
    private String diaReserva;
    private ArrayList<String> dades;
    private ArrayList<Integer> posicionsReservades = new ArrayList<>();
    private ArrayList<String> posicionsAdmin = new ArrayList<>();
    private DatabaseReference mRootRef;


    public HoraAdapter_Admin(Context context,ArrayList<Hora> data,String diaReserva, ArrayList<String> dades) {
        mContext=context;
        this.data = data;
        this.diaReserva = diaReserva;
        this.dades = dades;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mycardview_hora_admin, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    private ArrayList llistar(int pos){
        int count=9;
        int horesTreball =10; // suma de 8 hores + 2 hores de descans
        String[] quarterHours = {"00","15","30","45"};
        for (int i = 0; i <horesTreball; i++) {
            //L'horari es de 9 a 13 i de 15 a 19
            if(count==13){
                llista.add(new String("Descanso"));
                count = 15;
            }
            for (int j = 0; j < 4; j++) {
                llista.add(new String( count + ":" + quarterHours[j]));
            }
            count++;
        }
        return llista;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        mRootRef = FirebaseDatabase.getInstance().getReference();

        hora = data.get(position);
        holder.btncld.setText(hora.getClicked());//Linea afegida
        llistar(position);
        //Canvia de color les possicions ja reservades
        for (int i = 0; i < dades.size(); i++) {
            //Si la possicio ja està reservada
            if(llista.get(position).equals(dades.get(i))){
                holder.btncld.setBackgroundColor(Color.parseColor("#d64933"));
                holder.btncld.setText("Horari " + hora.getClicked() + " JA ESTÀ RESERVAT.");
                posicionsReservades.add(position);
                //Al pulsar el botó estigui incapacitat
                holder.btncld.setEnabled(false);
                //En el cas que sigui l'hora de descans...
            }
        }
        //Fem que sigui imposible pulsar sobre l'area de descans i que boto acceptar i
        //Descans tinguin un disseny diferent al RV
        if(llista.get(position).equals("19:00")){
            modifRV(holder);
        }else if(llista.get(position).equals("Descanso")){
            modifRV(holder);
            holder.btncld.setEnabled(false);
            posicionsReservades.add(position);
        }


        holder.btncld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( posicionsReservades.contains((position)) ){
                    Toast toast = Toast.makeText(v.getContext(), "No es pot fer aquesta reserva ",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(v.getContext(), "Reserva: " + llista.get(position) + " " + posicionsAdmin.size(),
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                    //cambiem el color de la posició del recyclerview
                    holder.btncld.setBackgroundColor(Color.parseColor("#4ecdc4"));
                    //Afegim la reserva feta per l'administrador
                    posicionsAdmin.add(llista.get(position));
                    //Si pulsem Acceptar, anem a una nova activitat
                    if(llista.get(position).equals("19:00")){
                        Intent intent = new Intent(mContext, FinalActivityAdmin.class);
                        intent.putExtra("resultat_dia", diaReserva);
                        intent.putExtra("resultat_horaris", posicionsAdmin);
                        mContext.startActivity(intent);
                    }
                }
            }
        });
    }

    public void modifRV(ViewHolder holder){
        holder.btncld.getLayoutParams().height = 140;
        holder.btncld.getLayoutParams().width = 1050;
        holder.cardView.getLayoutParams().width = 1050;
        holder.cardView.getLayoutParams().height = 108;
        holder.cardView.setCardBackgroundColor(Color.parseColor("#8d99ae"));
        holder.btncld.setBackgroundColor(Color.parseColor("#bcb8b1"));
    }

    /*public ArrayList<Integer> posiblesEntrades(ArrayList posicionsReservades,int resultat,int position){
        //Si la posicio quan pulsem es trova entre una posició inferior i la superior
        //trova la diferencia i si es mes petit que resultat(es el nostre temps que empleem) li afegim a l'array de
        //posicionsReservades, per postirment enviar un missatge de que no es pot fer la reserva
        for (int i = 0; i < posicionsReservades.size()-1; i++) {
            int inferior = (Integer)(posicionsReservades.get(i));
            int superior = (Integer)(posicionsReservades.get(i+1));
            if(inferior < position && superior > position){
                int difDesdePos = superior - position;
                if(difDesdePos < resultat){
                    for (int j = 0; j < difDesdePos; j++) {
                        posicionsReservades.add(position+j);
                    }
                }
            }
        }
        // Si ens trobem entre l'horari de descans(pos=16) i pulsem la nostra possició, si trepitjem la posició 16
        //Ens dona un missatge de que no es pot fer la reserva
        if(position + resultat >= 16){
            int difDesdeDescans = 16 - position;
            if(difDesdeDescans < resultat){
                for (int j = 0; j < difDesdeDescans-1; j++) {
                    posicionsReservades.add(position+j);
                }
            }
        }
        // Si ens passem l'horari de descans(pos=16) i estem entre la posició final (32), si trepitjem la posició 32
        //Ens dona un missatge de que no es pot fer la reserva
        if(position>16 && position<=32){
            int difFinsFinal = (32+1) - position;
            if(difFinsFinal < resultat){
                for (int i = 0; i < difFinsFinal; i++) {
                    posicionsReservades.add(position+i);
                }
            }
        }
        return posicionsReservades;
    }
*/
    @Override
    public int getItemCount() {
        return dades == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @Override //Metode afegit
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Button btncld;
        LinearLayout linearLayout;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            btncld = (Button)itemView.findViewById(R.id.btn_product);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
            cardView=(CardView)itemView.findViewById(R.id.cardview2);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}