package com.example.medium_term_forecast_app;



import static com.example.medium_term_forecast_app.MainActivity.weatherMap;
import static com.example.medium_term_forecast_app.WeatherFragment.now;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    static ArrayList<ArrayList<String>> items = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> item = items.get(position);
        holder.setItem(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(ArrayList<String> item) { items.add(item); }
    public void clear() { items.clear(); }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView leftText1, leftText2, afterText;
        TextView rightText1, rightText2;
        ImageView leftImage, rightImage;
        long n;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftText1 = itemView.findViewById(R.id.leftText1);
            leftText2 = itemView.findViewById(R.id.leftText2);
            afterText = itemView.findViewById(R.id.afterText);
            rightText1 = itemView.findViewById(R.id.rightText1);
            rightText2 = itemView.findViewById(R.id.rightText2);
            leftImage = itemView.findViewById(R.id.leftImage);
            rightImage = itemView.findViewById(R.id.rightImage);
            n = Long.valueOf(now);
        }
        public void setItem(ArrayList<String> item, int pos) {
            // 0~4, 총 5개는 ex) rnSt6Am, rnSt6Pm, wf6Am, wf6Pm 순으로 저장.
            // 5~7, 총 3개는 ex) rnSt7, wf7 순으로 저장.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREAN);
            LocalDate date = LocalDate.parse(now, formatter);
            LocalDate newDate = date.plusDays(pos+3);
            String val = newDate.format(formatter);
            if (pos < 5) {
                String wl = item.get(2);
                rightImage.setVisibility(View.VISIBLE);
                rightText1.setVisibility(View.VISIBLE);
                rightText2.setVisibility(View.VISIBLE);
                afterText.setText(String.format("%d일 후\n%s\n", pos+3, val));
                leftText1.setText(String.format("강수 %s%%",item.get(0)));
                leftText2.setText(wl);
                leftImage.setImageResource(returnId(wl));

                String wl2 = item.get(3);
                rightText1.setText(String.format("강수 %s%%",item.get(1)));
                rightText2.setText(wl2);
                rightImage.setImageResource(returnId(wl2));
            } else {
                afterText.setText(String.format("%d일 후\n%s", pos+3, val));
                String wl = item.get(1);
                leftText1.setText(String.format("강수 %s%%",item.get(0)));
                leftText2.setText(item.get(1));
                leftImage.setImageResource(returnId(wl));
                rightImage.setVisibility(View.INVISIBLE);
                rightText1.setVisibility(View.INVISIBLE);
                rightText2.setVisibility(View.INVISIBLE);
            }
        }
        private int returnId(String s) {
            int result = -1;
            switch (s) {
                case "맑음":
                    result = weatherMap.get("맑음");
                    break;
                case "구름많음":
                    result = weatherMap.get("구름많음");
                    break;
                case "구름많고 비":
                    result = weatherMap.get("비");
                    break;
                case "구름많고 눈":
                    result = weatherMap.get("눈");
                    break;
                case "구름많고 비/눈":
                    result = weatherMap.get("비/눈");
                    break;
                case "구름많고 소나기":
                    result = weatherMap.get("소나기");
                    break;
                case "흐림":
                    result = weatherMap.get("흐림");
                    break;
                case "흐리고 비":
                    result = weatherMap.get("비");
                    break;
                case "흐리고 눈":
                    result = weatherMap.get("눈");
                    break;
                case "흐리고 비/눈":
                    result = weatherMap.get("비/눈");
                    break;
                case "흐리고 소나기":
                    result = weatherMap.get("소나기");
                    break;
                default:
                    result = weatherMap.get("예외");
                    break;
            }
            return result;
        }
    }
}
