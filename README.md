### Bookrest

📖 인터파크 API를 활용한 독서 기록 및 관리 앱 <북레스트> 입니다

## Environment

- JDK 8 (1.8.0_151)
- Android 9.0 (Pie), Android Studio 4.1

## How to Run:

```bash
git clone https://github.com/dkrdjto1/bookrest.git
cd bookrest
```

### 1. 실제 스마트폰 기기에서 실행하기

> 스마트폰 기기를 USB로 연결하여 앱을 실행할 수 있습니다.

1. PC에 `{$기기 제조사} 드라이버` 설치 (ex: [Samsung Android USB Driver](https://developer.samsung.com/android-usb-driver))
2. 스마트폰 개발자 모드 활성화
```bash
- 1. 설정 → 휴대전화 정보 → 소프트웨어 정보
- 2. 빌드번호 7번 클릭
```
3. 설정 → `개발자 옵션` 클릭
4. `USB 디버깅` 옵션 활성화
5. USB로 PC-스마트폰 연결 후 Android Studio 디바이스 목록에 내 기기가 뜨면 연동 완료

### 2. Android Virtual Device (AVD) 에서 실행하기

> Android Studio의 AVD를 사용하여 앱을 실행할 수 있습니다.

1. AVD Manager → `Create Virtual Device`
```bash
- Pixel 3 : 5.46 1080x2160 xxhdpi
- Pie : Android 9.0 x86
- Startup orientation : Portrait
```

## Tree

```bash
.
├── README.md
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
├── gradle/wrapper/
├── .idea/
└── app/
    ├── build.gradle
    └── src/
        └── main/
            ├── java/com/example/myapplication/
            │   ├── MainActivity.java  # main
            │   ├── activity/
            │   │   ├── BestSellerAndReCommendAllListActivity.java  # 베스트셀러 전체보기 & 추천도서 더보기 Activity
            │   │   ├── BookAllListActivity.java     # 카테고리 탭의 전체보기 Activity
            │   │   ├── BookSearchListActivity.java  # 카테고리 탭의 검색 Activity
            │   │   ├── DetailActivity.java          # 도서 상세보기 Activity
            │   │   ├── MemoActivity.java            # 독서기록 탭의 메모 기능 Activity
            │   │   └── ReadingRecdActivity.java     # 독서기록 탭의 도서 상세보기 Activity
            │   ├── adapter/
            │   │   ├── BaseViewHolder.java  # RecyclerView 공통 기능
            │   │   ├── LibraryAdapter.java  # 서재 탭 도서 이미지 그리드뷰 표기
            │   │   ├── recycler/
            │   │   │   ├── BookAllListRecyclerViewAdapter.java    # 전체보기/더보기 & 검색 데이터 UI 연결
            │   │   │   ├── BookListRecyclerViewAdapter.java       # 베스트셀러 & 카테고리 탭 UI 연결
            │   │   │   ├── HorizontalRecyclerViewAdapter.java     # 베스트셀러 & 카테고리 탭 도서 이미지 가로뷰 UI 연결
            │   │   │   ├── MemoRecyclerViewAdapter.java           # 도서별 메모 리스트 UI 연결
            │   │   │   └── ReadingRecordRecyclerViewAdapter.java  # 독서기록 전체 리스트 UI 연결
            │   │   └── tab/
            │   │       └── TabPagerAdapter.java  # 각 탭에 해당하는 Fragment 표기 (탭-페이지 연결)
            │   ├── api/
            │   │   └── InterParkRestAPI.java  # 인터파크 REST API Reference
            │   ├── db/sqllite/
            │   │   ├── SQLiteDataBase.java  # SQLite DB 생성 (서재, 독서기록 table)
            │   │   └── SQLiteHelper.java    # DB 쿼리
            │   ├── fragment/
            │   │   ├── BestSellerFragment.java     # 베스트셀러 탭 페이지 Fragment
            │   │   ├── CategoryFragment.java       # 카테고리 탭 페이지 Fragment
            │   │   ├── LibraryFragment.java        # 서재 탭 페이지 Fragment
            │   │   └── ReadingRecordFragment.java  # 독서기록 탭 페이지 Fragment
            │   ├── listener/
            │   │   └── PaginationListener.java  # 카테고리 탭 전체보기/검색 페이징 처리 (스크롤)
            │   ├── receiver/
            │   │   └── AlarmReceiver.java  # 메모 알림 기능
            │   ├── util/
            │   │   └── RangeTimePickerDialog.java  # 알림 날짜/시간 선택 다이얼로그
            │   └── vo/
            │       ├── InterParkBookVo.java  # 인터파크 API 응답 vo
            │       ├── InterParkItemVo.java  # 인터파크 API 응답 맵핑 & 서재 DB용 vo
            │       └── ReadingVo.java        # 독서기록 DB용 독서기록 상세정보 vo
            ├── res
            │   ├── drawable/      # 이미지 리소스
            │   ├── mipmap/        # 런처 아이콘
            │   ├── values/        # (라이트 모드) 색상, 문자열, 스타일 리소스
            │   ├── values-night/  # (다크 모드) 색상, 문자열, 스타일 리소스
            │   ├── layout/        # 앱 UI 정의
            │   └── menu/          # 앱 메뉴 정의
            │
            └── AndroidManifest.xml  # 앱 권한, 컴포넌트 등 설정 정보
```
