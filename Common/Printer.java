package WebtoonConsole.Common;

import WebtoonConsole.DAO.PostDAO;
import WebtoonConsole.VO.PostVO;
import WebtoonConsole.VO.ReplyVO;
import WebtoonConsole.VO.WebtoonVO;

import java.util.ArrayList;
import java.util.List;

public class Printer {
    public void webtoonDayResult(List<WebtoonVO> list) {
        printWebtoonList(list, "day");  // 요일별 웹툰 목록 출력
    }

    public void webtoonGenreResult(List<WebtoonVO> list) {
        printWebtoonList(list, "genre");  // 장르별 웹툰 목록 출력
    }

    public void webtoonRecommendResult(List<WebtoonVO> list, String memberNickname) {
        if (memberNickname != null) {
            System.out.println("☆" + memberNickname + "님☆만을 위한 추천 목록");
        }
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
        String previousTitle = null;

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
                    System.out.println("<< " + platformName + " " + header + " >>");
                }
                if (!e.getWebtoonTitle().equals(previousTitle)) {
                    printWebtoonDetails(e, availableAgeStr);
                    countPlatform0++;
                    previousTitle = e.getWebtoonTitle();
                }
            } else if (platformNum == 1 && countPlatform1 < 9) {
                if (countPlatform1 == 0) {
                    System.out.println("<< " + platformName + " " + header + " >>");
                }
                if (!e.getWebtoonTitle().equals(previousTitle)) {
                    printWebtoonDetails(e, availableAgeStr);
                    countPlatform1++;
                    previousTitle = e.getWebtoonTitle();
                }
            }

            // 두 플랫폼 모두 9개씩 출력되면 루프 종료
            if (countPlatform0 >= 9 && countPlatform1 >= 9) {
                break;
            }
        }
    }

    private void printWebtoonDetails(WebtoonVO e, String availableAgeStr) {
        System.out.printf("%-25s ", e.getWebtoonTitle());
        System.out.printf("%-10s ", e.getGenreName());
        System.out.printf("%-10s ", availableAgeStr);
        System.out.printf("%-8.2f ", e.getWebtoonRating());
        System.out.printf("%-50s ", e.getWebtoonAuthor());
        System.out.println();

//        System.out.print("[" + e.getWebtoonTitle() + "] ");
//        System.out.print(e.getGenreName() + " ");
//        System.out.print(availableAgeStr + " ");
//        System.out.print(e.getWebtoonRating() + " ");
//        System.out.println();
    }

    public List<Integer> printPostList(List<PostVO> list, String boardChoice) {
        List<Integer> postNum = new ArrayList<>();
        PostDAO postDAO = new PostDAO();
        String header = null;
        int postIdx = 1;
        if (boardChoice.equals("1")) { header = "<<공지 게시판>>"; }
        else if (boardChoice.equals("2")) { header = "<<자유 게시판>>"; }
//        System.out.println("-".repeat(100));
        System.out.println(" ".repeat(20) + header);
//        System.out.println("-".repeat(100));
        if (list.isEmpty()) {
            System.out.println("게시글이 없습니다.");
        } else {
            System.out.printf("     %-18s %-10s %5s%n", "제목", "작성자", "조회수");
            for (PostVO e : list) {
                postNum.add(e.getPostNum());
                System.out.printf("(%d) ", postIdx++);
                System.out.printf("%-20s ", e.getPostTitle());
                String memberNickname = postDAO.getMemberNickname(e.getMemberNum());
                System.out.printf("%-10s ", memberNickname);
                System.out.printf("%5d", e.getPostVisit());
                System.out.println();
            }
        }
        return postNum;
    }

    public void printPostContent(String title, String content, String postIdx) {
        System.out.println("-".repeat(20) + " " + postIdx + "번 게시글 " + "-".repeat(20));
        System.out.println("제목 : " + title);
        System.out.print("내용 : ");
        System.out.println(content);
        System.out.println("-".repeat(50));
    }

    public List<Integer> printReplyList(List<ReplyVO> list) {
        List<Integer> replyNums = new ArrayList<>();
        int replyIdx = 1;
        if (list.isEmpty()) {
            System.out.println("댓글이 없습니다.");
        } else {
            System.out.printf("%-3s %-10s %-20s %-8s %-8s%n", "번호", "작성자", "내용", "공감수", "비공감수");
            for (ReplyVO e : list) {
                replyNums.add(e.getReplyNum());
                System.out.printf("%-5d %-10s %-20s %-9d %-8d%n", replyIdx++, e.getMemberNickname(), e.getReplyContent(), e.getReplyLikeCount(), e.getReplyDislikeCount());
            }
            System.out.println("-".repeat(50));
        }
        return replyNums;
    }

    public void printMemberInfo(List<String> memberInfo) { // id, nickname, pw, email, birth, 선호장르
        System.out.println("------------ 내 정보 ------------");
        System.out.println("I    D  : " + memberInfo.get(0));
        System.out.println("닉 네 임 : " + memberInfo.get(1));
        System.out.println("비밀번호 : " + "*".repeat(memberInfo.get(2).length()));
        System.out.println("E-Mail : " + memberInfo.get(3));
        System.out.println("생   일 : " + memberInfo.get(4));
        if (memberInfo.size() > 5) {
            System.out.print("선호장르 : ");
            for (int i = 5; i < memberInfo.size(); i++) {
                System.out.print(memberInfo.get(i) + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------------------");
    }

}
