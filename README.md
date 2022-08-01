## 영화정보 조회 및 관리 프로그램
#### 리팩토링 버전
<br>

#### 팀 프로젝트 기존 주소 
link: [프로젝트 깃 주소](https://github.com/sodamkimkim/DBprj_movieInfoSystem)
<br><br><br><br>

### 프로젝트 설명
* 데이터베이스를 이용한 영화정보 조회 및 관리 프로그램 ( 관리자용 )
* 영화정보, 배우정보, 스태프정보를 조회, 등록, 수정, 삭제 할 수 있는 프로그램.
<br><br><br><br>

## 기능
* ### 조회
<br><br>

1. 전체 조회<br>
무비, 배우, 스태프의 정보를 전체 조회 할 수 있다.<br><br>

2. 검색어 조회<br>
무비, 배우, 스태프의 정보를 영화제목, 이름 으로 조회 할 수 있다.<br><br>

3. 상세 조회<br>
더블 클릭시 상세정보를 조회 할 수 있다.<br><br><br>

* ### 등록
<br><br>
1. 새로운 무비, 배우, 스태프 를 등록 할 수 있다. <br>
중복된 값을 제거하기 위해서 무비는 제목과 감독이름으로 같은 값이 있는지 체크 후에 등록되도록 구현<br>
데이터 베이스에서 NOT NULL 인 필드는 View단에서 입력을 해야만 등록할 수 있도록 구현<br>
숫자를 입력해야하는 칸에 문자를 입력했을때 예외 처리<br><br><br>

* ### 수정
<br><br>
1. 무비, 배우, 스태프 를 수정 할 수 있다.<br>
JList에서 항목을 선택해야 form 패널로 이동할수 있으며, 이동을 하면 선택한 항목이 textField에 셋팅 되어있다.<br>
데이터 베이스에서 NOT NULL 인 필드는 View단에서 입력을 해야만 등록할 수 있도록 구현<br>
숫자를 입력해야하는 칸에 문자를 입력했을때 예외 처리<br><br><br>

* ### 삭제
<br><br>
1. 무비, 배우, 스태프 를 삭제 할 수 있다.<br>
JList에서 항목을 선택해야 해당 항목을 삭제할 수 있다.<br>

<br><br><br><br>

# 팀 프로젝트 : 영화 정보 관리 프로그램
**데이터 베이스에 접근하여 테이블을 직접 설계하여 CRUD를 중점으로 한 프로젝트** 

<br><br>

### 💁🏻‍♀️ 영화, 배우, 감독에 대한 정보를 관리할 수 있는 프로그램입니다.  

### ⌚ 프로젝트 기간

### 20220512 - 20220516 ( 5일 간 진행 )  

### 📂 프로젝트 설명

영화 제목, 개봉일, 줄거리, 별점 등 정보를 관리할 수 있고, 배우나 감독에 대한 정보 관리를 할 수 있다.  
<br><br><br><br>

# 1. 프로젝트 대표적인 기능

## 영화 | 배우 | 감독 정보 CRUD  

<br><br>


## 🌲 프로젝트 기능 중 나의 파트 🌲

**🚩 영화 정보 - DTO 설계   |   DAO 구현   |   CRUD 쿼리 작성   |   상세 보기   |   레이아웃** 

<br><br>


# 2. 프로젝트에 사용한 기술

## Back-End

Java | MySql

## Front-End

Java -Swing

<br><br>


# 3. 프로젝트 기능 상세 설명

# 조회 기능

### ✔️ 검색

검색할 때 textField의 문자열을 가져와서 비교한 후 검색어를 입력하지 않았을 때 오류 창을 띄웠습니다.

### ✔️ 전체 조회 기능

각각의 테이블 전체를 조회하여 리스트로 화면에 랜더링 했습니다.

### ✔️ 상세 보기

마우스 이벤트 리스너를 이용하여 더블 클릭을 감지해 상세 보기 창을 새로운 창으로 띄웠습니다.

<br><br>

| 조회 |
|------|
|<img src="https://user-images.githubusercontent.com/89136556/181675834-2ffe1296-fe6d-40d7-8875-cd01a6212d0c.gif"  width="400" height="300">|

<br><br><br><br>

# 등록 기능

### ✔️ 등록

### ✔️ 중복 오류 알림

영화 제목과 감독이름을 같이 비교하여 중복된 데이터가 있는지 검사 후에 insert하도록 방어코드를 설계했습니다.

<br><br>

| 등록 | 중복오류 |
|------|---|
|<img src="https://user-images.githubusercontent.com/89136556/181676040-bd3de7c5-a9a7-4e77-a199-3a468f01bf17.gif"  width="400" height="300">|<img src="https://user-images.githubusercontent.com/89136556/181676072-1ef4cf90-6a13-4947-bc48-9576edf85d39.gif"  width="400" height="300">|

<br><br><br><br>

# 수정 | 삭제 기능

| 수정 | 삭제 |
|------|---|
|<img src="https://user-images.githubusercontent.com/89136556/181676161-808c5ec5-64a3-4750-b69a-11c6915e08ce.gif"  width="400" height="300">|<img src="https://user-images.githubusercontent.com/89136556/181676186-4d61355d-6a8f-402d-a3ac-4445b85219e6.gif"  width="400" height="300">|

<br><br>


# 4. ERD
![movieinfo_erd](https://user-images.githubusercontent.com/89136556/182077718-0ddb2cd0-85f8-4fff-bbb9-1cb3a0caebe4.png)

