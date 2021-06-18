package com.example.pm1e1201910060126;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm1e1201910060126.Transacciones.Transacciones;

public class ActivityAddPais extends AppCompatActivity {

    EditText txtNombrePais, txtCodigoMarcado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pais);

        Button btnSalvarPais = (Button) findViewById(R.id.btnSalvarPais);
        Button btnVolver = (Button) findViewById(R.id.btnVolverInicio);
        txtNombrePais = (EditText) findViewById(R.id.txtNombrePais);
        txtCodigoMarcado = (EditText) findViewById(R.id.txtCodigoMarcado);

        btnSalvarPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarPais();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void AgregarPais() {
        int numeros = 0;
        if(txtNombrePais.getText().toString().isEmpty() || txtCodigoMarcado.getText().toString().isEmpty()) {
            mostrarDialogoVacios();
        } else {
            for( int i = 0; i < txtNombrePais.getText().toString().length(); i++ ) {
                if (Character.isDigit(txtNombrePais.getText().toString().charAt(i))) {
                    mostrarDialogoNumeros();
                    numeros = 1;
                    break;
                }
            }

            if(numeros == 0) {
                SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDataBase, null, 1);
                SQLiteDatabase db = conexion.getWritableDatabase();

                ContentValues valores = new ContentValues();
                valores.put(Transacciones.nombrePais, txtNombrePais.getText().toString());
                valores.put(Transacciones.codigoMarcado, txtCodigoMarcado.getText().toString());

                Long resultado = db.insert(Transacciones.tablaPaises, Transacciones.idPais, valores);
                Toast.makeText(getApplicationContext(), "País Añadido: " + resultado.toString(), Toast.LENGTH_LONG).show();
                db.close();
                ClearScreen();
            }
        }

    }

    private void ClearScreen() {
        txtNombrePais.setText("");
        txtCodigoMarcado.setText("");
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
                .setMessage("No puede ingresar números en el campo de Nombre del País")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

}