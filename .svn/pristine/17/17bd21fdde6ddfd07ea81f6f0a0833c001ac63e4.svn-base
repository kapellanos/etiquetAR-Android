/**
 * RegisterActivity.java
 * @author Miguel Olmedo Camacho - 100072925@alumnos.uc3m.es
 * @version 1.0
 * 30/07/2013
 * Implementa la actividad de registro a través de una interfaz simple que permite al usuario introducir su correo electrónico (utilizado como nombre de usuario
 * en la aplicación), su nombre (para proveer de un trato más personalizado) y la contraseña (para autenticarse).
 *
 * Se comprobará si el registro es adecuado en el momento de enviar los datos al servidor. Si ya existe un usuario previamente registrado en la base de datos
 * el registro será rechazado y se mostrará un AlertDialog informando al usuario de esta situación.
 *
 * Una vez que el registro se haya finalizado de forma correcta, se devolverá al usuario a la pantalla de login para que se identifique con los datos utilizados
 * en el registro.
 */
package es.uc3m.moc.etiquetar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;
import es.uc3m.moc.etiquetar.utilities.http.ExecuteInBackground;

public class RegisterActivity extends Activity {

    private EditText etRegisterEmail;
    private EditText etRegisterName;
    private EditText etRegisterPassword;
    private EditText getEtRegisterPassword2;
    private Button bRegister;
    private ProgressDialog pdRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);
        getReferences();
        createListeners();
    }

    private void getReferences() {
        etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        etRegisterName = (EditText) findViewById(R.id.etRegisterName);
        etRegisterPassword = (EditText) findViewById(R.id.etRegisterPassword);
        getEtRegisterPassword2 = (EditText) findViewById(R.id.etRegisterPassword2);
        bRegister = (Button) findViewById(R.id.bRegister);
        pdRegister = new ProgressDialog(this);
    }

    private void createListeners() {
        etRegisterEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                if(etRegisterEmail.getText() != null && !hasFocus && !isEmpty(etRegisterEmail) && !etRegisterEmail.getText().toString().matches(emailPattern)) {
                    etRegisterEmail.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    etRegisterEmail.setError(getResources().getString(R.string.errorEmail));
                } else if(hasFocus) {
                    etRegisterEmail.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    etRegisterEmail.setError(null);
                }
            }
        });

        etRegisterName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !isEmpty(etRegisterName) && etRegisterName.length() <= 3) {
                    etRegisterName.setError(getResources().getString(R.string.errorName));
                    etRegisterName.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                } else if(hasFocus)
                    etRegisterName.setTextColor(getResources().getColor(android.R.color.black));
                else
                    etRegisterName.setError(null);
            }
        });

        etRegisterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !isEmpty(etRegisterPassword) && etRegisterPassword.length() < 6) {
                    etRegisterPassword.setError(getResources().getString(R.string.errorShortPasword));
                    etRegisterPassword.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                } else if(hasFocus)
                    etRegisterPassword.setTextColor(getResources().getColor(android.R.color.black));
                else
                    etRegisterPassword.setError(null);

            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean allOk = true;
                // Observar que el correo cumple con una expresión regular
                String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                // Comprobar si están todos los campos introducidos
                if(isEmpty(etRegisterEmail)) {
                    etRegisterEmail.setError(getResources().getString(R.string.errorEmailEmpty));
                    allOk = false;
                }
                else {
                    if(etRegisterEmail.getText() != null && !etRegisterEmail.getText().toString().matches(emailPattern)) {
                        etRegisterEmail.setError(getResources().getString(R.string.errorEmail));
                        allOk = false;
                    } else
                        etRegisterEmail.setError(null);

                }

                if(isEmpty(etRegisterName)) {
                    etRegisterName.setError(getResources().getString(R.string.errorNameEmpty));
                    allOk = false;
                } else {
                    if(etRegisterName.getText() != null && etRegisterName.getText().length() <= 3) {
                        etRegisterName.setError(getResources().getString(R.string.errorName));
                        allOk = false;
                    } else {
                        etRegisterName.setError(null);
                    }
                }
                if(isEmpty(etRegisterPassword)) {
                    etRegisterPassword.setError(getResources().getString(R.string.errorPasswordEmpty));
                    allOk = false;
                } else {
                    if(etRegisterPassword.getText() != null && etRegisterPassword.getText().length() < 6) {
                        etRegisterPassword.setError(getResources().getString(R.string.errorShortPasword));
                        allOk = false;
                    } else if(etRegisterPassword.getText() != null && getEtRegisterPassword2.getText() != null && !etRegisterPassword.getText().toString().equals(getEtRegisterPassword2.getText().toString())) {
                        etRegisterPassword.setError(getResources().getString(R.string.errorPasswordsDontMatch));
                        allOk = false;
                    } else {
                        // Correcto
                        etRegisterPassword.setError(null);
                        getEtRegisterPassword2.setError(null);
                    }
                }
                if(allOk) {
                    ExecuteInBackground register = new ExecuteInBackground(RegisterActivity.this);
                    register.execute(Constants.ID_REGISTER, etRegisterEmail.getText().toString(), etRegisterName.getText().toString(), etRegisterPassword.getText().toString());
                    pdRegister.setMessage(getResources().getString(R.string.pdRegister));
                    pdRegister.setCancelable(false);
                    pdRegister.show();
                }
            }
        });
    }

    /**
     * Comprueba si un edit text está vacío
     * @param editText el edit text a comprobar
     * @return true en caso de que esté vacío; false en cualquier otro caso
     */
    private boolean isEmpty (EditText editText) {
        // Un edit text estará vacío cuando el texto tenga longitud 0 o sea igual a ""
        return editText.getText() != null && (editText.getText().length() == 0 || editText.getText().toString().trim().equalsIgnoreCase(""));
    }

    /**
     * Se llama desde la actividad encargada de realizar el registro cuando el registro se ha realizado correctamente
     */
    public void registerOk(Object data) {
        pdRegister.dismiss();
        if(data instanceof Integer) {
            if((Integer) data == -2) {
                FrequentMethods.serverNotResponding(this, false);
            }
        } else if (data instanceof JSONArray) {
            try {
                JSONArray resultArray = (JSONArray) data;
                JSONObject jsonData = resultArray.getJSONObject(0);
                // Se obtiene el resultado del acceso
                String success = jsonData.getString(Constants.JSON_SUCCESS);
                // Si correcto...
                if(jsonData.getString(Constants.JSON_SUCCESS).equals("OK")) {
                    Intent launchLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(launchLoginActivity);
                    Toast.makeText(this, getResources().getString(R.string.registerOk), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // TODO error message when we cannot register
                }
            } catch (JSONException e) {

            }
        }
    }
}
