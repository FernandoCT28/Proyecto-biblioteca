package com.api.biblioteca.entity;


import jakarta.persistence.*;

@Entity
@Table(name="Books")
public class BookPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "editorial")
    private String editorial;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "published_date")
    private String publishedDate;

    @Column(name = "price")
    private Double price;

    @Column(name = "disponibilidad")
    private String disponibilidad;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientPostEntity client;

    public BookPostEntity() {
    }


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public ClientPostEntity getClient() {
        return client;
    }

    public void setClient(ClientPostEntity client) {
        this.client = client;
    }





    }
