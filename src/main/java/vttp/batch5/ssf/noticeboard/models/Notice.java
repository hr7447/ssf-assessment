package vttp.batch5.ssf.noticeboard.models;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class Notice {
    
    @NotEmpty(message = "Title is mandatory")
    @Size(min = 3, max = 128, message = "Title must be between 3 and 128 characters")
    private String title;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Please provide a valid email")
    private String poster;

    @NotNull(message = "Post date is mandatory")
    @Future(message = "Post date must be a future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate postDate;

    @NotEmpty(message = "At least one category must be selected")
    private List<String> categories;

    @NotEmpty(message = "Text is mandatory")
    @Size(min = 1, max = 500, message = "Text must be between 1 and 500 characters")
    private String text;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public LocalDate getPostDate() { return postDate; }
    public void setPostDate(LocalDate postDate) { this.postDate = postDate; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
} 