package com.monk.discount;

import com.monk.discount.foreign.Cart;
import com.monk.discount.foreign.Product;
import com.monk.discount.foreign.Transaction;

import java.util.List;

public class TestUtils {
    public static final Product PRODUCT_1 = new Product(1L, 10.0, 2);
    public static final Product PRODUCT_2 = new Product(2L, 20.0, 2);
    public static final Product PRODUCT_3 = new Product(3L, 30.0, 2);
    public static final Product PRODUCT_4 = new Product(4L, 40.0, 20);
    public static final Cart CART_1 = new Cart(List.of(PRODUCT_1));
    public static final Cart CART_2 = new Cart(List.of(PRODUCT_1, PRODUCT_2, PRODUCT_3));
    public static final Cart CART_3 = new Cart(List.of(PRODUCT_1, PRODUCT_2, PRODUCT_3, PRODUCT_4));
    public static final Transaction TRANSACTION_1 = new Transaction(CART_1);
    public static final Transaction TRANSACTION_2 = new Transaction(CART_2);
    public static final Transaction TRANSACTION_3 = new Transaction(CART_3);

}
