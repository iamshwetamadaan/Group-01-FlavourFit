package com.flavourfit.User;

import com.flavourfit.ResponsesDTO.PutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private IUserService userService;

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
     * "userId" : 1,
     * "firstName": "John",
     * "lastName": "Doe",
     * "email": "john@doe.co",
     * "contact": "+1234567890",
     * "height": 170,
     * "weight": 70,
     * "targetWeight": 80,
     * <p>
     * "street":"6385 South Street",
     * "State":"NS",
     * "city":"Halifax",
     * "postal_code":"B3H 4j4"
     * <p>
     * "preferences": ["Vegetarian", "Eggs"],
     * }
     * <p>
     * Response = {
     * "success": true,
     * "message": "User updated successfully",
     * "data": {
     * }
     * }
     */
    @PutMapping("/update-user")
    public JSONObject editUser(@RequestBody UserDto user) throws SQLException {
        int count = userService.updateUser(user);
        JSONObject res = new JSONObject();
        if (count == 0) {
            res.put("success", false);
            res.put("message", "User details invalid");
            res.put("data", null);
        } else {
            res.put("success", true);
            res.put("message", "User details successfully updated");
            res.put("data", user);
        }
        return res;
    }

    @RequestMapping("/getuser-current")
    public ResponseEntity<PutResponse> getUserByID(@RequestBody Map<Integer, Object> request) {
        int userID = (int) request.get("userID");
        try {
            Map<String, Object> userdata = new HashMap<>();
            UserDto userDto = this.userService.getUserbyID(userID);
            if (userDto != null) {
                userdata.put("user details", userDto);
                return ResponseEntity.ok().body(new PutResponse(true, "Successfully loaded user details", userdata));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to load user details"));

        }
    }

    @RequestMapping("/getuser-premiummember")
    public ResponseEntity<PutResponse> getUserBymembership() {//@RequestBody Map<Integer, Object> request){
      //  int userID = (int) request.get("userID");
        try {
            Map<String, Object> userdata = new HashMap<>();
            PremiumUserDto premiumuserDto = this.userService.getUserBymembership(1);// ("userID");
            if (premiumuserDto != null) {
                userdata.put("Premium user details", premiumuserDto);
                return ResponseEntity.ok().body(new PutResponse(true, "Successfully loaded premium user details", userdata));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new PutResponse(false, "Failed to load user details"));

        }
    }


}
