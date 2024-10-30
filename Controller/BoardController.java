package WebtoonConsole.Controller;

import WebtoonConsole.Common.GetVal;
import WebtoonConsole.Common.Printer;
import WebtoonConsole.DAO.PostDAO;
import WebtoonConsole.DAO.ReplyDAO;
import WebtoonConsole.VO.PostVO;
import WebtoonConsole.VO.ReplyVO;

import java.util.List;
import java.util.Scanner;

public class BoardController {
    String boardChoice = null; // [1]공지게시판 [2]자유게시판
    String memberId = null;
    String memberNickname = null;
    int memberTypeNum = 0;

    Scanner sc = new Scanner(System.in);
    GetVal getVal = new GetVal();
    Printer printer = new Printer();
    PostDAO postDAO = new PostDAO();
    ReplyDAO replyDAO = new ReplyDAO();

    public BoardController(String memberNickname, String memberId, int memberTypeNum) {
        this.memberNickname = memberNickname;
        this.memberId = memberId;
        this.memberTypeNum = memberTypeNum;
    }

    public void displayBoardService() {
        while (true) { // 무한 루프를 사용하여 올바른 입력을 받을 때까지 반복
            System.out.print("[1]공지게시판 [2]자유게시판 : ");
            boardChoice = sc.nextLine();

            if (!boardChoice.equals("1") && !boardChoice.equals("2")) {
                System.out.println("잘못 입력하셨습니다.");
                continue; // 다시 입력 요청
            }

            int boardNum = Integer.parseInt(boardChoice) - 1; // 0(공지), 1(자유)

            List<PostVO> postList = postDAO.noticeSelect(boardNum);
            List<Integer> postNums = printer.printPostList(postList, boardChoice); // 게시글 프린트

            displayPostService(postNums, boardNum);

            break;
        } // 공지/자유 while문
    }

    public void displayPostService(List<Integer> postNums, Integer boardNum) {
        String postIdx = null; // 게시글 인덱스
        while(true) {
            System.out.print("[1]게시글 내용 보기 [2]게시글 작성 [3]게시글 수정 [4]게시글 삭제 [5]뒤로가기: ");
            String postChoice = sc.nextLine();
            switch (postChoice) {
                case "1": // 게시글 내용 보기
                    if (postNums.isEmpty()) {
                        System.out.println("열람할 게시글이 존재하지 않습니다.");
                    } else {
                        System.out.print("열람할 게시글 번호를 입력해주세요 : ");
                        postIdx = sc.nextLine();
                        int postIdxNum = Integer.parseInt(postIdx) - 1;
                        String content = postDAO.viewPostContent(postNums.get(postIdxNum));
                        printer.printPostContent(content); // 게시글 내용 출력
                        if (boardChoice.equals("2")) { // 자유게시판이면 댓글기능 ON
                            displayReplyService(postNums, postIdxNum);
                        }
                    }
                    break;
                case "2": // 게시글 작성
                    if (boardChoice.equals("1")) { // 공지게시판이면
                        if (memberId != null && memberTypeNum == 0) { // 공지게시판
                            String[] postContent = getVal.getPostContent();
                            postDAO.postInsert(postContent[0], postContent[1], memberId, boardNum);
                        } else if (memberId == null) {
                            System.out.println("로그인이 필요한 서비스입니다.");
                        } else if (memberTypeNum == 1) {
                            System.out.println("공지글은 운영자만 작성 가능합니다.");
                        }
                    } else {
                        if (memberId != null) {  // 자유게시판
                            String[] postContent = getVal.getPostContent();
                            postDAO.postInsert(postContent[0], postContent[1], memberId, boardNum);
                        } else {
                            System.out.println("로그인이 필요한 서비스입니다.");
                        }
                    }
                    postNums = printer.printPostList(postDAO.noticeSelect(boardNum), boardChoice);
                    break;
                case "3": // 게시글 수정
                    if (postNums.isEmpty()) {
                        System.out.println("수정할 게시글지 존재하지 않습니다.");
                    } else {
                        System.out.print("수정할 게시글 번호를 입력해주세요 : ");
                        postIdx = sc.nextLine();
                        int postIdxNum = Integer.parseInt(postIdx) - 1;
                        if (postDAO.getMemberId(postNums.get(postIdxNum)).equals(memberId)) {
                            postDAO.postUpdate(postNums.get(postIdxNum));
                            postNums = printer.printPostList(postDAO.noticeSelect(boardNum), boardChoice);
                        } else {
                            System.out.println("운영자만 수정 가능합니다.");
                        }
                    }
                    break;
                case "4": // 게시글 삭제
                    if (postNums.isEmpty()) {
                        System.out.println("삭제할 게시글이 존재하지 않습니다.");
                    } else {
                        System.out.print("삭제할 게시글 번호를 입력해주세요 : ");
                        postIdx = sc.nextLine();
                        int postIdxNum = Integer.parseInt(postIdx) - 1;
                        if (postDAO.getMemberId(postNums.get(postIdxNum)).equals(memberId)) {
                            System.out.print("정말 삭제하시겠습니까? [1]네 [2]아니오 : ");
                            String delChoice = sc.nextLine();
                            if (delChoice.equals("1")) {
                                postDAO.postDelete(postNums.get(postIdxNum));
                                postNums = printer.printPostList(postDAO.noticeSelect(boardNum), boardChoice);
                            } else {
                                postNums = printer.printPostList(postDAO.noticeSelect(boardNum), boardChoice);
                            }
                        } else {
                            System.out.println("운영자만 삭제 가능합니다.");
                        }
                    }
                    break;
                case "5":
                    return;
                default:
                    System.out.println("잘못 입력하셨습니다.");
            }
        }
    }

