package com.example.medium_term_forecast_app;

import static android.app.Activity.RESULT_OK;
import static com.example.medium_term_forecast_app.MainActivity.codeMap;
import static com.example.medium_term_forecast_app.MainActivity.weatherMap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int REQUEST_CODE_LIST = 101;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static TextView dateText;
    static DateFormat dateFormat;
    Handler handler = new Handler(Looper.getMainLooper());
    String urlStr;
    static String now, pre;
    static EditText locateEditText, editText;
    static RequestQueue requestQueue;


    WeatherAdapter adapter;
    RecyclerView recyclerView;
    private static Context mContext;
    static RadioGroup radioGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View l = inflater.inflate(R.layout.fragment_weather, container, false);

        Calendar cal = new GregorianCalendar();
        dateText = l.findViewById(R.id.dateText);

        // 현재시간을 넣는 코드.
        TimeZone tz;
        dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 (시간 안맞으면 새로고침)", Locale.KOREAN);
        DateFormat dateFormat2 = new SimpleDateFormat("yyyyMM", Locale.KOREAN);
        DateFormat dateFormat3 = new SimpleDateFormat("dd", Locale.KOREAN);
        tz = TimeZone.getTimeZone("Asia/Seoul");
        dateFormat.setTimeZone(tz);
        Date date = new Date();
        dateText.setText("현재시간: " + dateFormat.format(date).toString());
        int n = Integer.parseInt(dateFormat3.format(date).toString());
        int p = n-1;
        now = dateFormat2.format(date).toString();
        pre = now + String.valueOf(p);
        now += String.valueOf(n);
        // 위치 locateEditText가 변경될 시.
        locateEditText = l.findViewById(R.id.locateEditText);
        locateEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String locate = v.getText().toString();
                if (!codeMap.containsKey(locate)) { locateEditText.setText(""); printToast("입력하신 지역이 올바르지 않습니다. 확인 후 재입력 바랍니다."); }
                return false;
            }
        });

        Button codeButton = l.findViewById(R.id.codeButton);
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LIST);
            }
        });

        // 날짜 editText가 변경될 시, 현재 시간~하루 전까지의 조건에 맞지 않으면 editText의 값을 빈칸으로 되돌린다.
        editText = l.findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String d = v.getText().toString().replace("-", "");
//                printToast(String.valueOf(d.compareTo(now)) + " " + String.valueOf(d.compareTo(pre)));
                if (d.compareTo(now) > 0 || d.compareTo(pre) < 0) {
                    printToast("가능한 입력은 현재시간 ~ 24시간 전까지입니다. 현재시간 확인 후 재입력 바랍니다.");
                    editText.setText("");
                } else {
                    editText.setText(d); // -를 제거해준 상태로 update
                }
                return false;
            }
        });
        Button dateButton = l.findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        Button enterButton = l.findViewById(R.id.enterButton);
        radioGroup = l.findViewById(R.id.radioGroup);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage();
            }
        });

        requestQueue = Volley.newRequestQueue(l.getContext());

        // 리사이클러뷰 들어갈 레이아웃 설정.
        recyclerView = l.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WeatherAdapter();
        recyclerView.setAdapter(adapter);

        return l;
    }
    private void processResponse(String res) {
        adapter.clear();
        adapter.notifyDataSetChanged();
        Gson gson = new Gson();
        ResponseDict responsedict = gson.fromJson(res, ResponseDict.class);
        int resultCode = Integer.valueOf(responsedict.response.header.resultCode.toString());
//        printToast(String.valueOf(resultCode));
        if (resultCode == 99) {
            printToast("가능한 입력은 현재시간 기준 24시 전까지입니다. 현재시간 확인 후 재입력 바랍니다.");
        } else if (resultCode == 3) {
            printToast("입력에 맞는 데이터가 갱신되지 않았거나, 입력이 조건에 맞지 않습니다..");
        } else {
            printToast("정상적으로 요청 보냄");
            Item item = responsedict.response.body.items.item.get(0);

            ArrayList<ArrayList<String>> arrayList = new ArrayList<>();

            arrayList.add(new ArrayList<String>(Arrays.asList(String.valueOf(item.rnSt3Am), String.valueOf(item.rnSt3Pm), item.wf3Am, item.wf3Pm)));
            arrayList.add(new ArrayList<String>(Arrays.asList(String.valueOf(item.rnSt4Am), String.valueOf(item.rnSt4Pm), item.wf4Am, item.wf4Pm)));
            arrayList.add(new ArrayList<String>(Arrays.asList(String.valueOf(item.rnSt5Am), String.valueOf(item.rnSt5Pm), item.wf5Am, item.wf5Pm)));
            arrayList.add(new ArrayList<String>(Arrays.asList(String.valueOf(item.rnSt6Am), String.valueOf(item.rnSt6Pm), item.wf6Am, item.wf6Pm)));
            arrayList.add(new ArrayList<String>(Arrays.asList(String.valueOf(item.rnSt7Am), String.valueOf(item.rnSt7Pm), item.wf7Am, item.wf7Pm)));
            arrayList.add(new ArrayList<String>(Arrays.asList(String.valueOf(item.rnSt8), item.wf8)));
            arrayList.add(new ArrayList<String>(Arrays.asList(String.valueOf(item.rnSt9), item.wf9)));
            arrayList.add(new ArrayList<String>(Arrays.asList(String.valueOf(item.rnSt10), item.wf10)));
            for (int i = 0; i < 8; i++) {
//                textView.append(arrayList.get(i).toString());
                adapter.addItem(arrayList.get(i));
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void makeRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, urlStr, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    processResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printToast("에러 : " + error);
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void createURL(String reg, String time) throws UnsupportedEncodingException {
        urlStr = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst" +
                "?serviceKey=" + "Enter your API key here" + // 발급 받은 API 키를 입력하기. +
                "&numOfRows=100" +
                "&pageNo=1" +
                "&dataType=JSON" +
                "&regId=" + reg +
                "&tmFc=" + time;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void printToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public void showDatePicker(View v) {
        DialogFragment dialogFragment = new DateFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    public void processDatePickerResult(int Y, int M, int D) {
        String Y_s = Integer.toString(Y);
        String M_s = String.format("%02d", M+1);
        String D_s = String.format("%02d", D);
        String d = Y_s + M_s + D_s;
        if (d.compareTo(now) > 0 || d.compareTo(pre) < 0) {
            printToast("가능한 입력은 현재시간 ~ 24시간 전까지입니다. 재선택 바랍니다.");
            editText.setText("");
        } else {
            editText.setText(d);
        }
    }

    public void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("요청 전 안내");
        builder.setMessage(String.format("%s 데이터를 요청하시겠습니까?", locateEditText.getText().toString()));
        builder.setIcon(weatherMap.get("예외"));

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String loc = locateEditText.getText().toString();
                String d = editText.getText().toString();
                if (!codeMap.containsKey(loc)) { locateEditText.setText(""); printToast("입력하신 지역이 올바르지 않습니다. 확인 후 재입력 바랍니다."); return;}
                else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton) { d+="0600"; }
                else { d+="1800"; }
                try {
                    createURL(codeMap.get(loc), d);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                makeRequest();
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                printToast("요청을 취소하였습니다.");
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                printToast("아니오 버튼을 누르셨습니다.");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LIST) {
            if (resultCode == RESULT_OK) {
                String val = data.getStringExtra("val");
                locateEditText.setText(val);
            }
        }
    }

    public static void time_refresh() {
        Date date = new Date();
        dateText.setText("현재시간: " + dateFormat.format(date).toString());
    }
}