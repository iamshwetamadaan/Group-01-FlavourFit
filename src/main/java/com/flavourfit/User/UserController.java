package com.flavourfit.User;

import com.flavourfit.DatabaseManager.DatabaseManagerImpl;
import com.flavourfit.DatabaseManager.IDatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.json.simple.JSONObject;
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
}
