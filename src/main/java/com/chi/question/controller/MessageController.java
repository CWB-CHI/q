package com.chi.question.controller;


import com.chi.question.domain.HostHolder;
import com.chi.question.domain.Message;
import com.chi.question.domain.User;
import com.chi.question.service.MessageService;
import com.chi.question.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {

	@Autowired
	MessageService messageService;

	@Autowired
	UserService userService;

	@Autowired
	HostHolder hostHolder;

	@GetMapping("/msg/list")
	public String conversationList(Model model) {
		User user = hostHolder.getUser();

		List<Message> conversationList = messageService.getConversationList(user.getId(), 0, 10);
		for (int i = 0; i < conversationList.size(); i++) {
			Message m = conversationList.get(i);
			int otherId = m.getFromId() == user.getId() ? m.getToId() : m.getFromId();
			int unread = messageService.getConvesationUnreadCount(user.getId(), m.getConversationId());
			m.setUnread(unread);
			m.setOtherUser(userService.getUser(otherId));
		}
		model.addAttribute("conversations", conversationList);
		return "letter";
	}


	@PostMapping("/msg/addMessage")
	@ResponseBody
	public Map<String, Object> addMessage(@RequestParam("toName") String toName,
										  @RequestParam("content") String content) {
		Map<String, Object> map = new HashMap<>();

		if (hostHolder.getUser() == null) {
			map.put("code", 1);
			map.put("msg", "未登录");
			return map;
		}
		User user = userService.selectByName(toName);
		if (user == null) {
			map.put("code", 1);
			map.put("msg", "用户不存在");
			return map;
		}

		Message msg = new Message();
		msg.setContent(content);
		msg.setFromId(hostHolder.getUser().getId());
		msg.setToId(user.getId());
		msg.setCreatedDate(new Date());
		messageService.addMessage(msg);
		map.put("code", 0);
		return map;
	}

	@GetMapping("/msg/detail")
	public String detail(@RequestParam("conversationId") String conversationId, Model model) {
		List<Message> conversationDetail = messageService.getConversationDetail(conversationId, 0, 10);
		User user = hostHolder.getUser();
		Message m = conversationDetail.get(0);
		if (m != null) {
			int otherId = m.getFromId() == user.getId() ? m.getToId() : m.getFromId();
			User other = userService.getUser(otherId);
			model.addAttribute("other", other);
		}
		model.addAttribute("conversations", conversationDetail);
		return "letterDetail";
	}
}
