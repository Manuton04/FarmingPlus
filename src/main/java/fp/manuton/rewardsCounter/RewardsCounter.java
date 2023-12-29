package fp.manuton.rewardsCounter;

import java.util.*;

public class RewardsCounter {

    private List<RewardRecord> record;

    public RewardsCounter(List<RewardRecord> record) {
        this.record = record;
    }

    public List<RewardRecord> getRecord() {
        return record;
    }

    public void clearRecord() {
        getRecord().clear();
    }
    
    public void addRecord(String rewardName) {
        RewardRecord rewardsRecord = new RewardRecord(new Date(), rewardName);
        getRecord().add(rewardsRecord);
    }

    public List<RewardRecord> getRecordByDateAsc(){
        List<RewardRecord> list = getRecord();
        list.sort((o1, o2) -> o1.date.compareTo(o2.date));
        return list;
    }

    public List<RewardRecord> getRecordByDateDes(){
        List<RewardRecord> list = getRecord();
        list.sort((o1, o2) -> o2.date.compareTo(o1.date));
        return list;
    }

    public List<RewardRecord> getRecordByNameAsc(){
        List<RewardRecord> list = getRecord();
        list.sort((o1, o2) -> o1.rewardName.compareTo(o2.rewardName));
        return list;
    }

    public List<RewardRecord> getRecordByNameDes(){
        List<RewardRecord> list = getRecord();
        list.sort((o1, o2) -> o2.rewardName.compareTo(o1.rewardName));
        return list;
    }

    public int getTotal() {
        int total = 0;
        for (RewardRecord record : getRecord()) {
            total++;
        }
        return total;
    }

    public Map<String, Integer> getTotalRewards(){
        Map<String, Integer> rewardCounts = new HashMap<>();
        for (RewardRecord record : getRecord()){
            rewardCounts.put(record.rewardName, rewardCounts.getOrDefault(record.rewardName, 0) + 1);
        }
        return rewardCounts;
    }

    public List<Map.Entry<String, Integer>> getTotalRewardsAsc(){
        Map<String, Integer> rewardCounts = getTotalRewards();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(rewardCounts.entrySet());
        list.sort(Map.Entry.comparingByValue());
        return list;
    }

    public List<Map.Entry<String, Integer>> getTotalRewardsDes(){
        Map<String, Integer> rewardCounts = getTotalRewards();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(rewardCounts.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return list;
    }

    public int getRewardsQuantity(String rewardName){
        int total = 0;
        for (RewardRecord record : getRecord()){
            if (record.rewardName.equalsIgnoreCase(rewardName))
                total++;
        }
        return total;
    }

    
}

