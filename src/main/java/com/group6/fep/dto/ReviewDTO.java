package com.group6.fep.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private int reviewId;
    private String emailReviewer;
    private int orderDetailId;
    private String avatarLink;
    private String name;
    private double rating;
    private String comment;
    private Date date;
    private String emailAdmin;
    private boolean status;
    private List<ReviewImageDTO> image;
    private ProductDTO product;
    private int productId;

    public ReviewDTO(int reviewId, String emailReviewer, String avatarLink, String name, double rating, String comment, Date date, List<ReviewImageDTO> image) {
        this.reviewId = reviewId;
        this.emailReviewer = emailReviewer;
        this.avatarLink = avatarLink;
        this.name = name;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.image = image;
    }

    public ReviewDTO(int reviewId, String avatarLink, String name, double rating, String comment, Date date, String emailAdmin, boolean status, List<ReviewImageDTO> image, ProductDTO product) {
        this.reviewId = reviewId;
        this.avatarLink = avatarLink;
        this.name = name;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.emailAdmin = emailAdmin;
        this.status = status;
        this.image = image;
        this.product = product;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" + "avatarLink=" + avatarLink + ", name=" + name + ", rating=" + rating + ", comment=" + comment + ", date=" + date + ", image=" + image + '}';
    }
}
