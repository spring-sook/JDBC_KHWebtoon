package WebtoonConsole;

import WebtoonConsole.Common.Common;
import WebtoonConsole.Common.GetVal;
import WebtoonConsole.Common.Printer;
import WebtoonConsole.DAO.MemberDAO;
import WebtoonConsole.DAO.WebtoonDAO;
import WebtoonConsole.VO.MemberVO;
import WebtoonConsole.VO.WebtoonVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String choice = null;
        String memberId = null;
        String memberNickname = null;

        GetVal getVal = new GetVal();
        Printer printer = new Printer();
        MemberDAO memberDAO = new MemberDAO();
        WebtoonDAO webtoonDAO = new WebtoonDAO();
        while (true) {
            System.out.println("******************************* KH WEBTOON *******************************");
            if (memberId == null) {
                System.out.print("[1]웹툰 통합 조회 서비스(네이버, 카카오) [2]커뮤니티 게시판 [3] 로그인/회원가입 [4] 종료 : ");
            } else {
                System.out.print("[1]웹툰 통합 조회 서비스(네이버, 카카오) [2]커뮤니티 게시판 [3] 로그아웃 [4] 종료 : ");
            }
            choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("[1]요일별 조회 [2]장르별 조회 [3]웹툰 추천 [4]검색 [5]뒤로가기 : ");
                    choice = sc.nextLine();
                    switch (choice) {
                        case "1":
                            List<WebtoonVO> listDay = webtoonDAO.webtoonDaySelect();
                            printer.webtoonDayResult(listDay);
                            break;
                        case "2":
                            List<WebtoonVO> listGenre = webtoonDAO.webtoonGenreSelect();
                            printer.webtoonGenreResult(listGenre);
                            break;
                        case "3":
                            if (memberId == null){ // 비회원 웹툰 추천
                                List<WebtoonVO> listNoMem = webtoonDAO.webtoonNoMemberRecommend();
                                printer.webtoonRecommendResult(listNoMem);
                                break;
                            } else {  // 회원 웹툰 추천
                                List<WebtoonVO> listMem = webtoonDAO.webtoonMemberRecommend(memberId);
                                System.out.println("☆" + memberNickname + "님☆만을 위한 추천 목록");
                                printer.webtoonRecommendResult(listMem);
                                break;
                            }
                        case "4":
                            String searchKeyword = getVal.getSearchKeyword();
                            List<WebtoonVO> searchList = webtoonDAO.searchWebtoon(searchKeyword);
                            printer.printSearchResult(searchList, searchKeyword);
                        case "5":
                            break;
                        default:
                            System.out.println("잘못 입력하셨습니다.");
                    }
                    break;
                case "2":
                    System.out.println("게시판임");
                    break;
                case "3":
                    if (memberId == null) {
                        System.out.print("[1]로그인 [2]회원가입 [3]뒤로가기 : ");
                        choice = sc.nextLine();
                        switch (choice) {
                            case "1":
                                boolean loggedIn = false;
                                for (int attempts = 0; attempts < 3; attempts++) {  // 로그인 시도 3번까지
                                    String[] idpw = getVal.idPW();
                                    List<MemberVO> memberList = memberDAO.memberSelect(idpw[0], idpw[1]);
                                    if (!memberList.isEmpty()) {
                                        System.out.println("로그인 성공! 환영합니다, " + memberList.get(0).getMemberNickname() + ".");
                                        loggedIn = true;
                                        memberId = memberList.get(0).getMemberId();
                                        memberNickname = memberList.get(0).getMemberNickname();
                                        break;
                                    } else {
                                        System.out.println("일치하는 회원이 존재하지 않습니다. 남은 시도 횟수: " + (2 - attempts));
                                    }
                                }
                                if (!loggedIn) {
                                    System.out.println("3회 시도 후 로그인 실패.");
                                }
                                break;
                            case "2":
                                memberDAO.memberInsert();
                            case "3":
                                break;
                            default:
                                System.out.println("잘못 입력하셨습니다.");
                        }
                    } else {
                        System.out.print("정말 로그아웃 하시겠습니까? [1]네 [2]아니오 : ");
                        String doLogout = sc.nextLine();
                        if (doLogout.equals("1")) {
                            memberId = null;
                            memberNickname = null;
                            System.out.println("로그아웃 되었습니다.");
                        }
                    }
                    break;

                case "4":
                    System.out.println("종료하겠습니다."); System.exit(1);
                default: System.out.println("잘못 입력하셨습니다.");
            }
        }
    }
}
