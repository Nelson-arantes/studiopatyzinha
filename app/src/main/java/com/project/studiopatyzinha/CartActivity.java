package com.project.studiopatyzinha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.studiopatyzinha.Adapter.HoraAdapterChoosing;
import com.project.studiopatyzinha.Adapter.Produtoadapter;
import com.project.studiopatyzinha.banco.bancolocal;
import com.project.studiopatyzinha.pattern.Horapattern;
import com.project.studiopatyzinha.pattern.Pedidospattern;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.project.studiopatyzinha.services.estouChegandoListenerService;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    public static List<Produtopattern> listItemCart = new ArrayList<>();
    AlertDialog a;
    Produtopattern mip = null;
    public RecyclerView rv_medicine_cart;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static Produtoadapter adapter;
    boolean contunar = true;
    ImageView top_back_carrinhoB;
    static TextView total;
    static TextView img_empty_rv_medicine_cart;
    static TextView quant_medicine_cartT;
    String formaDePagamento = "";
    public static String idpessoa;
    Button comprarProdutos_cartActivity;
    Map<String, Object> rotina;
    AlertDialog choosingHora;
    ArrayList<Horapattern> horalist, manhalist, tardelist, noitelist;
    DatePicker pickerdate;
    TextInputLayout notificarAs;
    Button agendarbnt;
    HoraAdapterChoosing adapterChoosing;
    Date lastdate;
    boolean adicionar;
    public static String horaescolhida;
    String datafinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        idpessoa = Login.pessoa.getId();
        horaescolhida = "";
        datafinal = "";
        lastdate = Calendar.getInstance().getTime();
        new Thread(() -> {
            top_back_carrinhoB = findViewById(R.id.top_back_carrinho);
            RadioGroup radioGrup_waypayment_cart = findViewById(R.id.radioGrup_waypayment_cart);
            comprarProdutos_cartActivity = findViewById(R.id.comprarProdutos_cartActivity);
            img_empty_rv_medicine_cart = findViewById(R.id.img_empty_rv_medicine_cart);
            total = findViewById(R.id.total_price_number_cartActivity);
            quant_medicine_cartT = findViewById(R.id.quant_medicine_cart);
            rv_medicine_cart = findViewById(R.id.rv_medicine_cart);
            listItemCart = bancolocal.getINSTANCE(getBaseContext()).getListCarrinho();
            if (listItemCart.size() != 0) {
                changeproducttxt(View.GONE);
            }
            GridLayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.VERTICAL, false);
            Intent intent = getIntent();
            String quantidade = intent.getStringExtra("quant");
            if (quantidade != null) {
                String formadePagamento = intent.getStringExtra("formaPagamento");
                String idsProdutos = intent.getStringExtra("idsProdutos");
                String valorTotal = intent.getStringExtra("valorTotal");
                runOnUiThread(() -> {
                    quant_medicine_cartT.setText(quantidade);
                    total.setText(valorTotal);
                });
                switch (formadePagamento) {
                    case "Dinheiro":
                        radioGrup_waypayment_cart.check(R.id.checkbox_dinheiro_cart);
                        break;
                    case "Pix":
                        radioGrup_waypayment_cart.check(R.id.checkbox_pix_cart);
                        break;
                    case "Cartao debito/credito":
                        radioGrup_waypayment_cart.check(R.id.checkbox_cartao_cart);
                        break;
                }


                Map<String, String> map = getidProduto(idsProdutos);
                listItemCart.clear();
                adapter = new Produtoadapter(listItemCart, "True", 25, "0", "", getBaseContext());
                for (int i = 0; i < (map.size() / 2); i++) {
                    String id = map.get("idproduto" + i);
                    int finalI = i;
                    db.collection("Produtos").whereEqualTo("idexterno", id).get().addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            mip = new Produtopattern();
                            Map<String, Object> map2 = snapshot.getData();
                            mip.setIdexterno(map2.get("idexterno") + "");
                            mip.setNome(map2.get("nome") + "");
                            mip.setModelo(map2.get("modelo") + "");
                            mip.setFabricante(map2.get("fabricante") + "");
                            mip.setDesconto(map2.get("desconto") + "");
                            mip.setPreco_original(map2.get("preco_original") + "");
                            mip.setQuant_per_cx(map2.get("quant_per_cx") + "");
                            mip.setUsosindicados(map2.get("usosindicados") + "");
                            mip.setUsosNindicados(map2.get("usosNindicados") + "");
                            mip.setBitmapImg(map2.get("bitmapImg") + "");
                            mip.setQuant_minima(map2.get("quant_minima") + "");
                            mip.setQuant_produto_interno(map2.get("quant_produto_interno") + "");
                            mip.setQuant_produto(map.get("quant" + finalI));
                            listItemCart.add(mip);
                            changeproducttxt(View.GONE);
                            adapter.notifyDataSetChanged();
                            setpricecart(getBaseContext(), map.get("quant" + finalI));
                        }

                    });
                }
            } else {
                adapter = new Produtoadapter(listItemCart, "True", 2, "0", "", getBaseContext());
            }
            runOnUiThread(() -> {
                rv_medicine_cart.setLayoutManager(layoutManager);
                rv_medicine_cart.setAdapter(adapter);
            });
            comprarProdutos_cartActivity.setOnClickListener(v -> {
                contunar = true;
                String idpedido = System.currentTimeMillis() + "";
                switch (radioGrup_waypayment_cart.getCheckedRadioButtonId()) {
                    case R.id.checkbox_dinheiro_cart:
                        formaDePagamento = "Dinheiro";
                        break;
                    case R.id.checkbox_pix_cart:
                        formaDePagamento = "Pix";
                        break;
                    case R.id.checkbox_cartao_cart:
                        formaDePagamento = "Cartao debito/credito";
                        break;
                }
                String idsProdutos = "";
                for (int i = 0; i < listItemCart.size(); i++) {
                    idsProdutos += listItemCart.get(i).getIdexterno() + ":" + listItemCart.get(i).getQuant_produto() + ";";
                }
                if (listItemCart.size() == 0) {
                    contunar = false;
                    Toast.makeText(getBaseContext(), "Seu carrinho esta vazio", Toast.LENGTH_LONG).show();
                }
                if (contunar) {


                    horalist = new ArrayList<>();
                    manhalist = new ArrayList<>();
                    tardelist = new ArrayList<>();
                    noitelist = new ArrayList<>();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.choosing_time_dialog, null, false);
                    pickerdate = view.findViewById(R.id.timerpickerdialog);
                    notificarAs = view.findViewById(R.id.notificarAs);
                    SimpleMaskFormatter numb = new SimpleMaskFormatter("NN:NN");
                    MaskTextWatcher mtwrg = new MaskTextWatcher(notificarAs.getEditText(), numb);
                    notificarAs.getEditText().addTextChangedListener(mtwrg);

                    agendarbnt = view.findViewById(R.id.agendarbnt);
                    view.findViewById(R.id.close_dialog_choosingdate).setOnClickListener(v1 -> {
                        choosingHora.dismiss();
                    });
                    view.findViewById(R.id.manhabnt).setOnClickListener(v1 -> {
                        for (int i = 0; i < tardelist.size(); i++) {
                            tardelist.get(i).setSelected("");
                        }
                        for (int i = 0; i < manhalist.size(); i++) {
                            manhalist.get(i).setSelected("");
                        }
                        for (int i = 0; i < noitelist.size(); i++) {
                            noitelist.get(i).setSelected("");
                        }
                        horalist.clear();
                        horalist.addAll(manhalist);
                        adapterChoosing.notifyDataSetChanged();
                    });

                    view.findViewById(R.id.tardebnt).setOnClickListener(v1 -> {
                        horalist.clear();
                        for (int i = 0; i < tardelist.size(); i++) {
                            tardelist.get(i).setSelected("");
                        }
                        for (int i = 0; i < manhalist.size(); i++) {
                            manhalist.get(i).setSelected("");
                        }
                        for (int i = 0; i < noitelist.size(); i++) {
                            noitelist.get(i).setSelected("");
                        }
                        horalist.addAll(tardelist);
                        adapterChoosing.notifyDataSetChanged();

                    });

                    view.findViewById(R.id.noitebnt).setOnClickListener(v1 -> {
                        horalist.clear();
                        for (int i = 0; i < tardelist.size(); i++) {
                            tardelist.get(i).setSelected("");
                        }
                        for (int i = 0; i < manhalist.size(); i++) {
                            manhalist.get(i).setSelected("");
                        }
                        for (int i = 0; i < noitelist.size(); i++) {
                            noitelist.get(i).setSelected("");
                        }
                        horalist.addAll(noitelist);
                        adapterChoosing.notifyDataSetChanged();

                    });

                    RecyclerView rv_choosing_hora = view.findViewById(R.id.rv_choosing_hora);
                    rv_choosing_hora.setLayoutManager(new GridLayoutManager(CartActivity.this, 3));
                    adapterChoosing = new HoraAdapterChoosing(horalist);
                    rv_choosing_hora.setAdapter(adapterChoosing);
                    pickerdate.setMinDate(System.currentTimeMillis());
                    pickerdate.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
                        int dia = calendar.get(Calendar.DAY_OF_WEEK);
                        switch (dia) {
                            case Calendar.SATURDAY:
                                if (testDate((Map<String, Object>) rotina.get("SAB"))) {
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(lastdate);
                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                } else {
                                    Map<String, Object> diamap = (Map<String, Object>) rotina.get("SAB");
                                    lastdate = calendar.getTime();
                                    changelisthora(diamap.get("abreas") + "", diamap.get("fechaas") + "");
                                }
                                break;
                            case Calendar.SUNDAY:
                                if (testDate((Map<String, Object>) rotina.get("DOM"))) {
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(lastdate);
                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                } else {
                                    Map<String, Object> diamap = (Map<String, Object>) rotina.get("DOM");
                                    lastdate = calendar.getTime();
                                    changelisthora(diamap.get("abreas") + "", diamap.get("fechaas") + "");

                                }
                                break;
                            case Calendar.MONDAY:
                                if (testDate((Map<String, Object>) rotina.get("SEG"))) {
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(lastdate);
                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                } else {
                                    Map<String, Object> diamap = (Map<String, Object>) rotina.get("SEG");
                                    lastdate = calendar.getTime();
                                    changelisthora(diamap.get("abreas") + "", diamap.get("fechaas") + "");
                                }
                                break;
                            case Calendar.TUESDAY:

                                if (testDate((Map<String, Object>) rotina.get("TER"))) {
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(lastdate);
                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                } else {
                                    Map<String, Object> diamap = (Map<String, Object>) rotina.get("TER");
                                    lastdate = calendar.getTime();
                                    changelisthora(diamap.get("abreas") + "", diamap.get("fechaas") + "");
                                }
                                break;
                            case Calendar.WEDNESDAY:
                                if (testDate((Map<String, Object>) rotina.get("QUA"))) {
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(lastdate);
                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                } else {
                                    Map<String, Object> diamap = (Map<String, Object>) rotina.get("QUA");
                                    lastdate = calendar.getTime();
                                    changelisthora(diamap.get("abreas") + "", diamap.get("fechaas") + "");
                                }
                                break;
                            case Calendar.THURSDAY:
                                if (testDate((Map<String, Object>) rotina.get("QUI"))) {
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(lastdate);
                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                } else {
                                    Map<String, Object> diamap = (Map<String, Object>) rotina.get("QUI");
                                    lastdate = calendar.getTime();
                                    changelisthora(diamap.get("abreas") + "", diamap.get("fechaas") + "");
                                }

                                break;
                            case Calendar.FRIDAY:
                                if (testDate((Map<String, Object>) rotina.get("SEX"))) {
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(lastdate);
                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                } else {
                                    Map<String, Object> diamap = (Map<String, Object>) rotina.get("SEX");
                                    lastdate = calendar.getTime();
                                    changelisthora(diamap.get("abreas") + "", diamap.get("fechaas") + "");
                                }
                                break;
                        }
                    });

                    db.collection("Rotina").get().addOnSuccessListener(snapshot -> {
                        snapshot.forEach(snapshot1 -> {
                            rotina = snapshot1.getData();
                            pickerdate.updateDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                        });
                    });


                    String finalIdsProdutos = idsProdutos;
                    agendarbnt.setOnClickListener(v1 -> {
                        boolean continuar = true;
                        if (horaescolhida.isEmpty()) {
                            Toast.makeText(this, "Escolha algum horario disponível", Toast.LENGTH_SHORT).show();
                        } else {
                            Calendar calendaragendado = Calendar.getInstance();
                            calendaragendado.setTime(lastdate);
                            calendaragendado.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaescolhida.split(":")[0]));
                            calendaragendado.set(Calendar.MINUTE, Integer.parseInt(horaescolhida.split(":")[1]));
                            calendaragendado.set(Calendar.SECOND, 0);
                            calendaragendado.set(Calendar.MILLISECOND, 0);
                            String id = System.currentTimeMillis() + "";
                            if (!notificarAs.getEditText().getText().toString().trim().isEmpty()) {
                                if (notificarAs.getEditText().getText().toString().trim().split(":").length > 1) {
                                    if (Integer.parseInt(notificarAs.getEditText().getText().toString().split(":")[0]) > 23 || Integer.parseInt(notificarAs.getEditText().getText().toString().split(":")[1]) > 59) {
                                        notificarAs.setError("Preencha esse campo corretamente");
                                        continuar = false;
                                    }
                                } else {
                                    continuar = false;
                                    notificarAs.setError("Preencha esse campo corretamente");
                                }
                            } else {
                                if (!notificarAs.getEditText().getText().toString().isEmpty()) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(lastdate);
                                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(notificarAs.getEditText().getText().toString().split(":")[0]));
                                    calendar.set(Calendar.MINUTE, Integer.parseInt(notificarAs.getEditText().getText().toString().split(":")[1]));
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MILLISECOND, 0);
                                    String channelid = "Assistente dos agendamentos";
                                    CharSequence name = "Assistente dos agendamentos";
                                    String description = "Mensagens lembrando dos futuros agendamentos";
                                    NotificationCompat.Builder buildernotification = new NotificationCompat.Builder(getBaseContext(), channelid);
                                    buildernotification.setSmallIcon(R.drawable.ic_laucher);
                                    buildernotification.setContentTitle("ESTÁ QUASE NA HORA!!!");
                                    buildernotification.setContentText("A sua retirada " + mip.getNome() + " das " + horaescolhida + " está te esperando");
                                    buildernotification.setPriority(NotificationCompat.PRIORITY_HIGH);
                                    buildernotification.setAutoCancel(false);
                                    buildernotification.setWhen(calendar.getTimeInMillis());
                                    NotificationChannel channel = new NotificationChannel(channelid, name, NotificationManager.IMPORTANCE_HIGH);
                                    channel.setDescription(description);
                                    Intent TaskIntent = new Intent(getBaseContext(), estouChegandoListenerService.class);
                                    TaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    TaskIntent.putExtra("id", id);
                                    PendingIntent TaskPIntent = PendingIntent.getActivity(getBaseContext(), 0, TaskIntent, PendingIntent.FLAG_IMMUTABLE);
                                    buildernotification.setContentIntent(TaskPIntent);
                                    buildernotification.addAction(R.drawable.tic_enviada, "Já estou chegando", TaskPIntent);
                                    buildernotification.setStyle(new NotificationCompat.BigTextStyle());
                                    NotificationManager notificationManager = getBaseContext().getSystemService(NotificationManager.class);
                                    notificationManager.createNotificationChannel(channel);
                                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getBaseContext());
                                    notificationManagerCompat.notify(Integer.parseInt(id.substring(7)), buildernotification.build());
                                } else {
                                    notificarAs.setError("Preencha esse campo");
                                    continuar = false;
                                }
                            }
                            if (continuar) {
                                continuar(idpedido, finalIdsProdutos, quantidade, calendaragendado.getTimeInMillis() + "");
                                choosingHora.dismiss();
                            }
                        }
                    });
                    builder.setView(view);
                    choosingHora = builder.create();
                    if (view.getParent() != null) {
                        ((ViewGroup) view.getParent()).removeAllViews();
                    } else {
                        choosingHora.setView(null);
                        choosingHora.setView(view);
                    }
                    choosingHora.show();


                }
            });
            top_back_carrinhoB.setOnClickListener(v -> {
                finish();
            });
            if (listItemCart.size() > 1) {
                quant_medicine_cartT.setText(listItemCart.size() + " itens");
            } else if (listItemCart.size() == 1) {
                quant_medicine_cartT.setText(listItemCart.size() + " item");
            } else {
                quant_medicine_cartT.setText("");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setpricecart(getBaseContext(), quantidade);
                }
            });
        }).start();

    }


    private void continuar(String idpedido, String idsProdutos, String quantidade, String dataRetirada) {
        Pedidospattern a = new Pedidospattern(quant_medicine_cartT.getText().toString(), System.currentTimeMillis() + "", idpedido,
                total.getText().toString().replace("R$ ", "").replace(",", "."), formaDePagamento, "Processando", idsProdutos, dataRetirada, "false");
        a.setProdutosString(idsProdutos);
        a.setIdPessoa(Login.pessoa.getId());
        String dia = "";
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            dia += "0";
        }
        dia += calendar.get(Calendar.DAY_OF_MONTH);
        String mes = "";
        if ((calendar.get(Calendar.MONTH) + 1) < 10) {
            mes += "0";
        }
        mes += calendar.get(Calendar.MONTH) + 1;
        String dateselected = dia + "/" + mes + "/" + calendar.get(Calendar.YEAR);
        a.setDia(dateselected);
        a.setNomePessoa(Login.pessoa.getNome());
        db.collection("Pedidos").document(idpedido).set(a).addOnSuccessListener(command -> {//pedido feito //mudar a quantidade no estoque de cada produto
            if (quantidade != null) {
                adapter.notifyDataSetChanged();
            }
            for (int i = 0; i < listItemCart.size(); i++) {
                String id = listItemCart.get(i).getIdexterno();
                String quantproduto = listItemCart.get(i).getQuant_produto();
                int finalI = i;
                db.collection("Produtos").document(id).get().addOnSuccessListener(command1 -> {
                    String quantidade_interna = (String) command1.get("quant_produto_interno");
                    int novovalor = Integer.parseInt(quantidade_interna) - Integer.parseInt(quantproduto);
                    String teste = quantcheck(finalI, listItemCart.size());
                    if (teste.length() > 28) {
                        Toast.makeText(getBaseContext(), teste, Toast.LENGTH_SHORT).show();
                    } else {
                        HashMap<String, String> map = new HashMap<>();
                        for (int b = 0; b < listItemCart.size(); b++) {
                            map.put("idproduto" + b, listItemCart.get(b).getIdexterno());
                            map.put("quant" + b, "" + novovalor);
                        }
                        fazer_Pedido(map);
                    }

                });

            }
        });
    }


    public static void setpricecart(Context context, String quantidade) {
        double subtotalint = 0.f;
        if (quantidade != null) {

            if (listItemCart.size() == 1) {
                quant_medicine_cartT.setText(listItemCart.size() + " item");
            } else {
                quant_medicine_cartT.setText(listItemCart.size() + " itens");
            }
            if (listItemCart.size() == 0) {
                quant_medicine_cartT.setText("Carrinho vazio");
            }
            for (int i = 0; i < listItemCart.size(); i++) {
                double a = 0;
                if (!listItemCart.get(i).getDesconto().contains("0.f")) {
                    a = Integer.parseInt(listItemCart.get(i).getQuant_produto()) * Float.parseFloat(listItemCart.get(i).getPreco_original());
                } else {
                    String preco = String.valueOf(listItemCart.get(i).getPreco_original());
                    DecimalFormat df = new DecimalFormat("#.00");
                    String restul = df.format(Float.parseFloat(preco));
                    a = Integer.parseInt(listItemCart.get(i).getQuant_produto()) * Double.parseDouble(restul.replace(",", "."));
                }
                subtotalint += a;
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if (subtotalint == 0.0) {
                total.setText("R$ 0,00");
            } else {
                total.setText("R$ " + df.format(subtotalint).replace(".", ","));
            }
        } else {
            ArrayList<Produtopattern> listaexterna = bancolocal.getINSTANCE(context).getListCarrinho();
            if (listaexterna.size() < 2) {
                quant_medicine_cartT.setText(listaexterna.size() + " item");
            } else {
                quant_medicine_cartT.setText(listaexterna.size() + " itens");
            }
            if (listaexterna.size() == 0) {
                quant_medicine_cartT.setText("Carrinho vazio");

            }

            for (int i = 0; i < listaexterna.size(); i++) {
                double a = 0;
                if (!listaexterna.get(i).getDesconto().contains("0.f")) {
                    a = Integer.parseInt(listaexterna.get(i).getQuant_produto()) * Float.parseFloat(listaexterna.get(i).getDesconto());
                } else {
                    String preco = String.valueOf(listaexterna.get(i).getPreco_original());
                    DecimalFormat df = new DecimalFormat("#.00");
                    String restul = df.format(Float.parseFloat(preco));
                    a = Integer.parseInt(listaexterna.get(i).getQuant_produto()) * Double.parseDouble(restul.replace(",", "."));
                }
                subtotalint += a;

            }
            DecimalFormat df = new DecimalFormat("##.00");
            if (subtotalint == 0.0) {
                total.setText("R$ 0,00");
            } else {
                total.setText("R$ " + df.format(subtotalint).replace(".", ","));

            }
        }
    }


    private Map<String, String> getidProduto(String idComQuantidade) {
        String a = "";
        Map<String, String> map = new HashMap<>();

        int quant = 0;
        for (int i = 0; i != idComQuantidade.length(); i++) {
            String sss = String.valueOf(idComQuantidade.charAt(i));
            if (sss.equals(":")) {
                map.put("idproduto" + quant, a);
                a = "";
            } else if (sss.equals(";")) {
                map.put("quant" + quant, a);
                quant++;
                a = "";
            } else {
                a += sss;
            }
        }
        return map;
    }

    private String quantcheck(int atual, int total) {
        String nome = "";
        if (atual == total) {
            nome = "Quantidade indisponivel de ";
            for (int a = 0; a < listItemCart.size(); a++) {

                if (Integer.parseInt(listItemCart.get(a).getQuant_produto_interno()) < 0) {
                    if (a != listItemCart.size()) {
                        nome += listItemCart.get(a).getNome() + ", ";
                    } else {
                        nome += listItemCart.get(a).getNome();
                    }
                }
            }
        }
        return nome;
    }

    private void fazer_Pedido(HashMap<String, String> map) {
        listItemCart.clear();
        bancolocal.getINSTANCE(getBaseContext()).deletelistCarrinho();
        for (int a = 0; a < (map.size() / 2); a++) {
            String id = map.get("idproduto" + a);
            String novaquant = map.get("quant" + a);
            Map<String, Object> map2 = new HashMap<>();
            map2.put("quant_produto_interno", novaquant + "");
            db.collection("Produtos").document(id).update(map2);
        }
        if (Pedidos.s != null) {
            Pedidos.s.setText("");
        }
        finish();
    }

    public static void changeproducttxt(int sa) {
        img_empty_rv_medicine_cart.setVisibility(sa);
    }


    private boolean testDate(Map<String, Object> diamap) {
        boolean folga = false;
        if (Boolean.parseBoolean(diamap.get("iffechado") + "")) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(lastdate);
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
            lastdate = calendar1.getTime();
            pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
            folga = true;
        }
        return folga;
    }

    private void changelisthora(String comeco, String fim) {
        horalist.clear();
        manhalist.clear();
        noitelist.clear();
        tardelist.clear();
        Calendar comecocalendar = Calendar.getInstance();
        Calendar fimcalendar = Calendar.getInstance();
        fimcalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fim.split(":")[0]));
        fimcalendar.set(Calendar.MINUTE, Integer.parseInt(fim.split(":")[1]));

        comecocalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(comeco.split(":")[0]));
        comecocalendar.set(Calendar.MINUTE, Integer.parseInt(comeco.split(":")[1]));
        int partedodia = 0;//0 manha //1 tarde // 2 noite
        adicionar = true;
        while (fimcalendar.compareTo(comecocalendar) > 0) {
            String minutostring = "";
            String horastring = "";
            if (comecocalendar.get(Calendar.MINUTE) == 0) {
                minutostring += "0";
            }
            minutostring += comecocalendar.get(Calendar.MINUTE);
            if (comecocalendar.get(Calendar.HOUR_OF_DAY) < 10) {
                horastring += "0";
            }
            horastring += comecocalendar.get(Calendar.HOUR_OF_DAY);
            String horafinal = horastring + ":" + minutostring;
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(lastdate);

            String dia = "";

            if (calendar2.get(Calendar.DAY_OF_MONTH) < 10) {
                dia += "0";
            }
            dia += calendar2.get(Calendar.DAY_OF_MONTH);


            String mes = "";

            if ((calendar2.get(Calendar.MONTH) + 1) < 10) {
                mes += "0";
            }
            mes += calendar2.get(Calendar.MONTH) + 1;


            datafinal = dia + "/" + mes + "/" + calendar2.get(Calendar.YEAR);
            if ((horafinal).equals("12:00")) {
                partedodia++;
            }
            if ((horafinal).equals("18:00")) {
                partedodia++;
            }
            if (adicionar) {
                switch (partedodia) {
                    case 0:
                        manhalist.add(new Horapattern(horafinal, false, ""));
                        break;
                    case 1:
                        tardelist.add(new Horapattern(horafinal, false, ""));
                        break;
                    case 2:
                        noitelist.add(new Horapattern(horafinal, false, ""));
                        break;
                }
            }
            comecocalendar.add(Calendar.MINUTE, 15);
        }
        horalist.addAll(manhalist);
        adapterChoosing.notifyDataSetChanged();

    }

}