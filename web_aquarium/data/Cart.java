package web_aquarium.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;


public class Cart {
    private ArrayList<CartItem> listOfItems;

    public ArrayList<CartItem> getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(ArrayList<CartItem> listOfItems) {
        this.listOfItems = listOfItems;
    }

    public static class CartItem {
        private Product product;
        private int quantity;
        public CartItem(Product product, int quantity){
            this.product = product;
            this.quantity = quantity;
        }

        public CartItem(Product product){
            this.product = product;
            this.quantity = 1;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
    public Cart(){
        this.listOfItems = new ArrayList<>();
    }
    public void addToCart(CartItem cartItem){
       listOfItems.add(cartItem);
    }

    public void removeFromCart(int productId){
        listOfItems.removeIf(item -> item.getProduct().getId() == productId);
    }
    public ArrayList<CartItem> showCart(){
        return listOfItems;
    }
    public void emptyCart(){
        listOfItems.clear();
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.valueOf(0);
        for (CartItem item : listOfItems) {
            total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }
}
