package com.soprasteria.brewdog;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hector1.soprasteriabrewdogbeers.R;
import com.soprasteria.brewdog.objects.Beer;
import com.soprasteria.brewdog.objects.BeerAdapter;
import com.soprasteria.brewdog.utilities.Utils;
import com.soprasteria.brewdog.webservice.PunkapiClientUsage;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //SharedPreferences
    public static final String PREFS_NAME = "Preferences";
    public static final String CURRENT_BEER_LIST = "current_beer_list";
    private SharedPreferences prefs;

    //Clase para la gestión de llamadas a PunkAPI.
    private PunkapiClientUsage punkapiClientUsage = new PunkapiClientUsage();

    //Variables.
    private Context context;

    //Variables UI.
    Button reorderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        context = this;

        prefs.edit().putString(CURRENT_BEER_LIST, null).commit();

        setContentView(R.layout.activity_main);
        init_mainMenu();
    }

    // Inicializar los componentes del menú principal.
    private void init_mainMenu () {
        Button searchButtonBear = findViewById(R.id.btn_search_beer);
        searchButtonBear.setOnClickListener(this);

        reorderButton = findViewById(R.id.btn_change_order);
        reorderButton.setOnClickListener(this);
        reorderButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_search_beer:
                    Log.i(getClass().getCanonicalName(), "Botón: " + getResources().getResourceEntryName(v.getId()));

                    EditText foodEditText = findViewById(R.id.editText_food);
                    final String food = foodEditText.getText().toString();

                    //Buscar en shared preferences si ya existe una variable con el nombre buscado.
                    String foodAlreadyConsulted = prefs.getString(food, null);
                    if (foodAlreadyConsulted == null) {
                        Log.i(getClass().getCanonicalName(), "Primera busqueda para la comida [" + food + "].");
                        punkapiClientUsage.getBeersByFood(context, food, new PunkapiClientUsage.OnRestFullCallback() {
                            @Override
                            public void onRestFullResponse(boolean result, ArrayList<Beer> beers) {
                                if (result) {
                                    if (beers != null && beers.size() > 0) {
                                        Log.i(this.getClass().getName(), "Lista de cervezas: " + Arrays.toString(beers.toArray()));
                                        setBeersToListView(beers);
                                        reorderButton.setVisibility(View.VISIBLE);
                                    } else {
                                        //TODO tener en cuenta si no hay resultados a pesar de que la llamada haya ido bien.
                                        Log.i(getClass().getCanonicalName(), "No se han encontrado cervezas compatibles con [" + food + "].");
                                        setBeersToListView(null);
                                        reorderButton.setVisibility(View.GONE);
                                    }
                                } else {
                                    //TODO mostrar un popup con un mensaje de error.
                                    setBeersToListView(null);
                                    Log.i(getClass().getCanonicalName(), "La llamada ha fallado.");
                                    reorderButton.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else {
                        Log.i(getClass().getCanonicalName(), "La comida [" + food + "], ya tiene el resultado almacenado.");
                        ArrayList<Beer> beers = Utils.jsonToBeerArrayList(foodAlreadyConsulted);
                        if (beers.size() > 0) {
                            setBeersToListView(beers);
                            reorderButton.setVisibility(View.VISIBLE);
                            //Lista actual de cervezas.
                            prefs.edit().putString(CURRENT_BEER_LIST, foodAlreadyConsulted).commit();
                        }
                    }
                    break;
                case R.id.btn_change_order:
                    String currentBeerList = prefs.getString(CURRENT_BEER_LIST, null);
                    if (currentBeerList != null && !currentBeerList.equals("")) {
                        ArrayList<Beer> beers = Utils.jsonToBeerArrayList(currentBeerList);
                        //Orden descendente.
                        Collections.reverse(beers);
                        setBeersToListView(beers);
                    }
                    break;
                default:
                    Log.i(getClass().getCanonicalName(), "Botón sin implementar: " + getResources().getResourceEntryName(v.getId()));
            }
        } catch (JSONException e) {
            //TODO mostrar la exception.
            Log.e(getClass().getCanonicalName(), e.getMessage());
        }
    }

    //Mostrar la lista de cervezas en el listView.
    private void setBeersToListView (ArrayList<Beer> beers) {
        ListView listView = findViewById(R.id.listView_beers);
        if (beers == null) {
            listView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            BeerAdapter beerAdapter = new BeerAdapter(this, R.layout.list_view, beers);
            listView.setAdapter(beerAdapter);
        }
    }
}
