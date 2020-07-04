package com.karthik.iyer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;



@Entity
@Table(name = "bloodcells")
public class ImagePOJO {
private String name;
private String prediction;
private byte[] cellImage;

public ImagePOJO() {
	super();
}
public ImagePOJO(String name, String prediction, byte[] cellImage) {
	super();
	this.name = name;
	this.prediction = prediction;
	this.cellImage = cellImage;
}
@Id
@Column(name = "name")
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
@Column(name="prediction")
public String getPrediction() {
	return prediction;
}
public void setPrediction(String prediction) {
	this.prediction = prediction;
}



@Lob
@Type(type = "org.hibernate.type.BinaryType")
@Column(name = "cell_image")
public byte[] getCellImage() {
	return cellImage;
}
public void setCellImage(byte[] cellImage) {
	this.cellImage = cellImage;
}

}
