import React from "react";
import RecipeCard from "../Recipes/RecipeCard";
import { useNavigate } from "react-router-dom";

const HomeRecipes = ({ recipes }) => {
  const navigate = useNavigate();

  return (
    <div className="ff-home-recipes">
      <div className="saved-recipes-title">Saved recipes</div>
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
      ) : (
        `Recipes not found`
      )}
    </div>
  );
};

export default HomeRecipes;
