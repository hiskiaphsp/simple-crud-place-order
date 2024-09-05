package org.hiskia.simplecrudplaceorder.controller;

import org.hiskia.simplecrudplaceorder.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class OrderPageController {

    @GetMapping("/order")
    public String viewCartList(Model model) {
        return "order";
    }

    @GetMapping("/order/view/{id}")
    public String viewCart(@PathVariable("id") Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "viewOrder.html";
    }

}
