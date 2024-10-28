package WebtoonConsole.VO;

import java.sql.Date;

public class MemberVO {
    private int memberNum;  // 회원번호(PK)
    private String memberId;  // 아이디
    private String memberPw;  // 비밀번호
    private String memberEmail;
    private Date memberBirth;
    private String memberNickname;
    private Date memberRegistrationDate;
    private int memberExist;
    private int memberTypeNum;

    public MemberVO(int memberNum, String memberId, String memberPw, String memberEmail, Date memberBirth, String memberNickname, int memberTypeNum) {
        this.memberNum = memberNum;
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberEmail = memberEmail;
        this.memberBirth = memberBirth;
        this.memberNickname = memberNickname;
        this.memberTypeNum = memberTypeNum;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberPw() {
        return memberPw;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public Date getMemberBirth() {
        return memberBirth;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public Date getMemberRegistrationDate() {
        return memberRegistrationDate;
    }

    public int getMemberExist() {
        return memberExist;
    }

    public int getMemberTypeNum() {
        return memberTypeNum;
    }
}
