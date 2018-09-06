package br.com.boemyo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.boemyo.Activitys.DetalhesProdutosActivity;
import br.com.boemyo.Configure.FirebaseInstance;
import br.com.boemyo.Configure.Helper;
import br.com.boemyo.Configure.PicassoClient;
import br.com.boemyo.Configure.Preferencias;
import br.com.boemyo.Configure.RecyclerViewOnClickListenerHack;
import br.com.boemyo.Model.Adicional;
import br.com.boemyo.Model.Produto;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 12/10/2017.
 */

public class ListaAdicionalAdapter extends RecyclerView.Adapter<ListaAdicionalAdapter.ViewHolderAdicional>{

    private  ArrayList<String> adicionais;
    private Context context;
    private LayoutInflater liAdicional;
    private DatabaseReference firebase;
    private NumberFormat format = NumberFormat.getCurrencyInstance();
    public int valorTotalAdicional = 0;
    public Double somaTotalGeral = 0.0;
    boolean checked;
    public ArrayList<String> arrayIdAdicionais = new ArrayList<>();
    Button btTeste;
    Double valorProdutoEdit = 0.0;
    public int qtdeAtual = 1;

    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ListaAdicionalAdapter(Context c, ArrayList<String> adicionais){

        this.context = c;
        this.adicionais = adicionais;
        this.liAdicional = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderAdicional onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = liAdicional.inflate(R.layout.lista_adicional, viewGroup, false);
        ListaAdicionalAdapter.ViewHolderAdicional vhAdicional = new ListaAdicionalAdapter.ViewHolderAdicional(v);

        return vhAdicional;
    }

