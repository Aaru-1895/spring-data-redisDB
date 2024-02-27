package com.redis.controller;

import com.redis.entity.Product;
import com.redis.repo.ProductRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@EnableCaching
public class ProductController {

    @Autowired
    private ProductRepo repo;

    @PostMapping
    public Product save(@RequestBody Product product) {
        return repo.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    /*if we want to get any data then use @Cacheable , if you want to delete the record from cache
     when data delete from dab then use @CacheEvict, and if you want to add data inn cache when data added in dab then
     go through with @CachePut */
    //@Cacheable(key = "#id", value = "Product")
    //if you want to fetch data based on some condition
    @Cacheable(key = "#id", value = "Product", unless = "#result.price > 1000")
    public Product findProduct(@PathVariable int id) {
        return repo.findProductById(id);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#id",value = "Product")
    public String remove(@PathVariable int id)   {
        return repo.deleteProduct(id);
    }

}
