package son.nt.hellochao.dto;

/**
 * Created by Sonnt on 11/12/15.
 */
public class RankDto {
    int rankNo;
    String rankName;
    String rankIcon;
    int rankScoreStandard;

    public RankDto() {
    }

    public RankDto(int roleNo, String roleName, String roleIcon, int roleScoreStandard) {
        this.rankNo = roleNo;
        this.rankName = roleName;
        this.rankIcon = roleIcon;
        this.rankScoreStandard = roleScoreStandard;
    }

    public RankDto(String roleName, String roleIcon, int roleScoreStandard) {
        this.rankName = roleName;
        this.rankIcon = roleIcon;
        this.rankScoreStandard = roleScoreStandard;
    }

    public RankDto(int roleNo) {
        this.rankNo = roleNo;
    }

    public String getRankName() {
        return rankName;
    }

    public int getRankNo() {
        return rankNo;
    }

    public void setRankNo(int rankNo) {
        this.rankNo = rankNo;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRankIcon() {
        return rankIcon;
    }

    public void setRankIcon(String rankIcon) {
        this.rankIcon = rankIcon;
    }

    public int getRankScoreStandard() {
        return rankScoreStandard;
    }

    public void setRankScoreStandard(int rankScoreStandard) {
        this.rankScoreStandard = rankScoreStandard;
    }
}
