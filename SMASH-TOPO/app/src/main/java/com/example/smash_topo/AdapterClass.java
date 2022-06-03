package com.example.smash_topo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterClass extends  RecyclerView.Adapter<AdapterClass.MyHolder>{

    private Context context;
    private List<PlayerClass> playerClassList;

    public AdapterClass(Context context, List<PlayerClass> playerClassList) {
        this.context = context;
        this.playerClassList = playerClassList;
    }

    @NonNull
    @Override
    //AGREGACION DEL XML A LA ACTIVIDAD
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_clasificaciones_class,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //OBTENCION DE LOS DATOS DE PLAYER CLASS
        String imagenPlayerUrl = playerClassList.get(position).getImagenJugador();
        String nombrePlayer = playerClassList.get(position).getNombreJugador();
        String correoPlayer = playerClassList.get(position).getCorreoElectronico();
        int toposPlayer = playerClassList.get(position).getToposAplastados();String toposPlayerString = String.valueOf(toposPlayer); //conversion a string

        holder.nombreJugadorCardScreen.setText(nombrePlayer);
        holder.correoJugadorCardScreen.setText(correoPlayer);
        holder.puntuacionJugadorCardScreen.setText(toposPlayerString);
        try{
            Picasso.get().load(imagenPlayerUrl).into(holder.imagenPlayerCard);

        }catch (Exception e){
            "Error: "+e.getMessage();
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        //DECLARACIÓN
        CircleImageView imagenPlayerCard;
        TextView nombreJugadorCardScreen,puntuacionJugadorCardScreen,correoJugadorCardScreen;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //INICIALIZACION
            imagenPlayerCard = itemView.findViewById(R.id.imagenPlayerCard);
            nombreJugadorCardScreen = itemView.findViewById(R.id.nombreJugadorCardScreen);
            puntuacionJugadorCardScreen = itemView.findViewById(R.id.puntuacionJugadorCardScreen);
            correoJugadorCardScreen = itemView.findViewById(R.id.correoJugadorCardScreen);

        }
    }
}
