package com.example.photonetalbum;
import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class open_dir {
    GridView list_dir;
    TextView textPath;
    TextView code;
    Button btnGO;
    Context _context;
    int select_id_list = -1;
    LinearLayout panel;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
    String genCode = null;
    EditText newName;

    ArrayList<String> ArrayDir = new ArrayList<String>( );
    ArrayAdapter<String> adapter;

    protected void onCreate(Context con, View page, int userNumber) {
        _context = con;
        list_dir = (GridView) page.findViewById(R.id.grid_view);
        textPath = (TextView) page.findViewById(R.id.toPath);
        code = (TextView) page.findViewById(R.id.textCode);
        btnGO = (Button)  page.findViewById(R.id.btnGO);
        adapter = new ArrayAdapter<String>(_context, android.R.layout.simple_list_item_1, ArrayDir);
        panel = (LinearLayout) page.findViewById(R.id.panelSize);
        list_dir.setAdapter(adapter);
        update_list_dir();
        list_dir.setOnItemClickListener(new OnItemClickListener( ) {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ArrayDir.get((int)id).substring(ArrayDir.get((int)id).lastIndexOf(".") + 1).equals("jpg")){
                    ShowDialogBox(ArrayDir.get((int)id), path+ArrayDir.get((int)id));
                }
                else if (ArrayDir.get((int)id).equals("<--")) onClickBack();
                else {
                    select_id_list = (int) id;
                    update_list_dir( );
                }
            }
        });
        list_dir.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener( ) {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertdialog(ArrayDir.get((int)id));
                return true;
            }
        });
        switch (userNumber) {
            case 1:
                if (genCode != null) {
                    setGcode(genCode);
                }
                final Switch switcher = (Switch) page.findViewById(R.id.switcher);
                final SeekBar seek = (SeekBar) page.findViewById(R.id.seekBar);
                final TextView percent = (TextView) page.findViewById(R.id.percent);
                switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener( ) {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ViewGroup.LayoutParams paramforseek = (ViewGroup.LayoutParams) seek.getLayoutParams( );
                        ViewGroup.LayoutParams paramforprcnt = (ViewGroup.LayoutParams) percent.getLayoutParams( );
                        ViewGroup.LayoutParams paramforline = (ViewGroup.LayoutParams) panel.getLayoutParams( );
                        if (isChecked){
                            switcher.setText("Качество фото");
                            paramforline.height = 100;
                            paramforseek.width = 1000;
                            paramforseek.height = 110;
                            paramforprcnt.width = 600;
                            paramforprcnt.height = 70;
                            seek.setLayoutParams(paramforseek);
                            percent.setLayoutParams(paramforprcnt);
                            panel.setLayoutParams(paramforline);
                        }
                        else {
                            switcher.setText("Изменять размер фотографий ?");
                            paramforline.height = 85;
                            paramforseek.width = 0;
                            paramforprcnt.width = 0;
                            paramforseek.height = 0;
                            paramforprcnt.height = 0;
                            seek.setLayoutParams(paramforseek);
                            percent.setLayoutParams(paramforprcnt);
                            panel.setLayoutParams(paramforline);
                        }
                    }
                });
                percent.setText("90");
                seek.setProgress(90);
                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener( ) {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        percent.setText(String.valueOf(seekBar.getProgress()));
                        //TODO: Считать сжатие отсюда
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                btnGO.setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                        //TODO: сделать запуск сессии и вывод кода
                        genCode = "2f4s5";
                        setGcode(genCode);
                    }
                });
                break;
            case 2:
                ViewGroup.LayoutParams paramfornull = (ViewGroup.LayoutParams) panel.getLayoutParams( );
                paramfornull.height = 0;
                paramfornull.width = 0;
                panel.setLayoutParams(paramfornull);
                btnGO.setText("Загрузить");
                btnGO.setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                        //TODO: сделать загрузку файлов с сервера
                        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) code.getLayoutParams( );
                        params.height = 70;
                        code.setLayoutParams(params);
                        code.setText("Jlen");
                    }
                });
                break;
        }
    }

    private void setGcode(String gcode){
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) code.getLayoutParams( );
        ViewGroup.LayoutParams paramsforbtn = (ViewGroup.LayoutParams) btnGO.getLayoutParams( );
        paramsforbtn.height = 0;
        paramsforbtn.width = 0;
        params.height = 130;
        btnGO.setLayoutParams(paramsforbtn);
        code.setLayoutParams(params);
        code.setText(gcode);
    }

    public void onClickBack() {
        String tmp = (new File(path)).getParent();
        System.out.println("back");
        if (tmp != null) {
            if (!tmp.equals("/")) {
                path = tmp + "/";
            }
            else {
                path = tmp;
            }
            update_list_dir();
        }
    }

    private void update_list_dir() {
        if (select_id_list != -1)
        {
            path = path + ArrayDir.get(select_id_list) + "/";
        }
        select_id_list = -1;
        ArrayDir.clear();
        File[] files = new File(path).listFiles();
        System.out.println("path= "+path);
        if (files == null) {
            files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()).listFiles();
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        }
        if (!path.equals(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"))
            ArrayDir.add("<--");
        for (File aFile : files) {
            if (aFile.isDirectory( ) | aFile.getName().substring(aFile.getName().lastIndexOf(".") + 1).equals("jpg")){
                if (dir_opened(aFile.getPath( ))){
                    ArrayDir.add(aFile.getName( ));
                }
                else
                    ArrayDir.add(aFile.getName( ));
            }
        }
        adapter.notifyDataSetChanged( );
        textPath.setText(path);
    }

    private boolean dir_opened(String url) {
        try {
            File[] files = new File(url).listFiles( );
            for (@SuppressWarnings("unused") File aFile : files) {
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void alertdialog(final String filename) {
        final CharSequence[] items = {"Переименовать","два","три"};//имена методов Ваших в списке
        final Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("Выберите функцию");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) { //"один"
                    //rename(filename);
                    dialogname(filename);
                }
                if (item == 1) { //"два"
                    //method
                }
                if (item == 2) { //"три"
                    //method
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dialogname(final String filename){
        final AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("Введите новое название").setView(R.layout.rename_dialog);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.out.println(newName.getText());
                //TODO: поменять фун-ю ренаме
                rename(filename, newName.getText().toString());
                update_list_dir();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //_context.getDialog().cancel();
            }
        });
        newName = (EditText) builder.show().findViewById(R.id.newName);
        newName.setText(filename);
    }

    private void rename(String filename, String newFilename){
        java.io.File file = new java.io.File(path+filename); // создаем объект на файл test.txt
        file.renameTo(new java.io.File(path+newFilename));
    }

    public void ShowDialogBox(String img_name, String img_path){
        final Dialog dialog = new Dialog(_context);
        dialog.setContentView(R.layout.image);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView Image_name = dialog.findViewById(R.id.txt_Image_name);
        ImageView Image = dialog.findViewById(R.id.img);
        Image_name.setText(img_name);

        Image.setImageURI(Uri.parse(img_path));

        dialog.show();
    }
}