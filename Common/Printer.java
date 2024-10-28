package WebtoonConsole.Common;

import WebtoonConsole.VO.WebtoonVO;

import java.util.List;

public class Printer {
    public void webtoonDayResult(List<WebtoonVO> list) {
        printWebtoonList(list, "day");  // 요일별 웹툰 목록 출력
    }

    public void webtoonGenreResult(List<WebtoonVO> list) {
        printWebtoonList(list, "genre");  // 장르별 웹툰 목록 출력
    }

    public void webtoonRecommendResult(List<WebtoonVO> list) {
        printWebtoonList(list, "recommend");  // 추천 웹툰 목록 출력
    }

    public void printSearchResult(List<WebtoonVO> list, String searchKeyword) {
        if (list.isEmpty()) {
            System.out.println("\"" + searchKeyword + "\" 관련 검색 결과가 없습니다.");
        } else {
            System.out.println("\"" + searchKeyword + "\" 관련 검색 결과입니다.");
            printWebtoonList(list, "search");
        }
    }

    private void printWebtoonList(List<WebtoonVO> list, String headerType) {
        int countPlatform0 = 0;
        int countPlatform1 = 0;
        String header = null;

        for (WebtoonVO e : list) {
            int platformNum = e.getPlatformNum();
            String availableAgeStr = (e.getWebtoonAvailableAge() == 0) ? "전체이용가" : e.getWebtoonAvailableAge() + "세 이용가";
            String platformName = (platformNum == 0) ? "네이버" : "카카오";
            switch (headerType) {
                case "day": header = e.getReleaseDayStr() + " 요일별 웹툰 목록"; break;
                case "genre": header = e.getGenreName() + " 장르별 웹툰 목록"; break;
                case "recommend": header = "추천 웹툰 목록"; break;
                case "search" : header = "결과 목록"; break;
            }

            // 플랫폼별 첫 항목일 때 헤더 출력
            if (platformNum == 0 && countPlatform0 < 9) {
                if (countPlatform0 == 0) {
                    System.out.println(">> " + platformName + " " + header + " <<");
                }
                printWebtoonDetails(e, availableAgeStr);
                countPlatform0++;
            } else if (platformNum == 1 && countPlatform1 < 9) {
                if (countPlatform1 == 0) {
                    System.out.println(">> " + platformName + " " + header + " <<");
                }
                printWebtoonDetails(e, availableAgeStr);
                countPlatform1++;
            }

            // 두 플랫폼 모두 9개씩 출력되면 루프 종료
            if (countPlatform0 >= 9 && countPlatform1 >= 9) {
                break;
            }
        }
    }

    private void printWebtoonDetails(WebtoonVO e, String availableAgeStr) {
        System.out.print("[" + e.getWebtoonTitle() + "] ");
        System.out.print(e.getGenreName() + " ");
        System.out.print(availableAgeStr + " ");
        System.out.print(e.getWebtoonRating() + " ");
        System.out.println();
    }
}
