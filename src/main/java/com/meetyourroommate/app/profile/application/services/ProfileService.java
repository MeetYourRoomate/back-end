package com.meetyourroommate.app.profile.application.services;

import com.meetyourroommate.app.iam.domain.aggregates.User;
import com.meetyourroommate.app.profile.domain.aggregates.Profile;
import com.meetyourroommate.app.shared.application.services.CrudService;


public interface ProfileService extends CrudService<Profile, Long> {
    Profile findByUser(User user);
}