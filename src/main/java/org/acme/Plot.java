package org.acme;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Schema(name = "Plot", description = "Plot Model")
public class Plot {
    @Id
    private Long id;
    @Schema(required = true)

    @Column(length = 100)
    private String nama;
    @Column(length = 200)
    private String warna;
    private String deskripsi;
    private Double koordinatX;
    private Double koordinatY;
    public Double getKoordinatX() {
        return koordinatX;
    }
    public void setKoordinatX(Double koordinatX) {
        this.koordinatX = koordinatX;
    }
    public Double getKoordinatY() {
        return koordinatY;
    }
    public void setKoordinatY(Double koordinatY) {
        this.koordinatY = koordinatY;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getWarna() {
        return warna;
    }
    public void setWarna(String warna) {
        this.warna = warna;
    }
    public String getDeskripsi() {
        return deskripsi;
    }
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}