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
import com.album.Client1;
import com.client.Client;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static androidx.core.app.ActivityCompat.startActivityForResult;

class open_dir {
    private GridView list_dir;
    private TextView textPath;
    private TextView code;
    private Button btnGO;
    private Context _context;
    private int select_id_list = -1;
    private LinearLayout panel;
    private TextView textFolder;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    private String genCode = null;
    private EditText newName;
    private Client client;
    public static int userNumber;

    ArrayList<String> ArrayDir = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    protected void onCreate(Context con, View page, final int userNumber) {
        _context = con;
        list_dir = (GridView) page.findViewById(R.id.grid_view);
        textPath = (TextView) page.findViewById(R.id.toPath);
        code = (TextView) page.findViewById(R.id.textCode);
        btnGO = (Button) page.findViewById(R.id.btnGO);
        textFolder = (TextView) page.findViewById(R.id.lineforuser0);
        adapter = new ArrayAdapter<String>(_context, android.R.layout.simple_list_item_1, ArrayDir);
        panel = (LinearLayout) page.findViewById(R.id.panelSize);
        list_dir.setAdapter(adapter);
        open_dir.userNumber = userNumber;
        update_list_dir();
        list_dir.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ArrayDir.get((int) id).substring(ArrayDir.get((int) id).lastIndexOf(".") + 1).equals("jpg")) {
                    ShowDialogBox(ArrayDir.get((int) id), path + ArrayDir.get((int) id));
                } else if (ArrayDir.get((int) id).equals("<--")) onClickBack();
                else {
                    select_id_list = (int) id;
                    update_list_dir();
                }
            }
        });
        list_dir.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertdialog(ArrayDir.get((int) id));
                return true;
            }
        });
        switch (userNumber) {
            case 1:
                textFolder = (TextView) page.findViewById(R.id.lineforuser0);
                ViewGroup.LayoutParams paramnull0 = (ViewGroup.LayoutParams) textFolder.getLayoutParams();
                ViewGroup.LayoutParams paramnull = (ViewGroup.LayoutParams) page.findViewById(R.id.lineforuser1).getLayoutParams();
                paramnull0.height = 0;
                paramnull0.width = 0;
                paramnull.height = 0;
                paramnull.width = 0;
                textFolder.setLayoutParams(paramnull0);
                page.findViewById(R.id.lineforuser1).setLayoutParams(paramnull);
                page.findViewById(R.id.lineforuser2).setLayoutParams(paramnull);
                if (genCode != null) {
                    setGcode(genCode);
                }
                final Switch switcher = (Switch) page.findViewById(R.id.switcher);
                final SeekBar seek = (SeekBar) page.findViewById(R.id.seekBar);
                final TextView percent = (TextView) page.findViewById(R.id.percent);
                switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ViewGroup.LayoutParams paramforseek = (ViewGroup.LayoutParams) seek.getLayoutParams();
                        ViewGroup.LayoutParams paramforprcnt = (ViewGroup.LayoutParams) percent.getLayoutParams();
                        ViewGroup.LayoutParams paramforline = (ViewGroup.LayoutParams) panel.getLayoutParams();
                        if (isChecked) {
                            switcher.setText("Качество фото");
                            paramforline.height = 100;
                            paramforseek.width = 1000;
                            paramforseek.height = 110;
                            paramforprcnt.width = 600;
                            paramforprcnt.height = 70;
                            seek.setLayoutParams(paramforseek);
                            percent.setLayoutParams(paramforprcnt);
                            panel.setLayoutParams(paramforline);
                        } else {
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
                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        percent.setText(String.valueOf(seekBar.getProgress()));
                        Client1.compressionQuality = seekBar.getProgress();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                btnGO.setOnClickListener(v -> {
                    client = new Client(userNumber, UUID.randomUUID().toString());
                    genCode = client.getToken();
                    setGcode(genCode);
                });
                break;
            case 2:
                ViewGroup.LayoutParams paramfornull = panel.getLayoutParams();
                paramfornull.height = 0;
                paramfornull.width = 0;
                panel.setLayoutParams(paramfornull);
                btnGO.setText("Загрузить сюда");
                btnGO.setOnClickListener(v -> {
                    //TODO: сделать загрузку файлов с сервера
                    btnGO.setText("Отправить изменения");
                    textFolder.setText("Загруженная папка:");
                });
                break;
        }
    }

    private void setGcode(String gcode) {
        ViewGroup.LayoutParams params = code.getLayoutParams();
        ViewGroup.LayoutParams paramsforbtn = btnGO.getLayoutParams();
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
            } else {
                path = tmp;
            }
            update_list_dir();
        }
    }

    private void update_list_dir() {
        if (select_id_list != -1) {
            path = path + ArrayDir.get(select_id_list) + "/";
        }
        select_id_list = -1;
        ArrayDir.clear();
        File[] files = new File(path).listFiles();
        System.out.println("path= " + path);
        if (files == null) {
            files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()).listFiles();
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        }
        if (!path.equals(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"))
            ArrayDir.add("<--");
        for (File aFile : files) {
            if (aFile.isDirectory() | aFile.getName().substring(aFile.getName().lastIndexOf(".") + 1).equals("jpg")) {
                if (dir_opened(aFile.getPath())) {
                    ArrayDir.add(aFile.getName());
                } else
                    ArrayDir.add(aFile.getName());
            }
        }
        adapter.notifyDataSetChanged();
        textPath.setText(path);
    }

    private boolean dir_opened(String url) {
        try {
            File[] files = new File(url).listFiles();
            for (@SuppressWarnings("unused") File aFile : files) {
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void alertdialog(final String filename) {
        final CharSequence[] items = {"Создать папку", "Удалить", "Отправить папку", "Копировать", "Вырезать", "Вставить", "Переименовать", "Информация"};//имена методов Ваших в списке
        final Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("Выберите функцию");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(DialogInterface dialog, int item) {
                //TODO: Добавить все изменения для файлов и папок
                if (item == 0) { //"Создать папку"
                    dialogcreatefolder();
                }
                if (item == 1) {
                    //method
                }
                if (item == 2) {
                    //method
                }
                if (item == 3) {
                    //method
                }
                if (item == 4) {
                    //method
                }
                if (item == 5) {
                    //method
                }
                if (item == 6) {
                    //method
                    dialogname(filename);
                }
                if (item == 7) {
                    //method
                    dialoginfo(filename);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dialogname(final String filename) {
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

            }
        });
        newName = (EditText) builder.show().findViewById(R.id.newName);
        newName.setText(filename);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dialogcreatefolder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("Введите название папки").setView(R.layout.rename_dialog);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.out.println(newName.getText());
                //TODO: добавить фу-ю создания папки
                update_list_dir();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        newName = (EditText) builder.show().findViewById(R.id.newName);
        newName.setText("");
    }

    private void dialoginfo(String filename) {
        //TODO: добавить определение информации о папках и файлах
        final AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("Информация");
        java.io.File file = new java.io.File(path + filename);
        builder.setMessage("Название: " + filename + "\n" + "Вес: " + file.length());
        builder.show();
    }

    private void rename(String filename, String newFilename) {
        java.io.File file = new java.io.File(path + filename);
        file.renameTo(new java.io.File(path + newFilename));
    }

    public void ShowDialogBox(String img_name, String img_path) {
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