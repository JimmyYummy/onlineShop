package onlineShop.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Cart;
import onlineShop.model.CartItem;

@Repository
public class CartItemImpl implements CartItemDao {

	@Autowired
	SessionFactory sessionFactory;
	
	public void addCartItem(CartItem cartItem) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(cartItem);
			tx.commit();
		} finally {
			if (session != null) session.close();
		}
	}

	public void removeCartItem(int cartItemId) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			CartItem cartItem= session.get(CartItem.class, cartItemId);
			Cart cart = cartItem.getCart();
			List<CartItem> itemList = cart.getCartItem();
			itemList.remove(cartItem);
			Transaction tx = session.beginTransaction();
			session.delete(cartItem);
			tx.commit();
		} finally {
			if (session != null) session.close();
		}
	}

	public void removeAllCartItems(Cart cart) {
		List<CartItem> itemList = cart.getCartItem();
		for (CartItem cartItem : itemList) {
			removeCartItem(cartItem.getId());
		}
	}

}
