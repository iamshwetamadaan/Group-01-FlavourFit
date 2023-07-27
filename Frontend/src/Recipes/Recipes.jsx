import React, { useEffect, useState } from "react";
import "./recipes.scss";
import { Container } from "react-bootstrap";
import RecipesHeader from "./RecipesHeader";
import closeIcon from "../resources/Images/xmark-solid-white.svg";
import { axiosRequest } from "../HttpClients/axiosService";
import RecipeCard from "./RecipeCard";
import { useNavigate } from "react-router-dom";

const Recipes = (props) => {
  const navigate = useNavigate();

  const [filters, setFilters] = useState({ types: [], keyword: "" });
  const [recipes, setRecipes] = useState([]);

  const filterRecipes = (type, keyword) => {
    setFilters((prevState) => {
      let newState = { ...prevState };
      let types = [...newState.types];
      if (type && types.filter((type1) => type1 === type).length === 0) {
        types.push(type);
      }
      newState.types = [...types];

      if (keyword) {
        newState.keyword = keyword;
      }
      return newState;
    });
  };

  const removeFilter = (type, keyword = false) => {
    setFilters((prevState) => {
      let newState = { ...prevState };
      let types = [...newState.types];
      if (type) {
        types = types.filter((type1) => type1 !== type);
      }
      newState.types = [...types];

      if (keyword) {
        newState.keyword = "";
      }
      return newState;
    });
  };

  useEffect(() => {
    const types = filters.types ? filters.types.toString() : "";
    axiosRequest(
      {
        url: "/recipes/list",
        method: "POST",
        data: {
          types: types,
          keyword: filters.keyword ?? "",
          count: 100,
        },
      },
      (response) => {
        if (response?.data?.data) {
          setRecipes([...response.data.data]);
        }
      },
      (error) => {}
    );
  }, [filters]);

  const addRecipe = () => {
    navigate("/recipes/record/0");
  };

  return (
    <div className="ff-recipes">
      <Container>
        <RecipesHeader selectType={filterRecipes} addRecipe={addRecipe} />
        {filters?.types?.length > 0 || filters.keyword ? (
          <div className="selected-filters">
            {filters?.types
              ? filters.types.map((type, index) => {
                  return (
                    <div
                      className="recipe-filter"
                      key={`filter-${type}-${index}`}
                    >
                      {type}{" "}
                      <img
                        src={closeIcon}
                        alt="x"
                        width={10}
                        onClick={(e) => removeFilter(type)}
                      />
                    </div>
                  );
                })
              : null}

            {filters.keyword ? (
              <div className="recipe-filter">
                {filters.keyword}
                <img
                  src={closeIcon}
                  alt="x"
                  onClick={(e) => removeFilter(false, true)}
                />
              </div>
            ) : null}
          </div>
        ) : null}

        {recipes?.length > 0 ? (
          <div className="recipes-container">
            {recipes.map((recipe, index) => {
              let updatedRecipe = { ...recipe };
              updatedRecipe = {
                ...updatedRecipe,
                calories: Math.floor(Math.random() * 801) + 200,
              };

              updatedRecipe = {
                ...updatedRecipe,
                time: Math.ceil((Math.floor(Math.random() * 91) + 15) / 5) * 5,
              };
              return (
                <React.Fragment key={`${recipe.recipeName}-${index}`}>
                  <RecipeCard
                    recipe={updatedRecipe}
                    handleClick={(e) => {
                      navigate(`/recipes/${recipe.recipeId}`);
                    }}
                  />
                </React.Fragment>
              );
            })}
          </div>
        ) : null}
      </Container>
    </div>
  );
};

export default Recipes;
