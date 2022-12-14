package com.meetyourroommate.app.iam.application.transform;

import java.io.Serializable;
import java.util.List;

import com.meetyourroommate.app.iam.domain.aggregates.User;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.meetyourroommate.app.iam.application.transform.resources.UserResource;
import com.meetyourroommate.app.iam.domain.entities.Role;
import com.meetyourroommate.app.shared.application.transform.EnhancedModelMapper;

public class UserMapper implements Serializable {
  @Autowired
  EnhancedModelMapper mapper;

  Converter<Role, String> roleToStringConverter = new AbstractConverter<Role, String>() {
    @Override
    protected String convert(Role role) {
      return role == null ? null : role.getName().name();
    }
  };

  public UserResource toResource(User model) {
    return mapper.map(model, UserResource.class);
  }

  public User toEntity(UserResource resource) {
    return mapper.map(resource, User.class);
  }

  public Page<UserResource> modelListToPage(List<User> modelList, Pageable pageable) {
    mapper.addConverter(roleToStringConverter);
    return new PageImpl<>(mapper.mapList(modelList, UserResource.class), pageable, modelList.size());
  }
}
