package WebtoonConsole.DAO;

import WebtoonConsole.Common.Common;
import WebtoonConsole.Common.GetVal;
import WebtoonConsole.VO.WebtoonVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebtoonDAO {
    GetVal gv = new GetVal();
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    String day = null;

    public List<WebtoonVO> webtoonDaySelect() {  // 요일별 웹툰 검색
        int dayInt = gv.getDay();
        this.day = String.valueOf(dayInt);

        String query0 = "SELECT w.*, g.genre_name " +
                        "FROM WEBTOON w JOIN GENRE g " +
                            "ON w.genre_num = g.genre_num " +
                        "WHERE w.webtoon_release_day = ? AND w.platform_num = 0 " +
                        "ORDER BY w.webtoon_subscribe_count DESC";
        String query1 = "SELECT w.*, g.genre_name " +
                        "FROM WEBTOON w JOIN GENRE g " +
                            "ON w.genre_num = g.genre_num " +
                        "WHERE w.webtoon_release_day = ? AND w.platform_num = 1 " +
                        "ORDER BY w.webtoon_view_count DESC";
        return executeWebtoonQuery(query0, query1, day);
    }

    public List<WebtoonVO> webtoonGenreSelect() {  // 요일별 웹툰 검색
        int genreInt = gv.getGenre();

        String query0 = "SELECT w.*, g.genre_name " +
                "FROM WEBTOON w JOIN GENRE g " +
                "ON w.genre_num = g.genre_num " +
                "WHERE w.genre_num = ? AND w.platform_num = 0 " +
                "ORDER BY w.webtoon_subscribe_count DESC";
        String query1 = "SELECT w.*, g.genre_name " +
                "FROM WEBTOON w JOIN GENRE g " +
                "ON w.genre_num = g.genre_num " +
                "WHERE w.genre_num = ? AND w.platform_num = 1 " +
                "ORDER BY w.webtoon_view_count DESC";
        return executeWebtoonQuery(query0, query1, genreInt);
    }

    public List<WebtoonVO> webtoonNoMemberRecommend() {  // 요일별 웹툰 검색
        String query0 = "SELECT w.*, g.genre_name " +
                "FROM WEBTOON w JOIN GENRE g " +
                "ON w.genre_num = g.genre_num " +
                "WHERE w.platform_num = 0 " +
                "ORDER BY w.webtoon_subscribe_count DESC";
        String query1 = "SELECT w.*, g.genre_name " +
                "FROM WEBTOON w JOIN GENRE g " +
                "ON w.genre_num = g.genre_num " +
                "WHERE w.platform_num = 1 " +
                "ORDER BY w.webtoon_view_count DESC";

        List<WebtoonVO> platform0List = executeWebtoonQuery(query0);
        List<WebtoonVO> platform1List = executeWebtoonQuery(query1);

        // 각각의 리스트에서 무작위로 9개씩 선택
        Collections.shuffle(platform0List);
        List<WebtoonVO> randomPlatform0List = platform0List.subList(0, Math.min(9, platform0List.size()));

        Collections.shuffle(platform1List);
        List<WebtoonVO> randomPlatform1List = platform1List.subList(0, Math.min(9, platform1List.size()));

        // 두 플랫폼 리스트를 합쳐서 반환
        List<WebtoonVO> resultList = new ArrayList<>();
        resultList.addAll(randomPlatform0List);
        resultList.addAll(randomPlatform1List);

        return resultList;
    }

    public List<WebtoonVO> webtoonMemberRecommend(String memberId) {  // 회원 웹툰 추천
        List<Integer> genreList = getFavoriteGenre(memberId);
        List<WebtoonVO> resultList = new ArrayList<>();
        String query0Head = null, query0Tail = null, query1Head = null, query1Tail = null, queryAdd = "";
        if (genreList.isEmpty()) {
            resultList = webtoonNoMemberRecommend();
            return resultList;
        } else {
            for (int i = 0; i < genreList.size(); i++) {
                queryAdd += "?";
                if (i < genreList.size() - 1) {queryAdd += ",";}
            }
            query0Head = "SELECT w.*, g.genre_name " +
                        "FROM WEBTOON w JOIN GENRE g " +
                        "ON w.genre_num = g.genre_num " +
                        "WHERE w.platform_num = 0 " +
                        "AND w.genre_num IN (";
            query0Tail = ") ORDER BY w.webtoon_subscribe_count DESC";
            query1Head = "SELECT w.*, g.genre_name " +
                        "FROM WEBTOON w JOIN GENRE g " +
                        "ON w.genre_num = g.genre_num " +
                        "WHERE w.platform_num = 1 " +
                        "AND w.genre_num IN (";
            query1Tail = ") ORDER BY w.webtoon_view_count DESC";

            List<WebtoonVO> platform0List = new ArrayList<>();
            List<WebtoonVO> platform1List = new ArrayList<>();
            try {
                conn = Common.getConnection();
                psmt = conn.prepareStatement(query0Head + queryAdd + query0Tail);
                for (int i = 0; i < genreList.size() ; i++) {
                    psmt.setInt(i+1, genreList.get(i));
                }
                rs = psmt.executeQuery();
                while (rs.next()) {
                    WebtoonVO vo = extractWebtoonDay(rs);
                    platform0List.add(vo);
                }

                conn = Common.getConnection();
                psmt = conn.prepareStatement(query1Head + queryAdd + query1Tail);
                for (int i = 0; i < genreList.size() ; i++) {
                    psmt.setInt(i+1, genreList.get(i));
                }
                rs = psmt.executeQuery();
                while (rs.next()) {
                    WebtoonVO vo = extractWebtoonDay(rs);
                    platform1List.add(vo);
                }
            } catch (Exception e) {
            } finally {
                Common.close(rs);
                Common.close(psmt);
                Common.close(conn);
            }

            // 각각의 리스트에서 무작위로 9개씩 선택
            Collections.shuffle(platform0List);
            List<WebtoonVO> randomPlatform0List = platform0List.subList(0, Math.min(9, platform0List.size()));

            Collections.shuffle(platform1List);
            List<WebtoonVO> randomPlatform1List = platform1List.subList(0, Math.min(9, platform1List.size()));

            // 두 플랫폼 리스트를 합쳐서 반환
            resultList.addAll(randomPlatform0List);
            resultList.addAll(randomPlatform1List);

            return resultList;
        }
    }

    public List<WebtoonVO> searchWebtoon(String searchKeyword) {
        List<WebtoonVO> searchList = new ArrayList<>();
        String query0 = "SELECT w.*, g.genre_name " +
                        "FROM WEBTOON w JOIN GENRE g " +
                        "ON w.genre_num = g.genre_num " +
                        "WHERE w.platform_num = 0 " +
                        "AND (w.webtoon_title LIKE ? OR w.webtoon_author LIKE ?) " +
                        "ORDER BY w.webtoon_subscribe_count DESC";
        String query1 = "SELECT w.*, g.genre_name " +
                        "FROM WEBTOON w JOIN GENRE g " +
                        "ON w.genre_num = g.genre_num " +
                        "WHERE w.platform_num = 1 " +
                        "AND (w.webtoon_title LIKE ? OR w.webtoon_author LIKE ?) " +
                        "ORDER BY w.webtoon_view_count DESC";

        try {
            String keywordWildcards = "%" + searchKeyword + "%";
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query0);
            psmt.setString(1, keywordWildcards);
            psmt.setString(2, keywordWildcards);
            rs = psmt.executeQuery();
            while (rs.next()) {
                WebtoonVO vo = extractWebtoonDay(rs);
                searchList.add(vo);
            }

            conn = Common.getConnection();
            psmt = conn.prepareStatement(query1);
            psmt.setString(1, keywordWildcards);
            psmt.setString(2, keywordWildcards);
            rs = psmt.executeQuery();
            while (rs.next()) {
                WebtoonVO vo = extractWebtoonDay(rs);
                searchList.add(vo);
            }

        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return searchList;
    }

    private List<WebtoonVO> executeWebtoonQuery(String query0, String query1, Object param) {
        List<WebtoonVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();

            // 네이버 쿼리 실행
            psmt = conn.prepareStatement(query0);
            psmt.setObject(1, param);  // psmt.setObject는 파라미터의 데이터 타입에 맞게 자동으로 처리
            rs = psmt.executeQuery();
            while (rs.next()) {
                WebtoonVO vo = extractWebtoonDay(rs);
                list.add(vo);
            }

            // 카카오 쿼리 실행
            psmt = conn.prepareStatement(query1);
            psmt.setObject(1, param);
            rs = psmt.executeQuery();
            while (rs.next()) {
                WebtoonVO vo = extractWebtoonDay(rs);
                list.add(vo);
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return list;
    }

    private List<WebtoonVO> executeWebtoonQuery(String query) {
        List<WebtoonVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            rs = psmt.executeQuery();

            while (rs.next()) {
                WebtoonVO vo = extractWebtoonDay(rs);
                list.add(vo);
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return list;
    }

    private WebtoonVO extractWebtoonDay(ResultSet rs) throws SQLException {
        int webtoonNum = rs.getInt("webtoon_num");
        String webtoonTitle = rs.getString("webtoon_title");
        String webtoonAuthor = rs.getString("webtoon_author");
        String genreName = rs.getString("genre_name");
        int webtoonAvailableAge = rs.getInt("webtoon_available_age");
        double webtoonRating = rs.getDouble("webtoon_rating");
        int webtoonSubscribeCount = rs.getInt("webtoon_subscribe_count");
        int webtoonViewCount = rs.getInt("webtoon_view_count");
        int webtoonReleaseDay = rs.getInt("webtoon_release_day");
        String webtoonPageUrl = rs.getString("webtoon_page_url");
        String webtoonThumbnailUrl = rs.getString("webtoon_thumbnail_url");
        int platformNum = rs.getInt("platform_num");

        // WebtoonVO 객체 생성 및 releaseDayStr 설정
        WebtoonVO vo = new WebtoonVO(webtoonNum, webtoonTitle, webtoonAuthor, genreName, webtoonAvailableAge,
                webtoonRating, webtoonSubscribeCount, webtoonViewCount, webtoonReleaseDay,
                webtoonPageUrl, webtoonThumbnailUrl, platformNum);
        vo.setReleaseDayStr(webtoonReleaseDay);  // 요일 문자열 설정
        return vo;
    }

    private List<Integer> getFavoriteGenre(String memberId) {
        List<Integer> genreList = new ArrayList<>();
        String query = "SELECT fg.genre_num " +
                       "FROM MEMBER m JOIN FAVORITE_GENRE fg ON m.member_num = fg.member_num " +
                       "WHERE m.member_id = ? ";
        try {
            conn = Common.getConnection();
            psmt = conn.prepareStatement(query);
            psmt.setString(1, memberId);
            rs = psmt.executeQuery();

            while (rs.next()) {
                int genreNum = rs.getInt("genre_num");
                genreList.add(genreNum);
            }
        } catch (Exception e) {
        } finally {
            Common.close(rs);
            Common.close(psmt);
            Common.close(conn);
        }
        return genreList;
    }

}
