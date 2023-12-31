package com.example.project.controller;

import java.io.IOException;
import java.util.Optional;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.Admin.BlogController.Model.Blog;
import com.example.project.Admin.BlogController.Service.BlogsService;
import com.example.project.entity.review_blog;
import com.example.project.entity.user;
import com.example.project.service.BlogCategoryService;
import com.example.project.service.BlogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.project.service.ReviewBlogService;

import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlogController {

    @Autowired
    private ReviewBlogService ReviewBlogService;

    @Autowired
    private BlogService BlogService;

    @Autowired
    private BlogsService BService;

    @Autowired
    private BlogCategoryService blogCategoryService;

    @GetMapping("/blog")
    public String getBlog(@Param("key") String key, @Param("tags") String tags,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "3") int size, String category, Model model) {
        Page<Blog> blogPage = null;

        if (key == null && category == null && tags == null) {
            blogPage = BlogService.findPaginated(page, size);
        } else if (key != null) {
            blogPage = BService.searchbyTitleAuthorordescription(key, page, size);
            model.addAttribute("key", key);
        } else if (category != null) {
            int id = Integer.parseInt(category);
            blogPage = BlogService.getBlogByCategoryIdPaged(id, page, size);
            model.addAttribute("categoryId", id);
        } else if (tags != null) {
            blogPage = BlogService.getBlogByTagsPaged(tags, page, size);

            model.addAttribute("tags", tags);
        }

        model.addAttribute("result", blogPage);
        model.addAttribute("listCategory", blogCategoryService.fetchBLogCategoryList());
        return "blog";
    }

    @GetMapping("/blog-detail/savecmt")
    public void saveComment(@RequestParam(value = "message") String mess, @RequestParam(value = "blogId") int id,
            HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session)
            throws IOException {
        user u = (user) session.getAttribute("user");
        if (u != null) {
            response.sendRedirect("/home");
        } else {

            response.sendRedirect("/login");
        }

    }

    @GetMapping("/blog-detail/{id}")
    public String viewBlogDetail(@PathVariable int id, Model model, HttpSession session) {
        Optional<Blog> b = BlogService.findBlogById(id);
        int cate_id = b.get().getCategoryBlogId();
        List<Blog> list = BlogService.getBlogByCategoryId(id,cate_id);
        
        model.addAttribute("blogIdd", id);
        model.addAttribute("tags", BlogService.getTags(id));
        model.addAttribute("cmt", BlogService.getComment(id));
        model.addAttribute("listBlog", list);
        model.addAttribute("blog", BlogService.findBlogById(id).orElse(null));
        return "/blog-detail";
    }

    // Read to manage
    @GetMapping("/bloglistmanager")
    public String viewBlogList(Model model) {
        return findPaginated(1, "date", "asc", model);
    }

    @GetMapping("/bloglistmanager/page/{pageNo}")
    public String findPaginated(@PathVariable int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model) {

        int pageSize = 3;

        Page<Blog> page = BlogService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Blog> list = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("listBlog", list);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "bloglistmanager";
    }

    // Add Blog
    @GetMapping("/bloglistmanager/add")
    public String addBlog(Model model) {
        model.addAttribute("category", blogCategoryService.fetchBLogCategoryList());
        model.addAttribute("blog", new Blog());
        return "blognew";
    }

    // Edit Blog
    @RequestMapping("/bloglistmanager/edit/{blog_id}")
    public ModelAndView showEditStudentPage(@PathVariable(name = "blog_id") int id) {
        ModelAndView mav = new ModelAndView("blognew");
        Optional<Blog> blog = BlogService.findBlogById(id);
        mav.getModelMap().addAttribute("category", blogCategoryService.fetchBLogCategoryList());
        mav.addObject("blog", blog);
        return mav;
    }

    // Save cmt
    @PostMapping("/blog-detail/save-cmt")
    public String saveReviewBlog(@RequestParam("text") String text, HttpSession session,
            @RequestParam("id") String blogId) {
        user u = (user) session.getAttribute("user");
        if (u == null) {
            // session.setAttribute("blog-detail-id", Integer.parseInt(blogId));

            return "redirect:/login";
        }
        review_blog rv = new review_blog();
        rv.setUser_id(u.getUser_id());
        rv.setBlog_id(Integer.parseInt(blogId));
        rv.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        rv.setText(text);
        rv.setCreate_at(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        rv.setCreate_by(u.getFull_name());
        rv.setStatus(1);

        ReviewBlogService.save(rv);
        return "redirect:/blog-detail/" + blogId;
    }

}
