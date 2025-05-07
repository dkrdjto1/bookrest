package com.example.myapplication.api;

import com.example.myapplication.vo.InterParkBookVo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterParkRestAPI {
    /**
     * 카테고리 ID : 100 국내도서
     *              200 외국도서
     *              300 음반
     *              400 DVD
     * 카테고리 ID	카테고리명
     * 100 	국내도서
     * 101 	국내도서>소설
     * 102 	국내도서>시/에세이
     * 103 	국내도서>예술/대중문화
     * 104 	국내도서>사회과학
     * 105 	국내도서>역사와 문화
     * 107 	국내도서>잡지
     * 108 	국내도서>만화
     * 109 	국내도서>유아
     * 110 	국내도서>아동
     * 111 	국내도서>가정과 생활
     * 112 	국내도서>청소년
     * 113 	국내도서>초등학습서
     * 114 	국내도서>고등학습서
     * 115 	국내도서>국어/외국어/사전
     * 116 	국내도서>자연과 과학
     * 117 	국내도서>경제경영
     * 118 	국내도서>자기계발
     * 119 	국내도서>인문
     * 120 	국내도서>종교/역학
     * 122 	국내도서>컴퓨터/인터넷
     * 123 	국내도서>자격서/수험서
     * 124 	국내도서>취미/레저
     * 125 	국내도서>전공도서/대학교재
     * 126 	국내도서>건강/뷰티
     * 128 	국내도서>여행
     * 129 	국내도서>중등학습서
     * 200 	외국도서
     * 201 	외국도서>어린이
     * 203 	외국도서>ELT/사전
     * 205 	외국도서>문학
     * 206 	외국도서>경영/인문
     * 207 	외국도서>예술/디자인
     * 208 	외국도서>실용
     * 209 	외국도서>해외잡지
     * 210 	외국도서>대학교재/전문서적
     * 211 	외국도서>컴퓨터
     * 214 	외국도서>일본도서
     * 215 	외국도서>프랑스도서
     * 216 	외국도서>중국도서
     * 217 	외국도서>해외주문원서
     * 300 	음반>
     * 301 	음반>가요
     * 302 	음반>Pop
     * 303 	음반>Rock
     * 304 	음반>일본음악
     * 305 	음반>World Music
     * 306 	음반>Jazz
     * 307 	음반>클래식
     * 308 	음반>국악
     * 309 	음반>뉴에이지/명상
     * 310 	음반>O.S.T
     * 311 	음반>종교음악
     * 312 	음반>유아/아동/태교
     * 313 	음반>수입음반
     * 314 	음반>액세서리/관련상품
     * 315 	음반>뮤직 DVD
     * 314 	음반>액세서리/관련상품
     * 315 	음반>뮤직 DVD
     * 319 	음반>해외구매
     * 320 	음반>LP
     * 400 	DVD>
     * 409 	DVD>애니메이션
     * 411 	DVD>다큐멘터리
     * 412 	DVD>TV시리즈
     * 417 	DVD>건강/취미/스포츠
     * 425 	DVD>영화
     * 426 	DVD>해외구매
     * 427 	DVD>기타
     * 428 	DVD>블루레이
     * 429 	DVD>유아동/교육DVD
     * 430 	DVD>EBS 교육용
     *
     * 베스트 셀러
     *
     * http://book.interpark.com/bookPark/html/bookpinion/openup_3.html
     *
     * @param key
     * @param categoryId
     * @param output
     * @return
     */
    @GET("api/bestSeller.api")
    Call<InterParkBookVo> getBestSellerApi(@Query("key") String key, @Query("categoryId") String categoryId, @Query("output") String output);

    /**
     * 추천도서
     *
     * http://book.interpark.com/bookPark/html/bookpinion/openup_3.html
     *
     * @param key
     * @param categoryId
     * @param output
     * @return
     */
    @GET("api/recommend.api")
    Call<InterParkBookVo> getRecommendApi(@Query("key") String key, @Query("categoryId") String categoryId, @Query("output") String output);

    /**
     * 신간도서
     *
     * http://book.interpark.com/bookPark/html/bookpinion/openup_3.html
     *
     * @param key
     * @param categoryId
     * @param output
     * @return
     */
    @GET("api/newBook.api")
    Call<InterParkBookVo> getNewBookApi(@Query("key") String key, @Query("categoryId") String categoryId, @Query("output") String output);

    /**
     * 요청 변수    변수 옵션   설명
     * queryType
     *              title(기본값) : 제목검색
     *              author : 저자검색
     *              publisher : 출판사검색
     *              isbn : Isbn검색
     *              productNumber : 상품번호
     *              all : 제목, 저자, 출판사,ISBN 검색 대상
     *              검색어 종류에 따른 세부 설정을 합니다.
     * searchTarget
     *              book(기본값) : 국내도서
     *              foreign : 외국도서
     *              cd : 음반
     *              dvd : DVD
     *              검색하고자 하는 상품 구분을 설정합니다.
     * start
     *              1이상, 양의 정수(기본값:1)
     *              검색 결과의 시작 페이지의 페이지 번호를 설정합니다.
     * maxResults
     *              1이상 100이하, 양의 정수(기본값:10)
     *              검색 결과의 한페이지당 최대 출력 개수를 설정합니다.
     * sort
     *              accuracy(기본값) : 정확도순
     *              publishTime : 출간일
     *              title : 제목
     *              salesPoint : 판매량
     *              customerRating : 고객평점
     *              reviewCount : 리뷰갯수
     *              price : 가격오름순
     *              priceDesc : 가격내림순
     *              검색 결과의 정렬 방법을 설정합니다.
     * categoryId
     *              분야의 고유 번호(기본값:100 )
     *              지정한 카테고리에서만 검색 되도록 설정합니다.
     *              *searchTarget를 설정 하였을 경우, searchTarget의 변수종류 하위에 속하지 않는 분야번호는 검색되지 않습니다.
     * output
     *              xml(기본값) : REST XML형식
     *              json : JSON방식
     *              검색 결과의 출력 방식을 설정합니다.
     * inputEncoding
     *              utf-8(기본값)
     *              euc-kr
     *              검색의의 인코팅 값을 설정합니다.
     * callback
     *              javascript function이름
     *              호출후 불려질 javascript function의 이름을 지정합니다.
     *              * output이 json인 경우에 한해 작동됩니다.
     * adultImageExposure
     *              n(기본값) : 노출안함
     *              y : 노출함
     *              성인 이미지의 노출 여부를 설정합니다.
     * soldOut
     *              y(기본값) : 품절/절판함께보기
     *              n : 품절/절판빼고보기
     *             품절/절판 상품 노출 여부를 설정합니다.
     *
     *  책 검색
     * http://book.interpark.com/bookPark/html/bookpinion/api_booksearch.html
     *
     * @param key
     * @param query
     * @param queryType
     * @param searchTarget
     * @param start
     * @param maxResults
     * @param sort
     * @param categoryId
     * @param output
     * @param inputEncoding
     * @param callback
     * @param adultImageExposure
     * @param soldOut
     * @return
     */
    @GET("api/search.api")
    Call<InterParkBookVo> getSearchApi(@Query("key") String key
            , @Query(value = "query", encoded = true) String query
            , @Query("queryType") String queryType
            , @Query("searchTarget") String searchTarget
            , @Query("start") String start
            , @Query("maxResults") String maxResults
            , @Query("sort") String sort
            , @Query("categoryId") String categoryId
            , @Query("output") String output
            , @Query("inputEncoding") String inputEncoding
            , @Query("callback") String callback
            , @Query("adultImageExposure") String adultImageExposure
            , @Query("soldOut") String soldOut);
}
