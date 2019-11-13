package com.chi.question.service.impl;

import com.chi.question.service.SensitiveWordFilter;
import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SensitiveWordFilterImpl implements SensitiveWordFilter, InitializingBean {

	public static final Logger logger = LoggerFactory.getLogger(SensitiveWordFilterImpl.class);

	public final List<String> sensitiveWordList = new ArrayList<>();

	public static final String FILTER_WORD = "***";

	private TrieNode root = null;

	private boolean isSymbol(char c) {
		int ic = (int) c;
		// 0x2E80-0x9FFF 东亚文字范围
		return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
	}

	@Override
	public String filter(String str) {
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		int begin = 0, p = 0;
		TrieNode lastNode = root;
		while (p < chars.length) {
			char curChar = chars[p];
			if (isSymbol(curChar)) {
				if (p == begin) {
					begin++;
					sb.append(curChar);
				}
				p++;
				continue;
			}
			TrieNode currNode = lastNode.nextNodeMap.get(curChar);
			if (currNode == null) {
				sb.append(chars[begin]);
				begin++;
				p = begin;
				lastNode = root;
			} else if (currNode.isEnd()) {
				p++;
				begin = p;
				lastNode = root;
				sb.append(FILTER_WORD);
			} else {
				lastNode = currNode;
				p++;
			}
		}
		return sb.toString();
	}


	private class TrieNode {
		private Character key;
		public Map<Character, TrieNode> nextNodeMap = new HashMap<>();
		private boolean end;

		public TrieNode(List<String> sensitiveWordList) {
			this(null, false);
			for (int i = 0; i < sensitiveWordList.size(); i++) {
				char[] chars = sensitiveWordList.get(i).toLowerCase().toCharArray();
				TrieNode curNode = this;
				for (int j = 0; j < chars.length; j++) {
					TrieNode nextNode = curNode.nextNodeMap.get(chars[j]);
					if (nextNode == null) {
						nextNode = new TrieNode(chars[j], j == (chars.length - 1));
						curNode.nextNodeMap.put(chars[j], nextNode);
					}
					curNode = nextNode;
				}
			}
		}

		private TrieNode(Character key, boolean end) {
			this.key = key;
			this.end = end;
		}


		public boolean isEnd() {
			return this.end;
		}
	}

	@Override
	public void afterPropertiesSet() {
		try (InputStream in = SensitiveWordFilterImpl.class.getClassLoader().getResourceAsStream("sensitiveword.txt");
			 BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		) {
			String line = null;
			while ((line = reader.readLine()) != null)
				sensitiveWordList.add(line.toLowerCase().trim());
		} catch (Exception e) {
			logger.error("敏感词读取失败 " + e.getMessage());
		}
		this.root = new TrieNode(this.sensitiveWordList);
		System.out.println(sensitiveWordList);
	}

	public static void main(String[] args) throws Exception {

		SensitiveWordFilterImpl s = new SensitiveWordFilterImpl();
		s.afterPropertiesSet();
		String str = s.filter("你好 色 情是劳动法");
		System.out.println(str);
	}
}
