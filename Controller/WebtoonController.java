package WebtoonConsole.Controller;

import WebtoonConsole.Common.Printer;
import WebtoonConsole.DAO.WebtoonDAO;
import WebtoonConsole.VO.WebtoonVO;

import java.util.List;
import java.util.Scanner;

public class WebtoonController {
    String choice = null;
    String memberId = null;
    String memberNickname = null;

    Scanner sc = new Scanner(System.in);
    Printer printer = new Printer();
    WebtoonDAO webtoonDAO = new WebtoonDAO();

    public WebtoonController(String memberId, String memberNickname) {
        this.memberId = memberId;
        this.memberNickname = memberNickname;
    }

    public void displayWebtoonService() {
        while(true) {
            System.out.print("[1]요일별 조회 [2]장르별 조회 [3]웹툰 추천 [4]검색 [5]메인페이지 이동 [0]종료 : ");
            choice = sc.nextLine();
            switch (choice) {
                case "1": // 요일별 웹툰 조회
                    List<WebtoonVO> listDay = webtoonDAO.webtoonDaySelect();
                    printer.webtoonDayResult(listDay);
                    break;
                case "2": // 장르별 웹툰 조회
                    List<WebtoonVO> listGenre = webtoonDAO.webtoonGenreSelect();
                    printer.webtoonGenreResult(listGenre);
                    break;
                case "3": // 웹툰 추천
                    if (memberId == null) { // 비회원 웹툰 추천
                        List<WebtoonVO> listNoMem = webtoonDAO.webtoonNoMemberRecommend();
                        printer.webtoonRecommendResult(listNoMem, memberNickname);
                        break;
                    } else {  // 회원 웹툰 추천
                        List<WebtoonVO> listMem = webtoonDAO.webtoonMemberRecommend(memberId);
                        printer.webtoonRecommendResult(listMem, memberNickname);
                        break;
                    }
                case "4": // 검색
                    webtoonDAO.searchWebtoon();
                    break;
                case "5": // 메인페이지 이동
                    return;
                case "0": // 종료
                    System.out.println("종료합니다."); System.exit(1);
                default:
                    System.out.println("잘못 입력하셨습니다.");
            }
        }
    }
}
