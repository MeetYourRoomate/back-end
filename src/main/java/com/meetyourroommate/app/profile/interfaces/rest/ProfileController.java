package com.meetyourroommate.app.profile.interfaces.rest;

import com.meetyourroommate.app.iam.application.services.UserService;
import com.meetyourroommate.app.iam.domain.aggregates.User;
import com.meetyourroommate.app.profile.application.services.ProfileService;
import com.meetyourroommate.app.profile.application.transform.ProfileMapper;
import com.meetyourroommate.app.profile.application.transform.resources.ProfileResource;
import com.meetyourroommate.app.profile.domain.aggregates.Profile;
import com.meetyourroommate.app.property.application.services.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Profile", description = "Create, read, update and delete profile")
@RestController
@RequestMapping("/api/v1")
public class ProfileController {
    private ProfileService profileService;
    private UserService userService;
    private PropertyService propertyService;
    private ProfileMapper mapper;

    public ProfileController(ProfileService profileService, UserService userService, PropertyService propertyService, ProfileMapper mapper) {
        this.profileService = profileService;
        this.userService = userService;
        this.propertyService = propertyService;
        this.mapper = mapper;
    }

    @Operation(summary = "Create profile", description = "Create new profile")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Created new profile", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestParam String userId, @RequestBody ProfileResource profileResource){
        try
        {
            Optional<User> user = userService.findById(userId);
            if(user.isEmpty()){
                return new ResponseEntity<String>("User not found.", HttpStatus.NOT_FOUND);
            }
            Optional<Profile> profile = profileService.findByUser(user.get());
            if(profile.isPresent()){
                return new ResponseEntity<String>("Profile Already exist.", HttpStatus.CONFLICT);
            }
            Profile newProfile = mapper.toEntity(profileResource);
            newProfile.setUser(user.get());
            return new ResponseEntity<Profile>(profileService.save(newProfile), HttpStatus.OK);
        }catch(Exception e){
           return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
