import React from "react";
import recipeImage1 from "../resources/Images/recipe-placeholder1.jpg";
import recipeImage2 from "../resources/Images/recipe-placeholder2.jpg";
import recipeImage3 from "../resources/Images/recipe-placeholder3.jpg";
import recipeImage4 from "../resources/Images/recipe-placeholder4.jpg";
import "./recipeCard.scss";

const RecipeCard = ({ recipe, handleClick }) => {
  return (
    <div className="recipe-card" onClick={handleClick}>
      {recipe.recipeId % 4 === 0 ? (
        <img src={recipeImage4} alt="recipe" width="100%" />
      ) : recipe.recipeId % 3 === 0 ? (
        <img src={recipeImage3} alt="recipe" width="100%" />
      ) : recipe.recipeId % 2 === 0 ? (
        <img src={recipeImage2} alt="recipe" width="100%" />
      ) : (
        <img src={recipeImage1} alt="recipe" width="100%" />
      )}

      <div className="recipe-text">
        <div className="recipe-type">{recipe?.types ?? ""}</div>

        <div className="recipe-name">{recipe?.recipeName ?? ""}</div>

        <div className="recipe-desc">
          {recipe?.description
            ? recipe.description.length > 100
              ? recipe.description.substring(0, 100) + "..."
              : recipe.description
            : ""}
        </div>

        <div className="recipe-details">
          <div className="recipe-time">{recipe?.time ?? ""} min</div>
          <div className="recipe-cal">{recipe?.calories ?? ""} kCal</div>
        </div>
      </div>
    </div>
  );
};

export default RecipeCard;
