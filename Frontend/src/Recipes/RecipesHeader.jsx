import React, { useEffect, useState } from "react";
import plusCircle from "../resources/Images/circle-plus-solid.svg";
import { axiosRequest } from "../HttpClients/axiosService";
import { useNavigate } from "react-router-dom";

const RecipesHeader = ({ addRecipe, selectType }) => {
  const [types, setTypes] = useState([]);
  const [user, setUser] = useState(null);

  useEffect(() => {
    axiosRequest(
      {
        url: "/recipes/types",
        method: "GET",
      },
      (response) => {
        if (response?.data?.data) {
          setTypes([...response.data.data]);
        }
      },
      (error) => {}
    );

    axiosRequest(
      { url: "/users/get-current-user" },
      (response) => {
        if (response?.data?.data?.userDetails) {
          const userDetails = { ...response.data.data.userDetails };
          setUser(userDetails);
        }
      },
      (error) => {}
    );
  }, []);

  return (
    <div className="ff-recipes-header">
      <div className="ff-recipes-title">
        <div>Recipes</div>
        {user?.type === "registered" ? (
          <div className="add-recipe" onClick={addRecipe}>
            <img
              src={plusCircle}
              width={25}
              alt="+"
              title="Record water intake"
              style={{ cursor: "pointer" }}
            />
            Add Recipe
          </div>
        ) : null}
      </div>
      <div className="recipes-subtitle">
        Find and share everyday cooking inspiration on FlavourFit. Discover
        recipes based on the food you love
      </div>

      <div className="recipe-types">
        {types
          ? types.map((type, index) => {
              return (
                <div
                  className="recipe-type"
                  key={type + index}
                  onClick={(e) => selectType(type)}
                >
                  {type}
                </div>
              );
            })
          : null}
      </div>
    </div>
  );
};

export default RecipesHeader;
