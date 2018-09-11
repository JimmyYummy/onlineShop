package onlineShop.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Authorities;
import onlineShop.model.Cart;
import onlineShop.model.Customer;
import onlineShop.model.User;

import onlineShop.model.Customer;

@Repository
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void addCustomer(Customer customer) {
		User user = customer.getUser();
		user.setEnabled(true);
		
		Authorities authorities = new Authorities();
		authorities.setAuthorities("ROLE_USER");
		authorities.setEmailId(user.getEmailId());
		
		Cart cart = new Cart();
		customer.setCart(cart);
		cart.setCustomer(customer);

		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(authorities);
			session.saveOrUpdate(customer);
			tx.commit();
		} finally {
			if (session != null) session.close();
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<Customer> getAllCustomers() {
		Session session = null;
		List<Customer> customerList = new ArrayList<>();
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			customerList = session.createQuery("from Customer").list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(session != null) {
				session.close();
			}
		}
		return customerList;
	}


	@SuppressWarnings("deprecation")
	public Customer getCustomerByUserName(String userName) {
		Session session = null;
		User user = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			user = (User)session
					.createCriteria(User.class)
					.add(Restrictions.eq("emailId", userName))
					.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(session != null) {
				session.close();
			}
		}
		if(user != null) return user.getCustomer();
		return null;
	}

}
