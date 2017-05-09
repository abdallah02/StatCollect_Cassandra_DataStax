package tn.talan.entity.cassandra;

import com.datastax.driver.core.LocalDate;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.math.BigDecimal;

@Table(name = "stat_collect_daily_device_by_date", keyspace = "talankeyspace")
public class StatCollectDailyDeviceByDateCEntity implements java.io.Serializable {

    @PartitionKey(0)

    @Column(name = "date_collect")

    private LocalDate dateCollect;
    @ClusteringColumn(0)
    @Column(name = "dc")
    private String dc;
    @ClusteringColumn(2)

    @Column(name = "task_id")
    private String taskId;
    @ClusteringColumn(1)

    @Column(name = "device")
    private String device;
    @Column(name = "min_success_rate")
    private BigDecimal minSuccessRate;
    @Column(name = "mean_success_rate")
    private BigDecimal meanSuccessRate;
    @Column(name = "max_success_rate")
    private BigDecimal maxSuccessRate;
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

    public StatCollectDailyDeviceByDateCEntity() {
        super();
    }

    public StatCollectDailyDeviceByDateCEntity(LocalDate dateCollect, String dc, String taskId, String device,
                                               BigDecimal minSuccessRate, BigDecimal meanSuccessRate, BigDecimal maxSuccessRate, Integer minTaskDuration,
                                               Integer meanTaskDuration, Integer maxTaskDuration, Integer minHop, Integer meanHop, Integer maxHop) {
        super();
        this.dateCollect = dateCollect;
        this.dc = dc;
        this.taskId = taskId;
        this.device = device;
        this.minSuccessRate = minSuccessRate;
        this.meanSuccessRate = meanSuccessRate;
        this.maxSuccessRate = maxSuccessRate;
        this.minTaskDuration = minTaskDuration;
        this.meanTaskDuration = meanTaskDuration;
        this.maxTaskDuration = maxTaskDuration;
        this.minHop = minHop;
        this.meanHop = meanHop;
        this.maxHop = maxHop;
    }

    public LocalDate getDateCollect() {
        return dateCollect;
    }

    public void setDateCollect(LocalDate dateCollect) {
        this.dateCollect = dateCollect;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public BigDecimal getMinSuccessRate() {
        return minSuccessRate;
    }

    public void setMinSuccessRate(BigDecimal minSuccessRate) {
        this.minSuccessRate = minSuccessRate;
    }

    public BigDecimal getMeanSuccessRate() {
        return meanSuccessRate;
    }

    public void setMeanSuccessRate(BigDecimal meanSuccessRate) {
        this.meanSuccessRate = meanSuccessRate;
    }

    public BigDecimal getMaxSuccessRate() {
        return maxSuccessRate;
    }

    public void setMaxSuccessRate(BigDecimal maxSuccessRate) {
        this.maxSuccessRate = maxSuccessRate;
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
        result = prime * result + ((dc == null) ? 0 : dc.hashCode());
        result = prime * result + ((device == null) ? 0 : device.hashCode());
        result = prime * result + ((maxHop == null) ? 0 : maxHop.hashCode());
        result = prime * result + ((maxSuccessRate == null) ? 0 : maxSuccessRate.hashCode());
        result = prime * result + ((maxTaskDuration == null) ? 0 : maxTaskDuration.hashCode());
        result = prime * result + ((meanHop == null) ? 0 : meanHop.hashCode());
        result = prime * result + ((meanSuccessRate == null) ? 0 : meanSuccessRate.hashCode());
        result = prime * result + ((meanTaskDuration == null) ? 0 : meanTaskDuration.hashCode());
        result = prime * result + ((minHop == null) ? 0 : minHop.hashCode());
        result = prime * result + ((minSuccessRate == null) ? 0 : minSuccessRate.hashCode());
        result = prime * result + ((minTaskDuration == null) ? 0 : minTaskDuration.hashCode());
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
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
        StatCollectDailyDeviceByDateCEntity other = (StatCollectDailyDeviceByDateCEntity) obj;
        if (dateCollect == null) {
            if (other.dateCollect != null)
                return false;
        } else if (!dateCollect.equals(other.dateCollect))
            return false;
        if (dc == null) {
            if (other.dc != null)
                return false;
        } else if (!dc.equals(other.dc))
            return false;
        if (device == null) {
            if (other.device != null)
                return false;
        } else if (!device.equals(other.device))
            return false;
        if (maxHop == null) {
            if (other.maxHop != null)
                return false;
        } else if (!maxHop.equals(other.maxHop))
            return false;
        if (maxSuccessRate == null) {
            if (other.maxSuccessRate != null)
                return false;
        } else if (!maxSuccessRate.equals(other.maxSuccessRate))
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
        if (meanSuccessRate == null) {
            if (other.meanSuccessRate != null)
                return false;
        } else if (!meanSuccessRate.equals(other.meanSuccessRate))
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
        if (minSuccessRate == null) {
            if (other.minSuccessRate != null)
                return false;
        } else if (!minSuccessRate.equals(other.minSuccessRate))
            return false;
        if (minTaskDuration == null) {
            if (other.minTaskDuration != null)
                return false;
        } else if (!minTaskDuration.equals(other.minTaskDuration))
            return false;
        if (taskId == null) {
            if (other.taskId != null)
                return false;
        } else if (!taskId.equals(other.taskId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StatCollectDailyDeviceByDateCEntity [dateCollect=" + dateCollect + ", dc=" + dc + ", taskId=" + taskId
                + ", device=" + device + ", minSuccessRate=" + minSuccessRate + ", meanSuccessRate=" + meanSuccessRate
                + ", maxSuccessRate=" + maxSuccessRate + ", minTaskDuration=" + minTaskDuration + ", meanTaskDuration="
                + meanTaskDuration + ", maxTaskDuration=" + maxTaskDuration + ", minHop=" + minHop + ", meanHop="
                + meanHop + ", maxHop=" + maxHop + "]";
    }

}
