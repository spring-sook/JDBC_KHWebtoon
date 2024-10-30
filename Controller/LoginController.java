package WebtoonConsole.Controller;

import WebtoonConsole.Common.GetVal;
import WebtoonConsole.Common.Printer;
import WebtoonConsole.DAO.MemberDAO;
import WebtoonConsole.VO.MemberVO;

import java.util.List;
import java.util.Scanner;

public class LoginController {
    private String memberId = null;
    private String memberNickname = null;
    private int memberTypeNum = 1;
    String loginChoice = null;

    Scanner sc = new Scanner(System.in);
    GetVal getVal = new GetVal();
    Printer printer = new Printer();
    MemberDAO memberDAO = new MemberDAO();
    MainController mainController = new MainController();

    public void displayLoginService() {
        if(memberId == null) { // 로그인 안되어 있으면
            System.out.print("[1]로그인 [2]회원가입 [3]뒤로가기 : ");
            loginChoice = sc.nextLine();
            switch (loginChoice) {
                case "1": // 로그인
                    boolean loggedIn = false;
                    for (int attempts = 0; attempts < 3; attempts++) {  // 로그인 시도 3번까지
                        String[] idpw = getVal.idPW();
                        List<MemberVO> memberList = memberDAO.memberSelect(idpw[0], idpw[1]);
                        if (!memberList.isEmpty()) {
                            if(memberList.get(0).getMemberExist() == 0) {
                                System.out.println("로그인 성공! 환영합니다, " + memberList.get(0).getMemberNickname() + "님.");
                                loggedIn = true;
                                memberId = memberList.get(0).getMemberId();
                                memberNickname = memberList.get(0).getMemberNickname();
                                memberTypeNum = memberList.get(0).getMemberTypeNum();
                                break;
                            } else if(memberList.get(0).getMemberExist() == 1){
                                memberDAO.deleteMember(idpw[0], 0);
                                System.out.println(memberList.get(0).getMemberNickname() + "님! 돌아오신건가요? 반가워요!!!😁😁");
                                loggedIn = true;
                                break;
                            }
                        } else {
                            System.out.println("일치하는 회원이 존재하지 않습니다. 남은 시도 횟수: " + (2 - attempts));
                        }
                    }
                    if (!loggedIn) {
                        System.out.println("3회 시도 후 로그인 실패.");
                    }
                    break;
                case "2": // 회원가입
                    memberDAO.memberInsert();
                    break;
                case "3": // 뒤로가기
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
                memberTypeNum = 1;
                System.out.println("로그아웃 되었습니다.");
            }
        }
    }

    public void displayMyPage(String memberId) {
        String choice = null;
        while (true) {
            List<String> memberInfo = memberDAO.memberInfoSelect(memberId); // id, nickname, pw, email, birth, 선호장르
            printer.printMemberInfo(memberInfo); // 내 정보 출력
            System.out.print("[1]비밀번호 변경 [2]선호장르 수정 [3]회원 탈퇴 [4]메인페이지 이동 [0]종료 : ");
            choice = sc.nextLine();
            switch (choice) {
                case "1": // 비밀번호 변경
                    String newPW = null;
                    System.out.print("기존 비밀번호 입력 : ");
                    String pw = sc.nextLine();
                    if (pw.equals(memberInfo.get(2))) { // 입력한 비밀번호 일치하면
                        System.out.print("신규 비밀번호 입력 : ");
                        newPW = sc.nextLine();
                        System.out.print("신규 비밀번호 확인용 재입력 : ");
                        String newRePW = sc.nextLine();
                        if (newPW.equals(newRePW)) {
                            memberDAO.memberPwUpdate(memberInfo.get(0), newPW);
                        } else {
                            System.out.println("입력하신 비밀번호가 일치하지 않습니다.");
                        }
                    } else {
                        System.out.println("비밀번호를 잘못 입력하셨습니다.");
                    }
                    break;
                case "2": // 선호장르 수정
                    List<Integer> favoriteGenreNums = getVal.updateFavoriteGenre();
                    memberDAO.memberFavoriteGenreUpdate(memberInfo.get(0), favoriteGenreNums);
                    break;
                case "3": // 회원 탈퇴
                    System.out.print("비밀번호 확인 : ");
                    String memberPW = sc.nextLine();
                    if (memberInfo.get(2).equals(memberPW)) {
                        System.out.print("정말 탈퇴하시겠습니까? [1]네 [2]아니오 : ");
                        choice = sc.nextLine();
                        if (choice.equals("1")) {
                            memberDAO.deleteMember(memberInfo.get(0), 1);
                        }
                    }
                    memberId = null;
                    memberNickname = null;
                    memberTypeNum = 1;
                    mainController.displayMainMenu();
                    return;
                case "4": // 메인페이지 이동
                    mainController.displayMainMenu();
                    return;
                case "0": // 종료
                    System.out.println("종료합니다.");
                    System.exit(1);
                default:
                    System.out.println("잘못 입력하셨습니다.");
            }
        }
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public int getMemberTypeNum() {
        return memberTypeNum;
    }
}
