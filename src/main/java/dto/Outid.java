package dto;

import java.sql.Date;

/**
 * 탈퇴 ID 정보를 담는 DTO 클래스
 * 테이블: OUTID (ID, REASON, CREATEDATE)
 */
public class Outid {
    private String id;
    private String reason; // DB 컬럼명이 MEMO에서 REASON으로 변경되었을 가능성을 감안하여 Snippet의 REASON을 따름
    private Date createDate;

    // Getter and Setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for reason (기존 memo 컬럼에 해당)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // Getter and Setter for createDate
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    // toString (디버깅용)
    @Override
    public String toString() {
        return "Outid [id=" + id + ", reason=" + reason + ", createDate=" + createDate + "]";
    }
}