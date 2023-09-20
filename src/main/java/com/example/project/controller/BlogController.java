package com.example.project.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.project.entity.blog;
import com.example.project.service.BlogService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlogController {

    @Autowired
    private BlogService BlogService;

    @GetMapping("/blog")
    public String getBlog(Model model) {
        model.addAttribute("result", BlogService.fetchBLogList());
        return "blog";
    }

    // Read to manage
    @GetMapping("/bloglistmanager")
    public String viewBlogList(Model model) {
        List<blog> list = BlogService.fetchBLogList();
        model.addAttribute("listBlog", list);
        return "bloglistmanager";
    }

    @RequestMapping("/blog-detail/{blog_id}")
    public String getBlogById(@RequestParam(value = "blog_id") int id, Model model) {
        model.addAttribute("blog", BlogService.findBlogById(id));
        return "blog-detail";
    }

    // Add Blog
    @GetMapping("/bloglistmanager/add")
    public String addBlog(Model model) {
        model.addAttribute("blog", new blog());
        return "blognew";
    }

    //Edit Blog
    @RequestMapping("/bloglistmanager/edit/{blog_id}")
    public ModelAndView showEditStudentPage(@PathVariable(name = "blog_id") int id) {
        ModelAndView mav = new ModelAndView("blognew");
        Optional<blog> blog = BlogService.findBlogById(id);
        mav.addObject("blog", blog);
        return mav;
    }

    // Save Blog
    @RequestMapping(value = "/bloglistmanager/save", method = RequestMethod.POST)
    public String saveStudent(@ModelAttribute("student") blog blog) {
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        blog.setDate(date);
        BlogService.save(blog);
        return "redirect:/bloglistmanager";
    }

    // Delete Blog by ID
    @RequestMapping("/bloglistmanager/delete/{blog_id}")
    public String deletestudent(@PathVariable(name = "blog_id") int id) {
        BlogService.delete(id);
        return "redirect:/bloglistmanager";
    }


}
