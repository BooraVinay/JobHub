package com.jobhunt.finder.POJO;

import org.springframework.web.multipart.MultipartFile;

public class JobForm {

    private String jobTitle;
    private String companyName;
    private String jobLocation;
    private String postingDate;
    private String jobDescription;
 public JobForm(){}
    public JobForm(String jobTitle, String companyName, String jobLocation, String postingDate, String jobDescription) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobLocation = jobLocation;
        this.postingDate = postingDate;
        this.jobDescription = jobDescription;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}
