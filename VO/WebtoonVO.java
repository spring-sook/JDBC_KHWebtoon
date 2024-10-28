package WebtoonConsole.VO;

public class WebtoonVO {
    private int webtoonNum;
    private String webtoonTitle;
    private String webtoonAuthor;
    private int genreNum;
    private String genreName;
    private int webtoonAvailableAge;
    private double webtoonRating;
    private int webtoonSubscribeCount;
    private int webtoonViewCount;
    private int webtoonReleaseDay;
    private String releaseDayStr;
    private String webtoonPageUrl;
    private String webtoonThumbnailUrl;
    private int platformNum;

    public WebtoonVO(int webtoonNum, String webtoonTitle, String webtoonAuthor, String genreName, int webtoonAvailableAge, double webtoonRating, int webtoonSubscribeCount, int webtoonViewCount, int webtoonReleaseDay, String webtoonPageUrl, String webtoonThumbnailUrl, int platformNum) {
        this.webtoonNum = webtoonNum;
        this.webtoonTitle = webtoonTitle;
        this.webtoonAuthor = webtoonAuthor;
        this.genreName = genreName;
        this.webtoonAvailableAge = webtoonAvailableAge;
        this.webtoonRating = webtoonRating;
        this.webtoonSubscribeCount = webtoonSubscribeCount;
        this.webtoonViewCount = webtoonViewCount;
        this.webtoonReleaseDay = webtoonReleaseDay;
        this.webtoonPageUrl = webtoonPageUrl;
        this.webtoonThumbnailUrl = webtoonThumbnailUrl;
        this.platformNum = platformNum;

        setReleaseDayStr(webtoonReleaseDay);
    }

    public int getWebtoonNum() {
        return webtoonNum;
    }

    public String getWebtoonTitle() {
        return webtoonTitle;
    }

    public String getWebtoonAuthor() {
        return webtoonAuthor;
    }

    public int getGenreNum() {
        return genreNum;
    }

    public String getGenreName() {
        return genreName;
    }

    public int getWebtoonAvailableAge() {
        return webtoonAvailableAge;
    }

    public double getWebtoonRating() {
        return webtoonRating;
    }

    public int getwebtoonSubscribeCount() {
        return webtoonSubscribeCount;
    }

    public int getWebtoonViewCount() {
        return webtoonViewCount;
    }

    public int getWebtoonReleaseDay() {
        return webtoonReleaseDay;
    }

    public String getReleaseDayStr() {
        return releaseDayStr;
    }

    public void setReleaseDayStr(int getWebtoonReleaseDay) {
        String[] week = {"", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
        this.releaseDayStr = week[getWebtoonReleaseDay];
    }

    public String getWebtoonPageUrl() {
        return webtoonPageUrl;
    }

    public String getWebtoonThumbnailUrl() {
        return webtoonThumbnailUrl;
    }

    public int getPlatformNum() {
        return platformNum;
    }
}
