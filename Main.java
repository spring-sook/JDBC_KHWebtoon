package WebtoonConsole;

import WebtoonConsole.Common.Common;
import WebtoonConsole.Common.GetVal;
import WebtoonConsole.Common.Printer;
import WebtoonConsole.DAO.MemberDAO;
import WebtoonConsole.DAO.PostDAO;
import WebtoonConsole.DAO.ReplyDAO;
import WebtoonConsole.DAO.WebtoonDAO;
import WebtoonConsole.VO.MemberVO;
import WebtoonConsole.VO.PostVO;
import WebtoonConsole.VO.ReplyVO;
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
        int memberTypeNum = 1;

        GetVal getVal = new GetVal();
        Printer printer = new Printer();
        MemberDAO memberDAO = new MemberDAO();
        WebtoonDAO webtoonDAO = new WebtoonDAO();
        PostDAO postDAO = new PostDAO();
        ReplyDAO replyDAO = new ReplyDAO();

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
                    System.out.print("[1]공지게시판 [2]자유게시판 : ");
                    choice = sc.nextLine();
                    List<PostVO> postList = postDAO.noticeSelect(Integer.parseInt(choice) - 1);
                    List<Integer> postNums = printer.printPostList(postList, choice);
                    System.out.print("[1]게시글 내용 보기 [2]게시글 작성 [3]게시글 수정 [4]게시글 삭제 [5]뒤로가기: ");
                    String doPostChoice = sc.nextLine();
                    String postIdx = null;
                    switch (doPostChoice) {  // [1]게시글 내용 보기 [2]게시글 작성 [3]게시글 수정 [4]게시글 삭제
                        case "1":
                            System.out.print("열람할 게시글 번호를 입력해주세요 : ");
                            postIdx = sc.nextLine();
                            String content = postDAO.viewPostContent(postNums.get(Integer.parseInt(postIdx) - 1));
                            printer.printPostContent(content);
                            if (choice.equals("2")) {
                                System.out.print("[1]댓글 보기 [2]댓글 작성 [3]뒤로가기 : ");
                                String replyChoice = sc.nextLine();
                                switch (replyChoice) {
                                    case "1":  // 댓글 보기
                                        List<ReplyVO> replyList = replyDAO.replySelect(postNums.get(Integer.parseInt(postIdx) - 1));
                                        List<Integer> replyNums = printer.printReplyList(replyList);
                                        System.out.print("[1]댓글 수정 [2]댓글 작성 [3]댓글 공감 [4]댓글 비공감 [5]뒤로가기 : ");
                                        String replyDetailChoice = sc.nextLine();
                                        if (!replyDetailChoice.equals("2")) {
                                            System.out.print("댓글 번호 입력 : ");
                                            String replyIdx = sc.nextLine();
                                            switch (replyDetailChoice) {
                                                case "1":
                                                    if (replyDAO.getMemberId(replyNums.get(Integer.parseInt(replyIdx) - 1)).equals(memberId)) {
                                                        replyDAO.replyUpdate(postNums.get(Integer.parseInt(postIdx) - 1), replyNums.get(Integer.parseInt(replyIdx) - 1), memberId);
                                                    } else {
                                                        System.out.println("작성자만 수정 가능합니다.");
                                                    }
                                                case "2":
                                                    if (memberId != null) {
                                                        replyDAO.replyInsert(postNums.get(Integer.parseInt(postIdx) - 1), memberId);
                                                    } else {
                                                        System.out.println("로그인이 필요한 서비스입니다.");
                                                    }
                                                case "3":
                                                    replyDAO.replyLikeUpdate(replyNums.get(Integer.parseInt(replyIdx) - 1));
                                                case "4":
                                                    replyDAO.replyDislikeUpdate(replyNums.get(Integer.parseInt(replyIdx) - 1));
                                                case "5": break;
                                            }
                                            break;
                                        }
                                    case "2": // 댓글 작성
                                        if (memberId != null) {
                                            replyDAO.replyInsert(postNums.get(Integer.parseInt(postIdx) - 1), memberId);
                                        } else {
                                            System.out.println("로그인이 필요한 서비스입니다.");
                                        }
                                    case "3": break;
                                }
                            }
                            break;
                        case "2":
                            if (choice.equals("1")) {
                                if (memberId != null && memberTypeNum == 0) { // 공지게시판
                                    String[] postContent = getVal.getPostContent();
                                    postDAO.postInsert(postContent[0], postContent[1], memberId, memberTypeNum, 0);
                                } else if (memberId == null) {
                                    System.out.println("로그인이 필요한 서비스입니다.");
                                } else if (memberTypeNum == 1) {
                                    System.out.println("공지글은 운영자만 작성 가능합니다.");
                                }
                            } else {
                                if (memberId != null) {  // 자유게시판
                                    String[] postContent = getVal.getPostContent();
                                    postDAO.postInsert(postContent[0], postContent[1], memberId, memberTypeNum, 1);
                                } else { System.out.println("로그인이 필요한 서비스입니다.");}
                            }
                            break;
                        case "3":
                            System.out.print("수정할 게시글 번호를 입력해주세요 : ");
                            postIdx = sc.nextLine();
                            if(postDAO.getMemberId(postNums.get(Integer.parseInt(postIdx) - 1)).equals(memberId)) {
                                postDAO.postUpdate(Integer.parseInt(postIdx) - 1);
                            } else {
                                System.out.println("작성자만 수정 가능합니다.");
                            }
                            break;
                        case "4":
                            System.out.print("삭제할 게시글 번호를 입력해주세요 : ");
                            postIdx = sc.nextLine();
                            if(postDAO.getMemberId(postNums.get(Integer.parseInt(postIdx) - 1)).equals(memberId)) {
                                System.out.print("정말 삭제하시겠습니까? [1]네 [2]아니오 : ");
                                String delChoice = sc.nextLine();
                                if(delChoice.equals("1")) {
                                    postDAO.postDelete(Integer.parseInt(postIdx) - 1);
                                }
                            }
                            break;
                        case "5": break;
                    }
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
                            memberTypeNum = 1;
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
