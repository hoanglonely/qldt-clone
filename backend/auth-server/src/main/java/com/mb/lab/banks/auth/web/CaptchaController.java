package com.mb.lab.banks.auth.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.cage.Cage;
import com.github.cage.token.RandomTokenGenerator;
import com.mb.lab.banks.auth.security.captcha.CaptchaAuthenticationFilter;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {
	
	private final Cage cage;
	
	public CaptchaController() {
		RandomTokenGenerator random = new RandomTokenGenerator(null, 6);
		this.cage = new Cage(null, null, null, null, 0.5f, random, null);
	}

	@RequestMapping(method = RequestMethod.GET)
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/" + cage.getFormat());
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Progma", "no-cache");
		response.setDateHeader("Max-Age", 0);

		String token = cage.getTokenGenerator().next();

		HttpSession session = request.getSession();
		session.setAttribute(CaptchaAuthenticationFilter.SESSION_ATTRIBUTE_NAME, token);
		cage.draw(token, response.getOutputStream());
	}

}
