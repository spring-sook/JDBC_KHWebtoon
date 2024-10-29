package WebtoonConsole.Common;

import WebtoonConsole.DAO.MemberDAO;
import WebtoonConsole.DAO.PostDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class GetVal {
    private static Scanner sc;
    public GetVal() {
        sc = new Scanner(System.in);
    }

    public int getDay() {
        System.out.print("요일을 선택하세요 [1]월요일 [2]화요일 [3]수요일 [4]목요일 [5]금요일 [6]토요일 [7]일요일 : ");
        int day = sc.nextInt();
        sc.nextLine();
        return day;
    }

    public int getGenre(){
        System.out.print("장르를 선택하세요 [1]드라마 [2]로맨스 [3]무협 [4]액션 [5]판타지 [6]기타 : ");
        int genre = sc.nextInt();
        sc.nextLine();
        return genre;
    }

    public String[] idPW() {
        System.out.print("아이디 : ");
        String memberID = sc.nextLine();
        System.out.print("비밀번호 : ");
        String memberPW = sc.nextLine();
        return new String[] {memberID, memberPW};
    }

    public List<String> memberInfo() {
        MemberDAO memberDAO = new MemberDAO();
        String memberID, memberPW, memberEmail, memberBirth, memberNickname;
        String[] genre = {"없음", "드라마", "로맨스", "무협", "액션", "판타지"};
        while(true) {
            int[] favoriteGenre = {0, 0, 0, 0, 0, 0};

            System.out.print("아이디 : ");
            memberID = sc.nextLine();
            while (memberDAO.memberIdSelect(memberID)) {  // 아이디가 존재하면 true
                System.out.println("이미 존재하는 아이디입니다.");
                System.out.print("아이디 : ");
                memberID = sc.nextLine();
                if (!memberDAO.memberIdSelect(memberID)) break;
            }
            while(true) {
                System.out.print("비밀번호(8글자 이상) : ");
                memberPW = sc.nextLine();
                if(memberPW.length() >= 8) {
                    break;
                } else {
                    System.out.println("8글자 이상 다시 입력해주세요.");
                }
            }
            System.out.print("이메일 : ");
            memberEmail = sc.nextLine();
            memberBirth = getMemberBirth();
            System.out.print("닉네임 : ");
            memberNickname = sc.nextLine();
            System.out.println("선호 장르를 선택해주세요(최대 3개). [1]드라마 [2]로맨스 [3]무협 [4]액션 [5]판타지 [6]없음 [7]선택완료");
            int favoriteGenreCnt = 1;
            while(favoriteGenreCnt <= 3) {
                System.out.print("선호장르 " + String.valueOf(favoriteGenreCnt) + " : ");
                String input = sc.nextLine();
                int inputInt = Integer.parseInt(input);
                if (inputInt >= 1 && inputInt <= 5 && favoriteGenre[inputInt] == 0){
                    switch (inputInt) {
                        case 1: favoriteGenre[1]++; break;
                        case 2: favoriteGenre[2]++; break;
                        case 3: favoriteGenre[3]++; break;
                        case 4: favoriteGenre[4]++; break;
                        case 5: favoriteGenre[5]++; break;
                    }
                    favoriteGenreCnt++;
                } else if (inputInt == 6 || inputInt == 7) {
                    favoriteGenreCnt = 6;
                } else if (favoriteGenre[inputInt] == 1){
                    System.out.println("이미 선택하셨습니다.");
                } else {
                    System.out.println("잘못 입력하셨습니다.");
                }
            }

            System.out.println("[입력한 정보]");
            System.out.println("아이디: " + memberID);
            System.out.println("비밀번호: " + memberPW);
            System.out.println("이메일: " + memberEmail);
            System.out.println("생년월일: " + memberBirth);
            System.out.println("닉네임: " + memberNickname);
            if(Arrays.stream(favoriteGenre).sum() == 0) {
                System.out.println("선호장르 : 없음");
            } else {
                System.out.print("선호장르 : ");
                for (int i = 0; i < favoriteGenre.length; i++) {
                    if (favoriteGenre[i] == 1) {
                        System.out.print(genre[i] + " ");
                    }
                }
                System.out.println();
            }

            while(true) {
                System.out.print("입력하신 정보가 맞습니까? [1]확인 [2]다시 입력 : ");
                String confirm = sc.nextLine();
                if (confirm.equals("1")) {
                    List<String> result = new ArrayList<>();
                    Collections.addAll(result, memberID, memberPW, memberEmail, memberBirth, memberNickname);
                    for (int i = 0; i < favoriteGenre.length; i++) {
                        if (favoriteGenre[i] == 1) {
                            result.add(String.valueOf(i));
                        }
                    }
                    return result;
                } else if (confirm.equals("2")) {
                    System.out.println("다시 입력해주세요.");
                    break;
                } else {
                    System.out.println("잘못 입력하셨습니다.");
                }
            }
        }

    }

    private static String getMemberBirth() {
        String memberBirth = "";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.print("생년월일(YYYY-MM-DD) : ");
            memberBirth = sc.nextLine();
            try {
                LocalDate.parse(memberBirth, dateFormatter);
                break; // Valid date, exit loop
            } catch (DateTimeParseException e) {
                System.out.println("올바른 형식이 아닙니다. YYYY-MM-DD 형식으로 입력하세요.");
            }
        }
        return memberBirth;
    }

    public String getSearchKeyword() {
        System.out.print("검색어(제목 또는 작가) 입력 : ");
        return sc.nextLine();
    }

    public String[] getPostContent() {
        System.out.print("제목 : ");
        String postTitle = sc.nextLine();
        System.out.print("내용(입력 완료시에만 엔터) : ");
        String postContent = sc.nextLine();
        return new String[] {postTitle, postContent};
    }

    public String[] printPostUpdate(String postTitle, String postContent) {
        System.out.println("원본 제목 : " + postTitle);
        System.out.print("수정 제목 : ");
        String updateTitle = sc.nextLine();
        System.out.println("원본 내용 : " + postContent);
        System.out.print("수정 내용 : ");
        String updateContent = sc.nextLine();
        System.out.print("수정을 진행하시겠습니까? [1]네 [2]아니오 : ");
        String choice = sc.nextLine();
        return new String[] {choice, updateTitle, updateContent};
    }

    public String getReplyContent() {
        System.out.print("내용(입력 완료시에만 엔터) : ");
        String replyContent = sc.nextLine();
        return replyContent;
    }

    public String getUpdateReplyContent(String replyContent) {
        System.out.println("원본 내용 : " + replyContent);
        System.out.print("수정 내용 : ");
        String updateReplyContent = sc.nextLine();
        return updateReplyContent;
    }

}
