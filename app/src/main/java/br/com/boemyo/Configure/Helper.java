package br.com.boemyo.Configure;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.SupportedCardTypesView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.boemyo.Model.Carteira;
import br.com.boemyo.Model.Pagamento;
import br.com.boemyo.R;

/**
 * Created by Phelipe Oberst on 08/01/2018.
 */

public class Helper {

    public static Long getDiferencaData(int horaPedido, int minPedido, int horaAtual, int minAtual) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(Calendar.HOUR,horaPedido);
        c1.set(Calendar.MINUTE,minPedido);

        c2.set(Calendar.HOUR,horaAtual);
        c2.set(Calendar.MINUTE,minAtual);

        //diferenca em minutos
        long diffMinutos = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (60 * 1000);


        return diffMinutos;
    }

    public static int sumAdicionais(List<Integer> list){
        if(list==null || list.size()<1)
            return 0;

        int sum = 0;
        for(Integer i: list)
            sum = sum+i;

        return sum;
    }

    public void validaBandeira(Carteira carteira, SupportedCardTypesView cardTypesView){
        Log.i("LOG_TIPO_CARD", carteira.getBandeira());
        if(carteira.getBandeira().equals("VISA") ){
            cardTypesView.setSupportedCardTypes(CardType.VISA);
        }else if(carteira.getBandeira().equals("MASTER") ){
            cardTypesView.setSupportedCardTypes(CardType.MASTERCARD);
        }else if(carteira.getBandeira().equals("AMEX")){
            cardTypesView.setSupportedCardTypes(CardType.AMEX);
        }else if(carteira.getBandeira().equals("DINERS_CLUB")){
            cardTypesView.setSupportedCardTypes(CardType.DINERS_CLUB);
        }else{
            cardTypesView.setSupportedCardTypes(CardType.UNKNOWN);
        }
    }

    public void validaCodCielo(Button button, String codigo){

        Snackbar snackbar;
        View view;

        switch (codigo){

            case "01":
            case "02":
            case "14":
            case "15":
            case "59":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "4":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "05":
            case "R1":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "12":

                snackbar = Snackbar.make(button, R.string.cod_transacao_invalida, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "39":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "41":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "43":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "51":
            case "70":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "54":

                snackbar = Snackbar.make(button, R.string.cod_refazer_confirmar_dados, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "57":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "61":

                snackbar = Snackbar.make(button, R.string.cod_tente_novamente, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "65":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "75":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_processadas, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "78":

                snackbar = Snackbar.make(button, R.string.cod_primeiro_uso, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "98":
            case "99":
            case "999":

                snackbar = Snackbar.make(button, R.string.cod_sistemico, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "AC":

                snackbar = Snackbar.make(button, R.string.cod_uso_debito, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "AH":

                snackbar = Snackbar.make(button, R.string.cod_uso_credito, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "BL":
            case "EB":

                snackbar = Snackbar.make(button, R.string.cod_limite_excedido, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "BN":

                snackbar = Snackbar.make(button, R.string.cod_bloqueado, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "DF":

                snackbar = Snackbar.make(button, R.string.cod_nao_permitida, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;

            case "FC":

                snackbar = Snackbar.make(button, R.string.cod_transacao_nao_autorizada, Snackbar.LENGTH_LONG);
                view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
                break;




        }


    }
}
