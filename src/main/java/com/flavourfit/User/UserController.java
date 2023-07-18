package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.json.simple.JSONObject;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.*;

>>>>>>> b57d4a7c9b36d8d7686f446228fe4183f4ee6ea3
import java.sql.SQLException;

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

    @PutMapping({"/reset-password"})
    public JSONObject resetPassword(@RequestBody int userID, @RequestBody String newPassword) throws SQLException {
        boolean status = userService.resetPassword(userID, newPassword);

        JSONObject res = new JSONObject();

        if(!status){
            res.put("status",false);
            res.put("status message","User details invalid");
            res.put("reset values: ",null);
        }
        else{
            res.put("status",true);
            res.put("status message","User details successfully updated");
            res.put("reset values: ",userID + " " + newPassword);
        }
        return res;
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
