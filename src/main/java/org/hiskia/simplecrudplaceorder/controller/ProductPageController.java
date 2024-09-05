package org.hiskia.simplecrudplaceorder.controller;

import org.hiskia.simplecrudplaceorder.entity.Product;
import org.hiskia.simplecrudplaceorder.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
public class ProductPageController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String viewProducts(Model model) {
        return "product";
    }

    @GetMapping("/products/create")
    public String viewCreateProducts(Model model) {
        return "createProduct";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Long productId, Model model) {
        Optional<Product> productOpt = productService.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            model.addAttribute("product", product);
            return "editProduct";
        } else {
            return "redirect:/products";
        }
    }

    @RequestMapping(value = "/products/delete/{id}", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable("id") Long productId) {
        Optional<Product> productOpt = productService.findById(productId);  // Ambil produk berdasarkan ID

        if (productOpt.isPresent()) {
            productService.deleteProduct(productId);  // Hapus produk jika ditemukan
        }

        return "redirect:/products";  // Redirect ke halaman daftar produk setelah dihapus
    }
}