    @Override
    public void onBindViewHolder(final ViewHolderAdicional holder, final int position) {

        final Preferencias preferencias = new Preferencias(context);



        firebase = FirebaseInstance.getFirebase();
        firebase.child("adicional")
                    .child(preferencias.getIdEstabelecimento())
                        .child(adicionais.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    final Adicional adicional = dataSnapshot.getValue(Adicional.class);

                    holder.cbSelecionaAdicional.setHint(adicional.getNomeAdicional());
                    holder.tvValorAdicional.setText(String.valueOf(format.format(adicional.getValorAdicional() / 100)));
                    valorProdutoEdit = (((DetalhesProdutosActivity)context).produto.getValorProduto());
                    somaTotalGeral = (((DetalhesProdutosActivity)context).produto.getValorProduto());
                ((DetalhesProdutosActivity)context).ivSubtraiProduto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        qtdeAtual = qtdeAtual - 1;
                        //valor = String.valueOf(qtdeAtual);

                        if(qtdeAtual < 1){
                            qtdeAtual = 1;
                            //valor = String.valueOf(qtdeAtual);
                            ((DetalhesProdutosActivity)context).tvQtdeProduto.setText(String.valueOf(qtdeAtual));
                            valorProdutoEdit = ((((DetalhesProdutosActivity)context).produto.getValorProduto()) ) * qtdeAtual;
                            ((DetalhesProdutosActivity)context).btConfirmaProduto.setText("Confirmar Pedido - " + format.format(valorProdutoEdit));
                            somaTotalGeral = (valorProdutoEdit + (valorTotalAdicional * qtdeAtual));
                            ((DetalhesProdutosActivity)context).btConfirmaProduto.setText("Confirmar Pedido - " + format.format(somaTotalGeral /100));
                            ((DetalhesProdutosActivity)context).btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(somaTotalGeral /100));
                            ((DetalhesProdutosActivity)context).tvValorProduto.setText(format.format( somaTotalGeral /100));
                            //holder.tvValorAdicional.setText(String.valueOf(format.format((adicional.getValorAdicional()  / 100) * qtdeAtual) ));

                        }else{

                            ((DetalhesProdutosActivity)context).tvQtdeProduto.setText(String.valueOf(qtdeAtual));
                            valorProdutoEdit = ((((DetalhesProdutosActivity)context).produto.getValorProduto()) ) * qtdeAtual;
                            somaTotalGeral = (valorProdutoEdit + (valorTotalAdicional * qtdeAtual));
                            ((DetalhesProdutosActivity)context).btConfirmaProduto.setText("Confirmar Pedido - " + format.format(somaTotalGeral /100));
                            ((DetalhesProdutosActivity)context).btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(somaTotalGeral /100));
                            ((DetalhesProdutosActivity)context).tvValorProduto.setText(format.format(somaTotalGeral/100 ));
                            //holder.tvValorAdicional.setText(String.valueOf(format.format((adicional.getValorAdicional()  / 100) * qtdeAtual) ));

                        }
                    }
                });

                ((DetalhesProdutosActivity)context).ivSomaProduto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberFormat format = NumberFormat.getCurrencyInstance();

                        qtdeAtual = qtdeAtual + 1;
                        //valor = String.valueOf(qtdeAtual);

                        if(qtdeAtual > 10){
                            qtdeAtual = 10;
                            //valor = String.valueOf(qtdeAtual);
                            ((DetalhesProdutosActivity)context).tvQtdeProduto.setText(String.valueOf(qtdeAtual));
                            valorProdutoEdit = ((((DetalhesProdutosActivity)context).produto.getValorProduto() )) * qtdeAtual;
                            somaTotalGeral = (valorProdutoEdit + (valorTotalAdicional * qtdeAtual));
                            ((DetalhesProdutosActivity)context).btConfirmaProduto.setText("Confirmar Pedido - " + format.format(somaTotalGeral /100));
                            ((DetalhesProdutosActivity)context).btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(somaTotalGeral /100));
                            ((DetalhesProdutosActivity)context).tvValorProduto.setText(format.format( somaTotalGeral  /100));


                        }else{
                            ((DetalhesProdutosActivity)context).tvQtdeProduto.setText(String.valueOf(qtdeAtual));
                            valorProdutoEdit = ((((DetalhesProdutosActivity)context).produto.getValorProduto()) ) * qtdeAtual;
                            somaTotalGeral = (valorProdutoEdit + (valorTotalAdicional * qtdeAtual));
                            ((DetalhesProdutosActivity)context).btConfirmaProduto.setText("Confirmar Pedido - " + format.format(somaTotalGeral /100));
                            ((DetalhesProdutosActivity)context).btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(somaTotalGeral /100));
                            ((DetalhesProdutosActivity)context).tvValorProduto.setText(format.format(somaTotalGeral /100));

                        }


                    }
                });




                holder.cbSelecionaAdicional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            checked = isChecked;
                            if(isChecked == true){

                                arrayIdAdicionais.add(adicionais.get(position));
                                valorTotalAdicional = valorTotalAdicional + (adicional.getValorAdicional()) ;
                                //valorProdutoEdit = valorProdutoEdit + valorTotalAdicional;
                                //preferencias.salvarValorAdicional(String.valueOf(valorTotalAdicional));
                                Log.i("VALOR_ADICIONAL", String.valueOf(valorTotalAdicional));
                                somaTotalGeral = (valorProdutoEdit + (valorTotalAdicional * qtdeAtual));
                                ((DetalhesProdutosActivity)context).btConfirmaProduto.setText("Confirmar Pedido - " + format.format(somaTotalGeral /100));
                                ((DetalhesProdutosActivity)context).btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(somaTotalGeral /100));
                                ((DetalhesProdutosActivity)context).tvValorProduto.setText(format.format(somaTotalGeral  / 100) );

                                for (String id: arrayIdAdicionais) {
                                    Log.i("LOG_IDs", id);
                                }
                                
                            }else{
                                arrayIdAdicionais.remove(adicionais.get(position));
                                Log.i("VALOR_ADICIONAL", String.valueOf(adicional.getValorAdicional()));
                                //valores.remove(i);
                                valorTotalAdicional = valorTotalAdicional - (adicional.getValorAdicional() ) ;
                                //valorProdutoEdit = valorProdutoEdit - valorTotalAdicional;
                                somaTotalGeral = (valorProdutoEdit + (valorTotalAdicional * qtdeAtual));
                                ((DetalhesProdutosActivity)context).btConfirmaProduto.setText("Confirmar Pedido - " + format.format(somaTotalGeral /100));
                                ((DetalhesProdutosActivity)context).btConfirmaPedidoSheet.setText("Confirmar Pagamento - " + format.format(somaTotalGeral /100));
                                ((DetalhesProdutosActivity)context).tvValorProduto.setText(format.format(somaTotalGeral / 100) );

                                for (String id: arrayIdAdicionais) {
                                    Log.i("LOG_IDs", id);
                                }


                            }
                            //Adicionais(isChecked, adicional.getValorAdicional());
                        }
                    });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        /**YoYo.with(Techniques.FadeInLeft)
                .duration(700)
                .playOn(holder.itemView);*/
    }

    @Override
    public int getItemCount() {return adicionais.size();}

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    public class ViewHolderAdicional extends RecyclerView.ViewHolder implements View.OnClickListener{

        CheckBox cbSelecionaAdicional;

        TextView tvValorAdicional;

        public ViewHolderAdicional(View itemView) {
            super(itemView);

            cbSelecionaAdicional = (CheckBox) itemView.findViewById(R.id.cb_seleciona_adicional);
            tvValorAdicional = (TextView) itemView.findViewById(R.id.tv_valor_adicional);
            context = itemView.getContext();
            itemView.setOnClickListener(this);






        }

        @Override
        public void onClick(View view) {



        }



    }

    public void Adicionais(TextView textView){

            textView.setText(valorTotalAdicional);

            /*valores.add(valorAdicional);
            valorTotalAdicional = Helper.sumAdicionais(valores);
            Log.i("VALOR_ADICIONAL", String.valueOf(valorTotalAdicional));*/




    }

    /*public ListaProdutosAdapter(@NonNull Context c, @NonNull ArrayList<Produto> objects) {
        super(c, 0, objects);
        this.produtos = objects;
        this.context = c;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if( produtos != null ){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_categoria_produtos, parent, false);

            CircleImageView ivImgProduto = (CircleImageView) view.findViewById(R.id.iv_img_produto);
            TextView tvNomeProduto = (TextView) view.findViewById(R.id.tv_nome_produto_lista);
            TextView tvDescProduto = (TextView) view.findViewById(R.id.tv_des_produto_lista);
            TextView tvValorProduto = (TextView) view.findViewById(R.id.tv_valor_lista);

           Produto produto = produtos.get( position );

           if(produto.getUrlImagemProduto() != null){
                   PicassoClient.downloadImage(context, produto.getUrlImagemProduto(), ivImgProduto);
           }
           tvNomeProduto.setText(produto.getNomeProduto());
           tvDescProduto.setText(produto.getDescProduto());
            NumberFormat format = NumberFormat.getCurrencyInstance();
           tvValorProduto.setText(format.format(produto.getValorProduto()));

        }

        return view;

    }*/
}
