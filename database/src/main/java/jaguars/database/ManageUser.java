package jaguars.database;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

import jaguars.user.*;

//import org.hibernate.HibernateException; 
//import org.hibernate.Session; 
//import org.hibernate.Transaction;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;

public class ManageUser {
	//private static SessionFactory factory;
	public static void main(String[] args)
	{
		
	}
	
	public Integer addUser(int id, String email, String password, UserRole role)
	{
		Session session = factory.openSession();
		Transaction tx = null;
		Integer userID = null;
		
	}
}
