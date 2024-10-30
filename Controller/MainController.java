package WebtoonConsole.Controller;

import java.util.Scanner;

public class MainController {

    public void displayMainMenu() {
        String mainChoice = null;

        Scanner sc = new Scanner(System.in);
        LoginController loginController = new LoginController();

        while (true) {
            System.out.println("******************************* KH WEBTOON *******************************");
            if (loginController.getMemberId() == null) {
                System.out.print("[1]웹툰 통합 조회 서비스(네이버, 카카오) [2]커뮤니티 게시판 [3] 로그인/회원가입 [0] 종료 : ");
            } else {
                System.out.print("[1]웹툰 통합 조회 서비스(네이버, 카카오) [2]커뮤니티 게시판 [3]로그아웃 [4]마이페이지 [0] 종료 : ");
            }
            mainChoice = sc.nextLine();

            switch (mainChoice) {
                case "1": // 웹툰 통합 조회 서비스(네이버, 카카오)
                    WebtoonController webtoonController = new WebtoonController(loginController.getMemberId(), loginController.getMemberNickname());
                    webtoonController.displayWebtoonService();
                    break;
                case "2": // 커뮤니티 게시판
                    BoardController boardController = new BoardController(loginController.getMemberNickname(), loginController.getMemberId(), loginController.getMemberTypeNum());
                    boardController.displayBoardService();
                    break;
                case "3" : // 로그인/회원가입 or 로그아웃
                    loginController.displayLoginService();
                    break;
                case "4": // 마이페이지
                    loginController.displayMyPage(loginController.getMemberId());
                case "0":
                    System.out.println("종료합니다."); System.exit(1);
                default: System.out.println("잘못 입력하셨습니다.");

            }
        }
    }
}
