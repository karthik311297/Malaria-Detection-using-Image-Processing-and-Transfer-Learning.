package com.karthik.iyer;

import java.util.ArrayList;
import java.util.List;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.google.gson.Gson;

public class DatabaseManager {
private SessionFactory sessionFactory;
public DatabaseManager(){
	sessionFactory=new Configuration().configure("com/karthik/iyer/hibernate.cfg.xml").buildSessionFactory();
}
public void addImagetoDatabase(String name,String prediction,byte[] cellImage){
	Session session = sessionFactory.openSession();
    session.beginTransaction();
    ImagePOJO newCell=new ImagePOJO(name,prediction,cellImage);
    session.save(newCell);
    session.getTransaction().commit();
    session.close();
    System.out.println("Image added to database");
}
public byte[] 	getImageofSpecificCell(String name){
	Session session = sessionFactory.openSession();
    session.beginTransaction();
	String hql="FROM com.karthik.iyer.ImagePOJO b WHERE b.name = : cname ";
	Query query=session.createQuery(hql);
	query.setParameter("cname", name);
	List resultCell=query.list();
	session.getTransaction().commit();
    session.close();
	if(resultCell.size()==0)
	{
		return null;
	}
	return ((ImagePOJO) resultCell.get(0)).getCellImage();
}

public ArrayList<ImagePOJO> getDetailofEveryCell(){
	Session session = sessionFactory.openSession();
    session.beginTransaction();
	String hql="FROM com.karthik.iyer.ImagePOJO";
	ArrayList<ImagePOJO> allcellsDetails=new ArrayList<>();
	Query query=session.createQuery(hql);
	List resultDetail=query.list();
	session.getTransaction().commit();
    session.close();
    int i;
    for(i=0;i<resultDetail.size();i++){
    	allcellsDetails.add((ImagePOJO) resultDetail.get(i));
    }
	return allcellsDetails;
}

public String getJSONnamePrediction(ArrayList<ImagePOJO> cellDetail){
	ArrayList<ImagePOJO> keyValue=new ArrayList<>();
	for(int i=0;i<cellDetail.size();i++){
		ImagePOJO imagePojo=new ImagePOJO(cellDetail.get(i).getName(),cellDetail.get(i).getPrediction(),null);
		keyValue.add(imagePojo);
	}
	String json = new Gson().toJson(keyValue);
	return json;
}

}
