package com.example.pm1e1201910060126;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e1201910060126.Tablas.Paises;
import com.example.pm1e1201910060126.Transacciones.Transacciones;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteConexion conexion;
    Spinner cmbPaises;
    EditText txtNombreContacto, txtTelefono, txtNota;
    ArrayList<String> listaPaises;
    ArrayList<Paises> paises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conexion = new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
        cmbPaises = (Spinner) findViewById(R.id.cmbPaisesBuscado);

        //LLENADO DE CMB
        ObtenerListaPaises();
        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaPaises);
        cmbPaises.setAdapter(adp);

        //BOTON DE AÑADIR PAIS
        Button btnAddPais = (Button) findViewById(R.id.btnAddPais);
        btnAddPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityAddPais.class);
                startActivity(intent);
            }
        });

        //BOTON SALVAR CONTACTO
        Button btnSalvarContacto = (Button) findViewById(R.id.btnSalvarContacto);
        txtNombreContacto = (EditText) findViewById(R.id.txtNombreBuscado);
        txtTelefono = (EditText) findViewById(R.id.txtTelefonoBuscado);
        txtNota = (EditText) findViewById(R.id.txtNotaBuscado);
        btnSalvarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarContacto();
            }
        });

        //BOTON LISTAR CONTACTOS
        Button btnListaContactos = (Button) findViewById(R.id.btnListaContactos);
        btnListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListContactos.class);
                startActivity(intent);
            }
        });

    }

    private void AgregarContacto() {
        int numeros = 0;
        if(txtNombreContacto.getText().toString().isEmpty() || txtTelefono.getText().toString().isEmpty()) {
            mostrarDialogoVacios();
        } else {
            for (int i = 0; i < txtNombreContacto.getText().toString().length(); i++) {
                if (Character.isDigit(txtNombreContacto.getText().toString().charAt(i))) {
                    mostrarDialogoNumeros();
                    numeros = 1;
                    break;
                }
            }

            if (numeros == 0) {
                SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
                SQLiteDatabase db = conexion.getWritableDatabase();

                ContentValues valores = new ContentValues();
                valores.put(Transacciones.nombreContacto, txtNombreContacto.getText().toString());
                valores.put(Transacciones.telefonoContacto, txtTelefono.getText().toString());
                valores.put(Transacciones.pais, (cmbPaises.getSelectedItemId() + 1));
                valores.put(Transacciones.nota, txtNota.getText().toString());

                Long resultado = db.insert(Transacciones.tablaContactos, Transacciones.idContacto, valores);
                Toast.makeText(getApplicationContext(), "Contacto Guardado: " + resultado.toString(), Toast.LENGTH_LONG).show();
                db.close();
                ClearScreen();
            }
        }
    }

    private void ClearScreen() {
        txtNombreContacto.setText("");
        txtTelefono.setText("");
        txtNota.setText("");
    }

    private void mostrarDialogoVacios() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Vacíos")
                .setMessage("No puede dejar ningún campo vacío")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void mostrarDialogoNumeros() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Números")
                .setMessage("No puede ingresar números en el campo de Nombre")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void ObtenerListaPaises() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Paises pais = null;

        paises = new ArrayList<Paises>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablaPaises, null);

        while (cursor.moveToNext()){
            pais = new Paises();
            pais.setIdPais(cursor.getInt(0));
            pais.setNombrePais(cursor.getString(1));
            pais.setCodigoMarcado(cursor.getInt(2));

            paises.add(pais);
        }

        fillComb();
        cursor.close();
    }

    private void fillComb() {
        listaPaises = new ArrayList<String>();
        for(int i = 0; i < paises.size(); i++){
            listaPaises.add(paises.get(i).getNombrePais() + "  ("
                    + paises.get(i).getCodigoMarcado() + ")");
        }
    }
}