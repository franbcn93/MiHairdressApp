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
import android.widget.Toast;

/*import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;*/

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


class HoraAdapter extends RecyclerView.Adapter<HoraAdapter.ViewHolder>{

    private static ClickListener clickListener;
    private ArrayList<Hora> data;
    private ArrayList<String> llista = new ArrayList<>();
    private Hora hora;
    private Context mContext;
    private String diaReserva, telefon, preuTotal;
    private Bundle mBundle = new Bundle();
    private ArrayList<String> dades;
    private ArrayList<Integer> posicionsReservades = new ArrayList<>();
    private ArrayList<String> dadesProductes;
    private DatabaseReference mRootRef;
    int resultat = 0;
    int contador =0;


    public HoraAdapter(Context context,ArrayList<Hora> data,String diaReserva, ArrayList<String> dades, ArrayList dadesProductes) {
        mContext=context;
        this.data = data;
        this.diaReserva = diaReserva;
        this.dades = dades;
        this.dadesProductes = dadesProductes;
    }

    @Override
    /*public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musica, parent, false));
    }*/
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mycardview_hora, viewGroup, false);
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
        //holder.setIsRecyclable(false);
        hora = data.get(position);
        final int counter=0;
        final int quartsHora;
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
            }else if(llista.get(position).equals("Descanso")){
                holder.btncld.setText(hora.getClicked() + " IMPOSIBLE RESERVAR");
                holder.btncld.setEnabled(false);
                posicionsReservades.add(position);
            }
        }

        holder.btncld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Recorda cambiar aquesta dada, 45 es la meva, però n'hi ha que cambiar per dadesProductes.get(2)
            quartsHora(Integer.parseInt(dadesProductes.get(2)));
            posiblesEntrades(posicionsReservades, resultat, position);
            if( posicionsReservades.contains((position)) ){
                Toast toast = Toast.makeText(v.getContext(), "No es pot fer aquesta reserva ",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(v.getContext(), "Reserva: " + llista.get(position),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
                //cambiem el color de la posició del recyclerview
                holder.btncld.setBackgroundColor(Color.parseColor("#4ecdc4"));
                holder.cardView.setBackgroundColor(Color.parseColor("#4ecdc4"));
                Intent intent = new Intent(mContext, FinalActivity.class);
                //enviem les dades a la següent activitat
                mBundle.putString("resultat_dia", diaReserva);
                //mBundle.putString("resultat_dia", preuTotal);
                mBundle.putStringArrayList("dadesClient", dades);
                mBundle.putString("resultat_horari", llista.get(position));
                mBundle.putString("tempstotal", dadesProductes.get(2));
                mBundle.putString("res_productes", dadesProductes.get(0));
                mBundle.putString("res_preu", dadesProductes.get(1));
                mBundle.putString("res_temps", dadesProductes.get(2));
                mBundle.putString("res_correu",dadesProductes.get(3));
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
            }
            }
        });
    }

    public int quartsHora(int temps){
        int quarts = 15;
        resultat = temps/quarts;
        return resultat;
    }

    public ArrayList<Integer> posiblesEntrades(ArrayList posicionsReservades,int resultat,int position){
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

    public void setOnItemClickListener(ClickListener clickListener) {
        HoraAdapter.clickListener = clickListener;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Implementat onclick
        private Button btncld;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            btncld = (Button)itemView.findViewById(R.id.btn_product);
            cardView=(CardView)itemView.findViewById(R.id.cardview2);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}