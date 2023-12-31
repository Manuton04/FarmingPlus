package fp.manuton.rewardsCounter;

import java.util.Date;

public class RewardRecord {
    Date date;
    String rewardName;

    public RewardRecord(Date date, String rewardName) {
        this.date = date;
        this.rewardName = rewardName;
    }

    public Date getDate() {
        return date;
    }

    public String getRewardName() {
        return rewardName;
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", Reward Name='" + rewardName + '\'' +
                '}';
    }
}
