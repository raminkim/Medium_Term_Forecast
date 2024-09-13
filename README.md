# Medium_Term_Forecast
기상청 중기예보 API로 날씨 예보를 가져와 사용자에게 보여주는 앱

##  프로젝트 소개
해당 앱은 지역, 현재 시각 기준 24시간 내의 날짜와 발표시각을 입력받습니다.

입력 날짜 기준 3일 후부터 7일 후까지의 당일의 오전/오후 강수확률과 날씨를 제공하며, 
입력 날짜 기준 8일 후부터 10일후까지는 당일의 강수확률과 날씨를 제공합니다.

날씨에 맞춰 이미지 또한 제공합니다. Ex) 맑음, 구름 많음, 흐림, 눈, 비, 소나기 등
<img src=https://github.com/user-attachments/assets/aaef816c-d8fb-47a7-86bb-d7a0c12d966c width="500" height="400"/>
<img src=https://github.com/user-attachments/assets/5cdf35e1-f485-4942-94f8-3e324850727c width="500" height="400"/>

## 앱 사용 전 세팅
"WeatherFragment.java"파일에서 createURL function에 사용자 자신이 발급 받은 키를 입력하면 된다.
즉, createURL function의 "Enter your API key here" 대신 발급 받은 키를 채워넣으면 된다.
이때, 발급 받은 키는 아래 출처에 있는 API 링크인 "기상청_중기예보 조회서비스"를 활용신청해 발급 받은 키를 사용한다.


## 앱 사용 설명
① 지역을 직접 입력할 수도 있지만, 잘못된 지역 입력 시, 토스트창을 띄우며 지운다.<br>
‘지역 선택‘ 버튼 클릭 시 가능한 지역들의 리스트 뷰가 나온다.<br>
이때, 원하는 지역을 입력하면 지역 입력창에 자동 입력된다.<br>
<img src=https://github.com/user-attachments/assets/05a4e1fe-c236-4428-96b9-e7feaab52dde width="300" height="400"/>

② 날짜를 직접 입력이 가능하지만, 잘못된 입력 시 토스트창을 띄우며 지운다.<br>
‘날짜 선택’버튼 클릭 시 캘린더가 나오며 원하는 날짜 선택 후, 확인 시 자동 입력된다.<br>
<img src=https://github.com/user-attachments/assets/b9c6c9cc-2491-4d5e-bdd7-35163c621293 width="250" height="300"/>

③ 원하는 발표 시각까지 선택하고, 데이터 요청 시, 안내창이 뜬다.<br>
대신, 현재시각으로부터 24시간전까지만 데이터 요청 가능.<br>
<img src=https://github.com/user-attachments/assets/fb037b0e-9944-4072-8454-f26edae9a16f width="250" height="150"/>

<b>(주의) 요청한 날짜 및 시각이 발표시각로부터 얼마 안된 경우, 업데이트가 안되어 요청이 안될 가능성이 있을 수 있음.<b>

## 📅 만든 기간
- 2023.12.18(월) ~ 2023.12.22(금)
  
## 💻 개발 환경
- **Version** : Java 22
- **IDE** : Android Studio
- **Framework** : Android

## 출처
① 날씨 아이콘 출처: 공공누리 날씨 아이콘<br>
https://www.weather.go.kr/weather/icon_info.html

② 사용 API 출처: 기상청_중기예보 조회 서비스<br>
https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15059468
