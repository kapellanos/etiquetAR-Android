/**
 * ResourceCommentsActivity.java
 *
 * 16/08/13
 *
 * Implementará la actividad de comentarios de un determinado recurso. Dicha actividad permitirá a un determinado usuario
 * ver los comentarios asociados a un recurso, sea del tipo que sea. También permitirá a través de una serie de botones
 * pulsar sobre "me gusta" en un comentario concreto o responder a dichos comentarios, ya sea en modo pregunta-respuesta
 * o como réplica al contenido del comentario anterior.
 *
 * Por último la actividad también permitirá añadir un nuevo comentario existente sin necesidad de responder a ninguno anterior
 * a través de un campo de texto que se encontrará en la parte inferior de la pantalla.
 *
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 */
package es.uc3m.moc.etiquetar.user.activities.resources.comments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import es.uc3m.moc.etiquetar.LoginActivity;
import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.database.FrequentQuerys;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;
import es.uc3m.moc.etiquetar.utilities.http.ExecuteInBackground;
import es.uc3m.moc.etiquetar.utilities.layout.LinearLayoutOutlined;

public class ResourceCommentsActivity extends Activity {

    // Progress dialog que se mostrará mientras se obtienen los comentarios
    private ProgressDialog pdGettingComments;
    // Progress dialog que se mostrará mientras se almacena un nuevo comentario en la base de datos del servidor
    private ProgressDialog pdPuttingComment;
    // Variables de layout
    private LinearLayout llCommentsResource;
    private JSONArray result;
    private Button bAddNewComment;
    private int resourceID;
    private CheckBox cbAddNameForComment;
    private EditText editTextCommentTitle;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra(Constants.RESOURCE_NAME);
        if(savedInstanceState == null) {
            getComments();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_update, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getComments() {
        pdGettingComments = new ProgressDialog(this);
        pdGettingComments.setMessage(getString(R.string.pdGettingComments));
        pdGettingComments.show();
        resourceID = getIntent().getIntExtra(Constants.RESOURCE_ID_EXTRA, 0);
        // Obtiene los comentarios del recurso asociado
        ExecuteInBackground getComments = new ExecuteInBackground(this);
        getComments.execute(Constants.ID_GET_COMMENTS, getIntent().getIntExtra(Constants.RESOURCE_ID_EXTRA, 0));
    }
    /**
     * Método llamado cuando se reciben del servidor los comentarios de un determinado recurso
     * @param data El resultado de la petición al servidor
     */
    public void commentsObtained(Object data) {
        if(data instanceof Integer) {
            if((Integer) data ==  -2)
                FrequentMethods.serverNotResponding(this, true);
        } else if(data instanceof JSONArray) {
            JSONArray commentsObtained = (JSONArray) data;
            if(commentsObtained != null)
                this.result = commentsObtained;
            // Se cierra el progress dialog
            if(pdGettingComments != null)
                pdGettingComments.dismiss();
            // Una vez recibidos los comentarios, es necesario crear de forma programática las cajas que contendrán cada uno de ellos
            // El primer paso es obtener las referencias al layout
            setContentView(R.layout.resource_comments_layout);
            getReferences();
            createListener();
            // TODO prueba; para que todo esté equilibrado habrá que dar un peso a cada comentario pero el peso que dar a cada comentario habrá que verlo, ya que ponerlo de forma programática no es tan fácil
            LinearLayout llForAddComments = (LinearLayout) findViewById(R.id.llForAddComments);
            // Se recorre el array de comentarios
            if(commentsObtained.length() == 0) {
                // No existe ningún comentario. Se avisa al usuario a través de un AlertDialog
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.no_comments_title))
                        .setMessage(getString(R.string.no_comments))
                        .setPositiveButton(getString(R.string.addNewComment), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                launchNewCommentAlertDialog(true, false, null, 0, 0);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ResourceCommentsActivity.this.finish();
                            }
                        })
                        .setCancelable(false).
                        show();

            } else {
                // El bucle recorre de final a principio los comentarios para mostrar primero los más recientes
                for(int i = commentsObtained.length() - 1; i >= 0; i--) {
                    try {
                        final int numberResponses = commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT_RESPONSES).equals("null") ? 0 : commentsObtained.getJSONObject(i).getInt(Constants.JSON_COMMENT_RESPONSES);
                        final String name = commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT_NAME);

                        final int id = commentsObtained.getJSONObject(i).getInt(Constants.JSON_COMMENT_ID);
                        String date = FrequentMethods.parseDate(commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT_DATE));
                        // Hay que parsear la fecha.
                        // TODO Para cada elemento, hay que añadirlo al layout. Para ello, se puede utilizar el layout personalizado, que tiene bordes. Habrá que añadir posteriormente el icono de responder a un determinado recurso y ver como representar este estado en los comentarios
                        LinearLayoutOutlined lloForComment = (LinearLayoutOutlined) getLayoutInflater().inflate(R.layout.comment_template, null);
                        lloForComment.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                if(numberResponses != 0) {
                                    new AlertDialog.Builder(ResourceCommentsActivity.this)
                                            .setTitle(R.string.comment_actions_title)
                                            .setItems(R.array.comment_actions, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which){
                                                        case 0:
                                                            launchGetResponses(id);
                                                            break;
                                                        case 1:
                                                            launchNewCommentAlertDialog(false, true, name, id, numberResponses);
                                                            break;
                                                    }
                                                }
                                            })
                                            .show();
                                } else {
                                    new AlertDialog.Builder(ResourceCommentsActivity.this)
                                            .setTitle(R.string.comment_actions_title)
                                            .setItems(R.array.comment_actions_no_responses, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    launchNewCommentAlertDialog(false, true, name, id, numberResponses);
                                                }
                                            })
                                            .show();
                                }

                                return false;
                            }
                        });
                        TextView tvComment = (TextView) lloForComment.getChildAt(0);
                        LinearLayout aux = (LinearLayout) (lloForComment.getChildAt(1));
                        TextView tvName = (TextView) aux.getChildAt(0);
                        ImageView ivPoints = (ImageView) aux.getChildAt(1);

                        int drawable;
                        switch (numberResponses) {
                            case 0:
                                drawable = R.drawable.number_0;
                                break;
                            case 1:
                                drawable = R.drawable.number_1;
                                break;
                            case 2:
                                drawable = R.drawable.number_2;
                                break;
                            case 3:
                                drawable = R.drawable.number_3;
                                break;
                            case 4:
                                drawable = R.drawable.number_4;
                                break;
                            case 5:
                                drawable = R.drawable.number_5;
                                break;
                            case 6:
                                drawable = R.drawable.number_6;
                                break;
                            case 7:
                                drawable = R.drawable.number_7;
                                break;
                            case 8:
                                drawable = R.drawable.number_8;
                                break;
                            case 9:
                                drawable = R.drawable.number_9;
                                break;
                            default:
                                drawable = R.drawable.number_9;
                                break;
                        }
                        final Item[] items = {
                                new Item(getString(R.string.comment_respond_to), R.drawable.response),
                                new Item(getString(R.string.comment_see_answer_part_1) + numberResponses + getString(R.string.comment_see_answer_part_2), drawable),
                                new Item(getString(R.string.comment_delete), R.drawable.trash),
                                new Item(getString(R.string.comment_like_it), R.drawable.like),
                                new Item("Seguir las respuestas a este comentario", R.drawable.trash)
                        };

                        final ListAdapter adapter = new ArrayAdapter<Item>(
                                this,
                                android.R.layout.select_dialog_item,
                                android.R.id.text1,
                                items){
                            public View getView(int position, View convertView, ViewGroup parent) {
                                //User super class to create the View
                                View v = super.getView(position, convertView, parent);
                                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                                //Put the image on the TextView
                                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                                //Add margin between image and text (support various screen densities)
                                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                                tv.setCompoundDrawablePadding(dp5);

                                return v;
                            }
                        };
                        ivPoints.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(ResourceCommentsActivity.this)
                                        .setTitle("Selecciona una acción")
                                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                switch (item) {
                                                    case 0:
                                                        launchNewCommentAlertDialog(false, true, name, id, numberResponses);
                                                        break;
                                                    case 1:
                                                        launchGetResponses(id);
                                                        break;
                                                    case 2:
                                                        break;
                                                }
                                            }
                                        }).show();
                            }
                        });
                        if(name.equals("null"))
                            tvName.setText("Anónimo" + " el día " + date);
                        else
                            tvName.setText(commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT_NAME) + " el día " + date);
                        tvComment.setText(commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT));
                        LinearLayout emptyLinearLayout = new LinearLayout(this);
                        // Se añade el comentario
                        llForAddComments.addView(lloForComment);
                        // Se añade el LinearLayout vacío para crear márgenes entre los comentarios.
                        llForAddComments.addView(emptyLinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Obtiene las respuestas del comentario
     * @param id el identificador del comentario del que se quieren obtener las respuestas
     */
    private void launchGetResponses(int id) {
        ExecuteInBackground executeInBackground = new ExecuteInBackground(this);
        executeInBackground.execute(Constants.ID_GET_RESPONSES, id);
    }

    /**
     * Crea el método de escucha al pulsar el botón de añadir un nuevo comentario
     */
    private void createListener() {
        bAddNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewCommentAlertDialog(false, false, null, 0, 0);
            }
        });
    }

    /**
     * Crea un nuevo AlertDialog para que el usuario pueda introducir un nuevo comentario en el recurso
     */
    private void launchNewCommentAlertDialog(final boolean noComments, final boolean response, String name, final int commentID, final int numberResponses) {
        LinearLayout llForAddComment = new LinearLayout(this);
        LinearLayout llSecondLine = new LinearLayout(this);
        llSecondLine.setOrientation(LinearLayout.HORIZONTAL);
        llForAddComment.setOrientation(LinearLayout.VERTICAL);
        llSecondLine.setPadding(0,0,20,0);
        editTextCommentTitle = new EditText(this);
        editTextCommentTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAXIMUM_COMMENT_SIZE)});
        final TextView tvCount = new TextView(this);
        cbAddNameForComment = new CheckBox(this);
        cbAddNameForComment.setText(getString(R.string.checkbox_name_for_comment));
        cbAddNameForComment.setChecked(true);
        LinearLayout.LayoutParams tvCountParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams cbAddNameForCommentParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvCountParams.weight = 0.2f;
        cbAddNameForCommentParams.weight = 0.8f;
        tvCount.setGravity(Gravity.RIGHT);
        tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tvCount.setText(String.valueOf(Constants.MAXIMUM_COMMENT_SIZE));
        tvCount.setLayoutParams(tvCountParams);
        cbAddNameForComment.setLayoutParams(cbAddNameForCommentParams);
        llForAddComment.addView(editTextCommentTitle);
        llSecondLine.addView(cbAddNameForComment);
        llSecondLine.addView(tvCount);
        llForAddComment.addView(llSecondLine, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editTextCommentTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() >= Constants.MAXIMUM_COMMENT_SIZE) {
                    Toast.makeText(ResourceCommentsActivity.this, getResources().getString(R.string.toast_maximun_reach), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCount.setText(String.valueOf(Constants.MAXIMUM_COMMENT_SIZE - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        final EditText editTextComment = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(response) {
            if(name.equals("null")) {
                builder.setTitle("Respondiendo al comentario de Anónimo");
            } else {
                builder.setTitle("Respondiendo al comentario de " + name);
            }
        } else {
            builder.setTitle(getString(R.string.addNewComment));
        }
        builder.setPositiveButton(getString(R.string.addNewComment), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pdPuttingComment = new ProgressDialog(ResourceCommentsActivity.this);
                pdPuttingComment.setMessage(getString(R.string.pdPuttingComment));
                pdPuttingComment.show();
                ExecuteInBackground putNewComment = new ExecuteInBackground(ResourceCommentsActivity.this);
                if(response) {
                    if(cbAddNameForComment.isChecked()) {
                        putNewComment.execute(Constants.ID_REPLY_COMMENT, resourceID, editTextCommentTitle.getText().toString(), getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE).getString(Constants.SHARED_PREFERENCES_NAME, ""), commentID, numberResponses);
                    } else {
                        putNewComment.execute(Constants.ID_REPLY_COMMENT, resourceID, editTextCommentTitle.getText().toString(), null, commentID, numberResponses);
                    }
                } else {
                    if(cbAddNameForComment.isChecked()) {
                        putNewComment.execute(Constants.ID_PUT_COMMENT, resourceID, editTextCommentTitle.getText().toString(), getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE).getString(Constants.SHARED_PREFERENCES_NAME, ""));
                    } else {
                        putNewComment.execute(Constants.ID_PUT_COMMENT, resourceID, editTextCommentTitle.getText().toString(), null);
                    }
                }

            }
        })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(noComments)
                            ResourceCommentsActivity.this.finish();
                    }
                })
                .setView(llForAddComment)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Se almacena el JSON Array resultado para recuperarlo al rotar la pantalla
        outState.putString(Constants.JSON_RESULT_COMMENTS, this.result.toString());
        outState.putInt(Constants.RESOURCE_ID, resourceID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            // Se recupera el array resultado y se llama al método para crear el layout
            resourceID = savedInstanceState.getInt(Constants.RESOURCE_ID);
            this.result = new JSONArray(savedInstanceState.getString(Constants.JSON_RESULT_COMMENTS));
            commentsObtained(this.result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene las referencias a los elementos del layout
     */
    private void getReferences() {
        llCommentsResource = (LinearLayout) findViewById(R.id.llCommentsResource);
        bAddNewComment = (Button) findViewById(R.id.bAddNewComment);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Método llamado cuando se termina de almacenar un comentario en la base de datos del servidor
     * @param result 0 si se ha almacenado correctamente el comentario; 1 en caso de error
     */
    public void commentSave(Object result) {
        // Se elimina el progress dialog
        pdPuttingComment.dismiss();
        if(result instanceof Integer) {
            if((Integer) result == - 2) {
                FrequentMethods.serverNotResponding(this, false);
            }
        } else if(result instanceof JSONArray) {
            try {
                JSONArray resultArray = (JSONArray) result;
                JSONObject jsonData = resultArray.getJSONObject(0);
                // Se obtiene el resultado del acceso
                String success = jsonData.getString(Constants.JSON_SUCCESS);
                if(jsonData.getString(Constants.JSON_SUCCESS).equals("OK")) {
                    // Se almacena el comentario realizado en la base de datos local del móvil para mostrarlo al usuario
                    // TODO hay que pasarle el identificador del comentario.. hay que ver como obtenerlo
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm:ss");
                    Date date = new Date();
                    FrequentQuerys.saveNewComment(resourceID, editTextCommentTitle.getText().toString(), name, dateFormat.format(date), this);
                    // Se vuelve a realizar una petición al servidor de los comentarios para actualizar la vista y obtener los nuevos comentarios en caso de que se hayan añadido mientras se estaba escribiendo el nuevo
                    getComments();
                } else {
                    // TODO error saving the comment in the database
                }
            } catch(JSONException e) {

            }
        }
    }

    /**
     * Método llamado cuando se termina de obtener las respuestas a un determinado comentario
     * @param data un JSONArray que contendrá los comentarios de respuesta
     */
    public void getResponse(Object data) {
        if(data instanceof Integer) {
            if((Integer)data == -2) {
                FrequentMethods.serverNotResponding(this, false);
            }
        } else if(data instanceof JSONArray) {
            JSONArray result = (JSONArray) data;
            Intent responseActivity = new Intent(this, ResponseActivity.class);
            responseActivity.putExtra("JSON", result.toString());
            startActivity(responseActivity);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshIcon:
                getComments();
                break;
            default:
                FrequentMethods.onOptiomItemSelected(this, item.getItemId());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}