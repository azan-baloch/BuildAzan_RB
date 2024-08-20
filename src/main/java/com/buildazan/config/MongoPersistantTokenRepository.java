package com.buildazan.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MongoPersistantTokenRepository implements PersistentTokenRepository{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		mongoTemplate.save(token);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		PersistentRememberMeToken token = getTokenForSeries(series);
		if (token!=null) {
			PersistentRememberMeToken update = new PersistentRememberMeToken(token.getUsername(), series, tokenValue, lastUsed);
			mongoTemplate.save(update);
		}
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		return mongoTemplate.findById(seriesId, PersistentRememberMeToken.class);
	}

	@Override
	public void removeUserTokens(String username) {
		mongoTemplate.remove(Query.query(Criteria.where("username").is(username)), PersistentRememberMeToken.class);
	}

}
