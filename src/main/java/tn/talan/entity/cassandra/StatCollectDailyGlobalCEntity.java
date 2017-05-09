package tn.talan.entity.cassandra;

import com.datastax.driver.core.LocalDate;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.math.BigDecimal;

@Table(name = "stat_collect_daily_global", keyspace = "talankeyspace")
public class StatCollectDailyGlobalCEntity implements java.io.Serializable {

    @Column(name = "year_month_collect")
    @PartitionKey(0)

    private String yearmonthcollect;
    @Column(name = "date_collect")
    @ClusteringColumn(0)

    private LocalDate dateCollect;

    @Column(name = "task_id")
    @ClusteringColumn(1)

    private String taskId;
    @Column(name = "dc_count")
    private Integer dcCount;
    @Column(name = "device_count")
    private Integer deviceCount;

    @Column(name = "full_success_rate")

    private BigDecimal fullSuccessRate;
    @Column(name = "partial_success_rate")

    private BigDecimal partialSuccessRate;
    @Column(name = "min_task_duration")

    private Integer minTaskDuration;
    @Column(name = "mean_task_duration")

    private Integer meanTaskDuration;
    @Column(name = "max_task_duration")

    private Integer maxTaskDuration;
    @Column(name = "min_hop")

    private Integer minHop;
    @Column(name = "mean_hop")

    private Integer meanHop;
    @Column(name = "max_hop")

    private Integer maxHop;

    public String getYearmonthcollect() {
        return yearmonthcollect;
    }

    public void setYearmonthcollect(String yearmonthcollect) {
        this.yearmonthcollect = yearmonthcollect;
    }

    public LocalDate getDateCollect() {
        return dateCollect;
    }

    public void setDateCollect(LocalDate dateCollect) {
        this.dateCollect = dateCollect;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getDcCount() {
        return dcCount;
    }

    public void setDcCount(Integer dcCount) {
        this.dcCount = dcCount;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public BigDecimal getFullSuccessRate() {
        return fullSuccessRate;
    }

    public void setFullSuccessRate(BigDecimal fullSuccessRate) {
        this.fullSuccessRate = fullSuccessRate;
    }

    public BigDecimal getPartialSuccessRate() {
        return partialSuccessRate;
    }

    public void setPartialSuccessRate(BigDecimal partialSuccessRate) {
        this.partialSuccessRate = partialSuccessRate;
    }

    public Integer getMinTaskDuration() {
        return minTaskDuration;
    }

    public void setMinTaskDuration(Integer minTaskDuration) {
        this.minTaskDuration = minTaskDuration;
    }

    public Integer getMeanTaskDuration() {
        return meanTaskDuration;
    }

    public void setMeanTaskDuration(Integer meanTaskDuration) {
        this.meanTaskDuration = meanTaskDuration;
    }

    public Integer getMaxTaskDuration() {
        return maxTaskDuration;
    }

    public void setMaxTaskDuration(Integer maxTaskDuration) {
        this.maxTaskDuration = maxTaskDuration;
    }

    public Integer getMinHop() {
        return minHop;
    }

    public void setMinHop(Integer minHop) {
        this.minHop = minHop;
    }

    public Integer getMeanHop() {
        return meanHop;
    }

    public void setMeanHop(Integer meanHop) {
        this.meanHop = meanHop;
    }

    public Integer getMaxHop() {
        return maxHop;
    }

    public void setMaxHop(Integer maxHop) {
        this.maxHop = maxHop;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateCollect == null) ? 0 : dateCollect.hashCode());
        result = prime * result + ((dcCount == null) ? 0 : dcCount.hashCode());
        result = prime * result + ((deviceCount == null) ? 0 : deviceCount.hashCode());
        result = prime * result + ((fullSuccessRate == null) ? 0 : fullSuccessRate.hashCode());
        result = prime * result + ((maxHop == null) ? 0 : maxHop.hashCode());
        result = prime * result + ((maxTaskDuration == null) ? 0 : maxTaskDuration.hashCode());
        result = prime * result + ((meanHop == null) ? 0 : meanHop.hashCode());
        result = prime * result + ((meanTaskDuration == null) ? 0 : meanTaskDuration.hashCode());
        result = prime * result + ((minHop == null) ? 0 : minHop.hashCode());
        result = prime * result + ((minTaskDuration == null) ? 0 : minTaskDuration.hashCode());
        result = prime * result + ((partialSuccessRate == null) ? 0 : partialSuccessRate.hashCode());
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
        result = prime * result + ((yearmonthcollect == null) ? 0 : yearmonthcollect.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StatCollectDailyGlobalCEntity other = (StatCollectDailyGlobalCEntity) obj;
        if (dateCollect == null) {
            if (other.dateCollect != null)
                return false;
        } else if (!dateCollect.equals(other.dateCollect))
            return false;
        if (dcCount == null) {
            if (other.dcCount != null)
                return false;
        } else if (!dcCount.equals(other.dcCount))
            return false;
        if (deviceCount == null) {
            if (other.deviceCount != null)
                return false;
        } else if (!deviceCount.equals(other.deviceCount))
            return false;
        if (fullSuccessRate == null) {
            if (other.fullSuccessRate != null)
                return false;
        } else if (!fullSuccessRate.equals(other.fullSuccessRate))
            return false;
        if (maxHop == null) {
            if (other.maxHop != null)
                return false;
        } else if (!maxHop.equals(other.maxHop))
            return false;
        if (maxTaskDuration == null) {
            if (other.maxTaskDuration != null)
                return false;
        } else if (!maxTaskDuration.equals(other.maxTaskDuration))
            return false;
        if (meanHop == null) {
            if (other.meanHop != null)
                return false;
        } else if (!meanHop.equals(other.meanHop))
            return false;
        if (meanTaskDuration == null) {
            if (other.meanTaskDuration != null)
                return false;
        } else if (!meanTaskDuration.equals(other.meanTaskDuration))
            return false;
        if (minHop == null) {
            if (other.minHop != null)
                return false;
        } else if (!minHop.equals(other.minHop))
            return false;
        if (minTaskDuration == null) {
            if (other.minTaskDuration != null)
                return false;
        } else if (!minTaskDuration.equals(other.minTaskDuration))
            return false;
        if (partialSuccessRate == null) {
            if (other.partialSuccessRate != null)
                return false;
        } else if (!partialSuccessRate.equals(other.partialSuccessRate))
            return false;
        if (taskId == null) {
            if (other.taskId != null)
                return false;
        } else if (!taskId.equals(other.taskId))
            return false;
        if (yearmonthcollect == null) {
            if (other.yearmonthcollect != null)
                return false;
        } else if (!yearmonthcollect.equals(other.yearmonthcollect))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StatCollectDailyGlobalCEntity [yearmonthcollect=" + yearmonthcollect + ", dateCollect=" + dateCollect
                + ", taskId=" + taskId + ", dcCount=" + dcCount + ", deviceCount=" + deviceCount + ", fullSuccessRate="
                + fullSuccessRate + ", partialSuccessRate=" + partialSuccessRate + ", minTaskDuration="
                + minTaskDuration + ", meanTaskDuration=" + meanTaskDuration + ", maxTaskDuration=" + maxTaskDuration
                + ", minHop=" + minHop + ", meanHop=" + meanHop + ", maxHop=" + maxHop + "]";
    }

}
