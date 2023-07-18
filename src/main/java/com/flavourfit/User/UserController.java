package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.Trackers.TrackersController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {
    private IUserService userService;
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/fetch-all")
    public String fetchAllUsers() throws SQLException {
        return userService.fetchAllUsers();
    }

//    Method to edit user details.
    /**
     * Path - /user/update-user
     * Request Params = {
     *  "userId" : 1,
     *   "firstName": "John",
     *   "lastName": "Doe",
     *   "email": "john@doe.co",
     *   "contact": "+1234567890",
     *   "height": 170,
     *   "weight": 70,
     *   "targetWeight": 80,
     *
     *       "street":"6385 South Street",
     *       "State":"NS",
     *       "city":"Halifax",
     *       "postal_code":"B3H 4j4"
     *
     *   "preferences": ["Vegetarian", "Eggs"],
     * }
     *
     * Response = {
     *     "success": true,
     *     "message": "User updated successfully",
     *     "data": {
     *     }
     * }
     */
    @PostMapping("/register-user")
    public ResponseEntity<Object> registerUser(@RequestBody UserDto user) throws SQLException{
        logger.info("Entered controlled method registerUser()");
        try {
            logger.info("Updating calorie count through calorieHistoryService.");
            userService.registerUser(user);
            //yet to add functionality
            Map<String, Object> data = new HashMap<>();

            logger.info("Added user. Returning response through api");
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully registered user", data));
        }catch (SQLException e) {
            logger.error("Bad api request during registerUser()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to register user"));
        }
    }

    @PutMapping("/update-user")
    public JSONObject editUser(@RequestBody UserDto user) throws SQLException{
        int count = userService.updateUser(user);
        JSONObject res = new JSONObject();
        if(count==0){
            res.put("success",false);
            res.put("message","User details invalid");
            res.put("data",null);
        }
        else{
            res.put("success",true);
            res.put("message","User details successfully updated");
            res.put("data",user);
        }
        return res;
    }
}
