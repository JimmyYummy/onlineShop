package onlineShop.dao;

import java.io.IOException;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Cart;
import onlineShop.service.SalesOrderService;

@Repository
public class CartDaoImpl implements CartDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private SalesOrderService salesOrderService;

	public Cart getCartById(int cartId) {
		Session session = null;
		Cart cart = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			cart = (Cart) session.get(Cart.class, cartId);
			tx.commit();
		}finally {
			if (session != null) session.close();
		}
		return cart;
	}

	public Cart validate(int cartId) throws IOException {
		Cart cart = getCartById(cartId);
		if (cart == null || cart.getCartItem().size() == 0) {
			throw new IOException("cartId: " + cartId);
		}
		update(cart);
		return cart;
	}

	public void update(Cart cart) {
		int cartId = cart.getId();
		double total = salesOrderService.getSalesOrderTotal(cartId);
		cart.setTotalPrice(total);
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(cart);
		tx.commit();
		session.close();
		
	}

}
