package com.chi.question.async.hadler;

import com.chi.question.async.EventModel;
import com.chi.question.async.EventType;

import java.util.List;

public interface EventHandler {

	void doHandle(EventModel model);

	List<EventType> getSupportEventTypes();
}
