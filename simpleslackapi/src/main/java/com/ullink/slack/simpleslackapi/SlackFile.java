package com.ullink.slack.simpleslackapi;




public class SlackFile {

    private String id;
    private String name;
    private String title;
    private String mimetype;
    private String filetype;
    private String url;
    private String urlDownload;
    private String urlPrivate;
    private String urlPrivateDownload;
    private String thumb64;
    private String thumb80;
    private String thumb160;
    private String thumb360;
    private String thumb480;
    private String thumb720; 
    private Long imageExifRotation; 
    private Long originalW;
    private Long originalH;
    private String permalink;
    private String permalinkPublic;
    private String comment;
    
    public SlackFile() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public String getUrlPrivate() {
        return urlPrivate;
    }

    public void setUrlPrivate(String urlPrivate) {
        this.urlPrivate = urlPrivate;
    }

    public String getUrlPrivateDownload() {
        return urlPrivateDownload;
    }

    public void setUrlPrivateDownload(String urlPrivateDownload) {
        this.urlPrivateDownload = urlPrivateDownload;
    }

    public String getThumb64() {
        return thumb64;
    }

    public void setThumb64(String thumb64) {
        this.thumb64 = thumb64;
    }

    public String getThumb80() {
        return thumb80;
    }

    public void setThumb80(String thumb80) {
        this.thumb80 = thumb80;
    }

    public String getThumb160() {
        return thumb160;
    }

    public void setThumb160(String thumb160) {
        this.thumb160 = thumb160;
    }

    public String getThumb360() {
        return thumb360;
    }

    public void setThumb360(String thumb360) {
        this.thumb360 = thumb360;
    }

    public String getThumb480() {
        return thumb480;
    }

    public void setThumb480(String thumb480) {
        this.thumb480 = thumb480;
    }

    public String getThumb720() {
        return thumb720;
    }

    public void setThumb720(String thumb720) {
        this.thumb720 = thumb720;
    }

    public Long getImageExifRotation() {
        return imageExifRotation;
    }

    public void setImageExifRotation(Long imageExifRotation) {
        this.imageExifRotation = imageExifRotation;
    }

    public Long getOriginalW() {
        return originalW;
    }

    public void setOriginalW(Long originalW) {
        this.originalW = originalW;
    }

    public Long getOriginalH() {
        return originalH;
    }

    public void setOriginalH(Long originalH) {
        this.originalH = originalH;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getPermalinkPublic() {
        return permalinkPublic;
    }

    public void setPermalinkPublic(String permalinkPublic) {
        this.permalinkPublic = permalinkPublic;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
}
