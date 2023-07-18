package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import com.flavourfit.Helpers.DateHelpers;
import com.flavourfit.ResponsesDTO.PutResponse;
import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import com.flavourfit.Trackers.TrackersController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.json.simple.JSONObject;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/fetch-all")
    public String fetchAllUsers() throws SQLException {
        return userService.fetchAllUsers();
    }

    @PutMapping({"/reset-password"})
    public ResponseEntity<Object> resetPassword(@RequestBody Map<String, Object> request) throws SQLException {
        logger.info("Entered controlled method resetPassword()");
        int userID = 1;
        String newPassword = (String) request.get("newPassword");
        try {
            logger.info("Updated controlled method resetPassword()");
            this.userService.resetPassword(userID, newPassword);
            return ResponseEntity.ok().body(new PutResponse(true, "Successfully updated password"));
        } catch (Exception e) {
            logger.error("Bad api request during resetPassword()");
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to update password"));
        }
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
