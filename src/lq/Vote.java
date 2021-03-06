package lq;

import com.google.appengine.api.datastore.Key;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Vote {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Long quoteId;

    @Persistent
    private Long rating;

    @Persistent
    private Date timestamp;

    @Persistent
    private String ip;

    public Vote(Long quoteId, Long rating, String ip) {
        this.ip = ip;
        this.rating = rating;
        this.quoteId = quoteId;
        timestamp = new Date();
    }

    public Key getKey() { return key; }
    public Date getTimestamp() { return timestamp; }
    public String getIp() { return ip; }
    public Long getRating() { return rating; }
    public Long getQuoteId() { return quoteId; }

    public void setKey(Key key) { this.key = key; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public void setIp(String ip) { this.ip = ip; }
    public void setRating(Long rating) { this.rating = rating; }
    public void setQuote(Long quoteId) { this.quoteId = quoteId; }
}
