package com.example.nhom5.controller.user;

import com.example.nhom5.dto.CartItem;
import com.example.nhom5.dto.CartManager;
import com.example.nhom5.service.ProductService;
import com.example.nhom5.service.StockService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    private final CartManager cartManager;
    @Autowired
    private ProductService productService;
    @Autowired
    private StockService stockService;

    public CartController(CartManager cartManager) {
        this.cartManager = cartManager;
    }

    @GetMapping("/cart")
    public List<CartItem> getCartItems() {
        return cartManager.getCartItems();
    }

    @PostMapping("/add-cart")
    @ResponseBody
    public ResponseEntity<Void> addToCart(@RequestBody CartItem requestCart) {

        List<CartItem> cartItems = cartManager.getCartItems();

        for (CartItem cartItem : cartItems) {
            if (requestCart.getProductId().equals(cartItem.getProductId())
                    && requestCart.getColorName().equals(cartItem.getColorName())
                    && requestCart.getSizeName().equals(cartItem.getSizeName())) {
                // Nếu sản phẩm đã tồn tại trong giỏ hàng, chỉ cập nhật số lượng
                int newQuantity = cartItem.getQuantity() + requestCart.getQuantity();
                cartItem.setQuantity(newQuantity);
                return ResponseEntity.ok().build();
            }
        }

        // Nếu sản phẩm không tồn tại trong giỏ hàng, thêm sản phẩm mới
        CartItem cart = new CartItem();
        cart.setProductId(requestCart.getProductId());
        cart.setQuantity(requestCart.getQuantity());
        cart.setUnitPrice(requestCart.getUnitPrice());
        cart.setColorName(requestCart.getColorName());
        cart.setSizeName(requestCart.getSizeName());
        cartManager.addToCart(cart);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/remove-cart/{productId}/{sizeName}/{colorName}")
    public ResponseEntity<Void> removeFromCart(@PathVariable("productId") String productId,
                                               @PathVariable("sizeName") String sizeName,
                                               @PathVariable("colorName") String colorName) {
        List<CartItem> cartItems = cartManager.getCartItems();

        // Tìm sản phẩm trong giỏ hàng dựa trên productId
        CartItem cartItemToRemove = null;
        for (CartItem cartItem : cartItems) {
            if (productId.equals(cartItem.getProductId()) && sizeName.equals(cartItem.getSizeName()) && colorName.equals(cartItem.getColorName())) {
                cartItemToRemove = cartItem;
                break;
            }
        }

        // Nếu tìm thấy sản phẩm, xóa khỏi giỏ hàng
        if (cartItemToRemove != null) {
            cartManager.removeFromCart(cartItemToRemove);
            return ResponseEntity.ok().build();
        } else {
            // Nếu không tìm thấy sản phẩm, trả về lỗi 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

}