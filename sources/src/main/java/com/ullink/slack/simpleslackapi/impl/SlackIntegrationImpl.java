package com.ullink.slack.simpleslackapi.impl;

import com.ullink.slack.simpleslackapi.SlackIntegration;

class SlackIntegrationImpl implements SlackIntegration {

	private final String id;
	private final String name;
	private final boolean deleted;

	public SlackIntegrationImpl(String id, String name, boolean deleted) {
		this.id = id;
		this.name = name;
		this.deleted = deleted;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isDeleted() {
		return deleted;
	}
}
