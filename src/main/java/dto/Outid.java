package dto;

import java.sql.Date;

/**
 * 탈퇴 ID 정보를 담는 DTO 클래스
 * 테이블: OUTID (ID, MEMO, CREATEDATE)
 */
public class Outid {
    private String id;
    private String memo; // 탈퇴 사유 필드
    private Date createDate;

    // Getter and Setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for memo
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
        return "Outid [id=" + id + ", memo=" + memo + ", createDate=" + createDate + "]";
    }
}