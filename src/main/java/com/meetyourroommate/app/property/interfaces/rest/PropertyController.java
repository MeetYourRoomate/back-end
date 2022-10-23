package com.meetyourroommate.app.property.interfaces.rest;

import com.meetyourroommate.app.iam.application.services.UserService;
import com.meetyourroommate.app.iam.domain.aggregates.User;
import com.meetyourroommate.app.profile.application.services.ProfileService;
import com.meetyourroommate.app.profile.domain.aggregates.Profile;
import com.meetyourroommate.app.property.application.transform.PropertyMapper;
import com.meetyourroommate.app.property.domain.aggregates.Property;
import com.meetyourroommate.app.property.application.transform.resources.PropertyResource;
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

import java.util.List;
import java.util.Optional;

@Tag(name = "Property", description = "Create, read, update and delete properties")
@RestController
@RequestMapping("/api/v1")
public class PropertyController {
    private PropertyService propertyService;
    private ProfileService profileService;
    private UserService userService;

    private final PropertyMapper mapper;

    public PropertyController(PropertyService propertyService, ProfileService profileService, UserService userService, PropertyMapper mapper) {
        this.propertyService = propertyService;
        this.profileService = profileService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Operation(summary = "Create new property", description = "Create property to lessor")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "Created property", content = @Content(mediaType = "application/json"))
	})
	@PostMapping(path = "/properties", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestParam String userid, @RequestBody PropertyResource model){
        try{
            Optional<User> user = userService.findById(userid);
            if(user.isEmpty()){
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
            Optional<Profile> profile = profileService.findByUser(user.get());
            if(profile.isEmpty()){
               return new ResponseEntity<>("Profile not found", HttpStatus.NOT_FOUND);
            }
            Property newProperty = mapper.toEntity(model);
            newProperty.setProfile(profile.get());
            return new ResponseEntity<>(propertyService.save(newProperty),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete property by id", description = "Delete property by property id")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Deleted property", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping(path = "/properties/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@RequestParam("id") Long id) throws Exception{
        propertyService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get properties", description = "Get properties")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Properties", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(path = "/properties", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Property>> getALl(){
        try{
            List<Property> properties = propertyService.findAll();
            return new ResponseEntity<>(properties, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @Operation(summary = "Get Properties by user id", description = "Find all properties by profile")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Created new profile", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(path = "/users/{id}/profiles/properties", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>getProperties(@PathVariable String id){
        try{
            Optional<Profile> profile = profileService.findByUserId(id);
            if (profile.isEmpty()){
                return new ResponseEntity<>("Profile not found.", HttpStatus.NOT_FOUND);
            }
            List<Property> properties = propertyService.findAllByProfile(profile.get());
            return new ResponseEntity<>(properties, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
