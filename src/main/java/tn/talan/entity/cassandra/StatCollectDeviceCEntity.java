package tn.talan.entity.cassandra;

import com.datastax.driver.core.LocalDate;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "stat_collect_device", keyspace = "talankeyspace")
public class StatCollectDeviceCEntity implements java.io.Serializable {

    @Column(name = "year_month_day_collect")

    @PartitionKey(0)

    private LocalDate yearmonthdaycollect;

    @Column(name = "device")

    @PartitionKey(1)

    private String device;
    @Column(name = "timestamp_collect")

    @ClusteringColumn(0)
    private Date date_collect;
    @Column(name = "dc")
    private String dc;
    @Column(name = "task_id")
    private String taskId;
    @Column(name = "success")

    private Integer success;
    @Column(name = "failures")

    private Integer failures;
    @Column(name = "successrate")

    private BigDecimal successRate;
    @Column(name = "duration")

    private Integer duration;
    @Column(name = "hopcount")

    private Integer hopCount;

    public StatCollectDeviceCEntity(LocalDate yearmonthdaycollect, String device, Date date_collect, String dc,
                                    String taskId, Integer success, Integer failures, BigDecimal successRate, Integer duration,
                                    Integer hopCount) {
        super();
        this.yearmonthdaycollect = yearmonthdaycollect;
        this.device = device;
        this.date_collect = date_collect;
        this.dc = dc;
        this.taskId = taskId;
        this.success = success;
        this.failures = failures;
        this.successRate = successRate;
        this.duration = duration;
        this.hopCount = hopCount;
    }

    public StatCollectDeviceCEntity() {
        super();
    }

    public StatCollectDeviceCEntity(com.datastax.driver.core.LocalDate dateCollect, String device,
                                    Date timeStampCollect, Integer success, Integer failures, BigDecimal successRate, Integer duration,
                                    Integer hopCount, String dc, String taskId) {
        super();
        this.yearmonthdaycollect = dateCollect;
        this.device = device;
        this.date_collect = timeStampCollect;
        this.dc = dc;
        this.taskId = taskId;
        this.success = success;
        this.failures = failures;
        this.successRate = successRate;
        this.duration = duration;
        this.hopCount = hopCount;
    }

    public LocalDate getYearmonthdaycollect() {
        return yearmonthdaycollect;
    }

    public void setYearmonthdaycollect(LocalDate yearmonthdaycollect) {
        this.yearmonthdaycollect = yearmonthdaycollect;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getDate_collect() {
        return date_collect;
    }

    public void setDate_collect(Date date_collect) {
        this.date_collect = date_collect;
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

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailures() {
        return failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    public BigDecimal getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(BigDecimal successRate) {
        this.successRate = successRate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getHopCount() {
        return hopCount;
    }

    public void setHopCount(Integer hopCount) {
        this.hopCount = hopCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date_collect == null) ? 0 : date_collect.hashCode());
        result = prime * result + ((dc == null) ? 0 : dc.hashCode());
        result = prime * result + ((device == null) ? 0 : device.hashCode());
        result = prime * result + ((duration == null) ? 0 : duration.hashCode());
        result = prime * result + ((failures == null) ? 0 : failures.hashCode());
        result = prime * result + ((hopCount == null) ? 0 : hopCount.hashCode());
        result = prime * result + ((success == null) ? 0 : success.hashCode());
        result = prime * result + ((successRate == null) ? 0 : successRate.hashCode());
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
        result = prime * result + ((yearmonthdaycollect == null) ? 0 : yearmonthdaycollect.hashCode());
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
        StatCollectDeviceCEntity other = (StatCollectDeviceCEntity) obj;
        if (date_collect == null) {
            if (other.date_collect != null)
                return false;
        } else if (!date_collect.equals(other.date_collect))
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
        if (duration == null) {
            if (other.duration != null)
                return false;
        } else if (!duration.equals(other.duration))
            return false;
        if (failures == null) {
            if (other.failures != null)
                return false;
        } else if (!failures.equals(other.failures))
            return false;
        if (hopCount == null) {
            if (other.hopCount != null)
                return false;
        } else if (!hopCount.equals(other.hopCount))
            return false;
        if (success == null) {
            if (other.success != null)
                return false;
        } else if (!success.equals(other.success))
            return false;
        if (successRate == null) {
            if (other.successRate != null)
                return false;
        } else if (!successRate.equals(other.successRate))
            return false;
        if (taskId == null) {
            if (other.taskId != null)
                return false;
        } else if (!taskId.equals(other.taskId))
            return false;
        if (yearmonthdaycollect == null) {
            if (other.yearmonthdaycollect != null)
                return false;
        } else if (!yearmonthdaycollect.equals(other.yearmonthdaycollect))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StatCollectDeviceCEntity [yearmonthdaycollect=" + yearmonthdaycollect + ", device=" + device
                + ", date_collect=" + date_collect + ", dc=" + dc + ", taskId=" + taskId + ", success=" + success
                + ", failures=" + failures + ", successRate=" + successRate + ", duration=" + duration + ", hopCount="
                + hopCount + "]";
    }

}
