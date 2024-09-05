package org.hiskia.simplecrudplaceorder.controller;

import org.hiskia.simplecrudplaceorder.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class CartPageController {

    @GetMapping("/cart")
    public String viewCartList(Model model) {
        return "cart"; // Ini akan mengarahkan ke template "cart.html"
    }

    @GetMapping("/cart/view/{id}")
    public String viewCart(@PathVariable("id") Long cartId, Model model) {
        model.addAttribute("cartId", cartId); // Menyimpan cartId untuk digunakan di halaman
        return "viewCart"; // Ini akan mengarahkan ke template "viewCart.html"
    }

    @GetMapping("/cart/edit/{id}")
    public String editCart(@PathVariable("id") Long cartId, Model model) {
        model.addAttribute("cartId", cartId); // Menyimpan cartId untuk digunakan di halaman
        return "editCart"; // Ini akan mengarahkan ke template "editCart.html"
    }
}
