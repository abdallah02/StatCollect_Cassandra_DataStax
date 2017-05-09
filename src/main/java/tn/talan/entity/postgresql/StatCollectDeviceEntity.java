package tn.talan.entity.postgresql;
// Generated 15 ao�t 2016 00:21:09 by Hibernate Tools 5.1.0.Alpha1

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * StatCollectDeviceId generated by hbm2java
 */

@Entity
@Table(name = "stat_collect_device")
public class StatCollectDeviceEntity implements java.io.Serializable {

    @EmbeddedId
    private
    StatCollectDeviceId id;

    @Column(name = "success")

    private Integer success;
    @Column(name = "failures")

    private Integer failures;
    @Column(name = "success_rate")

    private BigDecimal successRate;
    @Column(name = "duration")

    private Integer duration;
    @Column(name = "hop_count")

    private Integer hopCount;

    public StatCollectDeviceEntity() {
    }

    public StatCollectDeviceEntity(StatCollectDeviceId id, Integer success, Integer failures, BigDecimal successRate,
                                   Integer duration, Integer hopCount) {
        this.id = id;
        this.success = success;
        this.failures = failures;
        this.successRate = successRate;
        this.duration = duration;
        this.hopCount = hopCount;
    }

    public Integer getSuccess() {
        return this.success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailures() {
        return this.failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    public BigDecimal getSuccessRate() {
        return this.successRate;
    }

    public void setSuccessRate(BigDecimal successRate) {
        this.successRate = successRate;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getHopCount() {
        return this.hopCount;
    }

    public void setHopCount(Integer hopCount) {
        this.hopCount = hopCount;
    }

    public StatCollectDeviceId getId() {
        return id;
    }

    public void setId(StatCollectDeviceId id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((duration == null) ? 0 : duration.hashCode());
        result = prime * result + ((failures == null) ? 0 : failures.hashCode());
        result = prime * result + ((hopCount == null) ? 0 : hopCount.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((success == null) ? 0 : success.hashCode());
        result = prime * result + ((successRate == null) ? 0 : successRate.hashCode());
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
        StatCollectDeviceEntity other = (StatCollectDeviceEntity) obj;
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
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
        return true;
    }

    @Override
    public String toString() {
        return "StatCollectDeviceEntity [id=" + id + ", success=" + success + ", failures=" + failures
                + ", successRate=" + successRate + ", duration=" + duration + ", hopCount=" + hopCount + "]";
    }

}