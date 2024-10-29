package WebtoonConsole.Controller;

import WebtoonConsole.Common.GetVal;
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
    MemberDAO memberDAO = new MemberDAO();

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
                            System.out.println("로그인 성공! 환영합니다, " + memberList.get(0).getMemberNickname() + ".");
                            loggedIn = true;
                            memberId = memberList.get(0).getMemberId();
                            memberNickname = memberList.get(0).getMemberNickname();
                            memberTypeNum = memberList.get(0).getMemberTypeNum();
                            break;
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