    public void displayReplyService(List<Integer> postNums, int postIdxNum) {
        System.out.print("[1]댓글 보기 [2]댓글 작성 [3]뒤로가기 : ");
        String replyChoice = sc.nextLine();
        switch (replyChoice) {
            case "1": // 댓글 보기
                List<ReplyVO> replyList = replyDAO.replySelect(postNums.get(postIdxNum));
                List<Integer> replyNums = printer.printReplyList(replyList);
                viewReplyDetail(postNums, replyNums, postIdxNum);
                break;
            case "2": // 댓글 작성
                if (memberId != null) {
                    replyDAO.replyInsert(postNums.get(postIdxNum), memberId);
                } else {
                    System.out.println("로그인이 필요한 서비스입니다.");
                }
                break;
            case "3": break;
            default:
                System.out.println("잘못 입력하셨습니다.");
        }
    }

    public void viewReplyDetail(List<Integer> postNums, List<Integer> replyNums, int postIdxNum) {
        String replyIdx = null;
        System.out.print("[1]댓글 수정 [2]댓글 작성 [3]댓글 공감 [4]댓글 비공감 [5]뒤로가기 : ");
        String replyDetailChoice = sc.nextLine();
        switch (replyDetailChoice) {
            case "1": // 댓글 수정
                System.out.print("댓글 번호 입력 : ");
                replyIdx = sc.nextLine();
                int replyIdxNum = Integer.parseInt(replyIdx) - 1;
                if (replyDAO.getMemberId(replyNums.get(replyIdxNum)).equals(memberId)) {
                    replyDAO.replyUpdate(postNums.get(postIdxNum), replyNums.get(replyIdxNum), memberId);
                } else {
                    System.out.println("작성자만 수정 가능합니다.");
                }
                break;
            case "2": // 댓글 작성
                if (memberId != null) {
                    replyDAO.replyInsert(postNums.get(postIdxNum), memberId);
                } else {
                    System.out.println("로그인이 필요한 서비스입니다.");
                }
                break;
            case "3": // 댓글 공감
                if (memberId != null) {
                    System.out.print("댓글 번호 입력 : ");
                    replyIdx = sc.nextLine();
                    replyIdxNum = Integer.parseInt(replyIdx) - 1;

                    replyDAO.replyLikeInsert(memberId, replyNums.get(replyIdxNum), 0);
                } else {
                    System.out.println("로그인이 필요한 서비스입니다.");
                }
                break;
            case "4": // 댓글 비공감
                if (memberId != null) {
                    System.out.print("댓글 번호 입력 : ");
                    replyIdx = sc.nextLine();
                    replyIdxNum = Integer.parseInt(replyIdx) - 1;
                    replyDAO.replyLikeInsert(memberId, replyNums.get(replyIdxNum), 1);
                } else {
                    System.out.println("로그인이 필요한 서비스입니다.");
                }
                break;
            case "5": break;
            default:
                System.out.println("잘못 입력하셨습니다.");
        }
    }
}
