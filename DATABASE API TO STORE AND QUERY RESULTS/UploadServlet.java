package com.karthik.iyer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;



/**
 * Servlet implementation class UploadServlet
 */
@MultipartConfig
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("File Uploaded ");
//		PrintWriter out=response.getWriter();
//		out.println("File Uploaded");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String result=request.getParameter("prediction");	
		Part filePart=request.getPart("image");
		String fileName=Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		InputStream filecontent = filePart.getInputStream();
		byte[] fileAsByteArray = IOUtils.toByteArray(filecontent);
        FileOutputStream imageOutFile = new FileOutputStream("C:\\Users\\VAISHAK`\\workspaceJer\\iyer\\FileUploads\\" + fileName);
        imageOutFile.write(fileAsByteArray);
        imageOutFile.close();
        DatabaseManager dbmanager=new DatabaseManager();
        dbmanager.addImagetoDatabase(fileName, result, fileAsByteArray);
        System.out.println("prediction : "+result);
        doGet(request, response);
	}

}
