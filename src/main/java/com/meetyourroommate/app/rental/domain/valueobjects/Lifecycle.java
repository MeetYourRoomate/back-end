package com.meetyourroommate.app.rental.domain.valueobjects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class Lifecycle implements Serializable {
    @Column(name="end_at", updatable = false)
    private Date endAt;

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }
}
