package com.company.repository;

import com.company.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Map;
import java.util.Objects;

public class CustomizedUserRepositoryImpl implements CustomizedUserRepository{
    private final MongoOperations mongoOperations;

    public CustomizedUserRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }


    @Override
    @SuppressWarnings("unchecked")
    public User update(User user) {
        System.out.println("----------------------------update");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> objectMap = objectMapper.convertValue(user,Map.class);
        objectMap.remove("id");
        objectMap.values().removeIf(Objects::isNull);
        Update update = new Update();
        objectMap.forEach(update::set);
        return mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(user.getId())), update, User.class);
    }
}
