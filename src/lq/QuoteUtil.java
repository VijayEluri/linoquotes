package lq;

import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.text.DecimalFormat;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class QuoteUtil {
    public static List<Quote> getQuotes() {
        PersistenceManager pm = PMF.get().getPersistenceManager(); 
        try {
            Query quoteQuery = pm.newQuery(Quote.class);
            quoteQuery.setFilter("approved == true");
            List<Quote> quotes = (List<Quote>) quoteQuery.execute();
            pm.retrieveAll(quotes);
            return quotes;
        }
        finally {
            pm.close();
        }
    }

    public static Quote getQuoteWithId(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager(); 
        try {
            Query quoteQuery = pm.newQuery(Quote.class);
            quoteQuery.setFilter("id == idParam");
            quoteQuery.declareParameters("Long idParam");
            List<Quote> quotes = (List<Quote>) quoteQuery.execute(id);
            if (quotes.isEmpty()) {
                return null;
            }
            else {
                Quote result = quotes.get(0);
                pm.retrieve(result);
                return result;
            }
        }
        finally {
            pm.close();
        }
    }

    public static List<Quote> getQuotesPendingApproval() {
        PersistenceManager pm = PMF.get().getPersistenceManager(); 
        try {
            Query quoteQuery = pm.newQuery(Quote.class);
            quoteQuery.setFilter("approved == null");
            List<Quote> quotes = (List<Quote>) quoteQuery.execute();
            pm.retrieveAll(quotes);
            return quotes;
        }
        finally {
            pm.close();
        }
    }

    public static List<Quote> getQuotesOrderedByIdDesc() {
        List<Quote> quotes = getQuotes();
        Collections.sort(quotes,
                new Comparator<Quote>() {
                    public int compare(Quote q1, Quote q2) {
                        return Long.signum(q2.getId() - q1.getId());
                    }
        });
        return quotes;
    }

    public static List<Quote> getQuotesOrderedByScoreDesc() {
        List<Quote> quotes = getQuotes();
        Collections.sort(quotes,
                new Comparator<Quote>() {
                    public int compare(Quote q1, Quote q2) {
                        return Doubles.signum(q2.getScorePoints() - q1.getScorePoints());
                    }
        });
        return quotes;
    }

    public static List<Quote> getQuotesOrderedByDateDesc() {
        List<Quote> quotes = getQuotes();
        Collections.sort(quotes,
                new Comparator<Quote>() {
                    public int compare(Quote q1, Quote q2) {
                        return q2.getQuoteDate().compareTo(q1.getQuoteDate());
                    }
        });
        return quotes;
    }

    public static List<Quote> getQuotesOrderedByTimestampDesc() {
        List<Quote> quotes = getQuotes();
        Collections.sort(quotes,
                new Comparator<Quote>() {
                    public int compare(Quote q1, Quote q2) {
                        return q2.getTimestamp().compareTo(q1.getTimestamp());
                    }
        });
        return quotes;
    }

    public static void approveQuote(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager(); 
        try {
            Query quoteQuery = pm.newQuery(Quote.class);
            quoteQuery.setFilter("id == idParam");
            quoteQuery.declareParameters("Long idParam");
            List<Quote> quotes = (List<Quote>) quoteQuery.execute(id);

            for (Quote quote : quotes) {
                quote.setApproved(true);
                pm.makePersistent(quote);
            }
        }
        finally {
            pm.close();
        }
    }

    public static void rejectQuote(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager(); 
        try {
            Query quoteQuery = pm.newQuery(Quote.class);
            quoteQuery.setFilter("id == idParam");
            quoteQuery.declareParameters("Long idParam");
            List<Quote> quotes = (List<Quote>) quoteQuery.execute(id);

            for (Quote quote : quotes) {
                quote.setApproved(false);
                pm.makePersistent(quote);
            }
        }
        finally {
            pm.close();
        }
    }

    public static void addVote(Quote quote, Vote vote) {
        quote.setSumVotes(quote.getSumVotes() + vote.getRating());
        quote.setNumVotes(quote.getNumVotes() + 1);
        double scorePoints = (vote.getRating()-2.5) * Math.abs((vote.getRating()-2.5));
        quote.setScorePoints(quote.getScorePoints() + scorePoints);
    }

    public static String formatScore(Quote quote) {
        if (quote.getNumVotes() == 0) {
            return "-";
        }
        String score = DecimalFormat.getInstance().format(quote.getScore());
        return (score + " (fra " + quote.getNumVotes() + ")");
    }
}
